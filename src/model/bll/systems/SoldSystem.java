package model.bll.systems;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.bll.objects.DingDanXiJie;
import model.bll.objects.Dingdan;
import model.bll.objects.Staff;
import model.dal.FileIO;

public class SoldSystem implements SystemFather   {
	static Staff staff;
	//static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>();
	
	static{
		property.put("�������", "dingdanID");
		property.put("�ͻ����", "kehuID");
		property.put("�µ�ʱ��", "baozhiqi");
		property.put("�˻�ʱ��", "dabaoguige");
		property.put("����״̬", "danwei");
		property.put("Ӧ�����", "cangkuID");
		property.put("ʵ�����", "cangkuzhurenID");
		property.put("��Ʒ���", "cangkuSTATE");
		property.put("��Ʒ����", "cangkuTYPE");
		property.put("�˻�ʱ��", "cangkuSIZE");
		property.put("�˻�����", "leftSIZE");

	}
	public SoldSystem() {
		
	}
	public SoldSystem(Staff staff) {
		this.staff = staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	
	public static String newOrder(String kehumingchen, Float shifuMoney, 
			String [] chanpinmingchen, Integer[] chanpinnum,Date tihuoTime, Integer yuangongID) {
		String kehuID = getkehuID(kehumingchen);
		System.out.println("kehuID: " + kehuID+" "+chanpinmingchen.length+" "+chanpinnum[0]);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String xiadanTime = df.format(new Date());
		float yingfuMoney = 0;
		String dingdanState = "������";
		
		if(chanpinmingchen != null && chanpinnum != null)
			for(int i = 0; i < chanpinmingchen.length; i++) {
				float danjia = getdanjia(chanpinmingchen[i]);
				
				yingfuMoney += danjia * chanpinnum[i];
				//System.out.println("s "+chanpinmingchen[i].toString()+floats[i].toString());
			}
		
		String[] chanpinID = new String[chanpinmingchen.length];
		for(int i = 0; i < chanpinID.length; i++) {
			chanpinID[i] = getchanpinID(chanpinmingchen[i]);
		}
		String[] chanpinNum = new String[chanpinnum.length];
		for(int i = 0; i < chanpinNum.length; i++)
			chanpinNum[i] = chanpinnum[i].toString();
		return CreateOrder(kehuID, dingdanState, df.format(tihuoTime), 
				xiadanTime, chanpinID, chanpinNum, yingfuMoney, shifuMoney, yuangongID.toString());
	}
	
	private static String getchanpinID(String m) {
		String sql = "select ID from ChanPin where mingchen='"+m+"'";
		String[][] ret = conn.get(sql);
		
		return ret[0][0];
	}
	
	private static float getdanjia(String m) {
		String sql = "select jiage from ChanPin where mingchen='"+m+"'";
		String[][] ret = conn.get(sql);
		if(ret != null)
			return Float.parseFloat(ret[0][0]);
		return 0;
	}
	
	public static  String[] getDingDanIDWithKeHuMingCheng(String kehumingchen) {
		String[][] ret = conn.get("select dingdanID from KeHu,DingDan where kehuNAME='"+kehumingchen+"'"
				+" and KeHu.kehuID = DingDan.kehuID and DingDan.dingdanSTATE = '������'");
		if(ret == null) {
			return null;
		}
		
		String[] res =  new String[ret.length];
		for(int i = 0; i < res.length; i++) res[i] = ret[i][0];
		return res;
 	}
	
	private static  String getkehuID(String kehumingchen) {
		String[][] ret = conn.get("select kehuID from KeHu where kehuNAME"
				+ " = '" + kehumingchen + "'");
		if(ret == null) {
			return null;
		}
		return ret[0][0];
 	}
	
	private static String CreateOrder(String kehuID,String dingdanSTATE,
			String tihuoTime, String xiadanTime,
			String [] chanpinID, String[] chanpinnum,
			float yingfuMoney, Float shifuMoney, String yuangongID) {
		int numParms = 7;
		String tableName = "DingDan";
		
		String parms[] = new String[numParms];
		parms[0] = "kehuID";
		parms[1] = "xiadanTime";
		parms[2] = "tihuoTime";
		parms[3] = "dingdanSTATE";
		parms[4] = "yingfuMONEY";
		parms[5] = "shifuMONEY";
		parms[6] = "YuanGongID";
		
		String values[] = new String[numParms];
		values[0] = kehuID;
		values[1] = xiadanTime;
		values[2] = tihuoTime;
		values[3] = dingdanSTATE;
		values[4] = Float.toString(yingfuMoney);
		values[5] = Float.toString(shifuMoney);
		values[6] = yuangongID;
		
		//��Ϣ�Ƿ�����
		if(!judgeIfWrongMessage(parms, values, tableName)) {
			return "��Ϣ����";
		}
//		//���ж��Ƿ�ӵ�
//		if(!judgeIfAccept(parms, values, tableName, chanpinID)) {
//			return "�޷��ӵ�";
//		}
		conn.insert(parms, values, tableName);
		
		String [] goalParms = {"dingdanID"};
		String [][] result = conn.select(goalParms, parms, values, "DingDan");
		int dingdanID = Integer.parseInt(result[0][0]);
		
		parms = new String[3];
		parms[0] = "dingdanID";
		parms[1] = "chanpinID";
		parms[2] = "chanpinNUM";
		tableName = "DingDanXiJie";
		
		values = new String[3];
		values[0] = Integer.toString(dingdanID);
		
		for(int i = 0; i < chanpinID.length; i++) {
			values[1] = chanpinID[i];
			values[2] = chanpinnum[i];
			conn.insert(parms, values, tableName);
		}
		
		return "OK";
	}

	// �ж���Ϣ�Ƿ�����
	private static boolean judgeIfWrongMessage(String[] parms, String[] values, String tableName) {
		
		return true;
	}

	@SuppressWarnings("unused")
	private static boolean judgeIfAccept(String[] parms, 
			String[] values, String tableName, String chanpinID) {
		// �õ��ò�Ʒ�ֿ��桢�������������������ĸò�Ʒ�Ķ����������ж��������Ƿ��㹻
		int kuCun = ProductSystem.Storage(Integer.parseInt(chanpinID));
		int shengChanLi = WorkSystem.chanNeng(Integer.parseInt(chanpinID));
//		int kuCun = 0;
//		int shengChanLi = 0;
		int dingDanNum = productNum(values[0], values[1]);
		@SuppressWarnings("deprecation")
		long different = new Date(values[3]).getTime() - new Date(values[4]).getTime();
		
		different = different/(1000 * 60 * 60 * 24);
		
		if(kuCun + shengChanLi * different - dingDanNum >= 0)
			return true;
		return false;
	}

	// ĳ�ֲ�Ʒ������������
	private static int productNum(String dingdanID, String chanpinID) {
		String parms[] = {"dingdanID", "dingdanSTATE", "chanpinID"};
		String values[] = {dingdanID, "������",chanpinID};
		String goalParms[] = {"chnapinNUM"};
		String tableName = "DingDanAndXiJie";
		int num = 0;
		
		String ret[][] = conn.select(goalParms, parms, values, tableName);
		
		if(ret == null || ret[0] == null || ret[0][0] == "NULL") {
			return 0;
		}
		for(int i = 0; i < ret[0].length; i++) {
			num += Integer.parseInt(ret[0][i]);
		}
		
		values[1] = "��ȡ��";
		ret = conn.select(goalParms, parms, values, tableName);
		if(ret == null || ret[0] == null || ret[0][0] == "NULL") {
			return 0;
		}
		for(int i = 0; i < ret[0].length; i++) {
			num += Integer.parseInt(ret[0][i]);
		}

		return num;
	}

	// ��ѯ
	public static String[][] select(String goalParms[], String parms[], String values[], String tableName) {
		return conn.select(goalParms, parms, values, tableName);
	}
	
	// Ԥ�����ۼƻ�
	private static String salePlan(Date t1, Date t2) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String ts1 = df.format(t1);
		String ts2 = df.format(t2);
		String sql = "select ChanPin.ID,chanpinNUM from DingDan, DingdanXiJie,ChanPin"
				+ " where DingDan.dingdanSTATE = '���' and DingDan.dingdanID = DingDanXiJie.dingdanID"
				+ " and DingDanXiJie.chanpinID = ChanPin.ID and (datename(dayofyear,'"+ts1+"') <= datename(dayofyear, tihuoTime) or "
						+ "datename(dayofyear, tihuoTime) <= datename(dayofyear,'"+ts2+"'))" ;
		
		String ret[][] = conn.get(sql);
		//ÿ�ֲ�Ʒ�ܹ����ٸ�
		Map<String, Float> map = new HashMap<String, Float>();
		//ÿ�ֲ�Ʒ�ܹ����˶��ٵ�
		Map<String, Float> map2 = new HashMap<String, Float>();
		
		if(ret != null)
			for(int i = 0; i < ret.length; i++) {
				if(map.containsKey(ret[i][0])) {
					Float tmp = map.get(ret[i][0]);
					map.put(ret[i][0], (float) Float.parseFloat(tmp + ret[i][1]));
					Float tmp2 = map.get(ret[i][0]);
					map2.put(ret[i][0], tmp2 + 1);
				}
				else {
					map.put(ret[i][0], (float) Float.parseFloat(ret[i][1]));
					map2.put(ret[i][0], (float) 1);
				}
			}
		
		int i = 0;
		Iterator<String> it = map.keySet().iterator();
		Iterator<String> it2 = map2.keySet().iterator();
		
		String[] parms2 = {"qishiTIME", "jieshushiTIME"};
		String[] values2 = {ts1, ts2};
		conn.insert(parms2, values2, "XiaoShouYuJi");
		String sql2 = "select max(xiaoshoujihuaID) from XiaoShouYuJi";
		String id = conn.get(sql2)[0][0];
		
		String[] parms = {"xiaoshoujihuaID","chanpinID", "chanpinNUM"};
		String[] values = new String[3];
		values[0] = id;
		
        while(it.hasNext()) {
            //�õ�ÿһ��key
            String key = it.next();
            it2.next();
            //ͨ��key��ȡ��Ӧ��value
            Float value = map.get(key);
            Float value2 = map2.get(key);
            
            Float v = (float) value.intValue();
            int v2 = value2.intValue();
            
            Double val = (Double) (v * 1.2  / v2);
            values[1] = key;
            Integer tp = val.intValue();
            values[2] = tp.toString();
           // System.out.println(key);
            conn.insert(parms, values, "XiaoShouYuJiXiJie");
            
            i = i + 1;
        }
        
        return id;
	}
	
	public static String[][] getPlan(Date t1, Date t2) {
		//deleteOld();
		String id = salePlan(t1, t2);
		return readPlan(id);
	}
	private static String[][] readPlan(String id) {
		String sql = "select mingchen,chanpinNUM,ChanPin.ID from XiaoShouYuJi,ChanPin,XiaoShouYuJiXiJie"
				+ " where XiaoShouYuJi.xiaoshoujihuaID=XiaoShouYuJiXiJie.xiaoshoujihuaID and"
				+ " XiaoShouYuJiXiJie.chanpinID=ChanPin.ID and XiaoShouYuJi.xiaoshoujihuaID="+id;
		return conn.get(sql);
	}
	private static void deleteOld() {
		conn.delete(null, null, "XiaoshouYuJi");
	}
	public static void savePlan(String[] chanpinID, String[] chanpinNUM) {
		String[] setparms = {"chanpinNUM"};
		String[] setvalues = {"chanpinNUM"};
		String[] parms = {"chanpinID"};
		String[] values = {"chanpinID"};
		for(int i = 0; i < chanpinID.length; i++) {
			setvalues[0] = chanpinNUM[i];
			values[0] = chanpinID[i];
			conn.update(setparms, setvalues, parms, values, "XiaoShouYuJi");
			//System.out.println("update   "+values[0]+" "+setvalues[0]);
		}
			
		//	conn.update(setparms, dingdanNUM[i], {dingdanNUM[i]}, values, tableName);
	}

	// ������Ȩ
	public boolean authorize() {
		String s[] = null;
		try {
			s = FileIO.readAlllines("salePlan.txt");
		} catch (IOException e) {
			return false;
		}
		if(s[s.length-1].equals("1")) {
			return true;
		}
		if(staff.getZhiwei().equals("�ܾ���")) {
			s[s.length-1] = "1";
			try {
				FileIO.writeAlllines(false, "salePlan.txt", s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		else {
			return false;
		}
	}
	
	public static String[]  selectAllKeHuMingCheng(){
		String [] goalParms = {"kehuNAME"};
		String [] parms = null;
		String [] values = null;
		
		String[][] arr = conn.select(goalParms, parms, values, "KeHu");
		if(arr == null) return new String[0];
		
		String [] res = new String[arr.length];
		for(int i = 0; i < arr.length; i++)
			res[i] = arr[i][0];
		return res;
	}
	
	//�鶩��ϸ��
	public static String[][] searchXiJie(String dingdanID) {
		String sql = "select DingDanXiJie.chanpinID,mingchen,chanpinNUM from"
				+ " DingDanXiJie,ChanPin where ChanPin.ID = DingDanXiJie.chanpinID"
				+ " and dingdanID = " + dingdanID;
		return conn.get(sql);
	}
	
	//��ѯ����,��������options��
	public static String[][] search(Dingdan options) {
		String sql = "select dingdanID,kehuID,xiadanTime,tihuoTime,dingdanSTATE,yingfuMONEY,shifuMONEY from DingDan where ";
		Boolean first = true;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ

		if(options.getDingdanID() != null) {
			if(!first) sql += " and ";
			sql += " dingdanID = " + options.getDingdanID().toString();
			first = false;
		}
		if(options.getKehuID() != null) {
			if(!first) sql += " and ";
			sql += " kehuID = " + options.getKehuID().toString();
			first = false;
		}
		if(options.getXiadanTime1() != null) {
			if(!first) sql += " and ";
			sql += " xiadanTime >= '" + df.format(options.getXiadanTime1())+"'";
			first = false;
		}
		if(options.getXiadanTime2() != null) {
			if(!first) sql += " and ";
			sql += " xiadanTime <= '" + df.format(options.getXiadanTime2())+"'";
			first = false;
		}
		if(options.getTihuoTime1() != null) {
			if(!first) sql += " and ";
			sql += " tihuoTime >= '" + df.format(options.getTihuoTime1())+"'";
			first = false;
		}
		if(options.getTihuoTime2() != null) {
			if(!first) sql += " and ";
			sql += " tihuoTime <= '" + df.format(options.getTihuoTime2())+"'";
			first = false;
		}
		if(options.getDingdanSTATE() != null && options.getDingdanSTATE().length() > 0) {
			if(!first) sql += " and ";
			sql += " dingdanSTATE = '" + options.getDingdanSTATE() +"'";
			first = false;
		}
		//System.out.println(sql);
		
		String res[][] = conn.get(sql);

		return res;
	}
	
	//���
	public static void tihuo(String kehumingchen, Integer dingdanID, 
			String activity_apply_money, String shoukuanTYPE, String chunaID) {
		//chanPinChuKu
		
		//���¶�����
		String[] setparms = {"dingdanSTATE"};
		String[] setvalues = {"�����"};
		String[] parms = {"dingdanID"};
		String[] values = {dingdanID.toString()};
 		conn.update(setparms, setvalues, parms, values, "DingDan");
		
 		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String nowTime = df.format(new Date());
		//�����տ��
 		String[] parms2 = {"dingdanID", "shoukuanMONEY", "chunaID", 
 				"shoukuanTIME", "shoukuanTYPE"};
 		String[] values2 = {dingdanID.toString(), activity_apply_money, chunaID,
 				nowTime, shoukuanTYPE};
 		conn.insert(parms2, values2, "ShouKuan");
	}
	public static String getMoney(String dingdanID) {
		String sql = "select yingfuMONEY,shifuMONEY from DingDan where dingdanID =" + dingdanID;
		System.out.println(sql);
		String[][] ret = conn.get(sql);
//		for(int i = 0; i < ret.length; i++)
//			for(int j = 0; j < ret[i].length; j++) 
//				System.out.println(ret[i][j]);
		Float yingfu = Float.parseFloat(ret[0][0]);
		Float yifu = Float.parseFloat(ret[0][1]);
		Float ans = yingfu-yifu;
		return ans.toString();
	}
	
	///��������������
	public static String getTuiMoney(String dingdanID) {
		String sql = "select yingfuMONEY,shifuMONEY from "
				+ "DingDan where DingDan.dingdanID =" + dingdanID +
				" and DingDan.dingdanSTATE='���' and DingDan.dingdanID "
				+ "not in (select dingdanID from TuiHuo)";
		String[][] ret = conn.get(sql);
		
		return ret[0][0];
	}
	public static String[] TuigetDingDanIDWithKeHuMingCheng(String name) {
		String[][] ret = conn.get("select DingDan.dingdanID from KeHu,DingDan where kehuNAME='"+name+"'"
				+" and KeHu.kehuID = DingDan.kehuID and DingDan.dingdanSTATE = '���' and DingDan.dingdanID " + 
				" not in (select dingdanID from TuiHuo)");
//		System.out.println("select DingDan.dingdanID from KeHu,DingDan where kehuNAME='"+name+"'"
//				+" and KeHu.kehuID = DingDan.kehuID and DingDan.dingdanSTATE = '������'");
//		
		if(ret == null) return null;
		
		String[] res =  new String[ret.length];
		for(int i = 0; i < res.length; i++) res[i] = ret[i][0];
		
		return res;
	}
	
	//�˻�	
	public static void tuihuo(String yuangongID, Integer dingdanID, String activity_apply_money) {
		String[] parms = {"dingdanID", "tuihuoTIME", "yuangongID","chuliTYPE"};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		String tuohuoTime = df.format(new Date());
		String[] values = {dingdanID.toString(), tuohuoTime, yuangongID,"�˿�"};
		conn.insert(parms, values, "TuiHuo");
	}
	
	//��ˮ
	public static String[][] getFlow(String yuangongID) {
		String sql = "select dingdanID,yingfuMONEY, shifuMONEY, xiadanTime "
				+ "from DingDan where yuangongID = "+yuangongID;
		return conn.get(sql);
	}
	
	public static String[][] getSoldMoneySum(String time1, String time2) {
		String sql = "select dingdanID,yingfuMONEY, shifuMONEY, xiadanTime from DingDan "
				+ "where xiadanTime >= '"+time1+"' and xiadanTime <= '"+time2+"'";
		return conn.get(sql);
	}
	public static String[][] readNowPlan() {
		String sql = "select mingchen,chanpinNUM,ChanPin.ID from XiaoShouYuJiXiJie,ChanPin"
				+ " where XiaoShouYuJiXiJie.chanpinID=ChanPin.ID";
		return conn.get(sql);
	}
	public static String[][] getDingDanXiJieWithDingdanID(String options) {
		String sql = "select mingchen,chanpinNUM from DingDanXiJie, ChanPin where"
				+ " DingDanXiJie.chanpinID = ChanPin.ID and dingdanID = "+options;
		return conn.get(sql);
	}
	//�õ���Ҫ�����Ĳ�Ʒ������
	public static String[][] getFutureChanpinNum() {
		String sql = "select chanpinID,sum(chanpinNUM) from DingDan, DingdanXiJie where "
				+ "DingDan.dingdanID = DingDanXiJie.dingdanID and (dingdanSTATE = '������' "
				+ " or dingdanSTATE = '�����')"
				+ " group by chanpinID";
		return conn.get(sql);
	}
	public static String[][] selectAllKeHu() {
		String sql = "select * from KeHu";
		return conn.get(sql);
	}
	//�����ϸ��
	public static String[][] tihuoGetXiJieAndKuCun(String dingdanID) {
		String sql = "select DingDanXiJie.chanpinID, mingchen,chanpinNUM,chanpinNUM from  Dingdan, DingDanXiJie, ChanPin"
				+ " where Dingdan.dingdanID = "+dingdanID +" and DingDanXiJie.dingdanID = Dingdan.dingdanID and chanpinID = ID";
		System.out.println(sql);
		String[][] res = conn.get(sql);
		if(res != null)
		for(int i = 0; i < res.length; i++)
			res[i][3] = ""+ProductSystem.Storage(Integer.parseInt(res[i][0]));
		return res;
	}
	//�������ۼƻ�
	public static String[][] selectSoldPlan() {
		String sql = "select * from XiaoShouYuJi";
		return conn.get(sql);
	}
	public static String[][] fixPlanGetXiJie(String id) {
		String sql = "select chanpinID, mingchen, chanpinNUM from XiaoShouYuJiXiJie,ChanPin where "
				+ "	xiaoshoujihuaID = "+id + " and chanpinID = ChanPin.ID";
		return conn.get(sql);
	}
	public static void fixPlanSavePlan(String id, 
			String[] chanpinID, String[] chanpinNUM) {
		String[] setparms = {"chanpinNUM"};
		String[] setvalues = {""};
		
		String[] parms = {"xiaoshoujihuaID", "chanpinID"};
		String[] values = new String[2];
		for(int i = 0; i < chanpinID.length; i++) {
			values[0] = id;
			values[1] = chanpinID[i];
			setvalues[0] = chanpinNUM[i];
			conn.update(setparms, setvalues, parms, values, "XiaoShouYuJiXiJie");
		}
	}
	public static String[][] selectKeHu(String id, String name) {
		String sql = "select * from KeHu  ";
		Boolean first = true;
		
		if(id != null && id.length() > 0) {
			if(!first) sql += " and ";
			else sql += "where ";
			sql += " kehuID = " + id;
			first = false;
		}
		if(name != null && name.length() > 0) {
			if(!first) sql += " and ";
			else sql += "where ";
			sql += " kehuNAME = '" + name+"'";
			first = false;
		}
		
		System.out.println(sql);
		
		String res[][] = conn.get(sql);

		return res;
	}
	public static String[][] getDingDanFlow1() {
		String sql = "select dingdanID,yingfuMONEY, shifuMONEY, xiadanTime from DingDan";
		return addFlowType(conn.get(sql));
	}
	//������
	public static String[][] getDingDanFlow(String format, String format2) {
		String sql = "select dingdanID,yingfuMONEY, shifuMONEY, xiadanTime from DingDan"
				+ " where xiadanTime >= '"+format + "' and xiadanTime <= '"+format2+"'";
		return addFlowType(conn.get(sql));
	}
	//������۶����ֶ�
	private static String[][] addFlowType(String[][] s) {
		if(s == null) return null;
		String[][] res = new String[s.length][s[0].length+1];
		for(int i = 0; i < s.length; i++) {
			for(int j = 0; j < s[i].length; j++)
				res[i][j] = s[i][j];
			res[i][s[i].length] = "���۶���";
		}
		return res;
	}
	
	public static String[][] selectAllChanPinMingCheng() {
		String sql = "select mingchen,jiage,danwei from ChanPin";
		String[][] res = conn.get(sql);
//		if(res == null)
//			return null;
//		String[] ans = new String[res.length];
//		for(int i = 0; i < ans.length; i++)
//			ans[i] = res[i][0];
		return res;
	}
	public static String[][] selectTuiHuo(Integer dingdanID, Integer kehuID, Date xiadanTime1, Date xiadanTime2) {
		String sql = "select * from TuiHuo ";
		Boolean f = true;
		
		if(dingdanID != null && dingdanID != 0) {
			if(f) {
				sql += " where "; f = false;
			}
			else sql += " and ";
			sql += " dingdanID =" + dingdanID.toString();
		}
		if(kehuID != null && kehuID != 0) {
			if(f) {
				sql += " where "; f = false;
			}
			else sql += " and ";
			sql += " kehuID =" + kehuID.toString();
		}
		
		if(xiadanTime1 != null && xiadanTime2 != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String t1 = df.format(xiadanTime1);
			String t2 = df.format(xiadanTime2);
			if(f) {
				sql += " where "; f = false;
			}
			sql += "tuihuoTIME >= '"+t1+"' and tuihuoTIME <= '"+t2+"'";
		}
		
		return conn.get(sql);
	}
	public static String[][]selectFuDingJin() {
	//	String sql = "select "
		return null;
	}
	public static String[][] TuiDing(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}
