package kr.co.kiosk.userView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class ReceiptView extends JFrame {

	public ReceiptView(String receiptText, int waitingNumber) {
		setTitle("영수증");
		setSize(405, 650);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 상단
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
		topPanel.setBackground(new Color(255,255,255));
		JLabel title = new JLabel("쌍용버거", SwingConstants.CENTER);
		title.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		title.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel orderNum = new JLabel("주문번호:" + waitingNumber);
		orderNum.setFont(new Font("맑은 고딕", Font.PLAIN, 18));
		orderNum.setAlignmentX(Component.CENTER_ALIGNMENT);

		topPanel.add(title);
		topPanel.add(orderNum);
		add(topPanel, BorderLayout.NORTH);

		// 중단 텍스트영역
		JTextArea receiptArea = new JTextArea();
		receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
		receiptArea.setEditable(false);
		receiptArea.setText(receiptText.toString());
		add(receiptArea, BorderLayout.CENTER);

		
		// 하단 패널
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		bottomPanel.setBackground(new Color(255,255,255));

		ImageIcon originalIcon = new ImageIcon(getClass().getResource("/kr/co/kiosk/assets/BrandLogoBlack.png"));
		Image resizedImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		ImageIcon resizedIcon = new ImageIcon(resizedImage);

		JButton confirmButton = new JButton(resizedIcon);
		confirmButton.setBorderPainted(false); // 테두리 제거
		confirmButton.setContentAreaFilled(false); // 버튼 배경 제거
		confirmButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		confirmButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new MainPageView();
			}
		});

		JLabel thankYou = new JLabel("주문해주셔서 감사합니다!", SwingConstants.CENTER);
		thankYou.setFont(new Font("맑은 고딕", Font.BOLD, 16));
		thankYou.setAlignmentX(Component.CENTER_ALIGNMENT);

		bottomPanel.add(confirmButton);
		bottomPanel.add(thankYou);
		add(bottomPanel, BorderLayout.SOUTH);

		setVisible(true);
	}
}
