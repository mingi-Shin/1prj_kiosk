package kr.co.kiosk.userView;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import kr.co.kiosk.userEvt.UseStampEvt;
import kr.co.kiosk.vo.MemberVO;

@SuppressWarnings("serial")
public class UseStampView extends JDialog {
	private JTextField jtfStamp;
	private JButton jbtnCancel;
	private JButton jbtnConfirm;
	private JButton jbtnStamp1;
	private JButton jbtnStamp2;
	private JButton jbtnStamp3;
	private JButton jbtnStamp4;
	private JLabel jlblStamp;

	public UseStampView(UserMainView umv, MemberVO mVO) {
		super((JFrame) null, true);
		// 프레임 설정
		setLayout(null);
		setBounds(600, 100, 400, 700);
		setLocationRelativeTo(null);


		// 제목
		JLabel jlblTitle = new JLabel("스탬프 사용", JLabel.CENTER);
		jlblTitle.setFont(new Font("맑은 고딕", Font.BOLD, 15));
		jlblTitle.setBounds(150, 30, 100, 20);

		jlblStamp = new JLabel("보유 스탬프");
		jlblStamp.setBounds(20, 70, 100, 50);
		// 보유 스탬프 jtf
		jtfStamp = new JTextField(String.valueOf(mVO.getStamps()));
		jtfStamp.setEditable(false); // 편집 불가
		jtfStamp.setBounds(130, 70, 220, 50);

		// 버튼 패널
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(4, 1, 0, 10)); // 4개의 버튼, 세로로 10px 간격

		// 버튼 네개
		jbtnStamp1 = new JButton("콜라M                             필요 스탬프 : 3");
		jbtnStamp2 = new JButton("감자튀김M                             필요 스탬프 : 5");
		jbtnStamp3 = new JButton("쌍용버거                             필요 스탬프 : 7");
		jbtnStamp4 = new JButton("HTML버거                             필요 스탬프 : 10");

//		jbtnStamp1.setForeground(Color.decode("#C13226"));
		jbtnStamp1.setBackground(Color.decode("#D8DAD1"));
//		jbtnStamp1.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		jbtnStamp1.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		
//		jbtnStamp2.setForeground(Color.decode("#C13226"));
		jbtnStamp2.setBackground(Color.decode("#D8DAD1"));
//		jbtnStamp2.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		jbtnStamp2.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		
//		jbtnStamp3.setForeground(Color.decode("#C13226"));
		jbtnStamp3.setBackground(Color.decode("#D8DAD1"));
//		jbtnStamp3.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		jbtnStamp3.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		
//		jbtnStamp4.setForeground(Color.decode("#C13226"));
		jbtnStamp4.setBackground(Color.decode("#D8DAD1"));
//		jbtnStamp4.setFont(new Font("휴먼엑스포", Font.BOLD, 10));
		jbtnStamp4.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		
		jbtnStamp1.setEnabled(false);
		jbtnStamp2.setEnabled(false);
		jbtnStamp3.setEnabled(false);
		jbtnStamp4.setEnabled(false);

		int stampCount = mVO.getStamps(); // 스탬프 개수

		if (stampCount >= 3)
			jbtnStamp1.setEnabled(true);
		if (stampCount >= 5)
			jbtnStamp2.setEnabled(true);
		if (stampCount >= 7)
			jbtnStamp3.setEnabled(true);
		if (stampCount >= 10)
			jbtnStamp4.setEnabled(true);

		// 버튼들을 버튼 패널에 추가
		buttonPanel.add(jbtnStamp1);
		buttonPanel.add(jbtnStamp2);
		buttonPanel.add(jbtnStamp3);
		buttonPanel.add(jbtnStamp4);

		buttonPanel.setBounds(50, 140, 300, 350);

		// 기존 버튼 설정 (취소, 사용 버튼)
		jbtnCancel = new JButton("취소");
		jbtnCancel.setForeground(Color.WHITE);
        jbtnCancel.setBackground(Color.decode("#D8DAD1"));
        jbtnCancel.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnCancel.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnCancel.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		jbtnConfirm = new JButton("사용");
		jbtnConfirm.setForeground(Color.WHITE);
		jbtnConfirm.setBackground(Color.decode("#C13226"));
		jbtnConfirm.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
		jbtnConfirm.setBorderPainted(false);   // 테두리 그리지 않기
		jbtnConfirm.setFocusPainted(false);    //

		// 취소, 사용 버튼 위치 설정
		jbtnCancel.setBounds(50, 510, 110, 60);
		jbtnConfirm.setBounds(240, 510, 110, 60);

		// 이벤트 리스너 설정
		UseStampEvt ipe = new UseStampEvt(this, umv, mVO);
		addWindowListener(ipe);

		// 추가
		add(jlblTitle);
		add(jtfStamp);
		add(buttonPanel);
		add(jbtnCancel);
		add(jbtnConfirm);
		add(jlblStamp);

		// 각 버튼에 이벤트 리스너 추가
		jbtnCancel.addActionListener(ipe);
		jbtnConfirm.addActionListener(ipe);
		jbtnStamp1.addActionListener(ipe);
		jbtnStamp2.addActionListener(ipe);
		jbtnStamp3.addActionListener(ipe);
		jbtnStamp4.addActionListener(ipe);

		setVisible(true); // 프레임을 보이게 설정
	}

	public JTextField getJtfStamp() {
		return jtfStamp;
	}

	public void setJtfStamp(JTextField jtfStamp) {
		this.jtfStamp = jtfStamp;
	}

	public JButton getJbtnCancel() {
		return jbtnCancel;
	}

	public void setJbtnCancel(JButton jbtnCancel) {
		this.jbtnCancel = jbtnCancel;
	}

	public JButton getJbtnConfirm() {
		return jbtnConfirm;
	}

	public void setJbtnConfirm(JButton jbtnConfirm) {
		this.jbtnConfirm = jbtnConfirm;
	}

	public JButton getJbtnStamp1() {
		return jbtnStamp1;
	}

	public void setJbtnStamp1(JButton jbtnStamp1) {
		this.jbtnStamp1 = jbtnStamp1;
	}

	public JButton getJbtnStamp2() {
		return jbtnStamp2;
	}

	public void setJbtnStamp2(JButton jbtnStamp2) {
		this.jbtnStamp2 = jbtnStamp2;
	}

	public JButton getJbtnStamp3() {
		return jbtnStamp3;
	}

	public void setJbtnStamp3(JButton jbtnStamp3) {
		this.jbtnStamp3 = jbtnStamp3;
	}

	public JButton getJbtnStamp4() {
		return jbtnStamp4;
	}

	public void setJbtnStamp4(JButton jbtnStamp4) {
		this.jbtnStamp4 = jbtnStamp4;
	}

	public JLabel getJlblStamp() {
		return jlblStamp;
	}

	public void setJlblStamp(JLabel jlblStamp) {
		this.jlblStamp = jlblStamp;
	}

}//class
