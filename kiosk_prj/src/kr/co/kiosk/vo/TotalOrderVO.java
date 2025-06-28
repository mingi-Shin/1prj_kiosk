package kr.co.kiosk.vo;

import java.sql.Date;

public class TotalOrderVO {

	private int orderId, memberId, price, orderWaitingNumber;
	private String orderType, orderStatus;
	private Date orderDateTime;
	
	public TotalOrderVO() {
	}
	
	//회원전용 생성자
	public TotalOrderVO(int memberId, String orderType, String orderStatus) {
		this.memberId = memberId;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
	}
	
	//비회원 전용 생성자
	public TotalOrderVO(String orderType, String orderStatus) {
		this.orderType = orderType;
		this.orderStatus = orderStatus;
	}
	

	public TotalOrderVO(int memberId, int price, int orderWaitingNumber, int guestId, String orderType,
			String orderStatus, Date orderDateTime) {
		this.memberId = memberId;
		this.price = price;
		this.orderWaitingNumber = orderWaitingNumber;
		this.orderType = orderType;
		this.orderStatus = orderStatus;
		this.orderDateTime = orderDateTime;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getOrderWaitingNumber() {
		return orderWaitingNumber;
	}

	public void setOrderWaitingNumber(int orderWaitingNumber) {
		this.orderWaitingNumber = orderWaitingNumber;
	}


	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	@Override
	public String toString() {
		return "TotalOrderVO [memberId=" + memberId + ", price=" + price + ", orderWaitingNumber=" + orderWaitingNumber
				+ ", guestId=" + ", orderType=" + orderType + ", orderStatus=" + orderStatus
				+ ", orderDateTime=" + orderDateTime + "]";
	}
	
	
	
	
	
}
