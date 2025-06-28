package kr.co.kiosk.adminView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kr.co.kiosk.vo.StockSummaryVO;
import kr.co.kiosk.vo.StockUpVO;
import kr.co.kiosk.vo.StockVO;

public class InOutDetailView extends JPanel {

	private DefaultTableModel dtmData; //상단 데이터 리스트 
	private JTable jtblInoutStatus;
	private JButton showDetailOut; //상세보기 
	
	private DefaultListModel<String> dlmDate; //하단 날짜 리스트 
	private JList<String> jlDate;

	public InOutDetailView() {
		
		setLayout(new BorderLayout()); //테이블과 리스트 넣을 건데, Gird로 하든, null로 하든.. 
		
		//----------------- DefaultTableModel -----------------------


		String[] columnNames = {"재료명", "날짜", "입고건수", "출고건수", "입고합계", "출고합계"};
		
		//데이터 직접 수정 불가
		this.dtmData = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		jtblInoutStatus = new JTable(dtmData);
		jtblInoutStatus.setRowHeight(30);
		JScrollPane jspinOut = new JScrollPane(jtblInoutStatus);
		
		JPanel jplData = new JPanel();//입출고 내역 테이블
		jplData.setLayout(new BorderLayout());
		jplData.add(jspinOut, "Center");

//컬럼 헤더 및 데이터 중앙정렬화 		
		//각 컬럼의 데이터들의 정렬 방식을 중앙 정렬로 설정
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //보더 선 긋기 
	
		//TableColumnModel을 사용하여 각 컬럼의 셀 렌더러를 중앙 정렬로 설정
		TableColumnModel columnModel = jtblInoutStatus.getColumnModel();
		for (int i = 0; i < jtblInoutStatus.getColumnCount(); i++) {
	        columnModel.getColumn(i).setCellRenderer(centerRenderer);
	    }
		
		// 패널의 크기 조정
		setPreferredSize(new java.awt.Dimension(700, 580)); // 가로 1000, 세로 600 크기로 설정
		
		//----------------- DefaultListModel -----------------------
	
		dlmDate = new DefaultListModel<String>();
		
		jlDate = new JList<String>(dlmDate);
		jlDate.setFixedCellHeight(30);
		JScrollPane jspDate = new JScrollPane(jlDate);
		
		JLabel titleLabel = new JLabel("조회 날짜");
		
		JPanel jplDate = new JPanel();
		jplDate.setLayout(new BorderLayout());
		jplDate.add("North", titleLabel);
		jplDate.add("Center", jspDate);
		
		
		//JSplitPane 레이아웃 
		JSplitPane jSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jplData, jplDate );
		jSplitPane.setResizeWeight(0.7);
		jSplitPane.setOneTouchExpandable(true);
		add(jSplitPane, "Center");
		
		
	}
	
	//dtmData에 데이터 추가
	public void updateTable(List<StockSummaryVO> voList) {
		dtmData.setRowCount(0); //초기화 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i = 0; i < voList.size(); i++) {
			StockSummaryVO vo = voList.get(i);
			//String formattedDate = sdf.format(vo.getInputDate());
			
			String[] row = {
					vo.getMenuName(),
					vo.getInputDate(),
					String.valueOf(vo.getInCount()),
					String.valueOf(vo.getOutCount()),
					String.valueOf(vo.getInTotal()),
					String.valueOf(vo.getOutTotal()),
			};
			
			dtmData.addRow(row);
		}
		
	}
	
	//주문이 있는 날짜만 가져온다, 설마 휴무일도 없는건 아니겠지  
	public void updateList(List<String> dateList) {
		dlmDate.setSize(0);
		dlmDate.addElement("All");
		for(int i = 0; i < dateList.size(); i++) {
			dlmDate.addElement( dateList.get(i));
		}
		
	}

	public DefaultTableModel getDtmData() {
		return dtmData;
	}

	public JTable getJtblInoutStatus() {
		return jtblInoutStatus;
	}

	public JButton getShowDetailOut() {
		return showDetailOut;
	}

	public DefaultListModel<String> getDlmDate() {
		return dlmDate;
	}

	public JList<String> getJlDate() {
		return jlDate;
	}
	
	
}
