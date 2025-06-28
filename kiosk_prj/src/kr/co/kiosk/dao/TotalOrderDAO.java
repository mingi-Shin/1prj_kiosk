package kr.co.kiosk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.TotalOrderVO;

public class TotalOrderDAO {

	private static TotalOrderDAO toDAO;

	private TotalOrderDAO() {

	}// TotalOrderDAO

	public static TotalOrderDAO getInstance() {
		if (toDAO == null) {
			toDAO = new TotalOrderDAO();
		}

		return toDAO;
	}// getInstance

	// 회원용 주문관리 생성
	public void insertTotalOrderMember(TotalOrderVO toVO) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {

			con = dbCon.getConn();
			String orderId = "SELECT seq_total_order_order__id.NEXTVAL nextval FROM dual";

			pstmt = con.prepareStatement(orderId);
			rs = pstmt.executeQuery();
			rs.next();
			// 번호를 전처리한 후
			toVO.setOrderId(rs.getInt("nextval"));
			dbCon.closeDB(rs, pstmt, null);

			StringBuilder insertTotalOrder = new StringBuilder();
			insertTotalOrder.append("	insert into total_order(order_id, member_id, order_type, order_status)	")
					.append("	values(?,?,?,?)				");

			pstmt = con.prepareStatement(insertTotalOrder.toString());

			pstmt.setInt(1, toVO.getOrderId());
			pstmt.setInt(2, toVO.getMemberId());
			pstmt.setString(3, toVO.getOrderType());
			pstmt.setString(4, toVO.getOrderStatus());

			pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
	}// insertTotalOrderMember

	// 비회원용 주문관리 생성
	public void insertTotalOrderGuest(TotalOrderVO toVO) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			
			String orderId = "SELECT seq_total_order_order__id.NEXTVAL nextval FROM dual";

			pstmt = con.prepareStatement(orderId);
			rs = pstmt.executeQuery();
			rs.next();
			
			// 번호를 전처리한 후
			toVO.setOrderId(rs.getInt("nextval"));
			dbCon.closeDB(rs, pstmt, null);
			
			StringBuilder insertTotalOrder = new StringBuilder();
			insertTotalOrder.append("	insert into total_order(order_id,order_type, order_status)	")
					.append("	values(?,?,?)				");

			pstmt = con.prepareStatement(insertTotalOrder.toString());

			pstmt.setInt(1, toVO.getOrderId());
			pstmt.setString(2, toVO.getOrderType());
			pstmt.setString(3, toVO.getOrderStatus());

			pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
	}// insertTotalOrderMember

	public int updateTotalOrder(TotalOrderVO toVO) throws SQLException {
		int rowCnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			
			String orderId = "SELECT SEQ_TOTAL_ORDER_ORDER_WAITING_NUMBER.nextval FROM dual";

			pstmt = con.prepareStatement(orderId);
			rs = pstmt.executeQuery();
			rs.next();
			
			toVO.setOrderWaitingNumber(rs.getInt("nextval"));
			dbCon.closeDB(rs, pstmt, null);
			
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE TOTAL_ORDER ")
					.append("SET MEMBER_ID=?, ORDER_DATETIME=sysdate, PRICE=?, ORDER_STATUS=?, ORDER_WAITING_NUMBER=?  ")
					.append("WHERE ORDER_ID=?");

			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, toVO.getMemberId());
			pstmt.setInt(2, toVO.getPrice());
			pstmt.setString(3, toVO.getOrderStatus());
			pstmt.setInt(4, toVO.getOrderWaitingNumber());
			pstmt.setInt(5, toVO.getOrderId());

			rowCnt = pstmt.executeUpdate();
		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
		return rowCnt;
	}// updateTotalOrder
	
	//게스트 전용 업데이트
	public int updateTotalOrderGuests(TotalOrderVO toVO) throws SQLException {
		int rowCnt = 0;
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		DbConnection dbCon = DbConnection.getInstance();
		
		try {
			con = dbCon.getConn();
			
			String orderId = "SELECT SEQ_TOTAL_ORDER_ORDER_WAITING_NUMBER.nextval FROM dual";

			pstmt = con.prepareStatement(orderId);
			rs = pstmt.executeQuery();
			rs.next();
			
			toVO.setOrderWaitingNumber(rs.getInt("nextval"));
			dbCon.closeDB(rs, pstmt, null);
			
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE TOTAL_ORDER ")
			.append("SET ORDER_DATETIME=sysdate, PRICE=?, ORDER_STATUS=?, ORDER_WAITING_NUMBER=? ")
			.append("WHERE ORDER_ID=?");
			
			pstmt = con.prepareStatement(sql.toString());
			
			pstmt.setInt(1, toVO.getPrice());
			pstmt.setString(2, toVO.getOrderStatus());
			pstmt.setInt(3, toVO.getOrderWaitingNumber());
			pstmt.setInt(4, toVO.getOrderId());
			
			rowCnt = pstmt.executeUpdate();
		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
		return rowCnt;
	}// updateTotalOrderGuest

	public int deleteTotalOrder(int orderId) throws SQLException {
		int rowCnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;

		DbConnection dbCon = DbConnection.getInstance();
		try {
			con = dbCon.getConn();

			StringBuilder deleteMenu = new StringBuilder();
			deleteMenu.append("	delete from total_order	").append("	where order_id=?		");

			pstmt = con.prepareStatement(deleteMenu.toString());

			pstmt.setInt(1, orderId);

			rowCnt = pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
		return rowCnt;
	}// deleteTotalOrder

	public TotalOrderVO selectTotalOrder(int orderId) throws SQLException {
		TotalOrderVO toVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT ORDER_ID, MEMBER_ID, ORDER_DATETIME, PRICE, ")
					.append("ORDER_TYPE, ORDER_STATUS, ORDER_WAITING_NUMBER ")
					.append("FROM TOTAL_ORDER WHERE ORDER_ID=?");

			pstmt = con.prepareStatement(sql.toString());

			pstmt.setInt(1, orderId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				toVO = new TotalOrderVO();
				toVO.setOrderId(rs.getInt("order_id"));
				toVO.setMemberId(rs.getInt("member_id"));
				toVO.setOrderDateTime(rs.getDate("order_datetime"));
				toVO.setPrice(rs.getInt("price"));
				toVO.setOrderType(rs.getString("order_type"));
				toVO.setOrderStatus(rs.getString("order_status"));
				toVO.setOrderWaitingNumber(rs.getInt("order_waiting_number"));
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return toVO;
	}// selectTotalOrder
}
