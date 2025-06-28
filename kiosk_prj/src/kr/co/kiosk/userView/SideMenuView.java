package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import kr.co.kiosk.userEvt.BurgerMenuEvt;
import kr.co.kiosk.userEvt.SideMenuEvt;

@SuppressWarnings("serial")
public class SideMenuView extends JPanel {
	
	private JPanel menuPanel; // 메뉴를 표시할 패널
	private JButton btnPrev, btnNext; // 페이지 이동 버튼

	private UserMainView umv;

	public SideMenuView(UserMainView umv) {
		this.umv = umv;
		setLayout(new BorderLayout());

		// 메뉴를 담을 패널 (GridLayout 적용)
		menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
		add(menuPanel);

		// 페이지 이동 버튼 추가
		JPanel jpnlBtn = new JPanel();
		btnPrev = new JButton("이전");
		btnPrev.setFont(new Font("휴먼엑스포",Font.BOLD,15));
		btnPrev.setBackground(Color.decode("#C13226"));
		btnPrev.setForeground(Color.WHITE);
		btnPrev.setBorderPainted(false);   // 테두리 그리지 않기
		btnPrev.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnNext = new JButton("다음");
		btnNext.setFont(new Font("휴먼엑스포",Font.BOLD,15));
		btnNext.setBackground(Color.decode("#C13226"));
		btnNext.setForeground(Color.WHITE);
		btnNext.setBorderPainted(false);   // 테두리 그리지 않기
		btnNext.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)

		SideMenuEvt sme = new SideMenuEvt(this, umv);
		sme.loadMenu();
		btnPrev.addActionListener(sme);
		btnNext.addActionListener(sme);

		jpnlBtn.add(btnPrev);
		jpnlBtn.add(btnNext);
		add(jpnlBtn, BorderLayout.SOUTH);
	}// SideMenuView

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public void setMenuPanel(JPanel menuPanel) {
		this.menuPanel = menuPanel;
	}

	public JButton getBtnPrev() {
		return btnPrev;
	}

	public void setBtnPrev(JButton btnPrev) {
		this.btnPrev = btnPrev;
	}

	public JButton getBtnNext() {
		return btnNext;
	}

	public void setBtnNext(JButton btnNext) {
		this.btnNext = btnNext;
	}

	public UserMainView getUmv() {
		return umv;
	}

	public void setUmv(UserMainView umv) {
		this.umv = umv;
	}

}
