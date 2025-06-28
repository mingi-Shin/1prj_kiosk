package kr.co.kiosk.adminEvt;

import javax.swing.*;

import kr.co.kiosk.adminView.FinancialManageView;
import kr.co.kiosk.dao.DbConnection;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class financialManageEvt extends JFrame {

    private Connection conn = null;
    private PreparedStatement pstmt =null;
    private ResultSet rs = null;
    private DbConnection db = DbConnection.getInstance();
    private int month;
    private int selectedDay;
    private JTextArea jta = new JTextArea();
	private int totalPrice;
	private int totalCnt;
	private String maxSell;
    
    public financialManageEvt(int month,int selectedDay) {
    	setTitle("정산");
        JPanel jp = new JPanel(new BorderLayout()); // BorderLayout 사용
        JScrollPane scrollPane = new JScrollPane(jta); // 스크롤 가능하도록 설정
        jp.add(scrollPane, BorderLayout.CENTER); 
        this.selectedDay=selectedDay;
        this.month=month;
       try {
        conn = db.getConn();
        Manage();
	} catch (SQLException e) {
		e.printStackTrace();
	}
        
        add(jp);
        setBounds(800, 300, 500, 300);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public void Manage() throws SQLException {
        totalPrice = 0;
        totalCnt = 0;
        maxSell = "";
        Map<String, Integer> map = new HashMap<>();
        Map<String, Integer> map2 = new HashMap<>();

        try {
            String sql;
            if (selectedDay == 0) {
                sql = "select quantity, menu_name, menu.price as price from menu_order "
                    + "join total_order using (order_id) "
                    + "join menu using(menu_id) "
                    + "where extract(month from order_datetime)=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, month);
                jta.append(month+"월 총매출\n");
            } else {
                sql = "select quantity, menu_name, menu.price as price from menu_order "
                    + "join total_order using (order_id) "
                    + "join menu using(menu_id) "
                    + "where extract(day from order_datetime)=?"
                    + "and extract(month from order_datetime)=?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, selectedDay);
                pstmt.setInt(2, month);
                jta.append(month+"월 " + selectedDay + "일 매출\n");

            }

            rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("menu_name");
                int quantity = rs.getInt("quantity");
                int price = rs.getInt("price");

                map.put(name, map.getOrDefault(name, 0) + quantity);
                map2.put(name, price);
                totalPrice += (price*quantity);
            }

            int max = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                totalCnt += value;
                if (max < value) {
                    max = value;
                    maxSell = key;
                }
            }

            jta.append("총 매출액: " + totalPrice + "\n");
            jta.append("총 판매수량: " + totalCnt + "개\n");
            jta.append("최다판매: " + maxSell + "\n");
            jta.append("--------------------------------\n");
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                Integer value = entry.getValue();
                jta.append(key+":"+value+"개 / "+(map2.get(key)*value)+"원\n");
            }
            jta.setCaretPosition(0);
        } finally {
            db.closeDB(rs, pstmt, conn);
        }
    }

    
}
