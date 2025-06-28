package kr.co.kiosk.adminView;

import java.awt.Color;
import java.awt.Panel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import kr.co.kiosk.adminEvt.MenuManageEvt;

public class MenuManageView extends Panel {

    private JTextField jtfSearch;// 검색어 입력 필드
    private JTable jtblMenu; // 메뉴 목록을 보여주는 테이블
    private DefaultTableModel tableModel; // 테이블 모델 (데이터를 담는 역할)
    private JLabel jlblImage;// 이미지 미리보기용 라벨
    private JButton jbtnSearch, jbtnAdd, jbtnEdit, jbtnDelete, jbtnReset, jbtnFind;

    // 콤보박스 및 텍스트필드 (메뉴 정보 입력용)
    private JComboBox<String> JcbCategory; 
    private JComboBox<String> JcbUnitName;
    private JTextField JtfName, JtfImage, JtfWeight, JtfCalorie, JtfPrice, JtfExplain;
    
 // 이미지 전체 경로 (이미지 찾기 시 저장)
    private String imgName;

    public MenuManageView() {
        add(new JLabel("메뉴명 관리"));
        setLayout(null);

        // 검색창 필드
        jtfSearch = new JTextField();
        jtfSearch.setBounds(35, 20, 200, 25);
        add(jtfSearch);

        // 검색 버튼
        jbtnSearch = new JButton("메뉴명 검색");
        jbtnSearch.setBounds(240, 20, 120, 25);
        add(jbtnSearch);

        // 테이블 컬럼 정의 (menuId는 숨길 예정)
        String[] columns = {"menuId", "카테고리", "메뉴명", "사진 경로", "중량", "단위", "칼로리", "가격", "설명"};
        tableModel = new DefaultTableModel(columns, 0);
        jtblMenu = new JTable(tableModel);

        // menuId 컬럼 숨기기
        TableColumnModel columnModel = jtblMenu.getColumnModel();
        //첫 번째 컬럼(menuId)을 숨기기 위해 최소, 최대, 너비를 0으로 설정
        //컬럼 인덱스 0은 'menuId' 컬럼을 의미
        columnModel.getColumn(0).setMinWidth(0);// 최소 너비 0으로 설정
        columnModel.getColumn(0).setMaxWidth(0);// 최대 너비 0으로 설정
        columnModel.getColumn(0).setWidth(0);// 너비를 0으로 설정하여 화면에서 보이지 않게 함

     // 테이블 스크롤 포함해서 추가
        JScrollPane jsp = new JScrollPane(jtblMenu);
        jsp.setBounds(10, 60, 500, 550);
        add(jsp);

     // 이미지 미리보기용 라벨
        jlblImage = new JLabel("", SwingConstants.CENTER);
        jlblImage.setBounds(580, 15, 200, 200);
        jlblImage.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(jlblImage);

     // 메뉴 추가/수정/삭제/초기화 버튼
        jbtnAdd = new JButton("메뉴 추가");
        jbtnAdd.setBounds(550, 560, 100, 30);
        add(jbtnAdd);

        jbtnEdit = new JButton("메뉴 수정");
        jbtnEdit.setBounds(550, 600, 100, 30);
        add(jbtnEdit);

        jbtnDelete = new JButton("메뉴 삭제");
        jbtnDelete.setBounds(680, 560, 100, 30);
        add(jbtnDelete);

        jbtnReset = new JButton("초기화");
        jbtnReset.setBounds(680, 600, 100, 30);
        add(jbtnReset);

        // 카테고리 콤보박스
        String[] item = {"세트", "버거", "사이드", "음료", "재료"};
        JcbCategory = new JComboBox<>(item);
        JLabel JlblCategory = new JLabel("카테고리*");
        JlblCategory.setBounds(520, 220, 70, 30);
        JcbCategory.setBounds(580, 220, 200, 30);
        add(JlblCategory);
        add(JcbCategory);

     // 메뉴 이름 입력
        JLabel JlblName = new JLabel("이름*");
        JlblName.setBounds(520, 260, 100, 30);
        JtfName = new JTextField();
        JtfName.setBounds(580, 260, 200, 30);
        add(JlblName);
        add(JtfName);

        // 이미지 경로 및 찾기 버튼
        JLabel JlblImage = new JLabel("사진*");
        JlblImage.setBounds(520, 300, 100, 30);
        JtfImage = new JTextField();
        JtfImage.setBounds(580, 300, 140, 30);
        jbtnFind = new JButton("찾기");
        jbtnFind.setBounds(720, 300, 60, 30);
        add(JlblImage);
        add(JtfImage);
        add(jbtnFind);

     // 중량 입력
        JLabel JlblWeight = new JLabel("중량*,N");
        JlblWeight.setBounds(520, 340, 100, 30);
        JtfWeight = new JTextField();
        JtfWeight.setBounds(580, 340, 200, 30);
        add(JlblWeight);
        add(JtfWeight);

        JLabel JlblUnitName = new JLabel("단위*");
        JlblUnitName.setBounds(520, 380, 70, 30);
        String[] item2 = {"g", "ml "};
        JcbUnitName = new JComboBox<>(item2);
        JcbUnitName.setBounds(580, 380, 200, 30);
        add(JlblUnitName);
        add(JcbUnitName);

        JLabel JlblCalorie = new JLabel("칼로리*,N");
        JlblCalorie.setBounds(520, 420, 100, 30);
        JtfCalorie = new JTextField();
        JtfCalorie.setBounds(580, 420, 200, 30);
        add(JlblCalorie);
        add(JtfCalorie);

        JLabel JlblPrice = new JLabel("가격*,N");
        JlblPrice.setBounds(520, 460, 100, 30);
        JtfPrice = new JTextField();
        JtfPrice.setBounds(580, 460, 200, 30);
        add(JlblPrice);
        add(JtfPrice);

        JLabel JlblExplain = new JLabel("설명*");
        JlblExplain.setBounds(520, 500, 100, 30);
        JtfExplain = new JTextField();
        JtfExplain.setBounds(580, 500, 200, 30);
        add(JlblExplain);
        add(JtfExplain);

        new MenuManageEvt(this);
        setVisible(true);
        setBounds(200, 200, 800, 800);
    }
    
    // 선택된 메뉴의 menuId 반환
    public int getSelectedMenuId() {
    	// 선택된 행의 인덱스를 얻음
    	int selectedRow = jtblMenu.getSelectedRow();
    	//만약 선택된 행이 없다면(-1이면), -1을 반환
    	if (selectedRow == -1) {
    		return -1;
    	}
    	
    	//선택된 행에서 'menuId' 값(0번 컬럼)을 가져옴
    	Object value = tableModel.getValueAt(selectedRow, 0); // 0번 컬럼 = menuId
    	
    	//가져온 값을 정수로 변환하여 반환, 변환에 실패하면 -1 반환
    	try {
    		return Integer.parseInt(value.toString());
    	} catch (NumberFormatException e) {
    		//만약 'menuId'가 정수로 변환되지 않으면 예외 처리하고 -1 반환
    		return -1;
    	}
    }

   

    public JTextField getJtfSearch() { 
    	return jtfSearch; 
    	}

    public JTable getJtblMenu() { return jtblMenu; }

    public DefaultTableModel getTableModel() { return tableModel; }

    public JLabel getJlblImage() { return jlblImage; }

    public JButton getJbtnSearch() { return jbtnSearch; }

    public JButton getJbtnAdd() { return jbtnAdd; }

    public JButton getJbtnEdit() { return jbtnEdit; }

    public JButton getJbtnDelete() { return jbtnDelete; }

    public JButton getJbtnReset() { return jbtnReset; }

    public JButton getJbtnFind() { return jbtnFind; }

    public JTextField getJtfName() { return JtfName; }

    public JTextField getJtfImage() { return JtfImage; }

    public JTextField getJtfWeight() { return JtfWeight; }

    public JTextField getJtfCalorie() { return JtfCalorie; }

    public JTextField getJtfPrice() { return JtfPrice; }

    public JTextField getJtfExplain() { return JtfExplain; }

    public JComboBox<String> getJcbCategory() { return JcbCategory; }

    
    

    public JComboBox<String> getJcbUnitName() {
		return JcbUnitName;
	}

	
	public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
    



	
}
