package kr.co.kiosk.userEvt;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.userView.AddIngredientsView;
import kr.co.kiosk.userView.RecommendMenuView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MenuVO;

public class RecommendMenuEvt {

	private UserMainView umv;

	private JPanel menuPanel;
	private DefaultTableModel dtm;
	private List<MenuVO> recommendList;

	public RecommendMenuEvt(RecommendMenuView rmv, UserMainView umv) {
		this.umv = umv;
		this.dtm = umv.getDtm();
		this.menuPanel = rmv.getMenuPanel();

		this.recommendList = getRecommendMenu();
	}// RecommendMenuEvt

	private List<MenuVO> getRecommendMenu() {
		List<MenuVO> recommendList = new ArrayList<MenuVO>();

		for (MenuVO mVO : umv.getAllMenuList()) {
			if (mVO.getCategoryId() != 5) {
				recommendList.add(mVO);
			}
		}

		return recommendList;
	}

	public void addMenuItem() {
		Random random = new Random();

		// 리스트를 섞고 처음 3개 선택
		Collections.shuffle(recommendList, random);
		List<MenuVO> randomMenu = recommendList.subList(0, 3);
		
		for (int i = 0; i < 3; i++) {
			MenuVO menu = randomMenu.get(i);
			ImageIcon icon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/BrandLogo.png"));
			if (menu.getImgName() != null) {
					icon = menu.getImage();
			}
			Image scaledImg = icon.getImage().getScaledInstance(180, 120, Image.SCALE_SMOOTH);
			ImageIcon img = new ImageIcon(scaledImg);
			JLabel jlblImg = new JLabel(img);

			JLabel jlblMenu = new JLabel(("<html>" + menu.getMenuName() + " / " + menu.getPrice() + "</html>"),
					SwingConstants.CENTER);
			jlblMenu.setFont(new Font("맑은 고딕", Font.BOLD, 15));

			JButton btnOrder = new JButton("주문하기");
			btnOrder.setFont(new Font("휴먼엑스포",Font.BOLD,20));
			btnOrder.setBackground(Color.decode("#C13226"));
			btnOrder.setForeground(Color.WHITE);
			btnOrder.setBorderPainted(false);   // 테두리 그리지 않기
			btnOrder.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
			btnOrder.addActionListener(e -> orderBtnClicked(menu));
//			btnOrder.setContentAreaFilled(false); // 버튼의 배경을 투명하게 만듦
			btnOrder.setBorderPainted(false);

			JPanel itemPanel = new JPanel(new GridLayout(1, 3));

			itemPanel.add(jlblImg);
			itemPanel.add(jlblMenu);
			itemPanel.add(btnOrder);

			menuPanel.add(itemPanel);
		}

	}// addMenuItem

	public void orderBtnClicked(MenuVO randomMenu) {
		boolean found = false;
		int totalQuantity = 0, totalPrice = 0;

		StringBuilder menuName = new StringBuilder();
		menuName.append(randomMenu.getMenuName());
		AtomicInteger menuPrice = new AtomicInteger(randomMenu.getPrice()); // 이런게 있었네...
		
		if (randomMenu.getCategoryId() == 1 || randomMenu.getCategoryId() == 2) {
			new AddIngredientsView(umv, menuName, menuPrice, randomMenu.getCategoryId());
		}

		// 이미 장바구니에 추가된 메뉴면 수량 및 금액 증가
		for (int i = 0; i < dtm.getRowCount(); i++) {
			String itemName = (String) dtm.getValueAt(i, 0);
			int quantity = (int) dtm.getValueAt(i, 1);
			int price = (int) dtm.getValueAt(i, 2);

			if (itemName.equals(menuName.toString())) {
				dtm.setValueAt(quantity + 1, i, 1);
				dtm.setValueAt(price + menuPrice.get(), i, 2);
				found = true;
			}
			totalQuantity += (int) dtm.getValueAt(i, 1);
			totalPrice += (int) dtm.getValueAt(i, 2);
		}

		// 아니면 새로 장바구니에 추가
		if (!found) {
			dtm.addRow(new Object[] { menuName.toString(), 1, menuPrice.get(), randomMenu.getMenuId() });
			totalQuantity++;
			totalPrice += randomMenu.getPrice();
		}

		// 총수량 및 총금액 업데이트
		umv.getJtfTotalQuantity().setText(String.valueOf(totalQuantity));
		umv.getJtfTotalPrice().setText(String.valueOf(totalPrice));
	}// orderBtnClicked
}
