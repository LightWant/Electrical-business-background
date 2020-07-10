package model.bll.objects;

import java.util.Date;

public class Dingdan {
	Integer dingdanID;
	Integer kehuID;
	String kehumingchen;
	Date xiadanTime; Date xiadanTime1; Date xiadanTime2;
	Date tihuoTime; Date tihuoTime1; Date tihuoTime2;
	String dingdanSTATE;
	Float yingfuMONEY;
	Float shifuMONEY;
	Integer[] chanpinnum;
	String[] chanpinmingchen;
	String activity_apply_money;
	String shoukuanTYPE;
	
	public void setShoukuanTYPE(String shoukuanTYPE) { this.shoukuanTYPE = shoukuanTYPE;}
	public void setDingdanID(Integer dingdanID) { this.dingdanID = dingdanID;}
	public void setKehuID(Integer kehuID) { this.kehuID = kehuID;}
	public void setXiadanTime(Date xiadanTime) { this.xiadanTime = xiadanTime;}
	public void setXiadanTime1(Date xiadanTime1) { this.xiadanTime1 = xiadanTime1;}
	public void setXiadanTime2(Date xiadanTime2) { this.xiadanTime2 = xiadanTime2;}
	public void setTihuoTime(Date tihuoTime) { this.tihuoTime = tihuoTime;}
	public void setTihuoTime1(Date tihuoTime1) { this.tihuoTime1 = tihuoTime1;}
	public void setTihuoTime2(Date tihuoTime2) { this.tihuoTime2 = tihuoTime2;}
	public void setDingdanSTATE(String dingdanSTATE) { this.dingdanSTATE = dingdanSTATE;}
	public void setYingfuMONEY(Float yingfuMONEY) { this.yingfuMONEY = yingfuMONEY;}
	public void setShifuMONEY(Float shifuMONEY) { this.shifuMONEY = shifuMONEY;}
	public void setKehumingchen(String kehumingchen) { this.kehumingchen = kehumingchen;}
	public void setActivity_apply_money(String activity_apply_money) { this.activity_apply_money = activity_apply_money;}
	public void setChanpinmingchen(String[] chanpinmingchen) { this.chanpinmingchen = chanpinmingchen;}
	public void setChanpinnum(Integer[] chanpinnum) { this.chanpinnum = chanpinnum;}
	
	public Integer getDingdanID() {return dingdanID;}
	public Integer getKehuID() {return kehuID;}
	public Date getXiadanTime() {return xiadanTime;}
	public Date getXiadanTime1() {return xiadanTime1;}
	public Date getXiadanTime2() {return xiadanTime2;}
	public Date getTihuoTime() {return tihuoTime;}
	public Date getTihuoTime1() {return tihuoTime1;}
	public Date getTihuoTime2() {return tihuoTime2;}
	public String getDingdanSTATE() {return dingdanSTATE;}
	public Float getYingfuMONEY() {return yingfuMONEY;}
	public Float getShifuMONEY() {return shifuMONEY;}
	public String getKehumingchen() {return kehumingchen;}
	public String getActivity_apply_money() {return activity_apply_money;}
	public String[] getChanpinmingchen() {return chanpinmingchen;}
	public Integer[] getChanpinnum() {return chanpinnum;}
	public String getShoukuanTYPE() {return shoukuanTYPE;}
}
