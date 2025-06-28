package kr.co.kiosk.userEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.service.MenuOrderService;
import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.service.TotalOrderService;
import kr.co.kiosk.userView.FinalOrderListView;
import kr.co.kiosk.userView.InputPhonenumberView;
import kr.co.kiosk.userView.PaymentView;
import kr.co.kiosk.userView.UsePointView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.MenuOrderVO;
import kr.co.kiosk.vo.MenuVO;
import kr.co.kiosk.vo.TotalOrderVO;

public class FinalOrderListEvt extends WindowAdapter implements ActionListener {

	private FinalOrderListView folv;
	private UserMainView umv;
	private DefaultTableModel dtm;
	private DefaultTableModel dtmUmv;
	private int totalResult;
	private int totalPriceAfterDiscount;

	public FinalOrderListEvt(FinalOrderListView folv, UserMainView umv) {
		this.folv = folv;
		this.umv = umv;
		dtm = folv.getDtm();
		dtmUmv = umv.getDtm();

		showOderSummary();
		updatePrice();

	}// FinalOrderListEvt

	public void showOderSummary() {
		for (int i = 0; i < dtmUmv.getRowCount(); i++) {
			Object[] rowData = new Object[dtmUmv.getColumnCount()];
			for (int j = 0; j < dtmUmv.getColumnCount(); j++) {
				rowData[j] = dtmUmv.getValueAt(i, j);
			}
			dtm.addRow(rowData);
		}
	}// showOderSummary

	public void updatePrice() {
		totalResult = 0;
		totalPriceAfterDiscount = 0;
		for (int i = 0; i < dtm.getRowCount(); i++) {// 합계
			try {
				// dtm 에서 1열, 2열을 가져와 int형 변수에 담음
				int price = Integer.parseInt(dtm.getValueAt(i, 2).toString());
				totalResult += price;
			} catch (NumberFormatException nfe) {// 숫자가 아니라 다르거 오는거 예외처리
				nfe.printStackTrace();
			} catch (NullPointerException npe) {// 값이 안들어있을거를 대비해 예외처리
				npe.printStackTrace();
			}
		}
		folv.getJlblTotalResult().setText(String.valueOf(totalResult) + "원");
		totalPriceAfterDiscount = totalResult - umv.getUsingPoints();
		folv.getJlblTotalPriceResult().setText(String.valueOf(totalPriceAfterDiscount) + "원");
	}// updatePrice

	public void openPointView() {
		if (umv.getMemberId() != -1) { // 이미 앞서 스탬프를 통해 번호 조회를 완료했다면
			MemberService ms = new MemberService();
			new UsePointView(umv, ms.searchMember(umv.getMemberId()));
		} else {
			new InputPhonenumberView(umv, "point");
		}

		// 할인 금액이 상품총액보다 크면
		if (umv.getUsingPoints() > totalResult) {
			umv.setUsingPoints(totalResult);
		}

		folv.getJlblDiscountResult().setText(String.valueOf(umv.getUsingPoints()) + "원");
		updatePrice();
	}// openPointView

	@Override
	public void windowClosing(WindowEvent e) {
		umv.getFrame().setVisible(true);
		folv.dispose();
	}// windowClosing

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == folv.getJbtnCancel()) {
			umv.setUsingPoints(0); //결제 취소하면 할인 내역도 초기화
			folv.dispose();
			umv.getFrame().setVisible(true);
		}
		if (e.getSource() == folv.getJbtnDiscount()) {
			openPointView();
		}
		if (e.getSource() == folv.getJbtnPay()) {
			new PaymentView(folv, umv, totalResult, totalPriceAfterDiscount);
			folv.dispose();

		}

	}// actionPerformed

}// class
