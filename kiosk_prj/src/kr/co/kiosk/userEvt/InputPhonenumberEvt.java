package kr.co.kiosk.userEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.userView.InputPhonenumberView;
import kr.co.kiosk.userView.UsePointView;
import kr.co.kiosk.userView.UseStampView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;

public class InputPhonenumberEvt extends WindowAdapter implements ActionListener {
	private InputPhonenumberView ipv;
	private UserMainView umv;
	private StringBuilder phoneNumber;
	private String caller;

	public InputPhonenumberEvt(InputPhonenumberView ipv, UserMainView umv, String caller) {
		this.ipv = ipv;
		this.umv = umv;
		this.caller = caller;
		phoneNumber = new StringBuilder();
	}// InputPhonenumberEvt

	@Override
	public void windowClosing(WindowEvent e) {
		ipv.dispose();
	}// windowClosing

	public void chkMember() {
		String formattedNumber = phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 7) + "-"
				+ phoneNumber.substring(7);

		MemberService ms = new MemberService();
		MemberVO mVO = ms.searchMemberWithPhone(formattedNumber);

		// 회원이면
		if (mVO != null) {
			ipv.dispose();
			umv.setMemberId(mVO.getMemberId());

			if (caller.equals("stamp")) { // 스탬프에서 실행됐으면
				new UseStampView(umv, mVO);
			} else if (caller.equals("point")) { // 할인에서 실행됐으면
				new UsePointView(umv, mVO);
			} else { 
				// 적립에서 실행됐으면 회원 적용만 하고 딱히 암것도 하지 않음
			}
		} else { // 회원이 아니면 회원가입 진행
			int result = JOptionPane.showConfirmDialog(null, "입력하신 번호가 존재하지 않습니다. 회원가입을 진행하시겠습니까?", "회원가입",
					JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION) {
				JOptionPane.showMessageDialog(null, "회원가입 완료");
				mVO = new MemberVO(formattedNumber);
				ms.addMember(mVO);
				ipv.dispose();
				umv.setMemberId(mVO.getMemberId());
			} else {
				ipv.dispose();
			}

		}

	}// chkMember

	/**
	 * 11자이상 적으면 안되서 제한하기 위한 메서드
	 * 
	 * @param result
	 * @return
	 */
	public boolean chkLength(String result) {
		if (result.length() >= 11) {
			JOptionPane.showMessageDialog(null, "전화번호는 11자입니다");
			return false;
		}
		return true;
	}// chkLength

	/**
	 * 처음 3글자가 010으로 시작해야되서 제한하기 위한 메서드
	 * 
	 * @param result
	 * @return
	 */
	public boolean chk010(String result) {
		if (result.length() == 3 && !result.equals("010")) {
			JOptionPane.showMessageDialog(null, "전화번호는 010으로 시작해야 합니다");
			phoneNumber.setLength(0);
			ipv.getJtfPhoneNumber().setText("");
			return false;
		}
		return true;
	}// chk010

	/**
	 * 번호 입력 메서드
	 * 
	 * @param number
	 */
	public void inputNumber(int number) {
		String result = ipv.getJtfPhoneNumber().getText();
		if (result.equals("휴대폰 번호를 입력해주세요")) {
			result = "";
		} // end if

		if (!(chkLength(result)))
			return;
		if (!(chk010(result)))
			return;

		phoneNumber.append(number);
		ipv.getJtfPhoneNumber().setText(phoneNumber.toString());
	}// inputNumber

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == ipv.getJbtnClear()) {
			phoneNumber.setLength(0);
			ipv.getJtfPhoneNumber().setText("");

		} // end if

		if (e.getSource() == ipv.getJbtnX()) {
			if (phoneNumber.length() > 0) {
				phoneNumber.deleteCharAt(phoneNumber.length() - 1);
				ipv.getJtfPhoneNumber().setText(phoneNumber.toString());
			}
		}

		if (e.getSource() == ipv.getJbtnCancel()) {
			ipv.dispose();
		} // end if

		for (int i = 0; i < 10; i++) {// 버튼을 배열로 처리했기 때문에 0~9를 jtf에 보내기 위한 메서드
			if (e.getSource() == ipv.getArrNumpad()[i]) {
				inputNumber(i);
				break;
			} // end if
		} // end for

		if (e.getSource() == ipv.getJbtnConfirm()) {
			if (phoneNumber.length() == 11) {
				chkMember();
			} else {
				JOptionPane.showMessageDialog(null, "전화번호 11자리를 모두 입력해주세요.");
			}
		}
	}// actionPerformed

}
