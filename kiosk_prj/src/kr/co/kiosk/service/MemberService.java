package kr.co.kiosk.service;

import java.sql.SQLException;
import java.util.List;

import kr.co.kiosk.dao.MemberDAO;
import kr.co.kiosk.vo.MemberVO;

public class MemberService {

	public MemberService() {
		
	}//MemberService
	
	public boolean addMember(MemberVO memVO) {
		boolean flag = false;
		MemberDAO memDAO = MemberDAO.getInstance();
				
		try {
			memDAO.insertMember(memVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//addMember
	
	public boolean modifyMember(MemberVO memVO) {
		boolean flag = false;
		MemberDAO memDAO = MemberDAO.getInstance();
		try {
			memDAO.updateMember(memVO);
			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//modifyMember
	
	public boolean removeMember(int memberId) {
		boolean flag = false;
		MemberDAO memDAO = MemberDAO.getInstance();
		try {
			memDAO.deleteMember(memberId);

			flag = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}//removeMember
	
	public List<MemberVO> searchAllMember(){
		List<MemberVO> list = null;
		MemberDAO memDAO = MemberDAO.getInstance();
		try {
			list = memDAO.selectAllMember();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}//searchAllMember
	
	public MemberVO searchMember(int memberId) {
		MemberVO memVO = null;
		MemberDAO memDAO = MemberDAO.getInstance();
		try {
			memVO = memDAO.selectMember(memberId);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memVO;
	}//searchMember

	public MemberVO searchMemberWithPhone(String phoneNumber) {
		MemberVO memVO = null;
		MemberDAO memDAO = MemberDAO.getInstance();
		try {
			memVO = memDAO.selectMemberWithPhone(phoneNumber);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return memVO;
	}//searchMemberWithPhone
	
}//class
