package kr.co.kiosk.service;

import java.sql.SQLException;
import java.util.List;

import kr.co.kiosk.dao.MenuOrderDAO;
import kr.co.kiosk.vo.MenuOrderVO;

public class MenuOrderService {
	
	public MenuOrderService() {
		
	}//MenuOrderService
	
	public boolean addMenuOrder(MenuOrderVO moVO) {
		boolean flag = false;
		MenuOrderDAO moDAO = MenuOrderDAO.getInstance();
		
		try {
			moDAO.insertMenuOrder(moVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return flag;
	}//addMenuOrder
	
	public boolean modifyMenuOrder(MenuOrderVO moVO) {
		boolean flag = false;
		MenuOrderDAO moDAO = MenuOrderDAO.getInstance();
		try {
			moDAO.updateMenuOrder(moVO);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return flag;
	}//modifyMenuOrder
	
	public boolean removeMenuOrder(int orderId) {
		boolean flag = false;
		MenuOrderDAO moDAO = MenuOrderDAO.getInstance();
		try {
			moDAO.deleteMenuOrder(orderId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//removeMenuOrder
	
	public List<MenuOrderVO> searchMenuOrder(int orderId){
		List<MenuOrderVO> list = null;
		
		MenuOrderDAO moDAO = MenuOrderDAO.getInstance();
		try {
			list = moDAO.selectMenuOrder(orderId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}//searchMenuOrder
	
	public MenuOrderVO searchOneMenuOrder(int orderId, int menuId) {
		MenuOrderVO moVO = null;
		MenuOrderDAO moDAO = MenuOrderDAO.getInstance();
		try {
			moVO = moDAO.selectOneMenuOrder(orderId, menuId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return moVO;
	}//searchOneMenuOrder
	
}//class
