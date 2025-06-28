package kr.co.kiosk.adminView;


import javax.swing.*;
import javax.swing.border.TitledBorder;

import kr.co.kiosk.adminEvt.AdminLoginEvt;

/**
 * 로그인 디자인
 */
public class AdminLoginView extends JFrame {
    private JTextField jtfId;
    private JPasswordField jpfPass;
    private JLabel jlblMsg;
    private JButton jbtnLogin;
    private JButton jbtnOut;

    public AdminLoginView() {
        super("관리자 로그인");

        // Create the labels
        JLabel jlblId = new JLabel("아이디");
        JLabel jlblPass = new JLabel("비밀번호");
        jbtnLogin = new JButton("로그인");
        jbtnOut = new JButton("취소");

        jtfId = new JTextField(10);
        jpfPass = new JPasswordField(10);
        
        JPanel jpId = new JPanel();
        jpId.setLayout(null);
        jlblId.setBounds(10, 10, 60, 25);
        jtfId.setBounds(80, 10, 150, 25);
        jpId.add(jlblId);
        jpId.add(jtfId);

        JPanel jpPass = new JPanel();
        jpPass.setLayout(null);
        jlblPass.setBounds(10, 10, 60, 25);
        jpfPass.setBounds(80, 10, 150, 25);
        jpPass.add(jlblPass);
        jpPass.add(jpfPass);

        JPanel jpLogin = new JPanel();
        jpLogin.setLayout(null);

        jpId.setBounds(50, 50, 250, 40);
        jpPass.setBounds(50, 100, 250, 40);

        jpLogin.add(jpId);
        jpLogin.add(jpPass);
        
        jbtnLogin.setBounds(50,150,80,40);
        jbtnOut.setBounds(200,150,80,40);
        jpLogin.add(jbtnLogin);
        jpLogin.add(jbtnOut);


        jpLogin.setBorder(new TitledBorder("관리자 페이지 로그인"));

        // jpLogin 패널의 크기 설정
        jpLogin.setBounds(50, 20, 350, 200); // 패널의 크기와 위치 설정

        // 이벤트 리스너 추가
        AdminLoginEvt le = new AdminLoginEvt(this);
        addWindowListener(le);
        jtfId.addActionListener(le);
        jpfPass.addActionListener(le);
        jbtnLogin.addActionListener(le);
        jbtnOut.addActionListener(le);
        // JFrame의 레이아웃을 null로 설정
        setLayout(null);

        // 컴포넌트 추가
        add(jpLogin);

        // JFrame 설정
        setBounds(700, 200, 450, 300); // JFrame 크기 조정
        setVisible(true);
    }

    public JButton getJbtnLogin() {
		return jbtnLogin;
	}

	public JButton getJbtnOut() {
		return jbtnOut;
	}

	public JTextField getJtfId() {
        return jtfId;
    }

    public JPasswordField getJpfPass() {
        return jpfPass;
    }

    public void setJtfId(JTextField jtfId) {
		this.jtfId = jtfId;
	}

	public void setJpfPass(JPasswordField jpfPass) {
		this.jpfPass = jpfPass;
	}

	public JLabel getJlblMsg() {
        return jlblMsg;
    }

    public static void main(String[] args) {
        new AdminLoginView();
    }
}