package kr.co.kiosk.adminService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kr.co.kiosk.dao.AdminStockDAO;
import kr.co.kiosk.vo.StockSummaryVO;
import kr.co.kiosk.vo.StockUpVO;
import kr.co.kiosk.vo.StockVO;

public class StockManageService {

	private AdminStockDAO aoDAO;
	
	public StockManageService() {
		this.aoDAO = AdminStockDAO.getInstatnce();
	}
	
	//재고 가져오기(2:버거, 3:사이드메뉴, 4:음료, 5:재료만 가져옴)
	public List<StockVO> stockVOList(){
		List<StockVO> list = new ArrayList<StockVO>();
		
		try {
			list = aoDAO.getStockListAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list; 
	}
	
	//상품 입고,출고하기(동시에 입고내역 테이블에도 행 생성하기)
	public boolean saveStock(int menuId, int quantity, boolean isIncrease) {
		
		boolean result = false;
		try {
			result = aoDAO.updateStock(menuId, quantity, isIncrease);
			//출고시(isIncrease = false일 때) menuId의 현재 quantity를 확인 후에, 출고할 quantity보다 적으면 ...
			
			int categoryId = aoDAO.selectCategory(menuId); //insertStockUp()에 필요하다.
			
			//STOCK_UP 테이블에도 insert해주기, 단, categoryId가 1(세트)는 빼고
			if(categoryId != 1) {
				LocalDateTime now = LocalDateTime.now();
				if(isIncrease == true) {
					aoDAO.insertStockUp("입고", menuId, categoryId, quantity, now);
				}else if (isIncrease == false) {
					aoDAO.insertStockUp("출고", menuId, categoryId, quantity, now);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//입출고내역 조회 
	public List<StockSummaryVO> selectStockUpAll(String date){
		List<StockSummaryVO> list = new ArrayList<StockSummaryVO>();
		
		try {
			list = aoDAO.SelectStockUpList(date);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
		
	}
	
	//영업일 리스트 
	public List<String> getWorkdays(){
		List<String> list = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			for (int i = 0; i < aoDAO.selectWorkDays().size(); i++) {
				list.add(sdf.format(aoDAO.selectWorkDays().get(i)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
