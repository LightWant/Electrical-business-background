package model.bll.objects;

import model.dal.Configuration;
import model.dal.Connsql;

//import model.dal.Configuration;
//import model.dal.Connsql;

public class Staff {
	private Integer id;
	@SuppressWarnings("unused")
	private String mingchen;
	@SuppressWarnings("unused")
	private String sex;
	private String zhiwei;
	@SuppressWarnings("unused")
	private String shenfenzhengID;
	@SuppressWarnings("unused")
	private String shouji;
	@SuppressWarnings("unused")
	private String mail;
	@SuppressWarnings("unused")
	private String zhuzhi;
	@SuppressWarnings("unused")
	private String ruzhiTIME;
	@SuppressWarnings("unused")
	private String zaigangSTAGE;
	private String password;
	@SuppressWarnings("unused")
	private float gongzi;
	String state;
	
	public Staff() {
		id = 0;
	}
	
	public void check() {
		//System.out.println("check");
		if(this.password == null) this.password = "";
		Connsql con = new Connsql(Configuration.user, Configuration.password);
		String goalparms[] = {"mingchen","sex","zhiwei","shenfenzhengID","shouji",
				"mail","zhuzhi","ruzhiTIME","zaigangSTAGE", "gongzi", "pass"};
		String parms[] = new String[1];
		parms[0] = "ID";
		String value[] = new String[1];
		value[0] = Integer.toString(id);
//		System.out.println(value[0]);
		String values[][] = con.select(goalparms, parms, value, "YuanGong");
		
		if(values == null) {
			System.out.println("state:"+state);
			state = "NOT EXIST";
			return;
		}
//		System.out.println(values[0][10]);
		if(values[0][10] == null || values[0][10] == "NULL" || values[0][10].equals(password)) {
			System.out.println("state:"+state);
			state = "OK";
		}
		else {
			System.out.println("state:"+state);
			state = "Wrong Password";
			return ;
		}
		
		//System.out.println(values.length);
		this.mingchen = values[0][0];
		this.sex = values[0][1];
		this.zhiwei = values[0][2];
		this.shenfenzhengID = values[0][3];
		this.shouji = values[0][4];
		this.mail = values[0][5];
		this.zhuzhi = values[0][6];
		this.ruzhiTIME = values[0][7];
		this.zaigangSTAGE = values[0][8];
		try {
			this.gongzi = Float.parseFloat(values[0][9]);
		}catch(Exception e) {
			e.printStackTrace();
		}
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
