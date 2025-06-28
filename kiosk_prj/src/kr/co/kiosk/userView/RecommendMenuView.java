package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Panel;

import javax.swing.JPanel;

import kr.co.kiosk.userEvt.RecommendMenuEvt;

@SuppressWarnings("serial")
public class RecommendMenuView extends Panel {
	
	private UserMainView umv;
	private JPanel menuPanel;

	public RecommendMenuView(UserMainView umv) {
		this.umv = umv;
		setLayout(new BorderLayout());
		
		menuPanel = new JPanel(new GridLayout(3,1,5,5));
		add(menuPanel);
		
		RecommendMenuEvt rme = new RecommendMenuEvt(this, umv);
		rme.addMenuItem();
		
	}//RecommendMenuView

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public UserMainView getUmv() {
		return umv;
	}

	
}//class