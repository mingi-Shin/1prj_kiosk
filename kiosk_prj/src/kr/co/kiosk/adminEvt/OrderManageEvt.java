package kr.co.kiosk.adminEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import kr.co.kiosk.adminService.OrderManageService;
import kr.co.kiosk.adminService.StockManageService;
import kr.co.kiosk.adminView.OrderDetailView;
import kr.co.kiosk.adminView.OrderManageView;
import kr.co.kiosk.vo.TotalOrderVO;

public class OrderManageEvt extends WindowAdapter implements ActionListener, MouseListener {

	private OrderManageView omv;
	
	private OrderManageService oms;
	
	private StockManageService sms;
	
	private OrderDetailView odv;
	
	public OrderManageEvt(OrderManageView omv) {
		this.omv = omv;
		this.odv = omv.getOdv();
		this.oms = new OrderManageService();
		this.sms = new StockManageService();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedRowNum = omv.getJtblOrderStatus().getSelectedRow(); //첫번째=0
		
		if(selectedRowNum != -1) { //선택을 했을때만 
			String orderId = omv.getJtblOrderStatus().getValueAt(selectedRowNum, 0).toString();
			TotalOrderVO vo = new TotalOrderVO();
			String phoneNumber = null;
			Map<String, Object> result = new HashMap<>();
			result = oms.getOrderVO(Integer.parseInt(orderId));
			vo = (TotalOrderVO)result.get("order");
			phoneNumber = (String)result.get("phoneNumber");
		
			omv.getOdv().getJtfOrderId().setText(String.valueOf(vo.getOrderId())); 
			omv.getOdv().getJtfOrderWaitingNum().setText(String.valueOf(vo.getOrderWaitingNumber()));
			omv.getOdv().getJtfOrderDate().setText(String.valueOf(vo.getOrderDateTime()));
			omv.getOdv().getJtfTakeOut().setText(String.valueOf(vo.getOrderType()));
			//omv.getOdv().getJtfMemberID().setText(String.valueOf(vo.getMemberId()));
			if(vo.getMemberId() != 0) {
				omv.getOdv().getJtfMemberID().setText(phoneNumber);
			} else {
				omv.getOdv().getJtfMemberID().setText("비회원");
			}
			omv.getOdv().getJtfTotalPrice().setText(String.valueOf(vo.getPrice())); //총가격
			if ("조리중".equals(vo.getOrderStatus()))  {		
				omv.getOdv().getBgMakingDone().clearSelection();
				omv.getOdv().getJrbMaking().setSelected(true);
				//조리중시 활성화
				omv.getOdv().getJrbMaking().setEnabled(true);
				omv.getOdv().getJrbDone().setEnabled(true);
			} if ("조리완료".equals(vo.getOrderStatus())) {
				omv.getOdv().getBgMakingDone().clearSelection();
				omv.getOdv().getJrbDone().setSelected(true);
				//조리완료시 비활성화
				omv.getOdv().getJrbMaking().setEnabled(false);
				omv.getOdv().getJrbDone().setEnabled(false);
			}
			
			//상세메뉴 조회출력
			List<String[]> menuList = new ArrayList<String[]>();
			menuList = oms.SelectMenuAndPrice(Integer.parseInt(orderId));
			omv.getOdv().updateTable(menuList);
			
		} else {
			System.out.println("잘모된 index값 ");
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//수정
		if(e.getSource() == odv.getJbtnModify()) {
			String status = null;
			int orderId = Integer.parseInt( odv.getJtfOrderId().getText().toString());
			
			if(odv.getJrbMaking().isSelected() && (!odv.getJtfOrderId().getText().isBlank()) && (orderId != 0)) {
				status = "조리중";
			} else if (odv.getJrbDone().isSelected() && (!odv.getJtfOrderId().getText().isBlank()) && (orderId != 0)) {
				status = "조리완료";
			} else {
				JOptionPane.showMessageDialog(odv, "정확한 리스트 항목을 클릭해주세요");
				return;
			}
			
			int result = JOptionPane.showConfirmDialog(odv, 
					orderId + "번 주문을 '" + status + "'로 상태를 변경하시겠습니까?",
					"주문 상태 변경",
					JOptionPane.YES_NO_OPTION
					);
			if(result == JOptionPane.YES_OPTION) {
				//status가 조리완료시, 해당 주문의 메뉴들을 재고에서 차감 
				if("조리완료".equals(status)) {
					//해당 orderId의 메뉴들을 재고에서 차감, 입출고 테이블에 생성
					//차감시에 category_id가 1인것은 제외 
					List<String[]> mNpList = new ArrayList<String[]>();
					//해당 orderId에 대한 메뉴명들과 각 메뉴들의수량 String 배열List -> 기존메서드 재활용, str로 바뀌는건 어쩔수 없
					mNpList = oms.SelectMenuAndPrice(orderId); 
					for(String[] menuAndQty : mNpList) {
						String menuName = menuAndQty[0];
						String quantityStr = menuAndQty[1];
						int menuId = oms.getMenuId(menuName);
						int quantity = Integer.parseInt(quantityStr);
					    sms.saveStock(menuId, quantity, false);//차감: 여기서quantity는 수량임 
					}
					
				}
				
				
				oms.changeOrderStatus(status, orderId);
				JOptionPane.showMessageDialog(odv, "성공적으로 변경되었습니다.");
				//수정 후 갱신
				List<TotalOrderVO> voList = oms.totalOrderVOList(0);
				omv.updateTable(voList);
			}
			
		}
		
		//삭제 
		if(e.getSource() == odv.getJbtnDelete()) {
			int orderId = Integer.parseInt( odv.getJtfOrderId().getText().toString());
			String status = null;
			if(odv.getJrbMaking().isSelected()) { //주문이 조리중일때만 status값을 조리중으로 변경 
				status = odv.getJrbMaking().getText();
			} else {
				status = "예외";
			}
			
			if(!status.equals("조리중")) {
				JOptionPane.showMessageDialog(odv, "'조리중'인 주문만 삭제하실 수 있습니다.");
				return;
			} else if((odv.getJtfOrderId().getText().isBlank()) || (orderId == 0)) {
				JOptionPane.showMessageDialog(odv, "정확한 리스트 항목을 클릭해주세요");
				return;
			} else {
				oms.deleteOrder(orderId);
				JOptionPane.showMessageDialog(odv, orderId + "번 주문을 삭제하였습니다.");
			}
			
		}
		
		
		//목록 갱신
		if(e.getSource() == odv.getJbtnNewList()) {
			List<TotalOrderVO> voList = oms.totalOrderVOList(0);
			omv.updateTable(voList);
		}
		
		//전체주문 리스트 
		if(e.getSource() == odv.getJbtnGuitar()) {
			List<TotalOrderVO> voList = oms.totalOrderVOList(1);
			omv.updateTable(voList);
		}
		
		
	}

}
