package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import kr.co.kiosk.userEvt.PaymentEvt;

@SuppressWarnings("serial")
public class PaymentView extends JFrame {
    private JButton creditcardBtn;
    private JButton giftcardBtn;
    private JButton kakaopayBtn;
    private JButton payCoinBtn;
    private JButton zeropayBtn;
    private JButton otherBtn;
    

    public PaymentView(FinalOrderListView folv, UserMainView umv, int totalPrice, int totalPriceAfterDiscount) {
        setTitle("결제 수단 선택");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 상단 텍스트
        JLabel titleLabel = new JLabel("결제 수단을 선택해 주세요", SwingConstants.CENTER);
        titleLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // 버튼 패널 (세로 정렬)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));

        creditcardBtn = createButton("신용카드");
        creditcardBtn.setPreferredSize(new Dimension(500, 50));
        creditcardBtn.setForeground(Color.WHITE);
        creditcardBtn.setBackground(Color.decode("#D8DAD1"));
        creditcardBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        creditcardBtn.setBorderPainted(false);   // 테두리 그리지 않기
        creditcardBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        creditcardBtn.setMaximumSize(new Dimension(500, 50));
        
        giftcardBtn = createButton("쌍용버거 선불카드");
        giftcardBtn.setForeground(Color.WHITE);
        giftcardBtn.setBackground(Color.decode("#C13226"));
        giftcardBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        giftcardBtn.setBorderPainted(false);   // 테두리 그리지 않기
        giftcardBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        giftcardBtn.setPreferredSize(new Dimension(500, 50));
        giftcardBtn.setMaximumSize(new Dimension(500, 50));

        buttonPanel.add(creditcardBtn);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(giftcardBtn);
        buttonPanel.add(Box.createVerticalStrut(15));

        // 2x2 버튼 패널
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        kakaopayBtn = createButton("카카오페이");
        kakaopayBtn.setForeground(Color.WHITE);
        kakaopayBtn.setBackground(Color.decode("#D8DAD1"));
        kakaopayBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        kakaopayBtn.setBorderPainted(false);   // 테두리 그리지 않기
        kakaopayBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        payCoinBtn = createButton("페이 코인");
        payCoinBtn.setForeground(Color.WHITE);
        payCoinBtn.setBackground(Color.decode("#D8DAD1"));
        payCoinBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        payCoinBtn.setBorderPainted(false);   // 테두리 그리지 않기
        payCoinBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        zeropayBtn = createButton("제로페이");
        zeropayBtn.setForeground(Color.WHITE);
        zeropayBtn.setBackground(Color.decode("#D8DAD1"));
        zeropayBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        zeropayBtn.setBorderPainted(false);   // 테두리 그리지 않기
        zeropayBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        otherBtn = createButton("기타");
        otherBtn.setForeground(Color.WHITE);
        otherBtn.setBackground(Color.decode("#D8DAD1"));
        otherBtn.setFont(new Font("휴먼엑스포", Font.BOLD, 18));
        otherBtn.setBorderPainted(false);   // 테두리 그리지 않기
        otherBtn.setFocusPainted(false);    // 포커스 테두리 제거 (선택사항)
        

        gridPanel.add(kakaopayBtn);
        gridPanel.add(payCoinBtn);
        gridPanel.add(zeropayBtn);
        gridPanel.add(otherBtn);

        buttonPanel.add(gridPanel);
        add(buttonPanel, BorderLayout.CENTER);

        // 하단 총 결제 금액 라벨
        JLabel amountLabel = new JLabel("총 결제 금액: " + totalPriceAfterDiscount + "원", SwingConstants.CENTER);
        amountLabel.setFont(new Font("휴먼엑스포", Font.PLAIN, 14));
        amountLabel.setOpaque(true);
        amountLabel.setBackground(Color.LIGHT_GRAY);
        amountLabel.setPreferredSize(new Dimension(400, 40));
        add(amountLabel, BorderLayout.SOUTH);

        PaymentEvt pme = new PaymentEvt(this, folv, umv, totalPrice, totalPriceAfterDiscount);
        addWindowListener(pme);
        creditcardBtn.addActionListener(pme);
        giftcardBtn.addActionListener(pme);
        kakaopayBtn.addActionListener(pme);
        payCoinBtn.addActionListener(pme);
        zeropayBtn.addActionListener(pme);
        otherBtn.addActionListener(pme);
        
        setVisible(true);

    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("맑은 고딕", Font.PLAIN, 14));
        btn.setPreferredSize(new Dimension(300, 40));
        btn.setMaximumSize(new Dimension(300, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

   
    public JButton getCreditcardBtn() { 
    	return creditcardBtn; 
    	}
    public JButton getGiftcardBtn() { 
    	return giftcardBtn; 
    	}
    public JButton getKakaopayBtn() { 
    	return kakaopayBtn; 
    	}
    public JButton getPayCoinBtn() { 
    	return payCoinBtn; 
    	}
    public JButton getZeropayBtn() { 
    	return zeropayBtn; 
    	}
    public JButton getOtherBtn() { 
    	return otherBtn; 
    	}
}
