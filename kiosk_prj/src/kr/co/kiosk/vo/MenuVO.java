package kr.co.kiosk.vo;

import java.sql.Timestamp;

import javax.swing.ImageIcon;

public class MenuVO {

	private int menuId, categoryId;
	private String menuName, unitName;
	private ImageIcon image;
	private int weight, calorie, price;
	private String notes;
	private Timestamp inputDate;
	private String imgName;

	public MenuVO() {

	}

	public MenuVO(int menuId, int categoryId, String menuName, String unitName, int weight, int calorie, int price,
			String notes, Timestamp inputDate, String imgName) {
		super();
		this.menuId = menuId;
		this.categoryId = categoryId;
		this.menuName = menuName;
		this.unitName = unitName;
		this.weight = weight;
		this.calorie = calorie;
		this.price = price;
		this.notes = notes;
		this.inputDate = inputDate;
		this.imgName = imgName;
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

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Timestamp getInputDate() {
		return inputDate;
	}

	public void setInputDate(Timestamp inputDate) {
		this.inputDate = inputDate;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	@Override
	public String toString() {
		return "menuVO [menuId=" + menuId + ", categoryId=" + categoryId + ", menuName=" + menuName + ", unitName="
				+ unitName + ", image=" + image + ", weight=" + weight + ", calorie=" + calorie + ", price=" + price
				+ ", notes=" + notes + ", inputDate=" + inputDate + ", imgName=" + imgName + "]";
	}

}
