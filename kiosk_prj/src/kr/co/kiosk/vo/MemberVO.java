package kr.co.kiosk.vo;

public class MemberVO {

	private int memberId;
	private String phoneNumber;
	private int totalAmount, points, stamps, levelId;
	
	public MemberVO() {
		super();
	}
	
	public MemberVO(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public MemberVO(int memberId, String phoneNumber, int totalAmount, int points, int stamps, int levelId) {
		super();
		this.memberId = memberId;
		this.phoneNumber = phoneNumber;
		this.totalAmount = totalAmount;
		this.points = points;
		this.stamps = stamps;
		this.levelId = levelId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getStamps() {
		return stamps;
	}

	public void setStamps(int stamps) {
		this.stamps = stamps;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	@Override
	public String toString() {
		return "MemberVO [memberId=" + memberId + ", phoneNumber=" + phoneNumber + ", totalAmount=" + totalAmount
				+ ", points=" + points + ", stamps=" + stamps + ", levelId=" + levelId + "]";
	}
	
	
}
