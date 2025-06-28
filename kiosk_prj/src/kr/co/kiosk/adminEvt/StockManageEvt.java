package kr.co.kiosk.adminEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import kr.co.kiosk.adminService.StockManageService;
import kr.co.kiosk.adminView.InOutDetailView;
import kr.co.kiosk.adminView.StockDetailView;
import kr.co.kiosk.adminView.StockManageView;
import kr.co.kiosk.vo.StockSummaryVO;
import kr.co.kiosk.vo.StockUpVO;
import kr.co.kiosk.vo.StockVO;

public class StockManageEvt extends WindowAdapter implements ActionListener, MouseListener {

	private StockManageView smv;
	
	private StockManageService sms;
	
	private StockDetailView sdv;
	
	private InOutDetailView iodv;
	
	private String menuIdStr = null;
	private String menuName = null;
	private String orderDate = null;
	
	public StockManageEvt(StockManageView smv) {
		this.smv = smv;
		this.sms = new StockManageService();
		this.sdv = smv.getScp().getSdtView();
		this.iodv = smv.getScp().getIodtView();
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		int selectedRowNum = sdv.getJtblStockStatus().getSelectedRow();

		if(selectedRowNum != -1) {
			this.menuIdStr = sdv.getJtblStockStatus().getValueAt(selectedRowNum, 0).toString(); //숨겨놨음
			
			this.menuName = sdv.getJtblStockStatus().getValueAt(selectedRowNum, 2).toString();
		}
		
		//리스트에서 클릭한 날짜 값 
		this.orderDate = iodv.getJlDate().getSelectedValue();
		List<StockSummaryVO> voList = sms.selectStockUpAll(orderDate);
		smv.getScp().getIodtView().updateTable(voList);
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		
		if(e.getSource() == smv.getStockDetail()) {
			List<StockVO> voList = sms.stockVOList();
			smv.getScp().showPanel("STOCKDETAIL");
			
			smv.getScp().getSdtView().updateTable(voList);
			
		}
		
		if(e.getSource() == smv.getInOutDetail()) {
			smv.getScp().showPanel("INOUTDETAIL");
			List<StockSummaryVO> voList = sms.selectStockUpAll("All");
			smv.getScp().getIodtView().updateTable(voList);
			
			List<String> dateList = sms.getWorkdays();
			smv.getScp().getIodtView().updateList(dateList);
		}
		
		if(e.getSource() == sdv.getSaveStock()) {
			
			 if (menuIdStr == null || menuName == null) {
			        JOptionPane.showMessageDialog(null, "먼저 테이블에서 재고 항목을 선택해주세요.");
			        return;
			    }
			 
			String input = JOptionPane.showInputDialog(
					null,
					menuName + "를 클릭하셨습니다. \n입고할 수량을 입력하세요 : ",
					"입고하기",
					JOptionPane.PLAIN_MESSAGE
					);
			if (input == null) {
			    return;
			} else if (input.isBlank()) {
			    return;
			}
			
			try {
				int quantity = Integer.parseInt(input);
				int menuId = Integer.parseInt(menuIdStr);
				sms.saveStock(menuId, quantity, true); //여기서 quantity는 ml, g 임
				
				JOptionPane.showMessageDialog(iodv, menuName + " 상품을 " + quantity + "만큼 추가하였습니다.");
				
			} catch (NumberFormatException ex) {
				ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "숫자만 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
			} finally {
				//목록 갱신 
				List<StockVO> voList = sms.stockVOList();
				smv.getScp().getSdtView().updateTable(voList);
			}
			
		}
		
		

	}

}
