package kr.co.kiosk.userEvt;

import java.awt.GridLayout;
import java.awt.Image;
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

import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.userView.ChangeDrinkView;
import kr.co.kiosk.userView.ChangeSideView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MenuVO;

public class ChangeSideEvt {

	private JPanel menuPanel;
	private ChangeSideView csv;
	private UserMainView umv;
	private StringBuilder menuName;
	private AtomicInteger menuPrice;
	private List<MenuVO> sideList;
	private int basicPrice; // 세트 기본 메뉴의 가격

	public ChangeSideEvt(ChangeSideView csv, StringBuilder menuName, AtomicInteger menuPrice, UserMainView umv) {
		this.csv = csv;
		this.umv = umv;
		this.menuName = menuName;
		this.menuPrice = menuPrice;
		this.menuPanel = csv.getMenuPanel();
		this.sideList = getSideMenu();
	}

	private List<MenuVO> getSideMenu() {
		List<MenuVO> sideList = new ArrayList<MenuVO>();

		for (MenuVO mVO : umv.getAllMenuList()) {
			if (mVO.getCategoryId() == 3) {
				sideList.add(mVO);
			}
		}
		return sideList;
	}// getSideMenu
	
	public void addMenuItem() {

		ImageIcon icon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/noChange.jpg"));
		Image scaledImg = icon.getImage().getScaledInstance(125, 110, Image.SCALE_FAST);
		ImageIcon img = new ImageIcon(scaledImg);

		//우선 세트 기본 메뉴를 찾아서 기본 가격을 설정
		for (MenuVO mv : sideList) {
		    if (mv.getMenuName().equals("감자튀김M")) {
		        basicPrice = mv.getPrice();
		        break; // 먼저 찾고 종료
		    }
		}
		// 재료 버튼들
		for (MenuVO mv : sideList) {
		
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
			
			JLabel lbl = new JLabel(("<html>" + mv.getMenuName() + "<br>+" + (mv.getPrice() - basicPrice) + "<br>" + alertText +"</html>"),
					SwingConstants.CENTER);

			JPanel itemPanel = new JPanel(new GridLayout(1, 1));
			itemPanel.add(btn);
			itemPanel.add(lbl);

			menuPanel.add(itemPanel);
		}

		// 빈공간 채우기
		while (menuPanel.getComponentCount() < 9) {
			menuPanel.add(new JPanel());
		}
	}// addMenuItem

	public void menuBtnClicked(MenuVO sideList) {
		menuName.append(" / (사이드)").append(sideList.getMenuName());
		menuPrice.addAndGet(sideList.getPrice()-basicPrice);
		csv.dispose();
		new ChangeDrinkView(umv,menuName, menuPrice);
	}

}
