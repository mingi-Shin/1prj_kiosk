package kr.co.kiosk.userView;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.userEvt.UserMainEvt;
import kr.co.kiosk.vo.MenuVO;

@SuppressWarnings("serial")
public class UserMainView extends JFrame {

	private JFrame frame;
	private JTextField jtfTotalQuantity, jtfTotalPrice;

	private DefaultTableModel dtm;
	private JTable table;
	private JScrollPane jsp;

	private CardLayout cl;
	private JPanel jpnlMain, jpnlBtn;

	private JButton btnHome, btnOrder, btnStamp, btnCancelAll, btnRecommendView, btnBurgerView, btnSideView,
			btnDrinkView;
	private JButton btnPlus, btnMinus, btnCancel;

	private RecommendMenuView rmv;
	private BurgerMenuView bmv;
	private SideMenuView smv;
	private DrinkMenuView dmv;

	// 스탬프 아이디 포인트 처리하기가 너무 힘들어서 모든 view가 상속받는 UserMainView의 전역 변수로 설정..
	private int memberId = -1; // 회원아이디
	private int usingPoints; // 사용한 포인트
	private int usingStamps; // 사용한 스탬프

	private boolean isHall;
	private List<MenuVO> allMenuList;
	private Map<Integer, Integer> stockMap;

	public UserMainView(boolean isHall) {
		this.isHall = isHall;
		MenuService ms = new MenuService();
		allMenuList = ms.searchAllMenu();
		stockMap = ms.getAllAvailableCounts();

		frame = new JFrame();
		frame.setBounds(400, 10, 800, 950);
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/BrandLogo.png"));

		// 원하는 크기로 리사이즈 (예: 150x100)
		Image resizedImage = originalIcon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		// JLabel에 적용
		JLabel jlblSsangyongBurger = new JLabel(resizedIcon, JLabel.LEFT);
		jlblSsangyongBurger.setFont(new Font("휴먼엑스포", Font.PLAIN, 18));
		jlblSsangyongBurger.setBounds(12, 0, 115, 45);
		frame.getContentPane().add(jlblSsangyongBurger);

		btnHome = new JButton("HOME");
		btnHome.setBorderPainted(false); // 테두리 그리지 않기
		btnHome.setFocusPainted(false); // 포커스 테두리 제거 (선택사항)
		btnHome.setContentAreaFilled(false); // 버튼 내부 배경 제거 (선택사항)
		btnHome.setForeground(Color.decode("#C13226"));
		btnHome.setBounds(662, 0, 115, 45);
		frame.getContentPane().add(btnHome);

		btnOrder = new JButton("주 문");
		btnOrder.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
		btnOrder.setBackground(Color.decode("#C13226"));
		btnOrder.setForeground(Color.WHITE);
		btnOrder.setBorderPainted(false); // 테두리 그리지 않기
		btnOrder.setFocusPainted(false); // 포커스 테두리 제거 (선택사항)
		btnOrder.setBounds(657, 804, 115, 97);
		frame.getContentPane().add(btnOrder);

		btnStamp = new JButton("스탬프");
		btnStamp.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
		btnStamp.setForeground(Color.WHITE);
		btnStamp.setBackground(Color.decode("#FF8000"));
		btnStamp.setBorderPainted(false); // 테두리 그리지 않기
		btnStamp.setFocusPainted(false); // 포커스 테두리 제거 (선택사항)
		btnStamp.setBounds(530, 804, 115, 97);
		frame.getContentPane().add(btnStamp);

		btnCancelAll = new JButton("<html>전체<br>취소</html>");
		btnCancelAll.setForeground(Color.WHITE);
		btnCancelAll.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
		btnCancelAll.setBackground(Color.decode("#454545"));
		btnCancelAll.setBorderPainted(false); // 테두리 그리지 않기
		btnCancelAll.setFocusPainted(false); // 포커스 테두리 제거 (선택사항)
		btnCancelAll.setBounds(403, 804, 115, 97);
		frame.getContentPane().add(btnCancelAll);

		jtfTotalQuantity = new JTextField("0");
		jtfTotalQuantity.setColumns(10);
		jtfTotalQuantity.setEditable(false);
		jtfTotalQuantity.setBounds(177, 804, 215, 35);
		frame.getContentPane().add(jtfTotalQuantity);

		jtfTotalPrice = new JTextField("0");
		jtfTotalPrice.setColumns(10);
		jtfTotalPrice.setEditable(false);
		jtfTotalPrice.setBounds(177, 864, 215, 35);
		frame.getContentPane().add(jtfTotalPrice);

		JLabel jlblTotalQuantity = new JLabel("수 량");
		jlblTotalQuantity.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
		jlblTotalQuantity.setBounds(65, 804, 85, 35);
		frame.getContentPane().add(jlblTotalQuantity);

		JLabel jlblTotalPrice = new JLabel("금 액");
		jlblTotalPrice.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
		jlblTotalPrice.setBounds(65, 864, 85, 35);
		frame.getContentPane().add(jlblTotalPrice);

		// dtm 설정
		String[] columnName = { "메뉴", "수량", "가격", "메뉴id" };
		dtm = new DefaultTableModel(columnName, 0);

		table = new JTable(dtm);

		table.setDefaultEditor(Object.class, null);
		table.getColumnModel().getColumn(0).setMinWidth(400); // 최소 너비
		table.getColumnModel().getColumn(0).setMaxWidth(400); // 최대 너비
		table.getColumnModel().getColumn(0).setPreferredWidth(400); // 기본 너비

		// 메뉴Id컬럼 숨기기(메뉴ID를 받아와야해서 어쩔 수 없이 dtm에 저장은 하되, 사용자에게는 보이지 않게
		table.getColumnModel().getColumn(3).setMinWidth(0);
		table.getColumnModel().getColumn(3).setMaxWidth(0);
		table.getColumnModel().getColumn(3).setWidth(0);
		table.getColumnModel().getColumn(3).setPreferredWidth(0);

		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.decode("#C13226")); // 배경색
		header.setForeground(Color.WHITE); // 글씨색
		header.setFont(new Font("휴먼엑스포", Font.BOLD, 18));

		// 행의크기 설정.
		table.setRowHeight(38);

		jsp = new JScrollPane(table);
		jsp.setBounds(15, 517, 755, 225);
		frame.getContentPane().add(jsp);

		btnMinus = new JButton("-");
		btnMinus.setBounds(307, 754, 50, 30);
		btnMinus.setFont(new Font("휴먼엑스포",Font.BOLD,15));
		btnMinus.setForeground(Color.WHITE);
		btnMinus.setBackground(Color.decode("#D8DAD1"));
		btnMinus.setBorderPainted(false);   // 테두리 그리지 않기
		btnMinus.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		frame.getContentPane().add(btnMinus);
		btnPlus = new JButton("+");
		btnPlus.setFont(new Font("휴먼엑스포",Font.BOLD,15));
		btnPlus.setForeground(Color.WHITE);
		btnPlus.setBackground(Color.decode("#C13226"));
		btnPlus.setBorderPainted(false);   // 테두리 그리지 않기
		btnPlus.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnPlus.setBounds(367, 754, 50, 30);
		frame.getContentPane().add(btnPlus);
		btnCancel = new JButton("x");
		btnCancel.setFont(new Font("휴먼엑스포",Font.BOLD,15));
		btnCancel.setForeground(Color.WHITE);
		btnCancel.setBackground(Color.decode("#454545"));
		btnCancel.setBorderPainted(false);   // 테두리 그리지 않기
		btnCancel.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnCancel.setBounds(427, 754, 50, 30);
		frame.getContentPane().add(btnCancel);

		// 상단 메뉴바
		jpnlBtn = new JPanel();
		jpnlBtn.setBounds(0, 45, 784, 114);
		frame.getContentPane().add(jpnlBtn);
		jpnlBtn.setLayout(null);

		btnRecommendView = new JButton("추천 메뉴");
		btnRecommendView.setBounds(0, 22, 195, 66);
		btnRecommendView.setBackground(Color.decode("#C13226"));
		btnRecommendView.setFont(new Font("휴먼엑스포",Font.BOLD,20));
		btnRecommendView.setBorderPainted(false);   // 테두리 그리지 않기
		btnRecommendView.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnRecommendView.setForeground(Color.WHITE);
		jpnlBtn.add(btnRecommendView);

		btnBurgerView = new JButton("버거 / 세트 ");
		btnBurgerView.setBackground(Color.decode("#D8DAD1"));
		btnBurgerView.setFont(new Font("휴먼엑스포",Font.PLAIN,20));
		btnBurgerView.setForeground(Color.decode("#C13226"));
		btnBurgerView.setBorderPainted(false);   // 테두리 그리지 않기
		btnBurgerView.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnBurgerView.setBounds(196, 22, 195, 66);
		jpnlBtn.add(btnBurgerView);

		btnSideView = new JButton("사이드 메뉴");
		btnSideView.setBackground(Color.decode("#D8DAD1"));
		btnSideView.setFont(new Font("휴먼엑스포",Font.PLAIN,20));
		btnSideView.setForeground(Color.decode("#C13226"));
		btnSideView.setBorderPainted(false);   // 테두리 그리지 않기
		btnSideView.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnSideView.setBounds(392, 22, 195, 66);
		jpnlBtn.add(btnSideView);

		btnDrinkView = new JButton("음료");
		btnDrinkView.setBackground(Color.decode("#D8DAD1"));
		btnDrinkView.setFont(new Font("휴먼엑스포",Font.PLAIN,20));
		btnDrinkView.setForeground(Color.decode("#C13226"));
		btnDrinkView.setBorderPainted(false);   // 테두리 그리지 않기
		btnDrinkView.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
		btnDrinkView.setBounds(589, 22, 195, 66);
		jpnlBtn.add(btnDrinkView);

		cl = new CardLayout();
		jpnlMain = new JPanel(cl);
		jpnlMain.setBounds(6, 161, 772, 346);
		frame.getContentPane().add(jpnlMain);

		// 카드레이아웃 패널 생성
		rmv = new RecommendMenuView(this);
		bmv = new BurgerMenuView(this);
		smv = new SideMenuView(this);
		dmv = new DrinkMenuView(this);

		jpnlMain.add(rmv, "rmv");
		jpnlMain.add(bmv, "bmv");
		jpnlMain.add(smv, "smv");
		jpnlMain.add(dmv, "dmv");

		UserMainEvt ume = new UserMainEvt(this);
		frame.addWindowListener(ume);

		btnRecommendView.addActionListener(ume);
		btnBurgerView.addActionListener(ume);
		btnSideView.addActionListener(ume);
		btnDrinkView.addActionListener(ume);
		btnCancelAll.addActionListener(ume);
		btnOrder.addActionListener(ume);
		btnPlus.addActionListener(ume);
		btnMinus.addActionListener(ume);
		btnCancel.addActionListener(ume);
		btnHome.addActionListener(ume);
		btnStamp.addActionListener(ume);

		frame.setVisible(true);

	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getJtfTotalQuantity() {
		return jtfTotalQuantity;
	}

	public void setJtfTotalQuantity(JTextField jtfTotalQuantity) {
		this.jtfTotalQuantity = jtfTotalQuantity;
	}

	public JTextField getJtfTotalPrice() {
		return jtfTotalPrice;
	}

	public void setJtfTotalPrice(JTextField jtfTotalPrice) {
		this.jtfTotalPrice = jtfTotalPrice;
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public void setDtm(DefaultTableModel dtm) {
		this.dtm = dtm;
	}

	public JTable getTable() {
		return table;
	}

	public void setTable(JTable table) {
		this.table = table;
	}

	public JScrollPane getJsp() {
		return jsp;
	}

	public void setJsp(JScrollPane jsp) {
		this.jsp = jsp;
	}

	public CardLayout getCl() {
		return cl;
	}

	public void setCl(CardLayout cl) {
		this.cl = cl;
	}

	public JPanel getJpnlMain() {
		return jpnlMain;
	}

	public void setJpnlMain(JPanel jpnlMain) {
		this.jpnlMain = jpnlMain;
	}

	public JPanel getJpnlBtn() {
		return jpnlBtn;
	}

	public void setJpnlBtn(JPanel jpnlBtn) {
		this.jpnlBtn = jpnlBtn;
	}

	public JButton getBtnHome() {
		return btnHome;
	}

	public void setBtnHome(JButton btnHome) {
		this.btnHome = btnHome;
	}

	public JButton getBtnOrder() {
		return btnOrder;
	}

	public void setBtnOrder(JButton btnOrder) {
		this.btnOrder = btnOrder;
	}

	public JButton getBtnStamp() {
		return btnStamp;
	}

	public void setBtnStamp(JButton btnStamp) {
		this.btnStamp = btnStamp;
	}

	public JButton getBtnCancelAll() {
		return btnCancelAll;
	}

	public void setBtnCancelAll(JButton btnCancelAll) {
		this.btnCancelAll = btnCancelAll;
	}

	public JButton getBtnRecommendView() {
		return btnRecommendView;
	}

	public void setBtnRecommendView(JButton btnRecommendView) {
		this.btnRecommendView = btnRecommendView;
	}

	public JButton getBtnBurgerView() {
		return btnBurgerView;
	}

	public void setBtnBurgerView(JButton btnBurgerView) {
		this.btnBurgerView = btnBurgerView;
	}

	public JButton getBtnSideView() {
		return btnSideView;
	}

	public void setBtnSideView(JButton btnSideView) {
		this.btnSideView = btnSideView;
	}

	public JButton getBtnDrinkView() {
		return btnDrinkView;
	}

	public void setBtnDrinkView(JButton btnDrinkView) {
		this.btnDrinkView = btnDrinkView;
	}

	public JButton getBtnPlus() {
		return btnPlus;
	}

	public void setBtnPlus(JButton btnPlus) {
		this.btnPlus = btnPlus;
	}

	public JButton getBtnMinus() {
		return btnMinus;
	}

	public void setBtnMinus(JButton btnMinus) {
		this.btnMinus = btnMinus;
	}

	public JButton getBtnCancel() {
		return btnCancel;
	}

	public void setBtnCancel(JButton btnCancel) {
		this.btnCancel = btnCancel;
	}

	public RecommendMenuView getRmv() {
		return rmv;
	}

	public void setRmv(RecommendMenuView rmv) {
		this.rmv = rmv;
	}

	public BurgerMenuView getBmv() {
		return bmv;
	}

	public void setBmv(BurgerMenuView bmv) {
		this.bmv = bmv;
	}

	public SideMenuView getSmv() {
		return smv;
	}

	public void setSmv(SideMenuView smv) {
		this.smv = smv;
	}

	public DrinkMenuView getDmv() {
		return dmv;
	}

	public void setDmv(DrinkMenuView dmv) {
		this.dmv = dmv;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getUsingPoints() {
		return usingPoints;
	}

	public void setUsingPoints(int usingPoints) {
		this.usingPoints = usingPoints;
	}

	public boolean isHall() {
		return isHall;
	}

	public void setHall(boolean isHall) {
		this.isHall = isHall;
	}

	public List<MenuVO> getAllMenuList() {
		return allMenuList;
	}

	public void setAllMenuList(List<MenuVO> allMenuList) {
		this.allMenuList = allMenuList;
	}

	public int getUsingStamps() {
		return usingStamps;
	}

	public void setUsingStamps(int usingStamps) {
		this.usingStamps = usingStamps;
	}

	public Map<Integer, Integer> getStockMap() {
		return stockMap;
	}

	public void setStockMap(Map<Integer, Integer> stockMap) {
		this.stockMap = stockMap;
	}

}// class
