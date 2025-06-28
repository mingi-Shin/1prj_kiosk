package kr.co.kiosk.adminView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;

import kr.co.kiosk.adminEvt.StockManageEvt;

public class StockManageView extends JPanel {
	
	private StockCenterPanel scp;
	
	private JButton stockDetail, inOutDetail;
	
	public StockManageView() {
		this.scp = new StockCenterPanel();
		
		setLayout(null);
		
		this.stockDetail = new JButton("재고 현황");
		this.inOutDetail = new JButton("입출고 내역");
		stockDetail.setFont(new Font("맑은 고딕", Font.BOLD, 40));
		inOutDetail.setFont(new Font("맑은 고딕", Font.BOLD, 40));
		
		JPanel jlblButton = new JPanel();
		jlblButton.setLayout(new GridLayout(1,2));
		jlblButton.add(stockDetail);
		jlblButton.add(inOutDetail);
		
		jlblButton.setBounds(100, 10, 600, 60);
		add(jlblButton);
		
		//카드레이아웃한 센터패널
        scp.setBounds(50, 90, 700, 600);  // 위치 및 크기 설정
        	
        add(scp);
        
		//JPanel크기 설정 : 없으면 표시 안됨 
		setPreferredSize(new java.awt.Dimension(800, 800)); 
		
		//이벤트
		StockManageEvt sme = new StockManageEvt(this);
		stockDetail.addActionListener(sme);
		inOutDetail.addActionListener(sme);
		
		//재고현황 테이블 이벤트 연결
		scp.getSdtView().getJtblStockStatus().addMouseListener(sme);
		scp.getSdtView().getSaveStock().addActionListener(sme);
		//입출고내역 테이블 이벤트 연결 
		scp.getIodtView().getJlDate().addMouseListener(sme);
		
		
	}

	public StockCenterPanel getScp() {
		return scp;
	}

	public JButton getStockDetail() {
		return stockDetail;
	}

	public JButton getInOutDetail() {
		return inOutDetail;
	}
	
	
	
}
