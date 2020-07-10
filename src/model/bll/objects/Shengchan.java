package model.bll.objects;


public class Shengchan {
//"data":[{"cpname":"","zjyid":"","num":""}]
	private String cpname;
	private String zjyid;
	private Integer num;
	private String cjid;
	public void setCpname(String cpname) {
		this.cpname = cpname;
	}
	public String getCpname() {
		return this.cpname;
	}
	public void setZjyid(String zjyid) {
		this.zjyid = zjyid;
	}
	public String getZjyid() {
		return this.zjyid;
	}
	public void setCjid(String cjid) {
		this.cjid = cjid;
	}
	public String getCjid() {
		return this.cjid;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getNum() {
		return this.num;
	}
}

