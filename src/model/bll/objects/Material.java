package model.bll.objects;

public class Material {
	private String mingcheng;
	private Integer dbgg;
	private Integer baozhiqi;
	private String danwei;
	public Material() {
	}
	public void setMingcheng(String mingcheng) {
		this.mingcheng = mingcheng;
	}
	public String getMingcheng() {
		return this.mingcheng;
	}
	public void setDbgg(Integer dbgg) {
		this.dbgg = dbgg;
		
	}
	public Integer getDbgg() {
		return this.dbgg;
	}
	public void setBaozhiqi(Integer baozhiqi) {
		this.baozhiqi = baozhiqi;
	}
	public Integer getBaozhiqi() {
		return this.baozhiqi;
	}
	public void setDanwei(String danwei) {
		this.danwei = danwei;
	}
	public String getDanwei() {
		return this.danwei;
	}
}
