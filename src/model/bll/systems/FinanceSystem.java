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
	//返回剩余尾款
	public static float leftMoney(int ddid) {
		String [] parms = {"dingdanID"};
		String [] values = {Integer.toString(ddid)};
		String [] goalParms = {"yingfuMONEY","shifuMONEY"};
		String [][] result = conn.select(goalParms,parms, values, "DingDan");
		float yingfu= Float.parseFloat(result[0][0]);
		float shifu= Float.parseFloat(result[0][1]);
		return yingfu-shifu;//返回订单尾款金额
	}
	//收款
	public int getMoney(int ddid,float money,String shoukuantype) {
		float leftmoney = leftMoney(ddid);
		if(money<leftmoney) return -1;//钱给少了
		if(money>leftmoney) return -2;//钱给多了
		
		String [] parms = {"dingdanID"};
		String [] values = {Integer.toString(ddid)};
		String [] goalparms = {"dingdanSTATE","yingfuMONEY"};
		String [][] result = conn.select(goalparms, parms, values, "DingDan");
		float yingfu = Float.parseFloat(result[0][1]);
		String state = result[0][0];
		String [] setparms = {"dingdanSTATE","shifuMONEY"};
		String [] setvalues = {"结清尾款",Float.toString(yingfu)};
		//更新订单状态，为结清尾款，实付金额改成应付金额
		conn.update(setparms, setvalues, parms, values,"DingDan");
		
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String [] insertparms = {"dingdanID","chunaID","shoukuanMONEY","shoukuanTIME","shoukuanTYPE","dingdanSTATE"};
		String [] insertvalues = {Integer.toString(ddid),Integer.toString(staff.getId()),Float.toString(money),timestr,shoukuantype,state};
		//收款表插入一行
		conn.insert(insertparms, insertvalues, "ShouKuan");
		return 1;//表示成功
		
	}
	//检查某一时间到现在的所有订单和收款记录是否一致
	public int checkMoney(Date begin) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String [] parms = {beginstr};
		int state = Integer.parseInt(conn.selectScale(parms,"Check_in"));
		return state;//1表示没错，0表示账目出错
	}
	public JTable expend(Date begin,Date end) {
		String [] title = {"进货批次", "进货时间","原料名称","进货数量","原料单价","打包规格" ,"负责员工"};
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
	//某段时间的收入情况
	public JTable income(Date begin,Date end) {
		String [] title = {"收款订单", "收款时间","出纳人"};
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
	//员工流
	public static String[][] getYuanGongFlow(String yuangongID) {
		String sql = "select jueseNAME from YuanGong,JueSe,YuanGongJueSe where "
				+ "YuanGong.ID="+yuangongID+" and YuanGong.ID = YuanGongJueSe.yuangongID and "
						+ "YuanGongJueSe.jueseID = JueSe.jueseID";
		String[][] jueseNAME = conn.get(sql);
		String[][] sold = null, material = null, allFlow = null;
		
		if(jueseNAME != null)
		for(int i = 0; i < jueseNAME.length; i++) {
			if(jueseNAME[i][0].startsWith("销售员工")) {
				sold = SoldSystem.getFlow(yuangongID);
			}
			else if(jueseNAME[i][0].startsWith("原料员工")) {
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
					allFlow[i][sold[i].length] = "销售订单";
				}
			}
			if(material != null) {
				for(int k = 0; k < material.length; k++, i++) {
					allFlow[i] = new String[material[k].length+1];
					for(int j = 0; j < material[k].length; j++)
						allFlow[i][j] = material[k][j];
					allFlow[i][material[k].length] = "原料订单";
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
	//按照时间条件筛选出进货订单
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
