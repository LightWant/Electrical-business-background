package model.bll.objects;

public class Storage {
	private Integer managerid;
	private String type;
	private String state;
	private Integer num;
	private Integer leftnum;
	public void setManagerid(Integer managerid) {
		this.managerid = managerid;
	}
	public Integer getManagerid() {
		return this.managerid;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getState() {
		return this.state;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getNum() {
		return this.num;
	}
	public void setLeftnum(Integer leftnum) {
		this.leftnum = leftnum;
	}
	public Integer getLeftnum() {
		return this.leftnum;
	}
}
