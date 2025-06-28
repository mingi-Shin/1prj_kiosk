package kr.co.kiosk.adminView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kr.co.kiosk.adminEvt.financialManageEvt;

public class FinancialManageView extends JPanel {
	private JPanel mainPanel;
	private JLabel yearLabel, monthLabel;
	private JButton prevYearButton, nextYearButton, prevMonthButton, nextMonthButton;
	private JButton[][] dateButtons = new JButton[6][7];
	private String[] daysOfWeek = { "일", "월", "화", "수", "목", "금", "토" };
	private int currentYear, currentMonth;
	private JButton monthManageButton;

	public FinancialManageView() {
		Calendar now = Calendar.getInstance();
		currentYear = now.get(Calendar.YEAR);
		currentMonth = now.get(Calendar.MONTH) + 1;

		// 메인 패널 생성
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		add(mainPanel, BorderLayout.CENTER);

		// 상단 패널: 연도와 월 선택
		JPanel topPanel = new JPanel(new BorderLayout());
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();

		prevMonthButton = new JButton("<");
		nextMonthButton = new JButton(">");

		yearLabel = new JLabel(currentYear + "년", JLabel.LEFT);
		monthLabel = new JLabel(currentMonth + "월", JLabel.LEFT);
		yearLabel.setFont(new Font("고딕",Font.BOLD,30));
		monthLabel.setFont(new Font("고딕",Font.BOLD,30));
		
		monthManageButton = new JButton("월 결산");
		leftPanel.add(yearLabel);
		leftPanel.add(monthLabel);
		leftPanel.add(monthManageButton);
		rightPanel.add(prevMonthButton);
		rightPanel.add(nextMonthButton);
		topPanel.add(leftPanel, BorderLayout.WEST);
		topPanel.add(rightPanel, BorderLayout.EAST);
		topPanel.setPreferredSize(new Dimension(750,40));

		mainPanel.add(topPanel, BorderLayout.NORTH);

		// 달력 날짜 버튼 패널
		JPanel calendarPanel = new JPanel();
		calendarPanel.setLayout(new GridLayout(7, 7)); // 7행 7열
		mainPanel.add(calendarPanel, BorderLayout.CENTER);
		mainPanel.setPreferredSize(new Dimension(750,600));
		// 요일 버튼 추가
		for (int i = 0; i < 7; i++) {
			JLabel dayLabel = new JLabel(daysOfWeek[i], JLabel.CENTER);
			calendarPanel.add(dayLabel); // 첫 번째 행에 요일 표시
		}

		// 날짜 버튼 초기화
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dateButtons[i][j] = new JButton();
				dateButtons[i][j].setEnabled(false); // 날짜 버튼 비활성화
				calendarPanel.add(dateButtons[i][j]);
			}
		}

		// 버튼 이벤트 리스너 추가
		monthManageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new financialManageEvt(currentMonth,0);
			}
		});
		
		prevMonthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeMonth(-1);
			}
		});

		nextMonthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeMonth(1);
			}
		});

		updateCalendar(currentYear, currentMonth);
	}

	private void changeYear(int amount) {
		currentYear += amount;
		yearLabel.setText(currentYear + "년");
		updateCalendar(currentYear, currentMonth);
	}

	private void changeMonth(int amount) {
		currentMonth += amount;
		if (currentMonth < 1) {
			currentMonth = 12;
			currentYear--;
		} else if (currentMonth > 12) {
			currentMonth = 1;
			currentYear++;
		}
		monthLabel.setText(currentMonth + "월");
		yearLabel.setText(currentYear + "년");
		updateCalendar(currentYear, currentMonth);
	}

	private void updateCalendar(int year, int month) {
		// 날짜 버튼 초기화
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				dateButtons[i][j].setText("");
				dateButtons[i][j].setEnabled(false);

				for (ActionListener al : dateButtons[i][j].getActionListeners()) {
					dateButtons[i][j].removeActionListener(al);
				}
			}
		}

		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);

		int startDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
		int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

		int row = 0;
		int col = startDay;
		for (int day = 1; day <= daysInMonth; day++) {
			dateButtons[row][col].setText(String.valueOf(day));
			dateButtons[row][col].setEnabled(true);

			final int selectedDay = day;
			dateButtons[row][col].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					new financialManageEvt(currentMonth,selectedDay);
				}
			});

			col++;
			if (col == 7) {
				col = 0;
				row++;
			}
		}
	}
}