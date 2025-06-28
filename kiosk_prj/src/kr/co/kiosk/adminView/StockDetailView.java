package kr.co.kiosk.adminView;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kr.co.kiosk.adminEvt.StockManageEvt;
import kr.co.kiosk.vo.StockVO;

public class StockDetailView extends JPanel {

	private DefaultTableModel dtm;
	
	private JTable jtblStockStatus;

	private int countDataLogs; //DefaultTableModel 객체를 생성하는데 필요한 매개변수의 값을 Evt에서 가져오기 위해 클래스변수화(재고에 있는 재료 중에 카테고리가 사이드,음료,추가재료, 부속재료 인 것들의 품목수 -> DAO만들기)
	
	private String[][] dataLogs;
	
	private JButton saveStock;
	
	public StockDetailView() {
		
		setLayout(new BorderLayout());
		
		this.countDataLogs = 20; //임시로 
		String[] columnNames = {"메뉴ID", "카테고리", "상품명", "변동날짜", "재고량"};
		
		//테이블 직접 수정 불가 
		this.dtm = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		dtm.setColumnIdentifiers(columnNames);
		
		jtblStockStatus = new JTable(dtm);
		
		//menuId를 넣고 숨기기위해 설정 
		jtblStockStatus.getColumnModel().getColumn(0).setMinWidth(0);
		jtblStockStatus.getColumnModel().getColumn(0).setMaxWidth(0);
		jtblStockStatus.getColumnModel().getColumn(0).setWidth(0);
		
		jtblStockStatus.setRowHeight(30);
		JScrollPane jspStock = new JScrollPane(jtblStockStatus);
		
		add(jspStock, "Center");
		
		JPanel btnSavePanel = new JPanel();
		btnSavePanel.setLayout(new GridLayout(1,4));
		btnSavePanel.add(new JLabel(""));
		btnSavePanel.add(new JLabel(""));
		btnSavePanel.add(new JLabel(""));
		saveStock = new JButton("재료 입고");
		btnSavePanel.add(saveStock);
		add(btnSavePanel, BorderLayout.SOUTH);
		
//컬럼 헤더 및 데이터 중앙정렬화 		
		//각 컬럼의 데이터들의 정렬 방식을 중앙 정렬로 설정
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		centerRenderer.setBorder(BorderFactory.createLineBorder(Color.BLACK)); //보더 선 긋기 

		//TableColumnModel을 사용하여 각 컬럼의 셀 렌더러를 중앙 정렬로 설정
		TableColumnModel columnModel = jtblStockStatus.getColumnModel();
		for (int i = 0; i < jtblStockStatus.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(centerRenderer);
        }
		
		// 패널의 크기 조정
		setPreferredSize(new java.awt.Dimension(700, 580)); // 가로 1000, 세로 600 크기로 설정

	}
	
	public void updateTable(List<StockVO> voList) {
		dtm.setRowCount(0); //초기화 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i = 0; i < voList.size(); i++) {
			StockVO vo = voList.get(i);
			String formattedDate = sdf.format(vo.getInputDate());
			
			String categoryStr = null;
			switch (vo.getCategoryId()) {
			case 2: {
				categoryStr = "햄버거";
				break;
			}
			case 3: {
				categoryStr = "사이드메뉴";
				break;
			}
			case 4: {
				categoryStr = "음료";
				break;
			}
			case 5: {
				categoryStr = "재료";
				break;
			}
			
			}
			//일정수치 이하의 수량은 앞에 이모티콘 표시
			int menuQuantity = vo.getQuantity();
			if(menuQuantity < 0) { //음수시 0값으로 대체 
				menuQuantity = 0;
			}
			String displayText = (vo.getQuantity() <= 100) ? "⚠️ " + menuQuantity + vo.getUnitName() : menuQuantity + vo.getUnitName();

			String[] row = {
					String.valueOf(vo.getMenuId()), //숨김
					String.valueOf(categoryStr),
					String.valueOf(vo.getMenuName()),
					formattedDate,
					displayText
			};
			
			dtm.addRow(row);
		}
		
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public JTable getJtblStockStatus() {
		return jtblStockStatus;
	}

	public int getCountDataLogs() {
		return countDataLogs;
	}

	public String[][] getDataLogs() {
		return dataLogs;
	}

	public JButton getSaveStock() {
		return saveStock;
	}
	
	
}
