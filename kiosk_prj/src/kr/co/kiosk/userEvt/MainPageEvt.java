package kr.co.kiosk.userEvt;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import kr.co.kiosk.adminView.AdminLoginView;
import kr.co.kiosk.adminView.AdminMainView;
import kr.co.kiosk.dao.MenuDAO;
import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.userView.MainPageView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.MenuVO;

public class MainPageEvt extends WindowAdapter implements ActionListener {

	private MainPageView mpv;
	private boolean isHall; // 매장식사인지 포장주문인지 판단하는 flag 변수
	public static boolean isOpen = true;
	private List<Integer> easterEgg;
	private boolean flag = true;
	ImageIcon resizedLogo;
	ImageIcon resizedLogo2;
	
	public MainPageEvt(MainPageView mpv) {
		this.mpv = mpv;
		easterEgg = new ArrayList<Integer>();
		
		ImageIcon logo = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/BrandLogo.png"));
		Image originImg = logo.getImage();
		Image resizedImg = originImg.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
		resizedLogo = new ImageIcon(resizedImg);
		
		ImageIcon logo2 = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/BrandLogo-reverse.png"));
		Image originImg2 = logo2.getImage();
		Image resizedImg2 = originImg2.getScaledInstance(400, 350, Image.SCALE_SMOOTH);
		resizedLogo2 = new ImageIcon(resizedImg2);
	}

	@Override
	public void windowClosing(WindowEvent e) {
		mpv.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == mpv.getBtnAdminView()) { // 관리자모드 버튼 클릭시
			mpv.dispose();
			new AdminLoginView().setVisible(true);
			// new AdminMainView().setVisible(true);
		}

		if (e.getSource() == mpv.getBtnHall()) { // 매장식사 버튼 클릭시
			isHall = true;
			if(isOpen == false) {
				JOptionPane.showMessageDialog(mpv, "영업시간이 종료되었습니다.");
			} else {
				new UserMainView(isHall);
				mpv.dispose();
			}
		}

		if (e.getSource() == mpv.getBtnTakeout()) { // 포장주문 버튼 클릭시
			isHall = false;
			if(isOpen == false) {
				JOptionPane.showMessageDialog(mpv, "영업시간이 종료되었습니다.");
			} else {
				new UserMainView(isHall);
				mpv.dispose();
			}
		}
		
		if(e.getSource() == mpv.getBtnImg()) {
			checkEasterEgg(1);
		}
	}
	
	public void checkEasterEgg(int a) {
		easterEgg.add(a);
		if (easterEgg.size() == 5) {
			if(flag == true) {
				mpv.getBtnImg().setIcon(resizedLogo2);
				flag = false;
				easterEgg.clear();
			} else {
				mpv.getBtnImg().setIcon(resizedLogo);
				flag = true;
				easterEgg.clear();
			}
		}
	}// checkEasterEgg

	public boolean isHall() {
		return isHall;
	}

}
