package kr.co.kiosk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.kiosk.vo.MemberVO;

public class MemberDAO {

	private static MemberDAO memDAO;

	private MemberDAO() {

	}// MemberDAO

	public static MemberDAO getInstance() {
		if (memDAO == null) {
			memDAO = new MemberDAO();
		}

		return memDAO;
	}// getInstance()

	public void insertMember(MemberVO memVO) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();

			String memberId = "select SEQ_MEMBERS_ID.nextval nextval from dual";
			
			pstmt=con.prepareStatement(memberId);
			rs=pstmt.executeQuery();
			rs.next();
			//번호를 전처리한 후
			memVO.setMemberId( rs.getInt("nextval"));
			
			dbCon.closeDB(rs, pstmt, null);

			String insertMember = "insert into members(member_id, phone_number) values (?, ?)";

			pstmt = con.prepareStatement(insertMember);
			pstmt.setInt(1, memVO.getMemberId());
			pstmt.setString(2, memVO.getPhoneNumber());
			pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
	}// insertMember

	// 아쉽게도 전화번호 변경한 사람은 새로 회원을 파시길
	public int updateMember(MemberVO memVO) throws SQLException {
		int rowCnt = 0;
		Connection con = null;
		PreparedStatement pstmt = null;
		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder updateMember = new StringBuilder();
			updateMember.append("	update members	").append("	set	total_amount=?,points=?,stamps=?,level_id=?		")
					.append("	where member_id=?	");

			pstmt = con.prepareStatement(updateMember.toString());

			pstmt.setInt(1, memVO.getTotalAmount());
			pstmt.setInt(2, memVO.getPoints());
			pstmt.setInt(3, memVO.getStamps());
			pstmt.setInt(4, memVO.getLevelId());
			pstmt.setInt(5, memVO.getMemberId());

			rowCnt = pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}

		return rowCnt;
	}// updateMember

	public int deleteMember(int memberId) throws SQLException {
		int rowCnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();

			String sql = "delete from MEMBERS where MEMBER_ID=?";

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, memberId);

			rowCnt = pstmt.executeUpdate();
		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
		return rowCnt;
	}// deleteMember

	public List<MemberVO> selectAllMember() throws SQLException {
		List<MemberVO> list = new ArrayList<MemberVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			String sql = "select MEMBER_ID, PHONE_NUMBER, TOTAL_AMOUNT, POINTS, STAMPS, LEVEL_ID from members";

			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			MemberVO moVO = null;
			while (rs.next()) {
				moVO = new MemberVO();
				moVO.setMemberId(rs.getInt("member_id"));
				moVO.setPhoneNumber(rs.getString("phone_number"));
				moVO.setTotalAmount(rs.getInt("total_amount"));
				moVO.setPoints(rs.getInt("points"));
				moVO.setStamps(rs.getInt("stamps"));
				moVO.setLevelId(rs.getInt("level_id"));

				list.add(moVO);
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}

		return list;

	}// selectAllMember

	public MemberVO selectMember(int memberId) throws SQLException {
		MemberVO memVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder selectMember = new StringBuilder();
			selectMember.append("	select MEMBER_ID, PHONE_NUMBER, TOTAL_AMOUNT, POINTS, STAMPS, LEVEL_ID	")
					.append("	from MEMBERS where MEMBER_ID=?										  	");

			pstmt = con.prepareStatement(selectMember.toString());

			pstmt.setInt(1, memberId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				memVO = new MemberVO();
				memVO.setMemberId(rs.getInt("member_id"));
				memVO.setPhoneNumber(rs.getString("phone_number"));
				memVO.setTotalAmount(rs.getInt("total_amount"));
				memVO.setPoints(rs.getInt("points"));
				memVO.setStamps(rs.getInt("stamps"));
				memVO.setLevelId(rs.getInt("level_id"));
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return memVO;
	}// selectMember

	public MemberVO selectMemberWithPhone(String phoneNumber) throws SQLException {
		MemberVO memVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder selectMember = new StringBuilder();
			selectMember.append("	select MEMBER_ID, PHONE_NUMBER, TOTAL_AMOUNT, POINTS, STAMPS, LEVEL_ID	")
					.append("	from MEMBERS where PHONE_NUMBER=?										  	");

			pstmt = con.prepareStatement(selectMember.toString());

			pstmt.setString(1, phoneNumber);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				memVO = new MemberVO();
				memVO.setMemberId(rs.getInt("member_id"));
				memVO.setPhoneNumber(rs.getString("phone_number"));
				memVO.setTotalAmount(rs.getInt("total_amount"));
				memVO.setPoints(rs.getInt("points"));
				memVO.setStamps(rs.getInt("stamps"));
				memVO.setLevelId(rs.getInt("level_id"));
			}
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}
		return memVO;

	}// selectMemberWithPhone

}// class
