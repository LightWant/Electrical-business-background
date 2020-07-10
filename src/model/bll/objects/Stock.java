package model.bll.objects;

import java.util.Date;

public class Stock {
	private String ylname;
	private String ghsname;
	private Integer num;
	private Float price;
	private Date scrq;
	public Stock() {
		
	}
	public void setYlname(String ylname) {
		this.ylname = ylname;
	}
	public String getYlname() {
		return ylname;
	}
	public void setGhsname(String ghsname) {
		this.ghsname = ghsname;
	}
	public String getGhsname() {
		return ghsname;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getNum() {
		return num;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Float getPrice() {
		return price;
	}
	public void setScrq(Date scrq) {
		this.scrq = scrq;
	}
	public Date getScrq() {
		return scrq;
	}
}
