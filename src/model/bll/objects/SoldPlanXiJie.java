package model.bll.objects;

public class SoldPlanXiJie {
	String id;
	String[] chanpinID;
	String[] chanpinNUM;
	
	public String getId() {return id;}
	public String[] getChanpinID() {return chanpinID;}
	public String[] getChanpinNUM() {return chanpinNUM;}
	
	public void setId(String id) {this.id = id;}
	public void setChanpinID(String[] chanpinID) {this.chanpinID = chanpinID;}
	public void setChanpinNUM(String[] chanpinNUM) {this.chanpinNUM = chanpinNUM;}
}
