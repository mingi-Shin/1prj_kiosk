package kr.co.kiosk.adminEvt;

import java.awt.Image;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.adminView.MenuManageView;
import kr.co.kiosk.service.MenuService;
import kr.co.kiosk.vo.MenuVO;

public class MenuManageEvt implements ActionListener, MouseListener {
    private MenuManageView mv;
    private MenuService menuService;
    
    // 이미지 경로 저장용 변수
    private String imagePath;

    public MenuManageEvt(MenuManageView mv) {
        this.mv = mv;
        this.menuService = new MenuService();

     // 버튼 및 테이블에 이벤트 리스너 연결
        this.mv.getJbtnAdd().addActionListener(this);
        this.mv.getJbtnEdit().addActionListener(this);
        this.mv.getJbtnReset().addActionListener(this);
        this.mv.getJbtnDelete().addActionListener(this);
        this.mv.getJbtnFind().addActionListener(this);
        this.mv.getJbtnSearch().addActionListener(this);
        this.mv.getJtblMenu().addMouseListener(this);

        loadMenu();// // 초기 메뉴 목록 불러오기
    }

    
   // 전체 메뉴 목록을 DB에서 불러와 테이블에 표시
    private void loadMenu() {
    	
    	DefaultTableModel model=(DefaultTableModel) mv.getJtblMenu().getModel();
    	model.setRowCount(0); // 기존 데이터 초기화
    	
    	List<MenuVO> menuList=menuService.searchAllMenu(); // 전체 메뉴 조회
    	
    	for(MenuVO vo:menuList) {
    		model.addRow(new Object[] {
    				 vo.getMenuId(),
    	             categoryIdToName(vo.getCategoryId()),
    	             vo.getMenuName(),
    	             vo.getImgName(),
    	             vo.getWeight(),
    	             vo.getUnitName(),
    	             vo.getCalorie(),
    	             vo.getPrice(),
    	             vo.getNotes()

    		});
    	}
    	
    }
    
    
    
 // 카테고리 ID를 이름으로 변환
    private String categoryIdToName(int id) {
        switch (id) {
            case 1: return "세트";
            case 2: return "버거";
            case 3: return "사이드";
            case 4: return "음료";
            case 5: return "재료";
            default: return "기타";
        }
    }

 // 카테고리 이름을 ID로 변환
    private int categoryNameToId(String name) {
        switch (name) {
            case "세트": return 1;
            case "버거": return 2;
            case "사이드": return 3;
            case "음료": return 4;
            case "재료": return 5;
            default: return 0;
        }
    }

    
 
    /**
     * 메뉴 추가 기능
     * 입력값을 검증 후, DB에 추가 요청
     */
    private void addMenu() {
    	
    	if (!Input()) return;// 필수 입력값 검증

       
    try {
    	 // 1. 콤보박스에서 선택한 카테고리명을 숫자 ID로 변환
    	int categoryId= categoryNameToId((String)  mv.getJcbCategory().getSelectedItem());
    	// 2. 이름 텍스트필드에서 메뉴명 가져오기 (앞뒤 공백 제거)
    	String name= mv.getJtfName().getText().trim();
        String imgName=mv.getImgName();// 전체 이미지 경로
        int weight=Integer.parseInt(mv.getJtfWeight().getText().trim());
        int calorie = Integer.parseInt(mv.getJtfCalorie().getText().trim());
        int price = Integer.parseInt(mv.getJtfPrice().getText().trim());
        Timestamp inputDate = new Timestamp(System.currentTimeMillis()); 
        String unitName=(String) mv.getJcbUnitName().getSelectedItem();
        String notes = mv.getJtfExplain().getText().trim();

        MenuVO vo = new MenuVO(0, categoryId, name, unitName, weight, calorie, price, notes,inputDate, imgName);
        boolean result=menuService.addMenu(vo);
        
        if(result) {
        	JOptionPane.showMessageDialog(mv, "성공적으로 추가되었습니다.");
        	loadMenu();// 테이블 다시 불러오기
        	resetFields();// 입력 필드 초기화
        	
        }else {
        	JOptionPane.showMessageDialog(mv, "추가 실패하였습니다.");
        	
        }

    }catch(NumberFormatException ex) {
    	JOptionPane.showMessageDialog(mv, "숫자 입력란(N)을 확인해주세요.");

    }
  }
    
    
    
   
    /**
     * 메뉴 수정 기능
     * 선택한 행의 메뉴를 DB에 수정 요청
     */
    private void editMenu() {
    	if (!Input()) return;
    	
    	int row=mv.getJtblMenu().getSelectedRow();
    	if(row==-1) {
    		JOptionPane.showMessageDialog(mv, "메뉴를 선택해주세요");
    		return;
    		
    	}
        
    try {
    	int MenuId=(int) mv.getJtblMenu().getValueAt(row, 0);
    	int categoryId= categoryNameToId((String)  mv.getJcbCategory().getSelectedItem());
    	String name= mv.getJtfName().getText().trim();
        String imgName=mv.getImgName();
        int weight=Integer.parseInt(mv.getJtfWeight().getText().trim());
        int calorie = Integer.parseInt(mv.getJtfCalorie().getText().trim());
        int price = Integer.parseInt(mv.getJtfPrice().getText().trim());
        Timestamp inputDate = new Timestamp(System.currentTimeMillis()); 
        String notes = mv.getJtfExplain().getText().trim();
    	
        //위 데이터를 바탕으로 새로운 MenuVO 객체 생성
        //menuId는 0으로 설정 (DB에서 자동 생성될 것으로 가정)
        MenuVO vo = new MenuVO(MenuId, categoryId, name, null,  weight, calorie, price, notes, null, imgName);
        
        // MenuService를 통해 DB에 메뉴 추가 요청
        boolean result=menuService.modifyMenu(vo);
        if (result) {
            JOptionPane.showMessageDialog(mv, "수정 성공");
            loadMenu();
            resetFields();
        } else {
            JOptionPane.showMessageDialog(mv, "수정 실패");
        }
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(mv, "입력값 오류 확인");
    }

    }

    
   
    /**
     * 메뉴 삭제 기능
     * 선택된 행의 메뉴를 DB에서 삭제
     */
    private void deleteMenu() {
        int row=mv.getJtblMenu().getSelectedRow();
        if(row==-1) {
        	JOptionPane.showMessageDialog(mv, "메뉴를 선택해주세요");
        	return;
        	
        }
        
        int menuId=(int) mv.getJtblMenu().getValueAt(row, 0);
        boolean result=menuService.removeMenu(menuId);
        
        if(result) {
        	JOptionPane.showMessageDialog(mv, "성공적으로 삭제되었습니다.");
        	loadMenu();
        	resetFields();
        }else {
        	JOptionPane.showMessageDialog(mv, "삭제 실패");
        }
    }

    
    
    /**
     * 입력 필드를 초기화하는 메서드
     */
    private void resetFields() {
       mv.getJcbCategory().setSelectedIndex(0);
       //메뉴명 텍스트필드 초기화 (비워둠)
       mv.getJtfName().setText("");
       //이미지 경로 텍스트필드 초기화 (비워둠)
       mv.getJtfImage().setText("");
       mv.getJtfWeight().setText("");
       mv.getJtfCalorie().setText("");
       mv.getJtfPrice().setText("");
       mv.getJtfExplain().setText("");
       mv.getJcbUnitName().setSelectedIndex(0);
       
       
    }
    
  
    /**
     * 이미지 찾기 버튼 클릭 시 파일 선택 및 UI 반영 
     */
    private void findImage() {
    	// 파일 선택기를 생성하여 사용자가 이미지를 선택할 수 있도록 함.
        JFileChooser chooser = new JFileChooser();
        
        //파일 선택 대화상자 열기. 사용자가 선택한 파일이 있으면 result에 저장됨.
        int result = chooser.showOpenDialog(mv);
        
        
        //사용자가 파일을 선택한 경우
        if (result == JFileChooser.APPROVE_OPTION) {
        	//선택한 파일 객체를 가져옴
            File selectedFile = chooser.getSelectedFile();
            // 파일의 이름을 추출 (파일 경로에서 파일 이름만)
            String fileName = selectedFile.getName();
            //지정된 폴더("c:/dev/img/")에 저장할 경로를 설정
            String targetPath = "c:/dev/img/" + fileName;

            try {
            	//선택한 파일이 실제 이미지인지 확인
                BufferedImage img = ImageIO.read(selectedFile);
                if (img == null) throw new IOException("선택한 파일이 이미지 파일이 아닙니다.");

             // 이미지 파일을 지정된 경로("c:/dev/img/")로 복사
                Files.copy(selectedFile.toPath(), new File(targetPath).toPath(), StandardCopyOption.REPLACE_EXISTING);
                
                // 텍스트 필드 및 라벨에 이미지 표시
                mv.getJtfImage().setText(fileName);
                
                //선택한 이미지를 UI에 표시할 수 있도록 크기를 조정
                ImageIcon ic = new ImageIcon(targetPath);
                Image tempImg = ic.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
                mv.getJlblImage().setIcon(new ImageIcon(tempImg));

                // 전체 경로 저장
                imagePath = fileName;
                mv.setImgName(targetPath);

            } catch (IOException e) {
            	//이미지 저장 또는 처리 중 오류가 발생한 경우, 오류 메시지 표시
                JOptionPane.showMessageDialog(mv, "이미지 저장 오류: " + e.getMessage());
            }
        }
    }



    
   
    /**
     * 메뉴명 검색 기능
     * 키워드를 포함하는 메뉴만 필터링
     */
    private void searchMenu() {
    	//검색어를 가져와서 공백을 제거하고 소문자로 변환
    	String keyword=mv.getJtfSearch().getText().trim().toLowerCase();
    	
    	//검색어가 비어 있는 경우, 전체 메뉴를 다시 불러옴
    	if(keyword.isEmpty()) {
    		loadMenu();
    		return;
    	}
    	
    	//기존 테이블 모델을 가져옴
    	DefaultTableModel model=(DefaultTableModel) mv.getJtblMenu().getModel();
    	//검색 결과를 저장할 새로운 테이블 모델을 생성
    	DefaultTableModel filteredModel=new DefaultTableModel(new String[] 
    			{"MenuId","카테고리", "메뉴명", "사진 경로", "중량", "단위", "칼로리", "가격","설명"}, 0);
    	//기존 메뉴 목록을 순회하면서 검색어와 일치하는 메뉴를 필터링
    	for (int i = 0; i < model.getRowCount(); i++) {
    		//각 메뉴의 메뉴명을 가져와서 소문자로 변환
            String menuName = model.getValueAt(i, 2).toString().toLowerCase();
            //메뉴명이 검색어를 포함하고 있으면 해당 행을 새로운 모델에 추가
            if (menuName.contains(keyword)) {
                filteredModel.addRow(new Object[] {
                    model.getValueAt(i, 0),
                    model.getValueAt(i, 1),
                    model.getValueAt(i, 2),
                    model.getValueAt(i, 3),
                    model.getValueAt(i, 4),
                    model.getValueAt(i, 5),
                    model.getValueAt(i, 6),
                    model.getValueAt(i, 7),
                    model.getValueAt(i, 8)
                });
            }
        }
    	//필터링된 결과로 테이블 모델을 업데이트
        mv.getJtblMenu().setModel(filteredModel);
    }
    
    
    /**
     * 필수 입력값이 비어있는지 확인
     * @return 입력값이 모두 채워졌다면 true
     */
    private boolean Input() {
        if (mv.getJtfName().getText().trim().isEmpty() || mv.getJtfImage().getText().trim().isEmpty()
        	|| mv.getJtfWeight().getText().trim().isEmpty() || mv.getJtfCalorie().getText().trim().isEmpty()
        	|| mv.getJtfPrice().getText().trim().isEmpty()|| mv.getJtfExplain().getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(mv, "필수 입력란(*)을 입력해주세요.");
            return false;
        }
        return true;
    }

    
    
    /**
     * 테이블의 메뉴를 선택했을 때, 해당 메뉴 정보 필드에 출력
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    	
    	  int row = mv.getJtblMenu().getSelectedRow();
          if (row == -1) return;
          
      	  // 선택한 행의 값을 입력 필드에 반영
          mv.getJcbCategory().setSelectedItem(mv.getJtblMenu().getValueAt(row, 1));
          mv.getJtfName().setText((String) mv.getJtblMenu().getValueAt(row, 2));
          
          MenuService ms = new MenuService();
          MenuVO mVO = ms.searchMenuWithName((String) mv.getJtblMenu().getValueAt(row, 2));
          System.out.println(mVO.getImage());
          
          //테이블에서 이미지 경로를 가져와 파일 경로가 "c:/dev/img/"로 시작하는지 확인
          String imgPath = (String) mv.getJtblMenu().getValueAt(row, 3);
          if (!imgPath.startsWith("c:/sdev/img/")) {
        	  //경로가 다르면 "c:/dev/img/"를 앞에 추가
              imgPath = "c:/dev/img/" + imgPath;
          }
         
          //텍스트 필드에 이미지 경로 표시
          mv.getJtfImage().setText(imgPath); 
          
          ImageIcon ic = mVO.getImage();
          Image tempImg = ic.getImage().getScaledInstance(125, 110, Image.SCALE_SMOOTH);
          ImageIcon realIc = new ImageIcon(tempImg);
          mv.getJlblImage().setIcon(realIc);
          
          //이 값은 나중에 DB에 저장하거나 이미지 처리할 때 사용될 수 있어요.
          mv.setImgName(imgPath);

          mv.getJtfWeight().setText(String.valueOf(mv.getJtblMenu().getValueAt(row, 4)));
          mv.getJcbUnitName().setSelectedItem(mv.getJtblMenu().getValueAt(row, 5));
          mv.getJtfCalorie().setText(String.valueOf(mv.getJtblMenu().getValueAt(row, 6)));
          mv.getJtfPrice().setText(String.valueOf(mv.getJtblMenu().getValueAt(row, 7)));
          mv.getJtfExplain().setText((String) mv.getJtblMenu().getValueAt(row, 8));
         
    }
     
    
    
    
    /**
     * 버튼 클릭 이벤트 처리
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	  if (e.getSource() == mv.getJbtnAdd()) {
    	         int result = JOptionPane.showConfirmDialog(mv, "메뉴를 추가하시겠습니까?", "메뉴 추가", JOptionPane.YES_NO_OPTION);
    	         if (result == JOptionPane.YES_OPTION) {
    	            addMenu();
    	         }
    	      } else if (e.getSource() == mv.getJbtnEdit()) {
    	         int result = JOptionPane.showConfirmDialog(mv, "메뉴를 수정하시겠습니까?", "메뉴 수정", JOptionPane.YES_NO_OPTION);
    	         if (result == JOptionPane.YES_OPTION) {
    	            editMenu();
    	         }
    	      } else if (e.getSource() == mv.getJbtnDelete()) {
    	         int result = JOptionPane.showConfirmDialog(mv, "메뉴를 삭제하시겠습니까?", "메뉴 삭제", JOptionPane.YES_NO_OPTION);
    	         if (result == JOptionPane.YES_OPTION) {
    	            deleteMenu();
    	         }
    	      } else if (e.getSource() == mv.getJbtnReset()) {
    	         int result = JOptionPane.showConfirmDialog(mv, "초기화 하시겠습니까?", "초기화", JOptionPane.YES_NO_OPTION);
    	         if (result == JOptionPane.YES_OPTION) {
    	            resetFields();
    	         }
    	      } else if (e.getSource() == mv.getJbtnFind()) {

    	    	 findImage();

    	      } else if (e.getSource() == mv.getJbtnSearch()) {

    	         searchMenu();
    	      }

    	   }



    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}


}