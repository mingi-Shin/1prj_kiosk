package kr.co.kiosk.userEvt;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import kr.co.kiosk.userView.BurgerMenuView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MenuVO;

public class BurgerMenuEvt implements ActionListener {

	private UserMainView umv;
	private JButton btnPrev, btnNext;
	private JPanel menuPanel;
	private DefaultTableModel dtm;
	private List<MenuVO> burgerList; // 버거 메뉴를 담는 VO 리스트

	private int maxPage = 9; // 한 페이지당 최대 메뉴 개수
	private int currentPage = 0; // 현재 페이지 번호

	public BurgerMenuEvt(BurgerMenuView bmv, UserMainView umv) {
		this.umv = umv;
		this.btnPrev = bmv.getBtnPrev();
		this.btnNext = bmv.getBtnNext();
		this.menuPanel = bmv.getMenuPanel();
		this.dtm = umv.getDtm();
		this.burgerList = getBurgerMenu();
	}// BurgerMenuEvt

	// 버거 메뉴를 가져오는 method
	private List<MenuVO> getBurgerMenu() {
		List<MenuVO> burgerList = new ArrayList<MenuVO>();

		for (MenuVO mVO : umv.getAllMenuList()) {
			if (mVO.getCategoryId() == 1 || mVO.getCategoryId() == 2) {
				burgerList.add(mVO);
			}
		}
		return burgerList;
	}// getBurgerMenu
	
	// 데이터를 가져와서 메뉴판을 채우는 method
	public void loadMenu() {
		menuPanel.removeAll(); // 기존 메뉴 삭제

		int start = currentPage * maxPage;
		int end = Math.min(start + maxPage, burgerList.size());

		for (int i = start; i < end; i++) {
			addMenuItem(burgerList.get(i));
		}

		// 부족한 공간 빈패널로 채우기
		while (menuPanel.getComponentCount() < maxPage) {
			menuPanel.add(new JPanel());
		}

		// 버튼 활성화/비활성화 조정
		btnPrev.setEnabled(currentPage > 0);
		btnNext.setEnabled((currentPage + 1) * maxPage < burgerList.size());

		// 메뉴 채우기
		menuPanel.revalidate();
		menuPanel.repaint();
	}// loadMenu

	// 메뉴판용 버튼 개별 생성 method
	public void addMenuItem(MenuVO burgerList) {
		ImageIcon icon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/noChange.jpg"));
		if (burgerList.getImgName() != null) {
			icon = burgerList.getImage();
		}
		JButton btn = new JButton(icon);
		btn.addActionListener(e -> menuBtnClicked(burgerList));
		
		/**
		 * 재고소진에 따른 주문 가능 횟수 표기			 
		 * */
		int availableCnt = umv.getStockMap().getOrDefault(burgerList.getMenuId(),0);
		String alertText = "";
		if (availableCnt <= 0 && burgerList.getCategoryId() != 1) {
		    alertText = "<font color='red'><b>Sold Out!</b></font>";
		    btn.setEnabled(false);
		} 
		
		JLabel lbl = new JLabel(("<html>" + burgerList.getMenuName() + "<br>" + burgerList.getPrice() + "<br>" + alertText + "<html>"),
				SwingConstants.CENTER);
		JPanel itemPanel = new JPanel(new GridLayout(1, 1));
	

		
		
		itemPanel.add(btn);
		itemPanel.add(lbl);

		menuPanel.add(itemPanel);
	}// addMenuItem

	// 메뉴 버튼 클릭 이벤트
	public void menuBtnClicked(MenuVO burgerList) {
		boolean found = false;
		int totalQuantity = 0, totalPrice = 0;

		StringBuilder menuName = new StringBuilder();
		menuName.append(burgerList.getMenuName());
		AtomicInteger menuPrice = new AtomicInteger(burgerList.getPrice()); //이런게 있었네...

		new AddIngredientsView(umv, menuName, menuPrice, burgerList.getCategoryId());

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
			dtm.addRow(new Object[] { menuName.toString(), 1, menuPrice.get(), burgerList.getMenuId() });
			totalQuantity++;
			totalPrice += menuPrice.get();
		}

		// 총수량 및 총금액 업데이트
		umv.getJtfTotalQuantity().setText(String.valueOf(totalQuantity));
		umv.getJtfTotalPrice().setText(String.valueOf(totalPrice));
	}// menuBtnClicked

	@Override
	public void actionPerformed(ActionEvent e) {

		// 다음 버튼 클릭
		if (e.getSource() == btnNext && (currentPage + 1) * maxPage < burgerList.size()) {
			currentPage += 1;
			loadMenu();
		}
		// 이전 버튼 클릭
		if (e.getSource() == btnPrev && currentPage > 0) {
			currentPage -= 1;
			loadMenu();
		}

	}// actionPerformed

}// class
