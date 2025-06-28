package kr.co.kiosk.adminView;

import java.awt.Font;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.adminEvt.OrderManageEvt;
import kr.co.kiosk.vo.TotalOrderVO;

/**
 * 주문관리 리스트 컬럼 =

	주문대기번호, 메뉴명, 홀/포장, 상태 를 컬럼으로,
	detail에서는 + 회원ID, 총가격, 주문일시(시간)
 * 
 */
public class OrderDetailView extends JPanel{

	private JTextField jtfOrderId;
	private JTextField jtfOrderWaitingNum;
	private JTextField jtfMemberID;
	private JTextField jtfTotalQuantity;
	private JTextField jtfTakeOut;
	private JTextField jtfOrderStatus;
	private JTextField jtfTotalPrice;
	private JTextField jtfOrderDate;
	
	private JRadioButton jrbOrdering;
	private JRadioButton jrbMaking;
	private JRadioButton jrbDone;
	private ButtonGroup bgMakingDone;
	
	private JButton jbtnModify; //주문수정 (주문수정시, 주문상태에 따라 재고 테이블과 회원주문내역에 영향을 미친다 -> 트리거 쓸까?)
	private JButton jbtnDelete; //주문삭제
	private JButton jbtnNewList; //리스트갱신
	private JButton jbtnGuitar; //기타
	
	//조그마한 해당 주문에 속한 메뉴리스트 스크롤창(메뉴, 수량)
	private DefaultTableModel dtmOrderDetail;
	private JTable jtblOrderDetail;
	private JScrollPane jspOrderDetail;
	
	private int countDataLogs;
	
	private OrderManageEvt ome;
	
	private OrderManageView omv;
	
	public OrderDetailView() {

		setLayout(null);
		
		String[] columnNames = {"메뉴명", "수량"};
		
		//상단 메뉴 상세 리스트 : 데이터 테이블
		this.dtmOrderDetail = new DefaultTableModel(columnNames, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		this.jtblOrderDetail = new JTable(dtmOrderDetail);
		this.jtblOrderDetail.setRowHeight(30);
		this.jspOrderDetail = new JScrollPane(jtblOrderDetail);
		
		//라벨과 텍스트창을 패널로 감싸기 
		
		//OrderId
		JLabel jlbOrderId = new JLabel("주문번호 : ", JLabel.CENTER );
		jlbOrderId.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfOrderId = new JTextField(10);
		jtfOrderId.setEditable(false);
		JLabel jlbOrderWaitingNum = new JLabel("대기번호 : ", JLabel.CENTER);
		jlbOrderWaitingNum.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfOrderWaitingNum = new JTextField(10);
		jtfOrderWaitingNum.setEditable(false);
		JLabel jlbTakeOut = new JLabel("홀/포장 : ", JLabel.CENTER);
		jlbTakeOut.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfTakeOut = new JTextField(10);
		jtfTakeOut.setEditable(false);
		JLabel jlbOrderDate = new JLabel("주문일시 : ", JLabel.CENTER);
		jlbOrderDate.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfOrderDate = new JTextField(10);
		jtfOrderDate.setEditable(false);
		JLabel jlbOrderStatus = new JLabel("주문상태 : ", JLabel.CENTER);
		jlbOrderStatus.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfOrderStatus = new JTextField(10);
		jtfOrderStatus.setEditable(false);
		JLabel jlbMemberID = new JLabel("주문자P.H : ", JLabel.CENTER);
		jlbMemberID.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfMemberID = new JTextField(10);
		jtfMemberID.setEditable(false);
		JLabel jlbTotalPrice = new JLabel("총가격 : ", JLabel.CENTER);
		jlbTotalPrice.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		jtfTotalPrice = new JTextField(10);
		jtfTotalPrice.setEditable(false);
		
		JPanel jpOrderId = new JPanel();
		jpOrderId.setLayout(new GridLayout(1,2));
		jpOrderId.add(jlbOrderId);
		jpOrderId.add(jtfOrderId);
		
		JPanel jpOrderWaitingNum = new JPanel();
		jpOrderWaitingNum.setLayout(new GridLayout(1,2));
		jpOrderWaitingNum.add(jlbOrderWaitingNum);
		jpOrderWaitingNum.add(jtfOrderWaitingNum);
		
		JPanel jpTakeOut = new JPanel();
		jpTakeOut.setLayout(new GridLayout(1,2));
		jpTakeOut.add(jlbTakeOut);
		jpTakeOut.add(jtfTakeOut);
		
		JPanel jpOrderDate = new JPanel();
		jpOrderDate.setLayout(new GridLayout(1,2));
		jpOrderDate.add(jlbOrderDate);
		jpOrderDate.add(jtfOrderDate);
		
		JPanel jpOrderStatus = new JPanel();
		jpOrderStatus.setLayout(new GridLayout(1,2));
		jpOrderStatus.add(jlbOrderStatus);
		//jlbTakeOut.setFont(new Font("맑은 고딕", Font.PLAIN, 15));
		
		JPanel jpOrderStatus2 = new JPanel();
		jpOrderStatus2.setLayout(new GridLayout(2,1));
		jrbMaking = new JRadioButton("조리중");
		jrbDone = new JRadioButton("완료");
		//jrbMaking.setEnabled(false);
		jrbDone = new JRadioButton("완료");
		//jrbDone.setEnabled(false);
		bgMakingDone = new ButtonGroup();
		bgMakingDone.add(jrbMaking);
		bgMakingDone.add(jrbDone);
		jpOrderStatus2.add(jrbMaking);
		jpOrderStatus2.add(jrbDone);
		
		jpOrderStatus.add(jpOrderStatus2);
		
		JPanel jpMemberID = new JPanel();
		jpMemberID.setLayout(new GridLayout(1,2));
		jpMemberID.add(jlbMemberID);
		jpMemberID.add(jtfMemberID);
		
		JPanel jpTotalPrice = new JPanel();
		jpTotalPrice.setLayout(new GridLayout(1,2));
		jpTotalPrice.add(jlbTotalPrice);
		jpTotalPrice.add(jtfTotalPrice);
		
		jbtnNewList = new JButton("목록갱신");
		jbtnDelete = new JButton("주문삭제");
		jbtnModify = new JButton("주문수정");
		jbtnGuitar = new JButton("전체주문");
		
		//컴포넌트 배치 
		jspOrderDetail.setBounds(0, 0, 250, 150); // 상세 메뉴 리스트 테이블
		
		jpOrderId.setBounds(0, 170, 250, 30);
		jpOrderWaitingNum.setBounds(0, 210, 250, 30);
		jpMemberID.setBounds(0, 250, 250, 30);
		jpTakeOut.setBounds(0, 290, 250, 30);
		jpOrderDate.setBounds(0, 330, 250, 30);
		jpOrderStatus.setBounds(0, 370, 250, 70);
		jpTotalPrice.setBounds(0, 450, 250, 30);
		
		JPanel jpButtons = new JPanel();
		jpButtons.setLayout(new GridLayout(2, 2, 10, 10)); //간격 10px
		jpButtons.add(jbtnModify);
		jpButtons.add(jbtnDelete);
		jpButtons.add(jbtnNewList);
		jpButtons.add(jbtnGuitar);
		jpButtons.setBounds(0, 520, 250, 80);
		
		
		add(jspOrderDetail);
		add(jpOrderId);
		add(jpOrderWaitingNum);
		add(jpMemberID);
		add(jpTakeOut);
		add(jpOrderDate);
		add(jpOrderStatus);
		add(jpTotalPrice);
		add(jpButtons);
		
	}
	
	public void updateTable(List<String[]> menuList) {
		dtmOrderDetail.setRowCount(0); //기존테이블 초기화
		
	    for (int i = 0; i < menuList.size(); i++) {
	    	String[] rowData = menuList.get(i);
	    	
	    	if (rowData.length >= 2) {
	            String name = rowData[0];
	            String value = rowData[1];
	            dtmOrderDetail.addRow(new String[]{name, value});
	        } else {
	            //System.out.println("rowData 길이가 2보다 작음: " + Arrays.toString(rowData));
	        }

	    }
		
	}
	

	public JTextField getJtfOrderId() {
		return jtfOrderId;
	}

	public JTextField getJtfOrderWaitingNum() {
		return jtfOrderWaitingNum;
	}

	public JTextField getJtfTotalQuantity() {
		return jtfTotalQuantity;
	}

	public JTextField getJtfTakeOut() {
		return jtfTakeOut;
	}

	public JTextField getJtfOrderStatus() {
		return jtfOrderStatus;
	}

	public JTextField getJtfMemberID() {
		return jtfMemberID;
	}

	public JTextField getJtfTotalPrice() {
		return jtfTotalPrice;
	}

	public JTextField getJtfOrderDate() {
		return jtfOrderDate;
	}

	public JRadioButton getJrbMaking() {
		return jrbMaking;
	}

	public JRadioButton getJrbDone() {
		return jrbDone;
	}

	public ButtonGroup getBgMakingDone() {
		return bgMakingDone;
	}

	public JButton getJbtnModify() {
		return jbtnModify;
	}

	public JButton getJbtnDelete() {
		return jbtnDelete;
	}

	public JButton getJbtnNewList() {
		return jbtnNewList;
	}

	public JButton getJbtnGuitar() {
		return jbtnGuitar;
	}

	public DefaultTableModel getDtmOrderDetail() {
		return dtmOrderDetail;
	}

	public JTable getJtblOrderDetail() {
		return jtblOrderDetail;
	}

	public JScrollPane getJspOrderDetail() {
		return jspOrderDetail;
	}

	public int getCountDataLogs() {
		return countDataLogs;
	}
	
	

}
