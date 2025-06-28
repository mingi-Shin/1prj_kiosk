package kr.co.kiosk.adminView;

import java.awt.CardLayout;

import javax.swing.JPanel;

/**
 *	카드레이아웃이 적용된 JPanel클래스
 *	AdminMainView에서 메인을 차지한다. 
 */
public class AdminCenterPanel extends JPanel {

	
	private CardLayout cardLayout;
	private JPanel orderPanel, menuPanel, financialPanel, stockPanel, memberPanel;
	
	private OrderManageView orderPage;
	private MenuManageView menuPage;
	private FinancialManageView financialPage;
	private StockManageView stockPage;
	private MemberManageView memberPage;
	
	public AdminCenterPanel() {
		
		//레이아웃을 카드레이아웃으로 변경
		cardLayout = new CardLayout(); 
		setLayout(cardLayout);
		
		//주문관리
		orderPanel = new JPanel();
		orderPage = new OrderManageView();
		orderPanel.add(orderPage);
		
		
		//메뉴관리
		menuPanel = new JPanel();
		menuPage = new MenuManageView();
		menuPanel.add(menuPage);
		
		
		//정산관리
		financialPanel = new JPanel();
		financialPage = new FinancialManageView();
		financialPanel.add(financialPage);
		
		
		//재고관리
		stockPanel = new JPanel();
		stockPage = new StockManageView();
		stockPanel.add(stockPage);
		
		
		//회원관리
		memberPanel = new JPanel();
		memberPage = new MemberManageView();
		memberPanel.add(memberPage);
		
		//cardLayout에 추가
		add(orderPanel, "ORDER");
		add(menuPanel, "MENU");
		add(financialPanel, "FINANCIAL");
		add(stockPanel, "STOCK");
		add(memberPanel, "MEMBER");

		/**
		 * this는 CardLayout이 적용된 컨테이너여야 한다. 
		 */
		cardLayout.show(this, "ORDER");
		
	}
	
	public void showPanel(String panelName) {
		cardLayout.show(this, panelName);
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public JPanel getOrderPanel() {
		return orderPanel;
	}

	public JPanel getMenuPanel() {
		return menuPanel;
	}

	public JPanel getFinancialPanel() {
		return financialPanel;
	}

	public JPanel getStockPanel() {
		return stockPanel;
	}

	public JPanel getMemberPanel() {
		return memberPanel;
	}

	public OrderManageView getOrderPage() {
		return orderPage;
	}

	public MenuManageView getMenuPage() {
		return menuPage;
	}

	public FinancialManageView getFinancialPage() {
		return financialPage;
	}

	public StockManageView getStockPage() {
		return stockPage;
	}

	public MemberManageView getMemberPage() {
		return memberPage;
	}
	
	
	
}
