package kr.co.kiosk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.MenuVO;
import kr.co.kiosk.vo.StockSummaryVO;
import kr.co.kiosk.vo.StockUpVO;
import kr.co.kiosk.vo.StockVO;

public class AdminStockDAO {

	private static AdminStockDAO aStockDAO;
	
	private AdminStockDAO() {
		
	}
	
	public static AdminStockDAO getInstatnce() {
		if(aStockDAO == null) {
			aStockDAO = new AdminStockDAO();
		}
		return aStockDAO;
	}
	
	//재고 리스트 불러오기 
	public List<StockVO> getStockListAll() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		List<StockVO> stVOList = new ArrayList<StockVO>();
		
		try {
			con = dbCon.getConn();
			String getStockListQuery = "SELECT MENU_ID, CATEGORY_ID, MENU_NAME, UNIT_NAME, INPUT_DATE, QUANTITY FROM STOCK ORDER BY CATEGORY_ID ASC ";
			
			pstmt = con.prepareStatement(getStockListQuery);
			rs = pstmt.executeQuery();
			
		    while (rs.next()) {
				StockVO sVO = new StockVO();
				sVO.setMenuId(rs.getInt("MENU_ID"));
				sVO.setCategoryId(rs.getInt("CATEGORY_ID"));
				sVO.setMenuName(rs.getString("MENU_NAME"));
				sVO.setUnitName(rs.getString("UNIT_NAME"));
				sVO.setInputDate(rs.getDate("INPUT_DATE"));
				sVO.setQuantity(rs.getInt("QUANTITY"));
				
				stVOList.add(sVO);
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
		return stVOList;
	}
	
	
	//재고에 새 상품 추가하기(MenuService.addMenu()에서 호출)
	public boolean insertNewStock(MenuVO menuVO) throws SQLException {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			
			String query = "INSERT INTO STOCK (MENU_ID, CATEGORY_ID , QUANTITY , INPUT_DATE , MENU_NAME , UNIT_NAME )\r\n"
					+ "VALUES (\r\n"
					+ "?, ?, ?, ?, ?, ?\r\n"
					+ ")";
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, menuVO.getMenuId());
			pstmt.setInt(2, menuVO.getCategoryId());
			pstmt.setInt(3, 0);
			pstmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
			pstmt.setString(5, menuVO.getMenuName());
			pstmt.setString(6, menuVO.getUnitName());
			
			return pstmt.executeUpdate() > 0;
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
	}
	
	
	//해당 상품 입고, 출고(재고량 변경) = isIncrease가 true면 입고, false면 출고
	public boolean updateStock(int menuId, int quantity, boolean isIncrease) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			
			String query = null;
			if(isIncrease == true) { //입고 : 이때의 quantity는 g, ml
				query = " UPDATE STOCK SET QUANTITY = QUANTITY + ? WHERE MENU_ID = ? ";
				pstmt = con.prepareStatement(query);
				
				pstmt.setInt(1, quantity);
				pstmt.setInt(2, menuId);
				
			} else  if(isIncrease == false) { //차감 : 이때의 quantity에는 MENU.WEIGHT * n 만큼 뺴줘야 
				query = "UPDATE STOCK " +
			             "SET QUANTITY = QUANTITY - (SELECT WEIGHT FROM MENU WHERE MENU_ID = ?) * ? " +
			             "WHERE MENU_ID = ?";
				pstmt = con.prepareStatement(query);
				
				pstmt.setInt(1, menuId);
				pstmt.setInt(2, quantity);
				pstmt.setInt(3, menuId);
			}
			return pstmt.executeUpdate() > 0;
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
	}
	
	//
	
	//입고 테이블 작성에 필요한, menuId로 category_id값 가져오기
	public int selectCategory(int menuId) throws SQLException {
		int category = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			String query = " SELECT category_id FROM MENU WHERE MENU_ID = ? ";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, menuId);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
	            category = rs.getInt("category_id");
	        }
			
			return category;
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
	}
	
	//입고 테이블(Stock_up) 작성 : insert
	public boolean insertStockUp(String ioType, int menuId, int categoryId, int quantity, LocalDateTime inputDate ) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
		
			String query = "INSERT INTO STOCK_UP (IO_TYPE, MENU_ID, CATEGORY_ID, QUANTITY_RECEIVED, INPUT_DATE)\r\n"
					+ "VALUES (?, ?, ?, ? ,?) ";
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, ioType);
			pstmt.setInt(2, menuId);
			pstmt.setInt(3, categoryId);
			pstmt.setInt(4, quantity);
			pstmt.setTimestamp(5, Timestamp.valueOf(inputDate));
			
			return pstmt.executeUpdate() > 0;

		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
	}
	
	//입출고 내역 전체 조회
	public List<StockSummaryVO> SelectStockUpList(String date) throws SQLException{
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		List<StockSummaryVO> ssList = new ArrayList<StockSummaryVO>();
		 
		try {
			con = dbCon.getConn();
			String query = null;
			if ("All".equals(date)) {
				query = " SELECT \r\n"
						+ "    s.MENU_NAME, \r\n"
						+ "    to_char(TRUNC(su.INPUT_DATE), 'yyyy-mm-dd') AS 입출고일자,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '입고' THEN 1 ELSE 0 END) AS 입고건수,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '출고' THEN 1 ELSE 0 END) AS 출고건수,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '입고' THEN su.QUANTITY_RECEIVED ELSE 0 END) AS 입고합계,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '출고' THEN su.QUANTITY_RECEIVED * m.WEIGHT ELSE 0 END) AS 출고합계\r\n"
						+ "FROM \r\n"
						+ "    STOCK_UP su\r\n"
						+ "JOIN \r\n"
						+ "    STOCK s ON su.MENU_ID = s.MENU_ID AND su.CATEGORY_ID = s.CATEGORY_ID\r\n"
						+ "JOIN \r\n"
						+ "    MENU m ON su.MENU_ID = m.MENU_ID\r\n"
						+ "GROUP BY \r\n"
						+ "    s.MENU_NAME, TRUNC(su.INPUT_DATE)\r\n"
						+ "ORDER BY \r\n"
						+ "    입출고일자 DESC ";
				pstmt = con.prepareStatement(query);
				
			} else {
				query = " SELECT \r\n"
						+ "    s.MENU_NAME, \r\n"
						+ "    to_char(TRUNC(su.INPUT_DATE), 'yyyy-mm-dd') AS 입출고일자,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '입고' THEN 1 ELSE 0 END) AS 입고건수,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '출고' THEN 1 ELSE 0 END) AS 출고건수,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '입고' THEN su.QUANTITY_RECEIVED ELSE 0 END) AS 입고합계,\r\n"
						+ "    SUM(CASE WHEN su.IO_TYPE = '출고' THEN su.QUANTITY_RECEIVED * m.WEIGHT ELSE 0 END) AS 출고합계\r\n"
						+ "FROM \r\n"
						+ "    STOCK_UP su\r\n"
						+ "JOIN \r\n"
						+ "    STOCK s ON su.MENU_ID = s.MENU_ID AND su.CATEGORY_ID = s.CATEGORY_ID\r\n"
						+ "JOIN \r\n"
						+ "    MENU m ON su.MENU_ID = m.MENU_ID\r\n"
						+ "WHERE\r\n"
						+ "	TRUNC(su.INPUT_DATE) = TO_DATE( ?, 'yyyy-MM-dd')\r\n"
						+ "GROUP BY \r\n"
						+ "    s.MENU_NAME, TRUNC(su.INPUT_DATE)\r\n"
						+ "ORDER BY \r\n"
						+ "    s.MENU_NAME ";
				pstmt = con.prepareStatement(query);
				pstmt.setString(1, date);
			}
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				StockSummaryVO vo = new StockSummaryVO();
				vo.setMenuName(rs.getString("MENU_NAME"));
				vo.setInputDate(rs.getString("입출고일자"));
				vo.setInCount(rs.getInt("입고건수"));
				vo.setOutCount(rs.getInt("출고건수"));
				vo.setInTotal(rs.getInt("입고합계"));
				vo.setOutTotal(rs.getInt("출고합계"));
				
				ssList.add(vo);
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return ssList;
		
	}
	
	//주문이 존재하는 날짜 가져오기 
	public List<Date> selectWorkDays() throws SQLException{
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		List<Date> workDays = new ArrayList<Date>();
		 
		try {
			con = dbCon.getConn();
				
			String query = " SELECT DISTINCT TO_CHAR(INPUT_DATE, 'yyyy-mm-dd') AS ORDER_DATE\r\n"
					+ "FROM STOCK_UP\r\n"
					+ "WHERE INPUT_DATE IS NOT NULL \r\n"
					+ "ORDER BY ORDER_DATE DESC ";
			
			pstmt = con.prepareStatement(query);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				workDays.add( rs.getDate("ORDER_DATE"));
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return workDays;
		
	}
	
	
}
