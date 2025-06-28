package kr.co.kiosk.vo;

import java.util.Date;

public class StockVO {

	private int menuId;
	private int categoryId;
	private String menuName;
	private String unitName;
	private Date inputDate;
	private int quantity;
	
	public StockVO() {
		super();
	}

	public StockVO(int menuId, int categoryId, String menuName, String unitName, Date inputDate, int quantity) {
		super();
		this.menuId = menuId;
		this.categoryId = categoryId;
		this.menuName = menuName;
		this.unitName = unitName;
		this.inputDate = inputDate;
		this.quantity = quantity;
	}

	public int getMenuId() {
		return menuId;
	}

	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Date getInputDate() {
		return inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "StockVO [menuId=" + menuId + ", categoryId=" + categoryId + ", menuName=" + menuName + ", unitName="
				+ unitName + ", inputDate=" + inputDate + ", quantity=" + quantity + "]";
	}
	
}
