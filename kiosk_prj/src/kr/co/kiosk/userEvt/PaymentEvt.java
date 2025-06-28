package kr.co.kiosk.userEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.service.MenuOrderService;
import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.service.TotalOrderService;
import kr.co.kiosk.userView.FinalOrderListView;
import kr.co.kiosk.userView.InputPhonenumberView;
import kr.co.kiosk.userView.PaymentView;
import kr.co.kiosk.userView.ReceiptView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.MenuOrderVO;
import kr.co.kiosk.vo.MenuVO;
import kr.co.kiosk.vo.TotalOrderVO;

public class PaymentEvt extends WindowAdapter implements ActionListener {
	private PaymentView pv;
	private FinalOrderListView folv;
	private UserMainView umv;
	private DefaultTableModel dtm;

	private StringBuilder sb;
	private String paymentMethod;
	private TotalOrderVO toVO;
	private MenuVO mVO;
	private MenuOrderVO moVO;

	private int waitingNumber;
	private int totalPrice;
	private int totalPriceAfterDiscount;

	public PaymentEvt(PaymentView pv, FinalOrderListView folv, UserMainView umv, int totalPrice,
			int totalPriceAfterDiscount) {
		this.pv = pv;
		this.folv = folv;
		this.umv = umv;
		this.dtm = umv.getDtm();
		this.totalPrice = totalPrice;
		this.totalPriceAfterDiscount = totalPriceAfterDiscount;

	}

	public void completeOrder() {

		String orderType = umv.isHall() ? "홀" : "포장";
		TotalOrderService tos = new TotalOrderService();

		if (umv.getMemberId() != -1) {
			toVO = new TotalOrderVO(umv.getMemberId(), orderType, "주문중");
			tos.addTotalOrderMember(toVO);
		} else {
			toVO = new TotalOrderVO(orderType, "주문중");
			tos.addTotalOrderGuest(toVO);
		}

		MenuService ms = new MenuService();
		MenuOrderService mos = new MenuOrderService();

		for (int i = 0; i < dtm.getRowCount(); i++) {
			mVO = ms.searchMenu((int) dtm.getValueAt(i, 3));
			moVO = mos.searchOneMenuOrder(toVO.getOrderId(), mVO.getMenuId());
			if (moVO != null) { // 이미 해당 제품이 MenuOrder에 올라가있으면
				moVO.setQuantity(moVO.getQuantity() + (int) dtm.getValueAt(i, 1));
				moVO.setTotalPrice(moVO.getTotalPrice() + (int) dtm.getValueAt(i, 2));
				mos.modifyMenuOrder(moVO);
			} else { // 아니면 새로 생성
				moVO = new MenuOrderVO(toVO.getOrderId(), mVO.getMenuId(), mVO.getCategoryId(),
						(int) dtm.getValueAt(i, 1), (int) dtm.getValueAt(i, 2));
				mos.addMenuOrder(moVO);
			}

			// 세트메뉴면(사이드 및 음료 변경 위해 따로 관리)
			if (mVO.getCategoryId() == 1 || mVO.getCategoryId() == 2) {
				String[] strArr = String.valueOf(dtm.getValueAt(i, 0)).split("/");
				for (int j = 1; j < strArr.length; j++) {
					strArr[j] = strArr[j].substring(strArr[j].indexOf(')') + 1).trim();
					mVO = ms.searchMenuWithName(strArr[j]);
					moVO = mos.searchOneMenuOrder(toVO.getOrderId(), mVO.getMenuId());
					if (moVO != null) { // 이미 해당 제품이 MenuOrder에 올라가있으면
						moVO.setQuantity(moVO.getQuantity() + 1);
						mos.modifyMenuOrder(moVO);
					} else { // 아니면 새로 생성
						moVO = new MenuOrderVO(toVO.getOrderId(), mVO.getMenuId(), mVO.getCategoryId(), 1, 0);
						mos.addMenuOrder(moVO);
					}
				}
			} // end if
		} // end for

		toVO.setOrderStatus("조리중");
		toVO.setPrice(totalPrice - umv.getUsingPoints());

		// 회원이면
		if (umv.getMemberId() != -1) {
			MemberService mems = new MemberService();
			MemberVO memVO = mems.searchMember(umv.getMemberId());
			memVO.setTotalAmount(memVO.getTotalAmount() + totalPriceAfterDiscount);
			memVO.setStamps(memVO.getStamps() - umv.getUsingStamps() + (totalPrice / 10000)); // 사용한 스탬프 빼고, 만원 당 스탬프
																								// 1개씩
			memVO.setPoints(memVO.getPoints() - umv.getUsingPoints() + ((int) (totalPrice * 0.05))); // 사용한 포인트 빼고, 일단
																										// 결제 금액의 5%로
																										// 포인트 적립

			tos.modifyTotalOrder(toVO);
			mems.modifyMember(memVO);
		} else {
			tos.modifyTotalOrderGuests(toVO);
		}
		waitingNumber = toVO.getOrderWaitingNumber();

	}// completeOrder

	public void confirmPointSaving() {
		if (umv.getMemberId() != -1) {
			pv.dispose();
			return;
		}
		int result = JOptionPane.showConfirmDialog(null, "적립하시겠습니까?", "적립", JOptionPane.YES_NO_OPTION);

		if (result == JOptionPane.YES_OPTION) {
			new InputPhonenumberView(umv, "적립");
			pv.dispose();
		} else {
			pv.dispose();
		}
	}// confirmPointSaving

	public void receiptCreate() {
		sb = new StringBuilder();
		sb.append("-------------------------------------------------------\n");
		sb.append("[판매영수증]\n");
		sb.append("서울특별시 강남구 테헤란로 132\n");
		sb.append("TEL:010-9120-5456\n");
		sb.append("쌍용버거 역삼역                                 대표:주현석\n");
		sb.append("결제수단:").append(paymentMethod).append("\n");
		sb.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n");
		sb.append("*******************************************************\n");
		sb.append(String.format("%-22s %7s %18s\n", "상품명", "수량", "금액")); // 간격 넓힘
		sb.append("-------------------------------------------------------\n");

		for (int i = 0; i < dtm.getRowCount(); i++) {
			String name = dtm.getValueAt(i, 0).toString().replaceAll(" ", "");
			if (name.length() > 15) {
				name = name.substring(0, 14) + "…"; // 15자까지만 표시 + 생략부호
			}
			String quantity = dtm.getValueAt(i, 1).toString();
			int price = Integer.parseInt(dtm.getValueAt(i, 2).toString());
			String formattedPrice = String.format("%,d", price);

			sb.append(String.format("%-15s\t%8s\t%15s\n", name, quantity, formattedPrice));

		}

		sb.append("-------------------------------------------------------\n");
		sb.append(String.format("%-15s\t%31s\n", "판매액", String.format("%,d", totalPriceAfterDiscount)));
		sb.append("-------------------------------------------------------\n");
		int priceWithoutVAT = (int)(totalPriceAfterDiscount * 0.9); // 부가세 제외 금액
		int vat = totalPriceAfterDiscount - priceWithoutVAT;    
		sb.append(String.format("%-15s\t%31s\n", "부가세제외총계", String.format("%,d", priceWithoutVAT)));
		sb.append(String.format("%-15s\t%31s\n", "부가세", String.format("%,d", vat)));
		sb.append(String.format("%-15s\t%31s\n", "합계", String.format("%,d", totalPriceAfterDiscount)));
		sb.append("-------------------------------------------------------\n");
	}// receiptCreate

	@Override
	public void windowClosing(WindowEvent e) {
		folv.setVisible(true);
		pv.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == pv.getCreditcardBtn()) {

			paymentMethod = "신용카드";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
		if (e.getSource() == pv.getGiftcardBtn()) {
			paymentMethod = "쌍용버거 선불카드";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
		if (e.getSource() == pv.getKakaopayBtn()) {
			paymentMethod = "카카오페이";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
		if (e.getSource() == pv.getPayCoinBtn()) {
			paymentMethod = "페이코인";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
		if (e.getSource() == pv.getZeropayBtn()) {
			paymentMethod = "제로페이";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
		if (e.getSource() == pv.getOtherBtn()) {
			paymentMethod = "기타";
			confirmPointSaving();
			completeOrder();
			receiptCreate();
			new ReceiptView(sb.toString(), waitingNumber);
		}
	}// actionPerformed
}
