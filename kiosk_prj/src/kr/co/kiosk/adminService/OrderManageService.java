package kr.co.kiosk.adminService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.kiosk.dao.AdminOrderDAO;
import kr.co.kiosk.vo.TotalOrderVO;

/**
 *	관리자 주문관리 서비스
 */
public class OrderManageService {

	private AdminOrderDAO aoDAO;
	
	public OrderManageService() {
		this.aoDAO = AdminOrderDAO.getInstance();
	}
	
	//조리중인 리스트 가져오기: 주문번호, 대기번호, 주문자ID, 홀포장, 주문일시, 상태, 총가격 
	public List<TotalOrderVO> totalOrderVOList(int number){
		List<TotalOrderVO> voList = new ArrayList<TotalOrderVO>();
		
		try {
			if(number == 0) {
				voList = aoDAO.getOrderList();
			}
			if(number == 1) {
				voList = aoDAO.getOrderListAll();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return voList;
	}
	
	//주문번호를 매개변수로 해당 주문의 TotalOrderVO 가져오기 
	public Map<String, Object> getOrderVO(int orderId) {
	    Map<String, Object> result = new HashMap<>();
		try {
			result = aoDAO.getOrderVO(orderId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//주문번호를 매개변수로 해당 주문의 메뉴들의 이름과 수량 
	public List<String[]> SelectMenuAndPrice(int orderId){
		List<Map<String, Integer>> hnpMap = new ArrayList<Map<String,Integer>>();
		List<String[]> tableDataStr = new ArrayList<String[]>();
		
		try {
			hnpMap = aoDAO.SelectMenuAndPrice(orderId);
			
			for(Map<String, Integer> map : hnpMap) {
				for(Map.Entry<String, Integer> entry : map.entrySet()) {
					tableDataStr.add(new String[] {entry.getKey().toString(), entry.getValue().toString()});
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tableDataStr; 
	}
	
	//메뉴명으로 menuId가져오기 
	public int getMenuId(String menuName) {
		int menuId = 0;
		
		try {
			menuId = aoDAO.selectMenuId(menuName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return menuId;
	}

	//해당 주문번호의 주문 상태를 조리중, 완료로 변경
	public boolean changeOrderStatus(String status, int orderId) {
		
		boolean result = false;
		
		try {
			result = aoDAO.changeOrderStatus(status, orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	//주문번호를 매개변수로 해당 주문을 삭제
	public boolean deleteOrder(int orderId) {
		
		boolean result = false;
		
		try {
			result = aoDAO.deleteOrder(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
}
