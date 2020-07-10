package model.bll.systems;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

import java.text.SimpleDateFormat;

public class FinanceSystem implements SystemFather{
	Staff staff;
	static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	public FinanceSystem() {
	}
	public FinanceSystem(Staff staff) {
		this.staff = staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	//����ʣ��β��
	public static float leftMoney(int ddid) {
		String [] parms = {"dingdanID"};
		String [] values = {Integer.toString(ddid)};
		String [] goalParms = {"yingfuMONEY","shifuMONEY"};
		String [][] result = conn.select(goalParms,parms, values, "DingDan");
		float yingfu= Float.parseFloat(result[0][0]);
		float shifu= Float.parseFloat(result[0][1]);
		return yingfu-shifu;//���ض���β����
	}
	//�տ�
	public int getMoney(int ddid,float money,String shoukuantype) {
		float leftmoney = leftMoney(ddid);
		if(money<leftmoney) return -1;//Ǯ������
		if(money>leftmoney) return -2;//Ǯ������
		
		String [] parms = {"dingdanID"};
		String [] values = {Integer.toString(ddid)};
		String [] goalparms = {"dingdanSTATE","yingfuMONEY"};
		String [][] result = conn.select(goalparms, parms, values, "DingDan");
		float yingfu = Float.parseFloat(result[0][1]);
		String state = result[0][0];
		String [] setparms = {"dingdanSTATE","shifuMONEY"};
		String [] setvalues = {"����β��",Float.toString(yingfu)};
		//���¶���״̬��Ϊ����β�ʵ�����ĳ�Ӧ�����
		conn.update(setparms, setvalues, parms, values,"DingDan");
		
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String [] insertparms = {"dingdanID","chunaID","shoukuanMONEY","shoukuanTIME","shoukuanTYPE","dingdanSTATE"};
		String [] insertvalues = {Integer.toString(ddid),Integer.toString(staff.getId()),Float.toString(money),timestr,shoukuantype,state};
		//�տ�����һ��
		conn.insert(insertparms, insertvalues, "ShouKuan");
		return 1;//��ʾ�ɹ�
		
	}
	//���ĳһʱ�䵽���ڵ����ж������տ��¼�Ƿ�һ��
	public int checkMoney(Date begin) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String [] parms = {beginstr};
		int state = Integer.parseInt(conn.selectScale(parms,"Check_in"));
		return state;//1��ʾû��0��ʾ��Ŀ����
	}
	public JTable expend(Date begin,Date end) {
		String [] title = {"��������", "����ʱ��","ԭ������","��������","ԭ�ϵ���","������" ,"����Ա��"};
		String [] goalParms = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String endstr = format.format(end);
		String [] parms = {beginstr,endstr};
		String [][] result = conn.selectTable(goalParms, parms, "Expend");
		DefaultTableModel tmodel = new DefaultTableModel(title,0);
		for(int i=0;i<result.length;i++) {
			tmodel.addRow(result[i]);
		}
		JTable table=new JTable(tmodel);
		return table;
	}
	//ĳ��ʱ����������
	public JTable income(Date begin,Date end) {
		String [] title = {"�տ��", "�տ�ʱ��","������"};
		String [] goalParms = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String endstr = format.format(end);
		String [] parms = {beginstr,endstr};
		String [][] result = conn.selectTable(goalParms, parms, "Income");
		DefaultTableModel tmodel = new DefaultTableModel(title,0);
		for(int i=0;i<result.length;i++) {
			tmodel.addRow(result[i]);
		}
		JTable table=new JTable(tmodel);
		return table;
	}
	public float profix(Date begin,Date end) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String endstr = format.format(end);
		String [] parms = {beginstr,endstr};
		return Float.parseFloat(conn.selectScale(parms,"Earn"));
	}
	//Ա����
	public static String[][] getYuanGongFlow(String yuangongID) {
		String sql = "select jueseNAME from YuanGong,JueSe,YuanGongJueSe where "
				+ "YuanGong.ID="+yuangongID+" and YuanGong.ID = YuanGongJueSe.yuangongID and "
						+ "YuanGongJueSe.jueseID = JueSe.jueseID";
		String[][] jueseNAME = conn.get(sql);
		String[][] sold = null, material = null, allFlow = null;
		
		if(jueseNAME != null)
		for(int i = 0; i < jueseNAME.length; i++) {
			if(jueseNAME[i][0].startsWith("����Ա��")) {
				sold = SoldSystem.getFlow(yuangongID);
			}
			else if(jueseNAME[i][0].startsWith("ԭ��Ա��")) {
				material = MaterialSystem.getFlow(yuangongID);
			}
		}
		
		int len = 0;
		if(sold != null) len += sold.length;
		if(material != null) len += material.length;
		//System.out.println(material.length);
		if(len != 0) {
			allFlow = new String[len][];
			int i = 0;
			if(sold != null) {
				for(; i < sold.length; i++) {
					allFlow[i] = new String[sold[i].length+1];
					for(int j = 0; j < sold[i].length; j++)
						allFlow[i][j] = sold[i][j];
					allFlow[i][sold[i].length] = "���۶���";
				}
			}
			if(material != null) {
				for(int k = 0; k < material.length; k++, i++) {
					allFlow[i] = new String[material[k].length+1];
					for(int j = 0; j < material[k].length; j++)
						allFlow[i][j] = material[k][j];
					allFlow[i][material[k].length] = "ԭ�϶���";
					//System.out.println("AllFlow " + material[i][0]+" "+allFlow[i][0]);
				}
			}
		}

		return allFlow;
	}
	public static String[][] getDingDanFlow1() {
		return SoldSystem.getDingDanFlow1();
	}
	public static String[][] getJinHuoFlow() {
		return MaterialSystem.getJinHuoFlow();
	}
	public static String[][] getSoldMoneySum(Date date, Date date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time1 = format.format(date);
		String time2 = format.format(date2);
		
		return SoldSystem.getSoldMoneySum(time1, time2);
	}
	public static String[][] getBuyMoneySum(Date date, Date date2) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time1 = format.format(date);
		String time2 = format.format(date2);
		
		return MaterialSystem.getBuyMoneySum(time1, time2);
	}
	public static String[][] DingdanflowFilter(Date date, Date date2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return SoldSystem.getDingDanFlow(df.format(date), df.format(date2));
	}
	//����ʱ������ɸѡ����������
	public static String[][] JinhuoflowFilter(Date date, Date date2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return MaterialSystem.getDingDanFlow(df.format(date), df.format(date2));
	}

	public static String[][] selectShouKuanSubmit(Integer dingdanID, Integer chunaID, Date xiadanTime1,
			Date xiadanTime2) {
		String sql = "select * from ShouKuan";
		Boolean first = true;
		if(dingdanID != null && dingdanID.toString().length() > 0) {
			if(first) {
				sql += " where "; first = false;
			}
			else sql += " and ";
			sql += " dingdanID  = " + dingdanID.toString();
		}
		
		if(chunaID != null && chunaID.toString().length() > 0) {
			if(first) {
				sql += " where "; first = false;
			}
			else sql += " and ";
			sql += " chunaID  = " + chunaID.toString();
		}
		
		if(xiadanTime1 != null && xiadanTime2 != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String t1 = df.format(xiadanTime1);
			String t2 = df.format(xiadanTime2);
			if(first) {
				sql += " where "; first = false;
			}
			else sql += " and ";
			sql += " shoukuanTIME  >= '" + t1 + "' and shoukuanTIME <= '"+t2+"'";
		}
		
		return conn.get(sql);
	}
} 
