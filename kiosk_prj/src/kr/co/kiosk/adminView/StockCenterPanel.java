package kr.co.kiosk.adminView;

import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StockCenterPanel extends JPanel {

	private CardLayout cardLayout;
	
	private JPanel stockPanel, inOutPanel;
	
	private StockDetailView sdtView;
	private InOutDetailView iodtView;
	
	public StockCenterPanel() {
		//재고관리 view단 입니다.
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		
		stockPanel = new JPanel();
		sdtView = new StockDetailView();
		stockPanel.add(sdtView);
		
		inOutPanel = new JPanel();
		iodtView = new InOutDetailView();
		inOutPanel.add(iodtView);
		
		
		//카드레이아웃에 추가 
		add(stockPanel, "STOCKDETAIL");
		add(inOutPanel, "INOUTDETAIL");
		
		cardLayout.show(this, "STOCKDETAIL");
	}
	
	public void showPanel(String panelName) {
		cardLayout.show(this, panelName);
	}

	public CardLayout getCardLayout() {
		return cardLayout;
	}

	public JPanel getStockPanel() {
		return stockPanel;
	}

	public JPanel getInOutPanel() {
		return inOutPanel;
	}

	public StockDetailView getSdtView() {
		return sdtView;
	}

	public InOutDetailView getIodtView() {
		return iodtView;
	}
	
	
}
