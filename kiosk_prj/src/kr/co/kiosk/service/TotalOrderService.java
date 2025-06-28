package kr.co.kiosk.service;

import java.sql.SQLException;

import kr.co.kiosk.dao.TotalOrderDAO;
import kr.co.kiosk.vo.TotalOrderVO;

public class TotalOrderService {

	public TotalOrderService() {

	}// TotalOrderService

	public boolean addTotalOrderMember(TotalOrderVO toVO) {
		boolean flag = false;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();

		try {
			toDAO.insertTotalOrderMember(toVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}// addTotalOrderMember

	public boolean addTotalOrderGuest(TotalOrderVO toVO) {
		boolean flag = false;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();

		try {
			toDAO.insertTotalOrderGuest(toVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}// addTotalOrderGuest

	public boolean modifyTotalOrder(TotalOrderVO toVO) {
		boolean flag = false;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();
		try {
			toDAO.updateTotalOrder(toVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}// modifyTotalOrder
	
	public boolean modifyTotalOrderGuests(TotalOrderVO toVO) {
		boolean flag = false;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();
		try {
			toDAO.updateTotalOrderGuests(toVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}// modifyTotalOrderGuests

	public boolean removeTotalOrder(int orderId) {
		boolean flag = false;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();
		try {
			toDAO.deleteTotalOrder(orderId);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}// removeTotalOrder

	public TotalOrderVO searchTotalOrder(int orderId) {
		TotalOrderVO toVO = null;
		TotalOrderDAO toDAO = TotalOrderDAO.getInstance();
		try {
			toVO = toDAO.selectTotalOrder(orderId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return toVO;
	}// searchTotalOrder

}
