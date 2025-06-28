package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import kr.co.kiosk.userEvt.ChangeDrinkEvt;

@SuppressWarnings("serial")
public class ChangeDrinkView extends JDialog {

	private JPanel menuPanel;
	private UserMainView umv;

	public ChangeDrinkView(UserMainView umv, StringBuilder menuName, AtomicInteger menuPrice) {
		super(umv, "음료 선택", true);
		this.umv = umv;

		setLayout(new BorderLayout());

		menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
		add(menuPanel);
		
		ChangeDrinkEvt cde = new ChangeDrinkEvt(this, menuName, menuPrice, umv);
		cde.addMenuItem();

		setBounds(300, 300, 772, 346);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setVisible(true);
	}

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public void setMenuPanel(JPanel menuPanel) {
		this.menuPanel = menuPanel;
	}

	public UserMainView getUmv() {
		return umv;
	}

	public void setUmv(UserMainView umv) {
		this.umv = umv;
	}

}
