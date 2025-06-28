package kr.co.kiosk.adminView;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import kr.co.kiosk.adminEvt.AdminMainEvt;

/**
 *	신민기 : 11, 12, 16, 17 (관리자 전체패널, 주문관리, 재고관리1,2)
	유연수 : 13, 15 (메뉴관리, 회원관리)
	한주성 : 10, 14 (관리자로그인, 정산관리)
 */
public class AdminMainView extends JFrame {

	private AdminMainButtons adminMainButtons;
	
	private AdminCenterPanel adminCenterPanel;
	
	private OrderManageView orderManageView;
	
	public AdminMainView() {
		this.adminMainButtons = new AdminMainButtons();
		this.adminCenterPanel = new AdminCenterPanel();
		this.orderManageView = adminCenterPanel.getOrderPage();
		
		add(adminMainButtons, "North");
		add(adminCenterPanel, "Center");
		
		setBounds(400,10,900,900);
		setVisible(true);
		setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 완전 종료
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null); //화면 중앙에 정렬
		//setExtendedState(JFrame.MAXIMIZED_BOTH); //전체화면 모드 
		
		//이벤트연결
		AdminMainEvt ame = new AdminMainEvt(this);
		
		adminMainButtons.getBtnLogout().addActionListener(ame);
		adminMainButtons.getBtnShutdown().addActionListener(ame);
		adminMainButtons.getBtnOrder().addActionListener(ame);
		
		adminMainButtons.getBtnMenu().addActionListener(ame);
		adminMainButtons.getBtnFinancial().addActionListener(ame);
		adminMainButtons.getBtnStock().addActionListener(ame);
		adminMainButtons.getBtnMember().addActionListener(ame);
		
	}

	public AdminMainButtons getAdminMainButtons() {
		return adminMainButtons;
	}

	public AdminCenterPanel getAdminCenterPanel() {
		return adminCenterPanel;
	}

	public OrderManageView getOrderManageView() {
		return orderManageView;
	}
	
	
	
}
