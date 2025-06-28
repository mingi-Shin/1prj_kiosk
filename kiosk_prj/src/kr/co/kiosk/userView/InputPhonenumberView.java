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

import kr.co.kiosk.userEvt.InputPhonenumberEvt;

@SuppressWarnings("serial")
public class InputPhonenumberView extends JDialog {

	private JTextField jtfPhoneNumber;
	private JButton[] arrNumpad;
	private JButton jbtnCancel;
	private JButton jbtnConfirm;
	private JButton jbtnClear;
	private JButton jbtnX;

	UserMainView umv;

	public InputPhonenumberView(UserMainView umv, String caller) {
		super((JFrame) null, true);
		this.umv = umv;
		// 프레임 설정
		getContentPane().setBackground(Color.WHITE);
		setLayout(null);
		setBounds(600, 100, 400, 600);
		setLocationRelativeTo(null);

		JLabel jlblTitle = new JLabel("번호 입력", JLabel.CENTER);
		jlblTitle.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
		jlblTitle.setBounds(150, 30, 100, 20);

		jtfPhoneNumber = new JTextField("휴대폰 번호를 입력해주세요", JTextField.CENTER);
		jtfPhoneNumber.setEditable(false);
		jtfPhoneNumber.setBounds(50, 70, 300, 50);

		// 숫자 패드 패널
		JPanel jpBtn = new JPanel(new GridLayout(4, 3));
		jpBtn.setBounds(50, 150, 300, 300);

		arrNumpad = new JButton[10];
		arrNumpad[0] = new JButton(String.valueOf(0));
//        ------------------------------이벤트를 여기서 해야 중복이 덜 발생----------------------
		InputPhonenumberEvt ipe = new InputPhonenumberEvt(this, umv, caller);
		addWindowListener(ipe);

		for (int i = 0; i < 10; i++) {
			arrNumpad[i] = new JButton(String.valueOf(i));
			if (i != 0) {
				jpBtn.add(arrNumpad[i]);
			} // end if
			arrNumpad[i].setForeground(Color.WHITE);
			arrNumpad[i].setBackground(Color.decode("#D8DAD1"));
			arrNumpad[i].setFocusPainted(false); // 포커스 테두리 제거 (선택사항)
			arrNumpad[i].addActionListener(ipe);
			arrNumpad[i].setFont(new Font("휴먼엑스포", Font.BOLD, 18));

		} // end for
		jbtnX = new JButton("x");
        jbtnX.setForeground(Color.WHITE);
        jbtnX.setBackground(Color.decode("#D8DAD1"));
        jbtnX.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnX.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        jpBtn.add(jbtnX);
        jpBtn.add(arrNumpad[0]);
        // 추가 버튼 (지우기, 확인 등)
        jbtnClear = new JButton("clear");
        jbtnClear.setForeground(Color.WHITE);
        jbtnClear.setBackground(Color.decode("#D8DAD1"));
        jbtnClear.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnClear.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        jpBtn.add(jbtnClear);
        
        jbtnCancel = new JButton("취소");
        jbtnCancel.setForeground(Color.WHITE);
        jbtnCancel.setBackground(Color.decode("#D8DAD1"));
        jbtnCancel.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnCancel.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnCancel.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        
        jbtnConfirm = new JButton("조회");
        jbtnConfirm.setForeground(Color.WHITE);
        jbtnConfirm.setBackground(Color.decode("#C13226"));
        jbtnConfirm.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnConfirm.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnConfirm.setFocusPainted(false);  

		jbtnCancel.setBounds(50, 470, 110, 60);
		jbtnConfirm.setBounds(240, 470, 110, 60);

//        ---------------------------추가------------------------------------

		add(jlblTitle);
		add(jtfPhoneNumber);
		add(jpBtn);
		add(jbtnCancel);
		add(jbtnConfirm);

		// --------------------이벤트 추가----------------------

		jbtnClear.addActionListener(ipe);
		jbtnCancel.addActionListener(ipe);
		jbtnConfirm.addActionListener(ipe);
		jbtnX.addActionListener(ipe);

		setVisible(true);
	}

	public JTextField getJtfPhoneNumber() {
		return jtfPhoneNumber;
	}

	public void setJtfPhoneNumber(JTextField jtfPhoneNumber) {
		this.jtfPhoneNumber = jtfPhoneNumber;
	}

	public JButton[] getArrNumpad() {
		return arrNumpad;
	}

	public void setArrNumpad(JButton[] arrNumpad) {
		this.arrNumpad = arrNumpad;
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

	public JButton getJbtnClear() {
		return jbtnClear;
	}

	public void setJbtnClear(JButton jbtnClear) {
		this.jbtnClear = jbtnClear;
	}

	public JButton getJbtnX() {
		return jbtnX;
	}

	public void setJbtnX(JButton jbtnX) {
		this.jbtnX = jbtnX;
	}

}