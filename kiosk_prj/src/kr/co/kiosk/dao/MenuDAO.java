package kr.co.kiosk.dao;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import kr.co.kiosk.vo.MenuVO;
import kr.co.sist.util.img.ImageResize;

public class MenuDAO {

	private static MenuDAO mDAO;

	private MenuDAO() {

	}// MenuDAO

	public static MenuDAO getInstance() {
		if (mDAO == null) {
			mDAO = new MenuDAO();
		}

		return mDAO;
	}// getInstance

	public void insertMenu(MenuVO mVO) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs=null;
		DbConnection dbConn = DbConnection.getInstance();
		FileInputStream fis = null;

		try {
			con = dbConn.getConn();
			
			String menuId="select  seq_menu_id.nextval nextval from dual";
			
			pstmt=con.prepareStatement(menuId); 
			rs=pstmt.executeQuery();
			rs.next();
			//번호를 전처리한 후
			mVO.setMenuId( rs.getInt("nextval"));

			dbConn.closeDB(rs, pstmt, null);
			
			
			StringBuilder insertMenu = new StringBuilder();
			insertMenu.append("insert into menu")
					.append("(menu_id,category_id,menu_name,unit_name,weight,calorie,price,");
			if (!mVO.getImgName().isEmpty()) {
				insertMenu.append("image, img_name,");
			}
			insertMenu.append("notes)").append("values").append("(?,?,?,?,?,?,?,");
			if (!mVO.getImgName().isEmpty()) {
				insertMenu.append("?,?,");
			}
			insertMenu.append("?)");

			pstmt = con.prepareStatement(insertMenu.toString());

			int bindIdx = 0;
			pstmt.setInt(++bindIdx, mVO.getMenuId());//전처리된 데이터 입력
			pstmt.setInt(++bindIdx, mVO.getCategoryId());
			pstmt.setString(++bindIdx, mVO.getMenuName());
			pstmt.setString(++bindIdx, mVO.getUnitName());
			pstmt.setInt(++bindIdx, mVO.getWeight());
			pstmt.setInt(++bindIdx, mVO.getCalorie());
			pstmt.setInt(++bindIdx, mVO.getPrice());
			if (!mVO.getImgName().isEmpty()) {
				File file = new File(mVO.getImgName());
				if (file.exists()) {
					ImageResize.resizeImage(file.getAbsolutePath(), 125, 110);
					fis = new FileInputStream(file.getParent()+File.separator+"rs_"+file.getName());
					pstmt.setBinaryStream(++bindIdx, fis, file.length());
					pstmt.setString(++bindIdx, file.getName());
				}
			}
			pstmt.setString(++bindIdx, mVO.getNotes());

			pstmt.executeUpdate();
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			dbConn.closeDB(null, pstmt, con);
			try {
				if (fis != null)
					fis.close();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		}
	}// insertMenu

	public int updateMenu(MenuVO mVO) throws SQLException {
		int rowCnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;
		FileInputStream fis = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder updateMenu = new StringBuilder();
			updateMenu.append("	update menu																	")
					.append("	set category_id=?, menu_name=?, weight=?, calorie=?, price=?, notes=?, image=?, img_name=?	")
					.append("	where menu_id=?																");

			pstmt = con.prepareStatement(updateMenu.toString());

			int bindIdx = 0;
			pstmt.setInt(++bindIdx, mVO.getCategoryId());
			pstmt.setString(++bindIdx, mVO.getMenuName());
			pstmt.setInt(++bindIdx, mVO.getWeight());
			pstmt.setInt(++bindIdx, mVO.getCalorie());
			pstmt.setInt(++bindIdx, mVO.getPrice());
			pstmt.setString(++bindIdx, mVO.getNotes());
			if (!mVO.getImgName().isEmpty()) {
				File file = new File(mVO.getImgName());
				if (file.exists()) {
					ImageResize.resizeImage(file.getAbsolutePath(), 125, 110);
					fis = new FileInputStream(file.getParent()+File.separator+"rs_"+file.getName());
					pstmt.setBinaryStream(++bindIdx, fis, file.length());
					pstmt.setString(++bindIdx, file.getName());
				}
			}
			pstmt.setInt(++bindIdx, mVO.getMenuId());
			rowCnt = pstmt.executeUpdate();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			dbCon.closeDB(null, pstmt, con);
		}

		return rowCnt;
	}// updateMenu

	public int deleteMenu(int menuId) throws SQLException {
		int rowCnt = 0;

		Connection con = null;
		PreparedStatement pstmt = null;

		DbConnection dbCon = DbConnection.getInstance();
		try {
			con = dbCon.getConn();

			StringBuilder deleteMenu = new StringBuilder();
			deleteMenu.append("	delete from menu	").append("	where menu_id=?		");

			pstmt = con.prepareStatement(deleteMenu.toString());

			pstmt.setInt(1, menuId);

			rowCnt = pstmt.executeUpdate();

		} finally {
			dbCon.closeDB(null, pstmt, con);
		}
		return rowCnt;
	}// deleteMenu

	public List<MenuVO> selectAllMenu() throws SQLException, IOException {
		List<MenuVO> list = new ArrayList<MenuVO>();

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		BufferedReader br = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder selectMenu = new StringBuilder();
			selectMenu.append(
					"	select menu_id, category_id, menu_name, unit_name, weight, calorie, price, notes, image, img_name	")
					.append("	from menu 																							");

			pstmt = con.prepareStatement(selectMenu.toString());

			rs = pstmt.executeQuery();

			MenuVO mVO = null;
			while (rs.next()) {
				mVO = new MenuVO();
				mVO.setMenuId(rs.getInt("menu_id"));
				mVO.setCategoryId(rs.getInt("category_id"));
				mVO.setMenuName(rs.getString("menu_name"));
				mVO.setUnitName(rs.getString("unit_name"));
				mVO.setWeight(rs.getInt("weight"));
				mVO.setCalorie(rs.getInt("calorie"));
				mVO.setPrice(rs.getInt("price"));
				mVO.setImgName(rs.getString("img_name"));
				
				Blob blob = rs.getBlob("image");
				InputStream inputStream = blob.getBinaryStream();
				
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				ImageIcon icon = new ImageIcon(bufferedImage);
				mVO.setImage(icon);

				// clob
				Clob clobNotes = rs.getClob("notes");
				if (clobNotes != null) {
					br = new BufferedReader(clobNotes.getCharacterStream());

					String tempNotes = "";
					StringBuilder sbNotes = new StringBuilder();

					while ((tempNotes = br.readLine()) != null) {
						sbNotes.append(tempNotes).append("\n");
					}
					mVO.setNotes(sbNotes.toString());
				}

				list.add(mVO);
			}

		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}

		return list;
	}// selectAllMenu

	public MenuVO selectMenu(int num) throws SQLException, IOException {
		MenuVO mVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		BufferedReader br = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder selectMenu = new StringBuilder();
			selectMenu.append(
					"	select menu_id, category_id, menu_name, unit_name, weight, calorie, price, notes, image, img_name	")
					.append("	from menu  																							")
					.append(" where menu_id=? 																					");

			pstmt = con.prepareStatement(selectMenu.toString());

			pstmt.setInt(1, num);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				mVO = new MenuVO();
				mVO.setMenuId(rs.getInt("menu_id"));
				mVO.setCategoryId(rs.getInt("category_id"));
				mVO.setMenuName(rs.getString("menu_name"));
				mVO.setUnitName(rs.getString("unit_name"));
				mVO.setWeight(rs.getInt("weight"));
				mVO.setCalorie(rs.getInt("calorie"));
				mVO.setPrice(rs.getInt("price"));
				mVO.setImgName(rs.getString("img_name"));
				
				Blob blob = rs.getBlob("image");
				InputStream inputStream = blob.getBinaryStream();
				
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				ImageIcon icon = new ImageIcon(bufferedImage);
				mVO.setImage(icon);

				// clob
				Clob clobNotes = rs.getClob("notes");
				if (clobNotes != null) {
					br = new BufferedReader(clobNotes.getCharacterStream());

					String tempNotes = "";
					StringBuilder sbNotes = new StringBuilder();

					while ((tempNotes = br.readLine()) != null) {
						sbNotes.append(tempNotes).append("\n");
					}
					mVO.setNotes(sbNotes.toString());
				}

			}

		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}

		return mVO;
	}// selectMenu
	
	//메뉴명으로 메뉴 찾는건 최대안 안하려했지만...
	public MenuVO selectMenuWithName(String menuName) throws SQLException, IOException {
		MenuVO mVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		BufferedReader br = null;

		DbConnection dbCon = DbConnection.getInstance();

		try {
			con = dbCon.getConn();
			StringBuilder selectMenu = new StringBuilder();
			selectMenu.append(
					"	select menu_id, category_id, menu_name, unit_name, weight, calorie, price, notes, image, img_name	")
					.append("	from menu  																							")
					.append(" where menu_name=? 																					");

			pstmt = con.prepareStatement(selectMenu.toString());

			pstmt.setString(1, menuName);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				mVO = new MenuVO();
				mVO.setMenuId(rs.getInt("menu_id"));
				mVO.setCategoryId(rs.getInt("category_id"));
				mVO.setMenuName(rs.getString("menu_name"));
				mVO.setUnitName(rs.getString("unit_name"));
				mVO.setWeight(rs.getInt("weight"));
				mVO.setCalorie(rs.getInt("calorie"));
				mVO.setPrice(rs.getInt("price"));
				mVO.setImgName(rs.getString("img_name"));
				
				Blob blob = rs.getBlob("image");
				InputStream inputStream = blob.getBinaryStream();
				
				BufferedImage bufferedImage = ImageIO.read(inputStream);
				ImageIcon icon = new ImageIcon(bufferedImage);
				mVO.setImage(icon);

				// clob
				Clob clobNotes = rs.getClob("notes");
				if (clobNotes != null) {
					br = new BufferedReader(clobNotes.getCharacterStream());

					String tempNotes = "";
					StringBuilder sbNotes = new StringBuilder();

					while ((tempNotes = br.readLine()) != null) {
						sbNotes.append(tempNotes).append("\n");
					}
					mVO.setNotes(sbNotes.toString());
				}

			}

		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}

		return mVO;
	}// selectMenuWithName
	
	public int selectAvailableCount(int menuId) throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		DbConnection dbCon = DbConnection.getInstance();
		
		int availableCnt = 0;
		
		try {
			con =dbCon.getConn();
			
			String query = " SELECT TRUNC(s.QUANTITY / m.WEIGHT) AS availableCount FROM STOCK s JOIN MENU m ON s.menu_id = m.menu_id WHERE m.MENU_ID = ?  ";
			
			pstmt = con.prepareStatement(query);
			
			pstmt.setInt(1, menuId);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				availableCnt = rs.getInt("availableCount");
			}
			
		} finally {
			dbCon.closeDB(rs, pstmt, con);
		}

		return availableCnt;
	}//selectAvailableCount
	
	public Map<Integer, Integer> selectAllAvailableCounts() throws SQLException {
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    DbConnection dbCon = DbConnection.getInstance();

	    Map<Integer, Integer> map = new HashMap<>();

	    try {
	        con = dbCon.getConn();
	        String query = "SELECT m.MENU_ID, TRUNC(s.QUANTITY / m.WEIGHT) AS availableCount " +
	                       "FROM STOCK s JOIN MENU m ON s.menu_id = m.menu_id";

	        pstmt = con.prepareStatement(query);
	        rs = pstmt.executeQuery();

	        while (rs.next()) {
	            int menuId = rs.getInt("MENU_ID");
	            int availableCount = rs.getInt("availableCount");
	            map.put(menuId, availableCount);
	        }

	    } finally {
	        dbCon.closeDB(rs, pstmt, con);
	    }

	    return map;
	}//selectAllAvailableCounts


}// class
