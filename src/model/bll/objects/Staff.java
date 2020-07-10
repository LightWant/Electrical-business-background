package model.bll.objects;

import com.CallStack;

import model.dal.Configuration;
import model.dal.Connsql;
import model.dal.MD5;

//import model.dal.Configuration;
//import model.dal.Connsql;

public class Staff {
	private Integer id;
	private String mingchen;
	public void setMingchen(String mingchen) {this.mingchen = mingchen;}
	public String getMingchen() {return mingchen;}
	private String sex;
	public void setSex(String sex) {this.sex = sex;}
	public String getSex() {return sex;}
	private String zhiwei;
	private String shenfenzhengID;
	public void setShenfenzhengID(String shenfenzhengID) {this.shenfenzhengID = shenfenzhengID;}
	public String getShenfenzhengID() {return shenfenzhengID;}
	private String shouji;
	public void setShouji(String shouji) {this.shouji = shouji;}
	public String getShouji() {return shouji;}
	private String mail;
	public void setMail(String mail) {this.mail = mail;}
	public String getMail() {return mail;}
	private String zhuzhi;
	public void setZhuzhi(String zhuzhi) {this.zhuzhi = zhuzhi;}
	public String getZhuzhi() {return zhuzhi;}
	private String ruzhiTIME;
	public void setRuzhiTIME(String ruzhiTIME) {this.ruzhiTIME = ruzhiTIME;}
	public String getRuzhiTIME() {return ruzhiTIME;}
	private String zaigangSTAGE;
	public void setZaigangSTAGE(String zaigangSTAGE) {this.zaigangSTAGE = zaigangSTAGE;}
	public String getZaigangSTAGE() {return zaigangSTAGE;}
	private String password;
	private float gongzi;
	public void setGongzi(float gongzi) {this.gongzi = gongzi;}
	public float getGongzi() {return gongzi;}
	String state;
	
	public Staff() {
		id = 0;
	}
	
	public void check() {
		//System.out.println("check");
		if(this.password == null) this.password = "";
		
		Connsql con = new Connsql(Configuration.user, Configuration.password);
		String goalparms[] = {"mingchen","sex","shenfenzhengID","shouji",
				"mail","zhuzhi","ruzhiTIME","zaigangSTAGE", "gongzi", "pass"};
		String parms[] = {"ID"};
		String value[] = {Integer.toString(id)};
//		System.out.println(value[0]);
		String values[][] = con.select(goalparms, parms, value, "YuanGong");
		
		if(values == null) {
			//System.out.println("state: "+state);
			state = "NOT EXIST";
			return;
		}
//		System.out.println(values[0][10]);
		//ÃÜÂë
		if(values[0][9] == null || values[0][9] == "NULL" || values[0][9].equals(MD5.encrypt(password))) {
			//System.out.println("state:"+state);
			state = "OK";
		}
		else {
			//System.out.println("state:"+state);
			state = "Wrong Password";
			return ;
		}
		
		String sql = "select jueseNAME from YuanGongJueSe, Juese where "
				+ "JueSe.jueseID = YuanGongJueSe.jueseID and yuangongID = "+Integer.toString(id);
		String res[][] = con.get(sql);
		
		if(res != null && res.length > 0 && res[0][0] != null) {
			this.zhiwei = res[0][0];
			for(int i = 1; i < res.length; i++) {
				this.zhiwei += ", " + res[i][0];
			}
		}
		
		this.mingchen = values[0][0];
		this.sex = values[0][1];
	//	this.zhiwei = values[0][2];
		this.shenfenzhengID = values[0][2];
		this.shouji = values[0][3];
		this.mail = values[0][4];
		this.zhuzhi = values[0][5];
		this.ruzhiTIME = values[0][6];
		this.zaigangSTAGE = values[0][7];
		try {
			this.gongzi = Float.parseFloat(values[0][8]);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//CallStack.printCallStatck();
	}
	
	public Staff(Integer id, String password) {
		this.id = id;
		this.password = password;
		this.check();
	}
	
	public void setId(Integer id) {
		System.out.println("setId");
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setPassword(String password) {
		System.out.println("setPassword");
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getState() {
		return state;
	}
	
	public String getZhiwei() {
		return zhiwei;
	}
	
	public void getinfo() {
		//System.out.println("check");
		if(this.password == null) this.password = "";
		
		Connsql con = new Connsql(Configuration.user, Configuration.password);
		String goalparms[] = {"mingchen","sex","shenfenzhengID","shouji",
				"mail","zhuzhi","ruzhiTIME","zaigangSTAGE", "gongzi", "pass"};
		String parms[] = {"ID"};
		String value[] = {Integer.toString(id)};
//		System.out.println(value[0]);
		String values[][] = con.select(goalparms, parms, value, "YuanGong");
		
		if(values == null) {
			//System.out.println("state: "+state);
			state = "NOT EXIST";
			return;
		}
		
		String sql = "select jueseNAME from YuanGongJueSe, Juese where "
				+ "JueSe.jueseID = YuanGongJueSe.jueseID and yuangongID = "+Integer.toString(id);
		String res[][] = con.get(sql);
		
		if(res != null && res.length > 0 && res[0][0] != null) {
			this.zhiwei = res[0][0];
			for(int i = 1; i < res.length; i++) {
				this.zhiwei += ", " + res[i][0];
			}
		}
		
		this.mingchen = values[0][0];
		this.sex = values[0][1];
		this.shenfenzhengID = values[0][2];
		this.shouji = values[0][3];
		this.mail = values[0][4];
		this.zhuzhi = values[0][5];
		this.ruzhiTIME = values[0][6];
		this.zaigangSTAGE = values[0][7];
		try {
			this.gongzi = Float.parseFloat(values[0][8]);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) throws ParseException {
//		// TODO Auto-generated method stub
//		Staff a = new Staff(1,"123456");
//		String state = a.getState();
//		System.out.println(state);
//	}
////	public void hahaha() {
////		System.out.println(this.mingchen + this.state);
////	}
}
