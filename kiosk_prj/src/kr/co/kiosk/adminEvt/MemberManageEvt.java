package kr.co.kiosk.adminEvt;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import kr.co.kiosk.adminView.MemberManageView;
import kr.co.kiosk.service.MemberService;
import kr.co.kiosk.vo.MemberVO;


/**
 * 
 */
public class MemberManageEvt implements ActionListener, MouseListener {

    private MemberManageView mmv;// View 클래스 (회원 관리 UI)
    private MemberService memberService;// 회원 관련 DB 서비스 클래스

    public MemberManageEvt(MemberManageView mmv) {
        this.mmv = mmv;
        this.memberService = new MemberService();

        // 각 버튼에 ActionListener 등록
        this.mmv.getJbtnSearch().addActionListener(this);
        this.mmv.getJbtnselect().addActionListener(this);
        this.mmv.getJbtnDelete().addActionListener(this);
        this.mmv.getJbtnPointAdd().addActionListener(this);
        this.mmv.getJbtnPointSubtract().addActionListener(this);
        this.mmv.getJbtnStempAdd().addActionListener(this);
        this.mmv.getJbtnStempSubtract().addActionListener(this);
        this.mmv.getJbtnLevelOk().addActionListener(this); 
        this.mmv.getJtblMember().addMouseListener(this);

        
        // 처음 시작 시 전체 회원 목록 불러오기
        loadMember();
    }

    /**
     * 전체 회원 데이터를 DB에서 가져와 JTable에 출력하는 메서드
     */
    private void loadMember() {
        DefaultTableModel model = (DefaultTableModel) mmv.getJtblMember().getModel();
        model.setRowCount(0);// 기존 테이블 데이터 초기화

        List<MemberVO> member = memberService.searchAllMember(); // 모든 회원 조회

        
     // 회원 정보를 테이블에 한 줄씩 추가
        for (MemberVO vo : member) {
            model.addRow(new Object[]{
                vo.getMemberId(),
                vo.getPhoneNumber(),
                vo.getTotalAmount(),
                vo.getPoints(),
                vo.getStamps(),
                vo.getLevelId()
            });
        }
    }
    


    /**
     * 버튼 클릭 이벤트 처리
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == mmv.getJbtnSearch()) {
            search(); // 검색 버튼
        
        }else if(e.getSource()==mmv.getJbtnselect()) {
        	 // 일괄 선택 확인 후 전체 선택
        	if(JOptionPane.showConfirmDialog(mmv, "선택하시겠습니까?", "일괄 선택", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		select();
        	}
        } else if (e.getSource() == mmv.getJbtnDelete()) {
        	 // 회원 삭제 확인 후 처리
            if (JOptionPane.showConfirmDialog(mmv, "삭제하시겠습니까?", "회원 삭제", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                Delete();
            }
        
        } else if (e.getSource() == mmv.getJbtnPointAdd()) {
        	// 포인트 지급
        	if (JOptionPane.showConfirmDialog(mmv, "포인트 지급하시겠습니까?", "지급", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		PointAdd();
            }
         
        } else if (e.getSource() == mmv.getJbtnPointSubtract()) {
        	// 포인트 차감
        	if (JOptionPane.showConfirmDialog(mmv, "포인트 차감하시겠습니까?", "차감", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		PointSubtract();
            }
         
        } else if (e.getSource() == mmv.getJbtnStempAdd()) {
        	// 스탬프 지급
        	if (JOptionPane.showConfirmDialog(mmv, "스탬프 지급하시겠습니까?", "지급", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		StempAdd();
            }
         
        } else if (e.getSource() == mmv.getJbtnStempSubtract()) {
        	// 스탬프 차감
        	if (JOptionPane.showConfirmDialog(mmv, "스탬프 차감하시겠습니까?", "차감", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
        		StempSubtract();
            }
         
        }else if(e.getSource()==mmv.getJbtnLevelOk()) {
        	// 등급 변경 확인
        	if(JOptionPane.showConfirmDialog(mmv, "변경하시겠습니까?","확인", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_NO_OPTION) {
        		LevelOk();
        	}
        }
    }

    /**
     * 선택한 회원의 등급을 콤보박스 값으로 변경
     */
    private void LevelOk() {
    	
    //테이블에서 선택된 회원의 행 번호를 가져옴
  	  int selectedRow = mmv.getJtblMember().getSelectedRow();
  	  
  	  
  	  if(selectedRow == -1) {
  		  JOptionPane.showMessageDialog(mmv, "회원을 선택해주세요");
  	  }
  	  
  	  //선택된 회원의 ID를 가져옴
  	  int memberId = (int) mmv.getTableModel().getValueAt(selectedRow, 0);
  	  
  	  //선택된 등급을 가져와서 공백을 제거한 후 변수에 저장
  	  String selectLevel= ((String) mmv.getCb().getSelectedItem()).trim();
  	  int levelId=0;
  	  
  	  
  	// 문자열 등급을 숫자로 변환
  	  switch(selectLevel) {
  	  case "뱀": levelId =1; break;
  	  case "이무기":  levelId=2; break;
  	  case "용": levelId=3;break;
  	  case "쌍용": levelId=4;break;
  	  
  	  }
  
  	  // 회원 ID를 기준으로 `MemberService`에서 해당 회원을 검색
  	  MemberVO memVO = memberService.searchMember(memberId);
  	  //검색한 회원의 등급을 변경할 ID로 설정
  	  memVO.setLevelId(levelId);
  	  
  	  //'modifyMember()` 메서드를 통해 등급을 수정하고 결과를 받아옴
  	    boolean result = memberService.modifyMember(memVO);
  	   
  	    if (result) {
  	        JOptionPane.showMessageDialog(mmv, "등급이 성공적으로 변경되었습니다.");
  	        loadMember(); 
  	    } else {
  	        JOptionPane.showMessageDialog(mmv, "등급 변경에 실패했습니다.");
  	    }
		

	}
    
    
	/**
	 * 회원 ID로 검색 (필터링)
	 */
	private void search() {
		//검색어를 입력 필드에서 가져오고, 양쪽 공백을 제거
        String keyword = mmv.getJtfSearch().getText().trim();
        //현재 회원 목록을 가져옴
        DefaultTableModel model = (DefaultTableModel) mmv.getJtblMember().getModel();

     // 검색어 없으면 전체 회원 다시 로드
        if (keyword.isEmpty()) {
            loadMember();//// 전체 회원 목록을 다시 로드
            return;// 검색어가 비었으므로 메서드를 종료
        }

        // 새로운 필터된 모델 생성
        DefaultTableModel filteredModel = new DefaultTableModel(new String[]{"회원ID", "전화번호", "누적금액", "포인트", "스탬프", "등급"}, 0);

        //기존 모델에서 각 행을 반복하며 검색어가 포함된 회원을 필터링
        for (int i = 0; i < model.getRowCount(); i++) {
        	// 현재 행에서 회원 ID를 가져옴
            String id = model.getValueAt(i, 0).toString();
           //회원 ID가 검색어를 포함하고 있으면 필터된 모델에 해당 행을 추가
            if (id.contains(keyword)) {
                filteredModel.addRow(new Object[]{
                    model.getValueAt(i, 0),
                    model.getValueAt(i, 1),
                    model.getValueAt(i, 2),
                    model.getValueAt(i, 3),
                    model.getValueAt(i, 4),
                    model.getValueAt(i, 5)
                });
            }
        }
        //필터된 모델을 JTable에 반영하여 UI에 업데이트
        mmv.getJtblMember().setModel(filteredModel);
    }

	
	
    /**
     * 회원 전체 선택 (테이블 행 전체 선택)
     */
    private void select() {
    	//현재 회원 목록에서 총 행 수를 가져옴
    	 int rowCount = mmv.getJtblMember().getRowCount();
    	   
    	 if (rowCount == 0) {
    	        JOptionPane.showMessageDialog(mmv, "선택할 회원이 없습니다.");
    	        return;
    	    }
    	 
    	 	//회원 목록에서 여러 개의 행을 선택할 수 있도록 설정
    	    mmv.getJtblMember().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    	    //모든 회원을 선택하도록 설정
    	    // 첫 번째 행부터 마지막 행까지 선택
    	    mmv.getJtblMember().setRowSelectionInterval(0, rowCount - 1);
    }

    
    
    /**
     * 선택한 회원 삭제
     */
    private void Delete() {
    	
    	//삭제할 회원을 선택했는지 확인
        int row = mmv.getJtblMember().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(mmv, "삭제할 회원을 선택하세요.");
            return;
        }

        //선택한 회원의 ID를 가져옴
        int memberId = (int) mmv.getJtblMember().getValueAt(row, 0);
        
        //회원 삭제 시도
        if (memberService.removeMember(memberId)) {
        	//삭제 성공 시 성공 메시지를 띄우고 회원 목록 새로 고침
            JOptionPane.showMessageDialog(mmv, "삭제 성공");
            loadMember();
        } else {
            JOptionPane.showMessageDialog(mmv, "삭제 실패");
        }
    }

    
    /**
     * 선택된 회원에게 포인트 지급 
     */
    private void PointAdd() {
    	//선택된 회원의 행 번호들을 가져옴
    	  int[] rows = mmv.getJtblMember().getSelectedRows();

    	  
    	   if (rows.length == 0) {
   	        JOptionPane.showMessageDialog(mmv, "회원이 선택되지 않았습니다. 회원을 선택해주세요.");
   	        return; 
   	    }
    	   
    	   //포인트 입력란에서 입력된 값을 가져옴
    	   String pointText = mmv.getJtfPoint().getText().trim();
    	   //포인트 입력란이 비어 있다면 경고 메시지를 표시
    	   if (pointText.isEmpty()) {
    	        JOptionPane.showMessageDialog(mmv, "포인트를 입력해주세요.");
    	        return;
    	    }

    	   //포인트 값을 정수로 변환
    	   int point = Integer.parseInt(pointText);

    	  //선택된 회원들에게 포인트를 추가
    	    for (int row : rows) {
    	    	//선택된 회원의 ID를 가져옴
    	        int memberId = (int) mmv.getJtblMember().getValueAt(row, 0);

    	        try {
    	        	//해당 회원의 정보를 DB에서 가져옴
    	            MemberVO memVO = memberService.searchMember(memberId);
    	            //기존 포인트에 입력된 포인트를 더함
    	            memVO.setPoints(memVO.getPoints() + point);
    	            // 회원 정보를 수정하여 DB에 반영
    	            memberService.modifyMember(memVO);
    	        } catch (Exception e) {
    	            e.printStackTrace();
    	        }
    	    }
    	    //포인트 추가 후, 전체 회원 목록을 새로 고침
    	    loadMember();
    	}
    

    /**
     * 선택된 회원의 포인트 차감
     */
    private void PointSubtract() {
    	//선택된 회원의 행 번호들을 가져옴
        int[] rows = mmv.getJtblMember().getSelectedRows();

        if (rows.length == 0) {
            JOptionPane.showMessageDialog(mmv, "회원이 선택되지 않았습니다. 회원을 선택해주세요.");
            return; 
        }

        //포인트 입력란에서 입력된 값을 가져옴
        String pointText = mmv.getJtfPoint().getText().trim();
        
        //포인트 입력란이 비어 있다면 경고 메시지를 표시
        if (pointText.isEmpty()) {
            JOptionPane.showMessageDialog(mmv, "포인트를 입력해주세요.");
            return;
        }

        //포인트 값을 정수로 변환
        int point = Integer.parseInt(pointText);

        //선택된 회원들에게 포인트 차감을 수행
        for (int row : rows) {
        	//선택된 회원의 ID를 가져옴
            int memberId = (int) mmv.getJtblMember().getValueAt(row, 0);

            try {
            	//해당 회원의 정보를 DB에서 가져옴
                MemberVO memVO = memberService.searchMember(memberId);
                int currentPoints = memVO.getPoints();// 현재 보유 포인트

                //현재 포인트가 입력된 포인트보다 적으면 차감 불가 메시지를 띄우고 계속 진행
                if (currentPoints < point) {
                    JOptionPane.showMessageDialog(mmv, "보유 포인트보다 많은 포인트를 차감할 수 없습니다.");
                    continue;// 차감을 건너뛰고 다음 회원으로 넘어감
                }

                //차감할 포인트를 현재 포인트에서 빼기
                memVO.setPoints(currentPoints - point);
                //수정된 회원 정보를 DB에 반영
                memberService.modifyMember(memVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //포인트 차감 후, 전체 회원 목록을 새로 고침
        loadMember();
    }

    
    /**
     * 선택된 회원에게 스탬프 지급
     */
    private void StempAdd() {
    	//선택된 회원의 행 번호들을 가져옴
        int[] rows = mmv.getJtblMember().getSelectedRows();
      
        //선택된 회원이 없다면 경고 메시지를 표시
        if (rows.length == 0) {
	        JOptionPane.showMessageDialog(mmv, "회원이 선택되지 않았습니다. 회원을 선택해주세요.");
	        return; 
	    }
        
	    //스탬프 입력란에서 입력된 값을 가져옴
	   String StempText = mmv.getJtfStemp().getText().trim();
	   //스탬프 입력란이 비어 있다면 경고 메시지를 표시
 	   if (StempText.isEmpty()) {
 	        JOptionPane.showMessageDialog(mmv, "스탬프를 입력해주세요.");
 	        return;// 메서드를 종료하여 추가 작업을 하지 않음
 	    }

 	   //입력된 스탬프 값을 정수로 변환
 	   int stemp = Integer.parseInt(StempText);
 	   
        //선택된 회원들에게 스탬프 추가 수행
        for (int row : rows) {
        	//선택된 회원의 ID를 가져옴
            int memberId = (int) mmv.getJtblMember().getValueAt(row, 0);

            try {
            	//해당 회원의 정보를 DB에서 가져옴
                MemberVO memVO = memberService.searchMember(memberId);
                //현재 보유한 스탬프에 입력된 스탬프를 더함
                memVO.setStamps(memVO.getStamps() + stemp);

                //수정된 회원 정보를 DB에 반영
                memberService.modifyMember(memVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //스탬프가 추가된 후, 전체 회원 목록을 새로 고침
        loadMember();
    }

    
    /**
     * 선택된 회원의 스탬프 차감 
     */
    private void StempSubtract() {
    	 //선택된 회원의 행 번호들을 가져옴
    	int[] rows = mmv.getJtblMember().getSelectedRows();
       
    	//선택된 회원이 없다면 경고 메시지를 표시
        if (rows.length == 0) {
	        JOptionPane.showMessageDialog(mmv, "회원이 선택되지 않았습니다. 회원을 선택해주세요.");
	        return; 
	    }
        
        //스탬프 입력란에서 입력된 값을 가져옴
        String StempText = mmv.getJtfStemp().getText().trim();

        
        //스탬프 입력란이 비어 있다면 경고 메시지를 표시
	    if (StempText.isEmpty()) {
	        JOptionPane.showMessageDialog(mmv, "스탬프를 입력해주세요.!!");
	        return;
	    }
	   

	    //입력된 스탬프 값을 정수로 변환
 	   int stemp = Integer.parseInt(StempText);

 	  //선택된 회원들에게 스탬프 차감 수행
        for (int row : rows) {
        	//선택된 회원의 ID를 가져옴
            int memberId = (int) mmv.getJtblMember().getValueAt(row, 0);

            try {
            	//해당 회원의 정보를 DB에서 가져옴
                MemberVO memVO = memberService.searchMember(memberId);
                // 현재 보유한 스탬프 수를 가져옴
                int currentStemp = memVO.getStamps();
                
                //차감하려는 스탬프가 현재 보유한 스탬프보다 많으면 경고 메시지를 표시하고 차감을 진행하지 않음
                if (currentStemp < stemp) {
                	JOptionPane.showMessageDialog(mmv, "보유 스탬프보다 많은 스탬프를 차감할 수 없습니다.");
                	continue;//// 현재 반복을 종료하고, 다음 회원에 대해 차감을 진행
                }//end if
                
                //현재 스탬프에서 차감한 후 수정된 스탬프 수를 설정
                memVO.setStamps(memVO.getStamps() - stemp);
                //수정된 회원 정보를 DB에 반영
                memberService.modifyMember(memVO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //스탬프가 차감된 후, 전체 회원 목록을 새로 고침
        loadMember();
    }

  
    
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}
