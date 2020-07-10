package model.bll.objects;

public class Product {
//"mingcheng":"","dbgg":"","baozhiqi":"","danwei":"","price":""
	private String mingcheng;
	private Integer dbgg;
	private Integer baozhiqi;
	private String danwei;
	private Float price;
	private String ylname;
	private String ylnum;
	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}
	public String getMingcheng() {
		return this.mingcheng;
	}
	public void setDbgg(Integer dbgg) {
		this.dbgg = dbgg;
	}
	public Integer getDbgg() {
		return this.dbgg;
	}
	public void setBaozhiqi(Integer baozhiqi) {
		this.baozhiqi = baozhiqi;
	}
	public Integer getBaozhiqi() {
		return this.baozhiqi;
	}
	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}
	public String getDanwei() {
		return this.danwei;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getPrice() {
		return this.price;
	}
	public void setYlname(String ylname) {
		this.ylname = ylname;
	}
	public String getYlname() {
		return this.ylname;
	}
	public void setYlnum(String ylnum) {
		this.ylnum = ylnum;
		
	}
	public String getYlnum() {
		return this.ylnum;
	}
}
