package kr.co.kiosk.adminEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import kr.co.kiosk.adminService.OrderManageService;
import kr.co.kiosk.adminService.StockManageService;
import kr.co.kiosk.adminView.AdminCenterPanel;
import kr.co.kiosk.adminView.AdminMainView;
import kr.co.kiosk.adminView.OrderManageView;
import kr.co.kiosk.userView.MainPageView;
import kr.co.kiosk.vo.StockVO;
import kr.co.kiosk.vo.TotalOrderVO;

public class AdminMainEvt extends WindowAdapter implements ActionListener {
	
	private AdminCenterPanel acp;
	private AdminMainView amv;
	private OrderManageView omv;
	private OrderManageService oms;
	private StockManageService sms;
	
	public AdminMainEvt(AdminMainView amv) {
		this.amv = amv;
		this.acp = amv.getAdminCenterPanel();
		this.omv = amv.getOrderManageView();
		this.oms = new OrderManageService();
		this.sms = new StockManageService();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == amv.getAdminMainButtons().getBtnLogout()) {
			amv.dispose(); //해당창 종료 	
			new MainPageView().setVisible(true); //초기화면 이동 
		}
		
		if(e.getSource() == amv.getAdminMainButtons().getBtnShutdown()) {
		}
		
		if(e.getSource() == amv.getAdminMainButtons().getBtnOrder()) {
			acp.showPanel("ORDER");
			List<TotalOrderVO> voList = oms.totalOrderVOList(0);
			omv.updateTable(voList);
			
			
		}
		if(e.getSource() == amv.getAdminMainButtons().getBtnMenu()) {
			acp.showPanel("MENU");
			
		}
		if(e.getSource() == amv.getAdminMainButtons().getBtnFinancial()) {
			acp.showPanel("FINANCIAL");
			
		}
		if(e.getSource() == amv.getAdminMainButtons().getBtnStock()) {
			acp.showPanel("STOCK");
			amv.getAdminCenterPanel().getStockPage().getScp().showPanel("STOCKDETAIL");
			List<StockVO> list = sms.stockVOList();
			amv.getAdminCenterPanel().getStockPage().getScp().getSdtView().updateTable(list);
			
		}
		if(e.getSource() == amv.getAdminMainButtons().getBtnMember()) {
			acp.showPanel("MEMBER");
			
		}
		
		

	}

}
