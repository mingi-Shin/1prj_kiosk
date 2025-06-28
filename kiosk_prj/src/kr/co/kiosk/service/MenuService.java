package kr.co.kiosk.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.kiosk.dao.AdminStockDAO;
import kr.co.kiosk.dao.MenuDAO;
import kr.co.kiosk.vo.MenuVO;

public class MenuService {

	public MenuService() {
		  
	}//MenuService
	
	public boolean addMenu(MenuVO mVO) {
		boolean flag = false;
		MenuDAO mDAO = MenuDAO.getInstance();
		AdminStockDAO sDAO = AdminStockDAO.getInstatnce();
		
		try {
			mDAO.insertMenu(mVO);
			
			//카테고리가 2, 3,4,5인 메뉴만 재고관리에 수량0으로 추가_신민기
			if(Arrays.asList(2,3,4,5).contains(mVO.getCategoryId())) {
				sDAO.insertNewStock(mVO);
			}
			
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//addMenu
	
	public boolean modifyMenu(MenuVO mVO) {
		boolean flag = false;
		
		MenuDAO mDAO = MenuDAO.getInstance();
		try {
			flag = mDAO.updateMenu(mVO) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//modifyMenu
	
	public boolean removeMenu(int menuId) {
		boolean flag = false;
		
		MenuDAO mDAO = MenuDAO.getInstance();
		try {
			flag = mDAO.deleteMenu(menuId) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}//removeMenu
	
	public List<MenuVO> searchAllMenu(){
		List<MenuVO> list = null;
		
		MenuDAO mDAO = MenuDAO.getInstance();
		try {
			list = mDAO.selectAllMenu();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}//searchAllMenu
	
	public MenuVO searchMenu(int menuId) {
		MenuVO mVO = null;
		
		MenuDAO mDAO = MenuDAO.getInstance();
		try {
			mVO = mDAO.selectMenu(menuId);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mVO;
	}//searchMenu
	
	public MenuVO searchMenuWithName(String menuName) {
		MenuVO mVO = null;
		MenuDAO mDAO = MenuDAO.getInstance();
		try {
			mVO = mDAO.selectMenuWithName(menuName);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mVO;
				
	}//searchMenuWithName
	     
	/**
	 * menuId를 매개변수로, 재고량과 join해서 제조가능 수량 체크하기 
	 * 왜 얘 push가안되냐고 
	 */
	public int getAvailableCount(int menuId) {
		int availableCnt = 0;
		
		MenuDAO mDAO = MenuDAO.getInstance();
		 
		try {
			availableCnt = mDAO.selectAvailableCount(menuId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return availableCnt;  
	}//getAvailableCount
	
	public Map<Integer, Integer> getAllAvailableCounts() {
		Map<Integer, Integer> map = new HashMap<>();
		MenuDAO mDAO = MenuDAO.getInstance();
	
		try {
			map = mDAO.selectAllAvailableCounts();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return map;
	}
	
}//class
