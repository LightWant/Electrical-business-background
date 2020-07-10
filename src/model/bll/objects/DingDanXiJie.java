package model.bll.objects;

public class DingDanXiJie {
	Integer dingdanID;
	Integer[] chanpinID;
	Integer[] chanpinnum;
	
	public void setDingdanID(Integer dingdanID) { this.dingdanID = dingdanID;}
	public Integer getDingdanID() {return dingdanID;}
	
	public Integer[] getChanpinnum() {return chanpinnum;}
	public void setChanpinnum(Integer[] chanpinnum) { this.chanpinnum = chanpinnum;}
	
	public Integer[] getChanpinID() {return chanpinID;}
	public void setChanpinID(Integer[] chanpinID) { this.chanpinID = chanpinID;}
}
