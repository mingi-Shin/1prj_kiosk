package kr.co.kiosk.userView;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import kr.co.kiosk.userEvt.FinalOrderListEvt;

@SuppressWarnings("serial")
public class FinalOrderListView extends JFrame {

    private JButton jbtnCancel;
    private JButton jbtnDiscount;
    private JButton jbtnPay;
    private JLabel jlblTotalPriceResult;
    private JLabel jlblTotalResult;
    private JLabel jlblDiscountResult;
    private DefaultTableModel dtm;
    
    
    public FinalOrderListView(UserMainView umv) {
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        // ----------------------나의 주문 리스트 타이틀----------------------
        JLabel jlblTitle = new JLabel("나의 주문 리스트", JLabel.CENTER);
        jlblTitle.setFont(new Font("휴먼엑스포", Font.BOLD, 20));
        jlblTitle.setBounds(0, 40, 730, 30);
        
        add(jlblTitle);

        // ----------------------테이블 설정----------------------
        String[] attribute = {"메뉴", "수량", "가격", "메뉴id"};
       
        dtm = new DefaultTableModel(attribute, 0);

        
//-----------------------------------------------------------------------------------------------------
        
        JTable jt = new JTable(dtm);
        jt.setDefaultEditor(Object.class, null);
		jt.getColumnModel().getColumn(0).setMinWidth(400); // 최소 너비
		jt.getColumnModel().getColumn(0).setMaxWidth(400); // 최대 너비
		jt.getColumnModel().getColumn(0).setPreferredWidth(400); // 기본 너비

		// 메뉴Id컬럼 숨기기(메뉴ID를 받아와야해서 어쩔 수 없이 dtm에 저장은 하되, 사용자에게는 보이지 않게
		jt.getColumnModel().getColumn(3).setMinWidth(0);
		jt.getColumnModel().getColumn(3).setMaxWidth(0);
		jt.getColumnModel().getColumn(3).setWidth(0);
		jt.getColumnModel().getColumn(3).setPreferredWidth(0);
//      jt.setBorder(new LineBorder(Color.BLACK, 1));
        jt.setRowHeight(30);

        JTableHeader header = jt.getTableHeader();
        header.setBackground(Color.decode("#C13226")); // 배경색
        header.setForeground(Color.WHITE);     // 글씨색
        header.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        
        JScrollPane jscJt = new JScrollPane(jt);
        jscJt.setBounds(50, 80, 630, 470); 
        jscJt.setBorder(new LineBorder(Color.BLACK, 1));
        add(jscJt);

        // ----------------------합계, 할인, 총 결제 금액 영역----------------------
        Font font = new Font("휴먼엑스포", Font.BOLD, 17);
        
        // 합계 패널
        JPanel jpTotal = new JPanel(null);
        jpTotal.setBackground(Color.GRAY);
        jpTotal.setBounds(50, 570, 630, 40);
        
        JLabel jlblTotal = new JLabel("합계");
        jlblTotal.setFont(font);
        jlblTotal.setBounds(10, 10, 50, 20);
        
        jlblTotalResult = new JLabel();
        jlblTotalResult.setFont(font);
        jlblTotalResult.setBounds(530, 10, 150, 20);
        
        jpTotal.add(jlblTotal);
        jpTotal.add(jlblTotalResult);
        add(jpTotal);

        // ----------------------할인 패널----------------------
        JPanel jpDiscount = new JPanel(null);
        jpDiscount.setBackground(Color.GRAY);
        jpDiscount.setBounds(50, 620, 630, 40);
        
        JLabel jlblDiscount = new JLabel("할인");
        jlblDiscount.setFont(font);
        jlblDiscount.setBounds(10, 10, 50, 20);
        
        jlblDiscountResult = new JLabel();
        jlblDiscountResult.setFont(font);
        jlblDiscountResult.setBounds(530, 10, 150, 20);
        
        jpDiscount.add(jlblDiscount);
        jpDiscount.add(jlblDiscountResult);
        add(jpDiscount);

        // ----------------------총 결제 금액 패널----------------------
        JPanel jpPrice = new JPanel(null);
        jpPrice.setBackground(Color.GRAY);
        jpPrice.setBounds(50, 670, 630, 40);
        
        JLabel jlblTotalPrice = new JLabel("총 결제 금액");
        jlblTotalPrice.setFont(font);
        jlblTotalPrice.setBounds(10, 10, 150, 20);
        
        jlblTotalPriceResult = new JLabel("");
        jlblTotalPriceResult.setFont(font);
        jlblTotalPriceResult.setBounds(530, 10, 150, 20);
        
        jpPrice.add(jlblTotalPrice);
        jpPrice.add(jlblTotalPriceResult);
        add(jpPrice);

        // ----------------------버튼 설정----------------------
        jbtnCancel = new JButton("취소");
        jbtnCancel.setBounds(50, 740, 150, 60);
        jbtnCancel.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnCancel.setForeground(Color.WHITE);
        jbtnCancel.setBackground(Color.decode("#D8DAD1"));
        jbtnCancel.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnCancel.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        add(jbtnCancel);

        jbtnDiscount = new JButton("할인");
        jbtnDiscount.setBounds(290, 740, 150, 60);
        jbtnDiscount.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnDiscount.setForeground(Color.WHITE);
        jbtnDiscount.setBackground(Color.decode("#D8DAD1"));
        jbtnDiscount.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnDiscount.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        add(jbtnDiscount);

        
        jbtnPay = new JButton("결제");
        jbtnPay.setBounds(530, 740, 150, 60);
        jbtnPay.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        jbtnPay.setBackground(Color.decode("#C13226")); // 배경색
        jbtnPay.setForeground(Color.WHITE);     // 글씨색
        jbtnPay.setBorderPainted(false);   // 테두리 그리지 않기
        jbtnPay.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        add(jbtnPay);
        
        // ----------------------이벤트 등록-------------------------
        FinalOrderListEvt fole = new FinalOrderListEvt(this, umv);
        
        
        jbtnCancel.addActionListener(fole);
        jbtnDiscount.addActionListener(fole);	
        jbtnPay.addActionListener(fole);
        
        addWindowListener(fole);
        // ----------------------프레임 설정----------------------
        setBounds(600, 100, 730, 900);
        setLocationRelativeTo(null);
        setVisible(true);
    }

	public JButton getJbtnCancel() {
		return jbtnCancel;
	}

	public JButton getJbtnDiscount() {
		return jbtnDiscount;
	}

	public JButton getJbtnPay() {
		return jbtnPay;
	}

	public JLabel getJlblTotalPriceResult() {
		return jlblTotalPriceResult;
	}

	public JLabel getJlblTotalResult() {
		return jlblTotalResult;
	}

	public JLabel getJlblDiscountResult() {
		return jlblDiscountResult;
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}
	
}