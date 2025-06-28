package kr.co.kiosk.vo;

import java.time.LocalDateTime;

public class StockUpVO {

	private int menuId;
	private int categoryId;
	private LocalDateTime inputDate;
	private int quantity;
	private String ioType;
	
	public StockUpVO() {
		super();
	}

	public StockUpVO(int menuId, int categoryId, LocalDateTime inputDate, int quantity, String ioType) {
		super();
		this.menuId = menuId;
		this.categoryId = categoryId;
		this.inputDate = inputDate;
		this.quantity = quantity;
		this.ioType = ioType;
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

	public LocalDateTime getInputDate() {
		return inputDate;
	}

	public void setInputDate(LocalDateTime inputDate) {
		this.inputDate = inputDate;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getIoType() {
		return ioType;
	}

	public void setIoType(String ioType) {
		this.ioType = ioType;
	}

	@Override
	public String toString() {
		return "StockUpVO [menuId=" + menuId + ", categoryId=" + categoryId + ", inputDate=" + inputDate + ", quantity="
				+ quantity + ", ioType=" + ioType + "]";
	}

}
