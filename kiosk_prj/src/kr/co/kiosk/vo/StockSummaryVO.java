package kr.co.kiosk.vo;

public class StockSummaryVO {

	private String menuName;
    private String inputDate; 
    private int inCount;
    private int outCount;
    private int inTotal;
    private int outTotal;
    
	public StockSummaryVO() {
		super();
	}

	public StockSummaryVO(String menuName, String inputDate, int inCount, int outCount, int inTotal, int outTotal) {
		super();
		this.menuName = menuName;
		this.inputDate = inputDate;
		this.inCount = inCount;
		this.outCount = outCount;
		this.inTotal = inTotal;
		this.outTotal = outTotal;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getInputDate() {
		return inputDate;
	}

	public void setInputDate(String inputDate) {
		this.inputDate = inputDate;
	}

	public int getInCount() {
		return inCount;
	}

	public void setInCount(int inCount) {
		this.inCount = inCount;
	}

	public int getOutCount() {
		return outCount;
	}

	public void setOutCount(int outCount) {
		this.outCount = outCount;
	}

	public int getInTotal() {
		return inTotal;
	}

	public void setInTotal(int inTotal) {
		this.inTotal = inTotal;
	}

	public int getOutTotal() {
		return outTotal;
	}

	public void setOutTotal(int outTotal) {
		this.outTotal = outTotal;
	}

	@Override
	public String toString() {
		return "StockSummaryVO [menuName=" + menuName + ", inputDate=" + inputDate + ", inCount=" + inCount
				+ ", outCount=" + outCount + ", inTotal=" + inTotal + ", outTotal=" + outTotal + "]";
	}
	
}
