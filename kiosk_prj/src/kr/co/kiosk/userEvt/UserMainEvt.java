
package kr.co.kiosk.userEvt;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.userView.FinalOrderListView;
import kr.co.kiosk.userView.InputPhonenumberView;
import kr.co.kiosk.userView.MainPageView;
import kr.co.kiosk.userView.UseStampView;
import kr.co.kiosk.userView.UserMainView;
import kr.co.kiosk.vo.MemberVO;

public class UserMainEvt extends WindowAdapter implements ActionListener {

	private UserMainView umv;
	private DefaultTableModel dtm;

	private List<String> easterEgg;
	private boolean easterEggUsed = false;

	public UserMainEvt(UserMainView umv) {
		this.umv = umv;
		this.dtm = umv.getDtm();
		easterEgg = new ArrayList<String>();

	} // UserMainEvt

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == umv.getBtnHome()) {
			umv.getFrame().dispose();
			new MainPageView();
		} else if (src == umv.getBtnStamp()) {
			openStampView();
		} else if (src == umv.getBtnRecommendView()) {
			switchPanel("rmv", umv.getBtnRecommendView());
		} else if (src == umv.getBtnBurgerView()) {
			switchPanel("bmv", umv.getBtnBurgerView());
		} else if (src == umv.getBtnSideView()) {
			switchPanel("smv", umv.getBtnSideView());
		} else if (src == umv.getBtnDrinkView()) {
			switchPanel("dmv", umv.getBtnDrinkView());
		} else if (src == umv.getBtnCancelAll())
			cancelAll();
		else if (src == umv.getBtnMinus()) {
			addClick("MINUS");
			itemMinus();
			checkEasterEgg();
		} else if (src == umv.getBtnPlus()) {
			addClick("PLUS");
			itemPlus();
			checkEasterEgg();
		} else if (src == umv.getBtnCancel()) {
			addClick("CANCEL");
			itemCancel();
			checkEasterEgg();
		} else if (src == umv.getBtnOrder())
			itemOrder();
	}// actionPerformed

	public void addClick(String str) {
		easterEgg.add(str);
		if (easterEgg.size() > 5) {
			easterEgg.remove(0);
		}
	}// addClick

	public void checkEasterEgg() {
		List<String> easterEggCode = List.of("PLUS", "PLUS", "MINUS", "MINUS", "CANCEL");
		if (easterEggUsed) {
			easterEgg.clear();
			return; // 이미 사용했으면 아무 일도 안 함
		}

		if (easterEgg.equals(easterEggCode) && umv.getMemberId() != -1) {
			JOptionPane.showMessageDialog(null, "축하합니다!!!\n 10000포인트 획득하셨습니다!", "이스터에그 발견",
					JOptionPane.INFORMATION_MESSAGE);
			MemberService ms = new MemberService();
			MemberVO memVO = ms.searchMember(umv.getMemberId());
			memVO.setPoints(memVO.getPoints() + 10000);
			ms.modifyMember(memVO);
			easterEggUsed = true;
		}
	}// checkEasterEgg

	public void openStampView() {
		if (umv.getMemberId() != -1) { // 이미 앞서 스탬프를 통해 번호 조회를 완료했다면
			MemberService ms = new MemberService();
			new UseStampView(umv, ms.searchMember(umv.getMemberId()));
		} else {
			new InputPhonenumberView(umv, "stamp");
		}
	}// openStampView

	public void resetButtonColors() {
		umv.getBtnRecommendView().setBackground(Color.decode("#C13226"));
		umv.getBtnBurgerView().setBackground(Color.decode("#D8DAD1"));
		umv.getBtnSideView().setBackground(Color.decode("#D8DAD1"));
		umv.getBtnDrinkView().setBackground(Color.decode("#D8DAD1"));
	}// resetButtonColors

	private void switchPanel(String panelName, javax.swing.JButton btn) {
		umv.getCl().show(umv.getJpnlMain(), panelName);

		// 먼저 모든 버튼을 기본 색상으로 초기화
		resetButtonColors();

		// 선택된 버튼만 색상 변경
		if (umv.getBtnRecommendView() == btn) {
			btn.setBackground(Color.decode("#C85566"));
			btn.setForeground(Color.WHITE);
		} else {
			btn.setBackground(Color.WHITE);
			btn.setForeground(Color.decode("#C13226"));
		}
	}// switchPanel

	public void cancelAll() {
		dtm.setNumRows(0);
		umv.getJtfTotalPrice().setText("");
		umv.getJtfTotalQuantity().setText("");
	}// cancelAll

	public void itemMinus() {
		int row = umv.getTable().getSelectedRow();
		if (row == -1)
			return;

		int quantity = (int) dtm.getValueAt(row, 1);
		int price = (int) dtm.getValueAt(row, 2);
		if (quantity == 1)
			return;

		dtm.setValueAt(quantity - 1, row, 1);
		dtm.setValueAt(price - (price / quantity), row, 2);
		updateSummary(-1, -price / quantity);
	}// itemMinus

	public void itemPlus() {
		int row = umv.getTable().getSelectedRow();
		if (row == -1)
			return;
		if (String.valueOf(dtm.getValueAt(row, 0)).contains("(증정)")) {
			return;
		}

		int quantity = (int) dtm.getValueAt(row, 1);
		int price = (int) dtm.getValueAt(row, 2);

		dtm.setValueAt(quantity + 1, row, 1);
		dtm.setValueAt(price + (price / quantity), row, 2);
		updateSummary(1, price / quantity);
	}// itemPlus

	public void itemCancel() {
		int row = umv.getTable().getSelectedRow();
		if (row == -1)
			return;

		int quantity = (int) dtm.getValueAt(row, 1);
		int price = (int) dtm.getValueAt(row, 2);

		dtm.removeRow(row);
		updateSummary(-quantity, -price);

		if (umv.getJtfTotalQuantity().getText().equals("0")) {
			umv.getJtfTotalPrice().setText("");
			umv.getJtfTotalQuantity().setText("");
		}
	}// itemCancel

	public void updateSummary(int quantityDiff, int priceDiff) {
		int totalQty = Integer.parseInt(umv.getJtfTotalQuantity().getText()) + quantityDiff;
		int totalPrice = Integer.parseInt(umv.getJtfTotalPrice().getText()) + priceDiff;

		umv.getJtfTotalQuantity().setText(String.valueOf(totalQty));
		umv.getJtfTotalPrice().setText(String.valueOf(totalPrice));
	}// updateSummary

	public void itemOrder() {
		if (dtm.getRowCount() == 0) {
			JOptionPane.showMessageDialog(umv, "장바구니가 비었습니다!");
			return;
		}

		new FinalOrderListView(umv);
		umv.getFrame().dispose();
	}// itemOrder

}
