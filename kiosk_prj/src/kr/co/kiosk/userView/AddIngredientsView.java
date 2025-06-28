package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import kr.co.kiosk.userEvt.AddIngredientsEvt;

@SuppressWarnings("serial")
public class AddIngredientsView extends JDialog {

	private JPanel menuPanel;
	private UserMainView umv;
	private int categoryId;

	public AddIngredientsView(UserMainView umv, StringBuilder menuName, AtomicInteger menuPrice, int categoryId) {
		super(umv, "재료추가", true);
		this.umv = umv;

		this.categoryId = categoryId;

		setLayout(new BorderLayout());

		menuPanel = new JPanel(new GridLayout(3, 3, 10, 10));
		add(menuPanel);

		AddIngredientsEvt aie = new AddIngredientsEvt(this, menuName, menuPrice, umv);
		aie.addMenuItem();

		setBounds(410,350, 772, 346);
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

}
