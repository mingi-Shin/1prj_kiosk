package kr.co.kiosk.vo;

public class MenuOrderVO {

	private int orderId, menuId, categoryId, quantity, totalPrice;

	public MenuOrderVO() {
		super();
	}

	public MenuOrderVO(int orderId, int menuId, int categoryId, int quantity, int totalPrice) {
		super();
		this.orderId = orderId;
		this.menuId = menuId;
		this.categoryId = categoryId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Override
	public String toString() {
		return "MenuOrderVO [orderId=" + orderId + ", menuId=" + menuId + ", categoryId=" + categoryId + ", quantity="
				+ quantity + ", totalPrice=" + totalPrice + "]";
	}

}