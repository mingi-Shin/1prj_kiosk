package kr.co.kiosk.adminView;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import kr.co.kiosk.userEvt.MainPageEvt;
import kr.co.kiosk.userView.MainPageView;

public class AdminMainButtons extends JPanel {
	
	private JButton btnLogout;
	private JButton btnShutdown;
	
	private JButton btnOrder;
	private JButton btnMenu;
	private JButton btnFinancial;
	private JButton btnStock;
	private JButton btnMember;
	
	public AdminMainButtons() {
		
		setLayout(new GridLayout(2, 5));
		
		btnLogout = new JButton("로그아웃");
		btnShutdown = new JButton("운영종료");
		btnShutdown.addActionListener(e->{
			if(MainPageEvt.isOpen == false) {
				btnShutdown.setText("운영종료");
				JOptionPane.showMessageDialog(null, "영업시작!");
				MainPageEvt.isOpen = true;
			} else {
				btnShutdown.setText("운영시작");
				JOptionPane.showMessageDialog(null, "영업종료!");
				MainPageEvt.isOpen = false;
			}
			
		});
		
		
		
		btnOrder = new JButton("주문 관리");
		btnMenu = new  JButton("메뉴관리");
		btnFinancial = new  JButton("정산관리");
		btnStock = new  JButton("재고관리");
		btnMember = new  JButton("회원관리");
		
		//버튼 색, 크기 설정
		btnOrder.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnMenu.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnFinancial.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnStock.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		btnMember.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		
		add(btnLogout);
		add(new JLabel(" "));
		add(new JLabel(" "));
		add(new JLabel(" "));
		add(btnShutdown);
		
		add(btnOrder);
		add(btnMenu);
		add(btnFinancial);
		add(btnStock);
		add(btnMember);
		
	}

	public JButton getBtnLogout() {
		return btnLogout;
	}

	public JButton getBtnShutdown() {
		return btnShutdown;
	}

	public JButton getBtnOrder() {
		return btnOrder;
	}

	public JButton getBtnMenu() {
		return btnMenu;
	}

	public JButton getBtnFinancial() {
		return btnFinancial;
	}

	public JButton getBtnStock() {
		return btnStock;
	}

	public JButton getBtnMember() {
		return btnMember;
	}
	

}
