package kr.co.kiosk.userEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.userView.UseStampView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;
import kr.co.kiosk.vo.MenuVO;

public class UseStampEvt extends WindowAdapter implements ActionListener {
	private UseStampView usv;
	private UserMainView umv;
	private MemberVO memVO;
	private DefaultTableModel dtm;
	private MenuVO mVO;
	private MenuService ms;
	
	public UseStampEvt(UseStampView usv, UserMainView umv, MemberVO memVO) {
		this.usv = usv;
		this.umv = umv;
		this.memVO = memVO;
		this.dtm = umv.getDtm();
		ms = new MenuService();
		
	}
	
	public void updateQuantity() {
		int totalQuantity = 0;
		int totalPrice = 0;

		//장바구니 돌면서 수량 누적
		for (int i = 0; i < dtm.getRowCount(); i++) {
			totalQuantity += (int) dtm.getValueAt(i, 1);
			totalPrice += (int) dtm.getValueAt(i, 2);
		}

		// 총수량 업데이트
		umv.getJtfTotalQuantity().setText(String.valueOf(totalQuantity));
		umv.getJtfTotalPrice().setText(String.valueOf(totalPrice));
	}//updateQuantity
	
	public void findSampleAlready() {
		for(int i=0; i<dtm.getRowCount(); i++) {
			String itemName = (String) dtm.getValueAt(i, 0);
			if (itemName.contains(("(증정)"))){
				dtm.removeRow(i);
				updateQuantity();
				break;
			}
		}
	}//findSampleAlready

	@Override
	public void windowClosing(WindowEvent e) {
		usv.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == usv.getJbtnCancel()) {
			usv.dispose();
		} else if (src == usv.getJbtnStamp1()) {
			findSampleAlready();
			mVO = ms.searchMenuWithName("콜라M");
			dtm.addRow(new Object[] {"(증정)"+mVO.getMenuName(), 1, 0, mVO.getMenuId()});
			updateQuantity();
			umv.setUsingStamps(3);
			usv.dispose();

		} else if (src == usv.getJbtnStamp2()) {
			findSampleAlready();
			mVO = ms.searchMenuWithName("감자튀김M");
			dtm.addRow(new Object[] {"(증정)"+mVO.getMenuName(), 1, 0, mVO.getMenuId()});
			updateQuantity();
			umv.setUsingStamps(5);
			usv.dispose();

		} else if (src == usv.getJbtnStamp3()) {
			findSampleAlready();
			mVO = ms.searchMenuWithName("쌍용버거");
			dtm.addRow(new Object[] {"(증정)"+mVO.getMenuName(), 1, 0, mVO.getMenuId()});
			updateQuantity();
			umv.setUsingStamps(7);
			usv.dispose();

		} else if (src == usv.getJbtnStamp4()) {
			findSampleAlready();
			mVO = ms.searchMenuWithName("HTML버거");
			dtm.addRow(new Object[] {"(증정)"+mVO.getMenuName(), 1, 0, mVO.getMenuId()});
			updateQuantity();
			umv.setUsingStamps(10);
			usv.dispose();

		}

	}// actionPerformed

}
