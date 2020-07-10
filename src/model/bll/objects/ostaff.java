package model.bll.objects;
import java.util.*;
public class ostaff {
	private String mingchen;
	private String sex;
	private String zhiwei;
	private String sfzid;
	private String shouji;
	private String mail;
	private String zhuzhi;
	private Date rztime;
	private String zgstate;
	private String password;
	private Float gongzi;
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setZhiwei(String zhiwei) {
		this.zhiwei = zhiwei;
	}
	public String getZhiwei() {
		return zhiwei;
	}
	public void setMingchen(String mingchen) {
		this.mingchen = mingchen;
	}
	public String getMingchen() {
		return this.mingchen;
	}
	public void setShouji(String shouji) {
		this.shouji = shouji;
	}
	public String getShouji() { 
		return this.shouji;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getMail() {
		return this.mail;
	}
	public void setZhuzhi(String zhuzhi) {
		this.zhuzhi = zhuzhi;
	}
	public String getZhuzhi() {
		return this.zhuzhi;
	}
	public void setSfzid(String sfzid) {
		this.sfzid = sfzid;
	}
	public String getSfzid() {
		return this.sfzid;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSex() {
		return this.sex;
	}
	public void setRztime(Date rztime) {
		this.rztime = rztime;
	}
	public Date getRztime() {
		return this.rztime;
	}
	public void setZgstate(String zgstate) {
		this.zgstate = zgstate;
	}
	public String getZgstate() {
		return this.zgstate;
	}
	public void setGongzi(Float gongzi) {
		this.gongzi = gongzi;
	}
	public Float getGongzi() {
		return this.gongzi;
	}
}
