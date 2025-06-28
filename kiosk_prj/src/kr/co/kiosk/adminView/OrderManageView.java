package kr.co.kiosk.adminView;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import kr.co.kiosk.adminEvt.OrderManageEvt;
import kr.co.kiosk.vo.TotalOrderVO;

public class OrderManageView extends JPanel {
	
	private OrderManageEvt ome;
	
	private OrderDetailView odv; //수정칸 패널 
	
	private DefaultTableModel dtm; //데이터 모델
	
	private JTable jtblOrderStatus;
	
	private String[][] dataLogs;
	
	private int countDataLogs; //주문 개수 (조건: 조리중)  

	public OrderManageView() {
		this.odv = new OrderDetailView(); //우측 상세 텍스트 페이지 
		
		setLayout(null);
		String[] columnNames = {"주문번호", "대기번호", "주문일시", "포장", "상태"};
		
		//테이블 직접 수정 불가
		this.dtm = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		jtblOrderStatus = new JTable(dtm);
		jtblOrderStatus.setRowHeight(30);
		
//컬럼 헤더 및 데이터 중앙정렬화 		
		//각 컬럼의 데이터들의 정렬 방식을 중앙 정렬로 설정
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //보더 선 긋기 

		//TableColumnModel을 사용하여 각 컬럼의 셀 렌더러를 중앙 정렬로 설정
		TableColumnModel columnModel = jtblOrderStatus.getColumnModel();
		for (int i = 0; i < jtblOrderStatus.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
		
		//스크롤 
		JScrollPane jspStatus = new JScrollPane(jtblOrderStatus);
		
		//창크기 설정 및 배치 
		jspStatus.setBounds(30, 10, 470, 600);
		add(jspStatus); 
		
		odv.setBounds(520, 10, 250, 600);
		add(odv);
		
		//JPanel크기 설정 : 없으면 표시 안됨 
		setPreferredSize(new java.awt.Dimension(800, 800)); 
		
		
//이벤트 추가
		OrderManageEvt ome = new OrderManageEvt(this);
		jtblOrderStatus.addMouseListener(ome);
		odv.getJbtnGuitar().addActionListener(ome);
		odv.getJbtnModify().addActionListener(ome);
		odv.getJbtnDelete().addActionListener(ome);
		odv.getJbtnNewList().addActionListener(ome);
		
		
	}
	
	public void updateTable(List<TotalOrderVO> voList) {
		dtm.setRowCount(0); //기존테이블 초기화
		
	    SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm");

	    for (int i = 0; i < voList.size(); i++) {
	        TotalOrderVO vo = voList.get(i);
	        String formattedDate = sdf.format(vo.getOrderDateTime());

	        String[] row = {
	            String.valueOf( vo.getOrderId()),
	            String.valueOf( vo.getOrderWaitingNumber()),
	            formattedDate,
	            vo.getOrderType(),
	            vo.getOrderStatus()
	        };
	        dtm.addRow(row);
	    }
		
	}
	
	
	public OrderDetailView getOdv() {
		return odv;
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public JTable getJtblOrderStatus() {
		return jtblOrderStatus;
	}

	public String[][] getDataLogs() {
		return dataLogs;
	}

	public int getCountDataLogs() {
		return countDataLogs;
	}

	public void setOdv(OrderDetailView odv) {
		this.odv = odv;
	}

	public void setDtm(DefaultTableModel dtm) {
		this.dtm = dtm;
	}

	public void setJtblOrderStatus(JTable jtblOrderStatus) {
		this.jtblOrderStatus = jtblOrderStatus;
	}

	public void setDataLogs(String[][] dataLogs) {
		this.dataLogs = dataLogs;
	}

	public void setCountDataLogs(int countDataLogs) {
		this.countDataLogs = countDataLogs;
	}

	
	
}




/**
 * setLayout의 종류에따라, add된 컴포넌트들의 배치 위치가 달라짐
 * 
 * setLayout(new BoerderLayout()) : 바깥 테두리 기본레이아웃, 생략가능
 * 	add(컴포넌트객체, South||North||West||East )
 * setLayout(new FolowLayout()) : 컴포넌트가 고유 크기를 가지며 왼쪽->오른쪽 순으로 배치
 * 	add(컴포넌트객체)
 * setLayout(new GridLayout(m,n)) : 행, 열을 가지는 레이아웃. 행,열에 맞춰 정렬된다. 컴포넌트가 고유 크기를 잃고, 모두 일정한 크기를 갖게된다.
 *  add(컴포넌트객체)
 *  
 * JPanel 클래스 상속하여, 여러 컴포넌트를 묶어 객체 생성하고, 그 객체를 레이아웃 할 수도 있음 
 * 
 * 컴포넌트 종류: 
 * 	JLabel
 * 	JButton
 * 	JTextArea (scrollbar 있음)
 * 	JTextField (scrollbar 없음, 한줄짜리 텍스트이니까.. )
 * 	JRadioButton ("", true)
 * 	ButtonGroup
 * 		- buttonGroup.add(JRadioButton객체);
 * 	JScrollPane(JTextArea 객체)
 * 	JPasswordField 
 * 	JComboBox<E>()
 * 		- jComboBox.addItem("");
 * 		
 *  //4. 윈도우의 크기 설정(사용자UI)
 *	setSize(400,400);
 *	//5. 윈도우를 보여주기 위한 설정 
 *	setVisible(true);
 *	//6. 윈도우 종료처리 (x버튼 눌렀을때, 인스턴스 소멸되게)
 *	setDefaultCloseOperation(EXIT_ON_CLOSE);
 *
 *
 *	//------------------------ 2월 17일 배움 ---------------------------------
 *	폰트, 색 설정
 *	Font 클래스
 *	Color 클래스
 *
 *	메뉴
 *	JMenuBar
 *	JMenu
 *	JMenuItem
 *		- jmenuBar.add(jmenu);	//메뉴를 메뉴바에 배치 (메뉴바는 사용자에게 제공할 모든 메뉴를 가진다)
 *		- jmenu.add(jmenuItem); //메뉴아이템을 메뉴에 배치
 *	
 *	메뉴바를 Frame설정
 *		- setJMenuBar(jmenuBar); //setJMenuBar로 JFrame에 설치 
 *
 *
 *	Border
 *	- 컴포넌트들을 묶어서 보여줄 때 
 *	- javax.swing.border 패키지에서 Border클래스 제공 
 *	- 
 */


