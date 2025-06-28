package kr.co.kiosk.userEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import kr.co.kiosk.userView.UsePointView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;

public class UsePointEvt extends WindowAdapter implements ActionListener {
	private UsePointView upv;
	private UserMainView umv;
	private MemberVO mVO;
	
	private StringBuilder points;

	public UsePointEvt(UsePointView usePointView, UserMainView umv, MemberVO mVO) {
		this.upv = usePointView;
		this.umv = umv;
		this.mVO = mVO;
		points = new StringBuilder();
		umv.setUsingPoints(0); //할인 금액 초기화
		upv.getJtfTotalPoints().setText(String.valueOf(mVO.getPoints()));
	}// InputPhonenumberEvt

	public void UsingPoints() {
		if (points.length() == 0) {
			JOptionPane.showMessageDialog(null, "사용할 금액을 입력해주세요.");
			return;
		}
		int inputValue = Integer.parseInt(points.toString());
		if (Integer.parseInt(points.toString()) > mVO.getPoints()) {
			JOptionPane.showMessageDialog(null, "사용 가능한 최대 금액은 " + mVO.getPoints() + "원 입니다.");
			points.setLength(0);
			points.append(mVO.getPoints());
			upv.getJtfUsingPoints().setText(points.toString());
			return;
		}
		
		umv.setUsingPoints(inputValue);
		upv.dispose();
	}//UsingPoints

	@Override
	public void windowClosing(WindowEvent e) {
		upv.dispose();
	}// windowClosing

	public void inputNumber(int number) {
		points.append(number);
		upv.getJtfUsingPoints().setText(points.toString());
	}// inputNumber

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == upv.getjbtnUse()) {
			UsingPoints();
		}

		if (e.getSource() == upv.getJbtnClear()) {// clear 버튼
			points.setLength(0);
			upv.getJtfUsingPoints().setText("");

		} // end if

		if (e.getSource() == upv.getJbtnCancel()) {// 취소버튼
			upv.dispose();
		} // end if

		for (int i = 0; i < 10; i++) {// 버튼 배열에 0부터 9까지 넣기 위해 반복문으로 처리
			if (e.getSource() == upv.getArrNumpad()[i]) {
				inputNumber(i);
				break;
			}
		}
	}//actionPerformed
	

}
