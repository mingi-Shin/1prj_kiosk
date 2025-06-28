package kr.co.kiosk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.kiosk.vo.TotalOrderVO;

public class AdminOrderDAO {

	private static AdminOrderDAO aOderDAO;
	
	private AdminOrderDAO() {
	}
	
	public static AdminOrderDAO getInstance() {
		if(aOderDAO == null) {
			aOderDAO = new AdminOrderDAO();
		}
		return aOderDAO;
	}
	//금일의 모든 주문(조리중, 완료) 모두 가져오기 
	public List<TotalOrderVO> getOrderListAll() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		List<TotalOrderVO> totalOrderVOList = new ArrayList<TotalOrderVO>();
		try {
			con = dbCon.getConn();
			StringBuilder getOrderListQuery = new StringBuilder();
			getOrderListQuery
				.append("	SELECT 	ORDER_ID, MEMBER_ID , ORDER_TYPE , ORDER_DATETIME , ORDER_STATUS , PRICE, ORDER_WAITING_NUMBER ")
				.append("	FROM TOTAL_ORDER	")
				.append("	WHERE to_char(ORDER_DATETIME, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd')	")
				.append("	ORDER BY ORDER_WAITING_NUMBER ASC	");
			pstmt = con.prepareStatement(getOrderListQuery.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				TotalOrderVO toVO = new TotalOrderVO();
				toVO.setOrderId(rs.getInt("ORDER_ID"));
				toVO.setMemberId((rs.getInt("MEMBER_ID"))); //null -> return 0
				toVO.setOrderType((rs.getString("ORDER_TYPE")));
				toVO.setOrderDateTime(rs.getDate("ORDER_DATETIME"));
				toVO.setOrderStatus(rs.getString("ORDER_STATUS"));
				toVO.setOrderWaitingNumber(rs.getInt("ORDER_WAITING_NUMBER"));
				toVO.setPrice(rs.getInt("PRICE"));
				
				totalOrderVOList.add(toVO);
				
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return totalOrderVOList;
		
	}
	
	//상태값 = '조리중'인 리스트 가져오기, 매개변수 = 주문일시(yy-mm-dd), 상태값(조리중)
	// 주문번호, 대기번호, 주문자ID, 홀/포장, 주문일시, 상태(조리중,완료), 총가격
	public List<TotalOrderVO> getOrderList() throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		List<TotalOrderVO> totalOrderVOList = new ArrayList<TotalOrderVO>();
		try {
			con = dbCon.getConn();
			StringBuilder getOrderListQuery = new StringBuilder();
			getOrderListQuery
				.append("	SELECT 	ORDER_ID, MEMBER_ID , ORDER_TYPE , ORDER_DATETIME , ORDER_STATUS , PRICE, ORDER_WAITING_NUMBER ")
				.append("	FROM TOTAL_ORDER	")
				.append("	WHERE ORDER_STATUS = '조리중' 	")
				.append("	AND to_char(ORDER_DATETIME, 'yyyy-mm-dd') = to_char(sysdate, 'yyyy-mm-dd')	")
				.append(" 	ORDER BY ORDER_WAITING_NUMBER ASC	");
			pstmt = con.prepareStatement(getOrderListQuery.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				TotalOrderVO toVO = new TotalOrderVO();
				toVO.setOrderId(rs.getInt("ORDER_ID"));
				toVO.setMemberId((rs.getInt("MEMBER_ID"))); //null -> return 0
				toVO.setOrderType((rs.getString("ORDER_TYPE")));
				toVO.setOrderDateTime(rs.getDate("ORDER_DATETIME"));
				toVO.setOrderStatus(rs.getString("ORDER_STATUS"));
				toVO.setOrderWaitingNumber(rs.getInt("ORDER_WAITING_NUMBER"));
				toVO.setPrice(rs.getInt("PRICE"));
				
				totalOrderVOList.add(toVO);
				
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
		return totalOrderVOList;
	}
	
	//주문번호를 매개변수로 TotalOrderVO 와 MemberVO의 값 호출 
	public Map<String, Object> getOrderVO(int orderId) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		TotalOrderVO vo = new TotalOrderVO();
		String phoneNumber = null;
	    Map<String, Object> result = new HashMap<>();

		try {
			con = dbCon.getConn();
			String query = "SELECT t.ORDER_ID, t.MEMBER_ID , t.ORDER_TYPE , t.ORDER_DATETIME , t.ORDER_STATUS , t.PRICE, t.ORDER_WAITING_NUMBER, m.PHONE_NUMBER \r\n"
					+ "FROM TOTAL_ORDER t\r\n"
					+ "LEFT JOIN MEMBERS m ON t.MEMBER_ID = m.MEMBER_ID \r\n"
					+ "WHERE ORDER_ID = ? ";
			//LEFT JOIN해야 member_id가 null(비회원)이어도 조회가 된다. 
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, orderId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				vo.setMemberId(rs.getInt("MEMBER_ID"));
				vo.setOrderId(rs.getInt("ORDER_ID"));
				vo.setOrderType(rs.getString("ORDER_TYPE"));
				vo.setOrderStatus(rs.getString("ORDER_STATUS"));
				vo.setOrderWaitingNumber(rs.getInt("ORDER_WAITING_NUMBER"));
				vo.setPrice(rs.getInt("PRICE"));
				vo.setOrderDateTime(rs.getDate("ORDER_DATETIME"));
				phoneNumber = rs.getString("PHONE_NUMBER");
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		result.put("order", vo);
	    result.put("phoneNumber", phoneNumber);
		return result;
	}
	
	
	//주문번호를 매개변수로 해당 주문에 해당하는 메뉴들 이름과 각각의 수량
	public List<Map<String, Integer>> SelectMenuAndPrice (int orderId) throws SQLException{
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		Map<String, Integer> hNpMap = new HashMap<String, Integer>();
		List<Map<String, Integer>> mapList = new ArrayList<Map<String,Integer>>();

		try {
			con = dbCon.getConn();
			StringBuilder hnp = new StringBuilder();
			hnp
				.append("	SELECT m.menu_name, mor.QUANTITY	")
				.append("	FROM TOTAL_ORDER tor	")
				.append("	INNER JOIN MENU_ORDER mor ON mor.ORDER_ID = tor.ORDER_ID	")
				.append("	INNER JOIN MENU m ON mor.MENU_ID = m.MENU_ID	")
				.append("	WHERE tor.ORDER_ID = ? ");
			
			pstmt = con.prepareStatement(hnp.toString());
			
			pstmt.setInt(1, orderId);
			
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				hNpMap.put(rs.getString("MENU_NAME"), rs.getInt("QUANTITY"));
				
				
			}
			mapList.add(hNpMap);
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return mapList;
		
	}
	
	//List<String[메뉴이름, 수량]>을 매개변수로 받아 메뉴ID를 찾아 return
	public int selectMenuId(String menuName) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		int menuId = 0;
		
		try {
			con = dbCon.getConn();
			
			String query = "SELECT menu_id FROM MENU WHERE menu_name = ?";
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, menuName);
			
			rs = pstmt.executeQuery();
			if (rs.next()) {
				menuId = rs.getInt("menu_id");
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return menuId;
	}
	
	
	//조리중,완료 글자로 해당 주문번호의 주문의 상태를 (조리중||완료)로 수정
	public boolean changeOrderStatus(String status, int orderId) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			
			String changeQuery = "UPDATE TOTAL_ORDER SET ORDER_STATUS = ? WHERE ORDER_ID  = ? ";
			
			pstmt = con.prepareStatement(changeQuery);
			
			pstmt.setString(1, status);
			pstmt.setInt(2, orderId);
			
			return pstmt.executeUpdate() > 0; //성공시 true
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
	}
	
	//주문번호를 매개변수로, 해당 주문을 삭제
	public boolean deleteOrder(int orderId) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			
			String changeQuery = "DELETE FROM TOTAL_ORDER WHERE ORDER_ID  = ? ";
			
			pstmt = con.prepareStatement(changeQuery);
			
			pstmt.setInt(1, orderId);
			
			return pstmt.executeUpdate() > 0; //성공시 true
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		
	}
	
	
	
}

