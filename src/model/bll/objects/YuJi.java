package model.bll.objects;

import java.util.Date;

public class YuJi {
	String[] chanpinID;
	String[] chanpinNUM;
	String[] mingchens;
	Date xiadanTime; Date xiadanTime1; Date xiadanTime2;
	Date tihuoTime; Date tihuoTime1; Date tihuoTime2;
	
	public void setChanpinID(String[] chanpinID) {
		this.chanpinID = chanpinID;
	}
	public void setChanpinNUM(String[] chanpinNUM) {
		this.chanpinNUM = chanpinNUM;
	}
	public void setMingchens(String[] mingchens) {
		this.mingchens = mingchens;
	}
	public String[] getMingchens() {
		return mingchens;
	}
	public String[] getChanpinID() {
		return chanpinID;
	}
	public String[] getChanpinNUM() {
		return chanpinNUM;
	}
	public void setTihuoTime(Date tihuoTime) { this.tihuoTime = tihuoTime;}
	public void setTihuoTime1(Date tihuoTime1) { this.tihuoTime1 = tihuoTime1;}
	public void setTihuoTime2(Date tihuoTime2) { this.tihuoTime2 = tihuoTime2;}
	public Date getTihuoTime() {return tihuoTime;}
	public Date getTihuoTime1() {return tihuoTime1;}
	public Date getTihuoTime2() {return tihuoTime2;}
}
