package kr.co.kiosk.userEvt;

import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.userView.AddIngredientsView;
import kr.co.kiosk.userView.ChangeSideView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MenuVO;

public class AddIngredientsEvt {

	private JPanel menuPanel;

	private AddIngredientsView aiv;
	private UserMainView umv;
	private StringBuilder menuName;
	private AtomicInteger menuPrice;
	private List<MenuVO> ingredList;

	public AddIngredientsEvt(AddIngredientsView aiv, StringBuilder menuName, AtomicInteger menuPrice, UserMainView umv) {
		this.aiv = aiv;
		this.umv = umv;
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuPanel = aiv.getMenuPanel();
		this.ingredList = getIngredMenu();
	}

	private List<MenuVO> getIngredMenu() {
		List<MenuVO> ingredList = new ArrayList<MenuVO>();

		for (MenuVO mVO : umv.getAllMenuList()) {
			if (mVO.getCategoryId() == 5) {
				ingredList.add(mVO);
			}
		}
		return ingredList;
	}// getIngredMenu
	
	public void addMenuItem() {

		ImageIcon icon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/noChange.jpg"));
		Image scaledImg = icon.getImage().getScaledInstance(125, 110, Image.SCALE_FAST);
		ImageIcon img = new ImageIcon(scaledImg);

		// "변경안함" 버튼
		JButton btnNotChange = new JButton(img);
		btnNotChange.addActionListener(e -> openViewAfterSelect());

		JLabel lblNotChange = new JLabel("<html>추가안함<br>+0</html>", SwingConstants.CENTER);

		JPanel noChangePanel = new JPanel(new GridLayout(1, 1));
		noChangePanel.add(btnNotChange);
		noChangePanel.add(lblNotChange);

		aiv.getMenuPanel().add(noChangePanel);

		// 재료 버튼들
		for (MenuVO mv : ingredList) {
			ImageIcon menuIcon = img;

			if (mv.getImgName() != null) {
					icon = mv.getImage();
			}

			JButton btn = new JButton(icon);
			btn.addActionListener(e -> menuBtnClicked(mv));

			
			/**
			 * 재고소진에 따른 주문 가능 횟수 표기			 
			 * */
			int availableCnt = umv.getStockMap().get(mv.getMenuId());
			String alertText = "";
			if (availableCnt <= 0) {
			    alertText = "<font color='red'><b>Sold Out!</b></font>";
			    btn.setEnabled(false);
			} 
			
			JLabel lbl = new JLabel("<html>" + mv.getMenuName() + "<br>+" + mv.getPrice() + "<br>" + alertText + "</html>",
					SwingConstants.CENTER);

			JPanel itemPanel = new JPanel(new GridLayout(1, 1));
			itemPanel.add(btn);
			itemPanel.add(lbl);

			aiv.getMenuPanel().add(itemPanel);
		}

		// 빈공간 채우기
		while (menuPanel.getComponentCount() < 9) {
			menuPanel.add(new JPanel());
		}
	}// addMenuItem

	public void menuBtnClicked(MenuVO ingredList) {
		menuName.append(" / (추가)").append(ingredList.getMenuName());
		menuPrice.addAndGet(ingredList.getPrice());
		openViewAfterSelect();

	}// menuBtnClicked

	public void openViewAfterSelect() {
		if (aiv.getCategoryId() == 2) {
			aiv.dispose();
		} else {
			aiv.dispose();
			new ChangeSideView(umv, menuName, menuPrice);
		}
	}// openViewAfterSelect

}
