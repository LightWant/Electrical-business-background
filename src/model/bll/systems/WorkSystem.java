package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

//import model.dal.*;
public class WorkSystem implements SystemFather{
	private static Connsql conn;
	static Map<Integer, String> product;
	static Map<Integer, String> capacity;
	static Map<String,String>property = new HashMap<String,String>();
	private Staff staff;
	//private int wno;//������
	static {
		conn = new Connsql(Configuration.user,Configuration.password);
		product = new HashMap<Integer,String>();
		capacity = new HashMap<Integer,String>();
		String [] goal = {"chejianID"};
		String [][] cj = conn.select(goal,null,null,"CheJian");//���еĳ���ID
		if(cj!=null&&cj.length!=0) {
			//����г���
			for(int i = 0 ; i < cj.length; i++) {
				//��ÿ�����������ܺͲ�Ʒ
				String cjid = cj[i][0];
				String[] goalParms = {"chanpinID","channeng"};
				String[] parms = {"chejianID"};
				String[] values = {cjid};
				String[][]cjstr = conn.select(goalParms, parms, values, "CheJianXiJie");
				if(cjstr == null || cjstr.length == 0) {
					//����ó��䲻������Ʒ������
					continue;
				}
				String cp=cjstr[0][0] ,cn = cjstr[0][1];//�ó���Ĳ�Ʒstring������string
				
				for(int j = 1; j < cjstr.length; j++) {
					cp += ","+cjstr[j][0];
					cn += ","+cjstr[j][1];
				}
				product.put(Integer.parseInt(cjid), cp);// ��Ʒmap����Ӹó���Ĳ�Ʒ
				capacity.put(Integer.parseInt(cjid), cn); // ��Ʒmap����Ӹó���Ĳ���
			}
		}
		property.put("������","chejianID");
		property.put("�������α��","chejianzhurenID");
		property.put("�����Ʒ���","chejianchanpinID");
		property.put("�������","chejianchanneng");
		property.put("����״̬","chejianSTAGE");
		
	}
	public WorkSystem() {
		
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	//���ĳ����Ʒ�Ĳ���
	public static int chanNeng(int id) {
		String idstr = Integer.toString(id);
		int sum = 0;
		for(Integer key:capacity.keySet()) {
			String prostr = product.get(key);
			String capstr = capacity.get(key);
			if(!prostr.contains(idstr)) continue;
			String [] pro = prostr.split(",");
			String [] cap = capstr.split(",");
			for(int i = 0;i<pro.length;i++) {
				if(pro[i].equals(idstr)) {
					sum += Integer.parseInt(cap[i]);
				}
			}
		}
		return sum;
	}
	//ת�������ݿ�������
	public static String [] toSqlProperty(String [] parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	
	public WorkSystem(Staff staff) {
		this.staff = staff;
	}
	//pass�������Ըı�ʱ���²�Ʒ�Ͳ�������
	public static void refresh() {
		String [] goal = {"chejianID"};
		String [][] cj = conn.select(goal,null,null,"CheJian");//���еĳ���ID
		if(cj!=null&&cj.length!=0) {
			//����г���
			for(int i = 0 ; i < cj.length; i++) {
				//��ÿ�����������ܺͲ�Ʒ
				String cjid = cj[i][0];
				String[] goalParms = {"chanpinID","channeng"};
				String[] parms = {"chejianID"};
				String[] values = {cjid};
				String[][]cjstr = conn.select(goalParms, parms, values, "CheJianXiJie");
				if(cjstr == null || cjstr.length == 0) {
					//����ó��䲻������Ʒ������
					continue;
				}
				String cp=cjstr[0][0] ,cn = cjstr[0][1];//�ó���Ĳ�Ʒstring������string
				
				for(int j = 1; j < cjstr.length; j++) {
					cp += cjstr[j][0];
					cn += cjstr[j][1];
				}
				product.put(Integer.parseInt(cjid), cp);// ��Ʒmap����Ӹó���Ĳ�Ʒ
				capacity.put(Integer.parseInt(cjid), cn); // ��Ʒmap����Ӹó���Ĳ���
			}
		}
	}
	//ͨ�����ԣ���������
	public static String [][] work(int chanpinID,int num,String zjyid,int wno,String zhijian) {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String cpidstr = Integer.toString(chanpinID);
		String numstr=  Integer.toString(num);
		String zjyidstr = zjyid;
		String cjid = Integer.toString(wno);
		String [] parms = {"zhijianyuanID","chanpinID","shengchanNUM","shengyuNUM","shengchanTIME","chejianID"};
		String [] values = {zjyidstr,cpidstr,numstr,numstr,timestr,cjid};
		int state = conn.insert(parms, values, "ShengChan"); 
		if(state<0)return null;//-1 �ظ��-2����ʧ��
		int scpc = findSCPC(chanpinID,timestr);
		//System.out.println("get ��������Ϊ:"+scpc);
		
		//�������ϸ�ڱ�
		workUseMaterial(scpc,chanpinID,num,wno);
		String [] xjparms = {"shengchanpiciID"};
		String [] xjvalue = {""+scpc};
		String [][] xjresult = conn.select(null, xjparms, xjvalue, "ShengChanXiJie");
		if(xjresult == null||xjresult.length == 0) return null;//-3����ϸ��ʧ��
		//����ʼ��¼
		String [] pa = {"shengchanpici","chejianID","zhijianTIME","zhijianrenyuanID","zhijianSTATE"};
		String [] val = {""+scpc,cjid,timestr,zjyidstr,zhijian};
		if(conn.insert(pa, val, "ZhiJianJiLu")<0) {
			System.out.println("�����ʼ��¼ʧ��");
			return null;
		}
		return xjresult;//��������ϸ�ڣ����Ρ�ʹ�õĳ������Ρ�����
		//return scpc;//������������
	}
	//ͨ�����ԣ���������ϸ�����
	public static void workUseMaterial(int scpc,int cpID,int num,int wno) {
		//System.out.println("WorkUseMaterial");
		String [][]peifang = ProductSystem.findPeiFang(cpID);
		String [] ylstr = new String [peifang.length];
		//= peifang[0].split(",");
		String [] ylnumstr = new String [peifang.length];
		//peifang[1].split(",");
		System.out.println("��ƷiDΪ"+cpID+",�䷽���£�\n");
		for(int i = 0 ;i < peifang.length; i++) {
			ylstr[i] = peifang[i][0];
			System.out.print("ԭ��ID��"+ylstr[i]);
			ylnumstr[i] = peifang[i][1];
			System.out.println(",������"+ylnumstr[i]);
		}
		int cjID = wno;
		//int [] yl = new int[ylstr.length];
		//int [] ylnum = new int [ylnumstr.length];
		System.out.println("������Ҫ��������");
		for(int i = 0;i<ylstr.length;i++) {
			int ylID = Integer.parseInt(ylstr[i]);
			int ylnum = Integer.parseInt(ylnumstr[i])*num;
			System.out.println("ylid:"+ylID);
			System.out.println("ylnum:"+ylnum);
			//System.out.println(""+ylID+""+ylnum);
			conn.executeUYL(scpc,ylID,ylnum,cjID);//��ÿһ���䷽�е�ԭ�϶�ִ��useMaterial�洢����
		}
		
	}
	//pass�������г���id 
	public static int[] selectAllWork() {
		String []goal = {"chejianID"};
		String [][]result = conn.select(goal, null, null, "CheJian");
		int [] ids = new int[result.length];
		for(int i = 0;i<result.length;i++) {
			ids[i] = Integer.parseInt(result[i][0]);
		}
		return ids;
	}
	//pass����ĳ�������ĳЩ��Ϣ
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"chejianID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "CheJian");
		return result[0];
	}
	
	//pass����ĳ�������ȫ����Ϣ
	public static String[] selectAllInfo(int ID) {
		String [] parms = {"chejianID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "CheJian");
		return result[0];
	}
	//pass ���ݲ�ƷID ��ʱ�������������,�ڲ�����
	public static int findSCPC(int cpID,String timestr){
		String cpidstr = Integer.toString(cpID);
		String []goalParms = {"shengchanpiciID"};
		String []parms = {"chanpinID","shengchanTIME"};
		String []values = {cpidstr,timestr};
		String [][] result = conn.select(goalParms, parms, values, "ShengChan");
		return Integer.parseInt(result[0][0]);
	}
	//�ҵ�ĳ��Ʒ��û��������������
	public static Integer[] findSCPCNotIn(String name){
		System.out.println("find not in");
		int cpID = ProductSystem.findProduct(name);
		String cpidstr = Integer.toString(cpID);
		String [] goalParms = {"shengchanpiciID"};
		String [] parms = {cpidstr};
		String [][] result1 = conn.selectTable(goalParms, parms, "findScpc");
		if(result1==null||result1.length==0)return null;
	
		//ArrayList <Integer> allscpc = new ArrayList<Integer>();
		Integer r [] = new Integer [result1.length];
		for(int i = 0;i<result1.length;i++) {
			//allscpc.add(Integer.parseInt(result1[i][0]));
			r[i] = Integer.parseInt(result1[i][0]);
		}
		return r;
	}
	//pass�޸�ĳ��������
	public static int changeWork(int ID,String[] setvalues,String[]power,String [] prod) {
		System.out.println("changework");
		System.out.println("zrid:"+setvalues[0]+"state : "+setvalues[1]);
		System.out.println("number of pro:"+prod.length);
		//���³��������
		String[] parms = {"chejianID"};
		String [] setparms = {"chejianzhurenID","chejianSTAGE"};
		String[] values = {Integer.toString(ID)};
		if(!conn.update(setparms, setvalues, parms, values, "CheJian"))//����ĳԭ����Ϣ
		{
			return -1;//���³����ʧ��
		}
		String [] delset = {"chejianID"};
		String [] delval = {Integer.toString(ID)};
		//ɾ������ϸ���иó���ļ�¼
 		if(!conn.delete(delset, delval, "chejianxijie")) {
 			return -2;//ɾ��ϸ��ʧ��
 		}
 		for(int i = 0;i < prod.length;i++) {
 			String [] set = {"chejianID","chanpinID","channeng"};
 			String [] val = {Integer.toString(ID),prod[i],power[i]};
 			//����µĳ���ϸ��
 			int state = conn.insert(set, val, "chejianxijie");
 			if(state < 0)
 				return -3;//�����¼�¼ʧ��
 		}
		
		//���²��ܺͲ�Ʒ
		WorkSystem.refresh();
		return 1;//���³ɹ� 
	}
	public static boolean changeWorkstate(int ID,String state) {
		String [] parms = {"chejianID"};
		String [] values = {Integer.toString(ID)};
		String [] setparms = {"chejianSTAGE"};
		String [] setvalues = {state};
		return conn.update(setparms, setvalues, parms, values, "chejian");
	}
	public static String [][] findScpcInfo(int wno,Date begin,Date end){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String beginstr = format.format(begin);
		String endstr = format.format(end);
		String wnostr = Integer.toString(wno);
		String [] parms = {wnostr,beginstr,endstr};
		String [][] result = conn.selectTable(null, parms, "findscpcinfo");
		if(result == null||result.length == 0)
			return null;
		else return result;
			
	}
//	���ĳ����������ռƻ�
//	public Map<Integer,Integer> dayPlan(int wno) {
//		return PlanSystem.dayPlan(wno);
//	}
	//pass ����ĳԭ����ĳ����ĳ�������ʣ����������0 �ĳ������κ�ʣ������
	public static Map<Integer,Integer> findCkpc(String ylname,int cjid){
		String cjidstr = Integer.toString(cjid);
		String [] parms = {ylname,cjidstr};
		String [][] result = conn.selectTable(null, parms, "findCkpc");
		if(result == null||result.length == 0) {
			return null;
		}
		else {
			Map<Integer,Integer>map = new HashMap<Integer,Integer>();
			for(int i = 0;i < result.length; i++) {
				map.put(Integer.parseInt(result[i][0]), Integer.parseInt(result[i][1]));
			}
			return map;
		}
		
	}
	
	//������������Ĳ�Ʒ���٣��������ϸ񣩣��ݲ����ԣ��߼���ԭ��һ��
		public  int destroyFromWork(int scpc) {
			Integer ygid = this.staff.getId();
			String [] goal = {"shengyuNUM"};
			String [] parm = {"shengchanpiciID"};
			String scpcstr = Integer.toString(scpc);
			String [] value = {scpcstr};
			String [][] result1 = conn.select(goal, parm, value, "ShengChan");
			int num = Integer.parseInt(result1[0][0]);
			if(num<0)num = 0;
			Date time = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			String timestr = format.format(time);
			String []parms = {"xiaohuiyuangongID","shengchanpiciID","xiaohuiTIME","xiaohuiNUM"};
			String []values = {""+ygid,Integer.toString(scpc),timestr,Integer.toString(num)};
			int state = conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
			return state;
		}
	public static String getPower(int id) {
		return capacity.get(id);
	}
	public static String getProduct(int id) {
		return product.get(id);
	}
	//��������������Ϣ
	//��������
	public static String getScxjViaPc(String scpc) {
		String [] parms = {scpc};
		String [][] result = conn.selectTable(null, parms, "getScxjViaScpc");
		if(result == null ||result.length == 0)return null;
		String re = result[0][0];
		for(int i = 1;i < result[0].length ;i++) {
			re += ","+result[0][i];
			System.out.println(re);
		}
		return re;
	}
	//ʱ��+����
	public static String [] getScxjViaTimeper(String cjid,String begin,String end) {
		String [] parms = {cjid,begin,end};
		String [][] result = conn.selectTable(null, parms, "getScxjViaTime");
		if(result == null ||result.length == 0)return null;
		String [] re = new String[result.length];
		for(int i = 0;i < result.length ;i++) {
			String per = result[i][0];
			for(int j = 1;j < result[i].length ;j++) {
				per += ","+result[i][j];
				System.out.println(per);
			}
			re[i] = per;
		}
		return re;
	}
	public static String [] getScxjViaTime(String cjid,String begin,String end) {
		if(cjid.equals("ȫ��")) {
			ArrayList<String>result = new ArrayList<String>();
			
			int [] ids =  selectAllWork();
			for(int i = 0;i < ids.length;i++) {
				String idstr = ""+ids[i];
				String [] per = getScxjViaTimeper(idstr,begin,end);
				if(per == null||per.length == 0)continue;
				for(int j = 0;j < per.length; j++) {
					result.add(per[j]);
				}
			}
			if(result.size() == 0)return null;
			String [] all = new String [result.size()];
			for(int i = 0;i<result.size();i++) {
				all[i] = result.get(i);
			}
			
			return all;
		}
		else return getScxjViaTimeper(cjid,begin,end);
	}
	//����ʼ���Ϣ
	public static String getZjInfoViapc(String zjid) {
		String [] parms = {"zhijianID"};
		String [] values = {zjid};
		String[] goalparms = {"zhijianID","shengchanpici","chejianID","zhijianTIME","zhijianrenyuanID","zhijianSTATE"};
		String [][] result = conn.select(goalparms, parms, values, "ZhiJianJiLu");
		if(result == null || result.length <= 0) return null;
		String per = result[0][0];
		for(int i = 1 ;i < result[0].length ;i++) {
			per += "," + result[0][i];
		}
		return per;
	}
	public static String[] getZjInfoViaTimecj(String cjid,String begin,String end) {
		String sql = "select zhijianID,shengchanpici,chejianID,zhijianTIME,zhijianrenyuanID,zhijianSTATE " + 
				"from ZhiJianJiLu " + 
				"where chejianID = "+cjid+" and zhijianTIME >= '"+begin+"' and zhijianTIME <= '"+end+"'";
		System.out.println(sql);
		String [][] result = conn.get(sql);
		if(result== null || result.length == 0) {
			return null;
			
		}
		String [] all = new String[result.length];
		for(int i = 0;i < result.length ;i++) {
			String per = result[i][0];
			for(int j = 1; j < result[i].length ;j++) {
				per += "," + result[i][j];
			}
			all[i] = per;
		}
		return all;
	}
	public static String[] getZjInfoViaTimery(String zjy,String begin,String end) {
		String sql = "select zhijianID,shengchanpici,chejianID,zhijianTIME,zhijianrenyuanID,zhijianSTATE " + 
				"from ZhiJianJiLu " + 
				"where zhijianrenyuanID = "+zjy+" and zhijianTIME > '"+begin+"' "
						+ "and zhijianTIME < '"+end+"'";
		String [][] result = conn.get(sql);
		if(result== null || result.length == 0) {
			return null;
			
		}
		String [] all = new String[result.length];
		for(int i = 0;i < result.length ;i++) {
			String per = result[i][0];
			for(int j = 1; j < result[i].length ;j++) {
				per += "," + result[i][j];
			}
			all[i] = per;
		}
		return all;
	}
	//���ĳ�����������Ϣ
	public static String[] getWorkInfo(String cjid) {
		String sql = "";
		if(cjid.equals("ȫ��")) {
			sql = "select chejian.chejianID,chejianzhurenID,chejianSTAGE,ChanPin.mingchen,CheJianXiJie.channeng,ChanPin.danwei " + 
					"from CheJian,CheJianXiJie,ChanPin " + 
					"where chejian.chejianID = CheJianXiJie.chejianID and CheJianXiJie.chanpinID = ChanPin.ID";
				
		}
		else {
			sql = "select chejian.chejianID,chejianzhurenID,chejianSTAGE,ChanPin.mingchen,CheJianXiJie.channeng,ChanPin.danwei " + 
					"from CheJian,CheJianXiJie,ChanPin " + 
					"where chejian.chejianID = CheJianXiJie.chejianID and CheJianXiJie.chanpinID = ChanPin.ID " + 
					"and CheJian.chejianID = " + cjid;
		}
		System.out.println(sql);
		String[][] result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		String [] allresult = new String [result.length];
		for(int i = 0 ;i < result.length ;i++) {
			String re = result[i][0];
			for(int j = 1;j < result[i].length ;j++) {
				if(j == 2) {
					String state = result[i][j].equals("1")?"ʹ����":"����";
					re += "," + state;
				}
				else{
					re += "," + result[i][j];
				}
			}
			allresult[i] = re;
		}
		
		return allresult;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		WorkSystem ws = new WorkSystem(new Staff(1,"123456"),1);
//		Map<Integer,Integer> map = WorkSystem.findCkpc("ƻ��", 1);
//		Object[]keys = map.keySet().toArray();
		int [] ws = WorkSystem.selectAllWork();
		for(int i = 0 ;i < ws.length; i++) {
			System.out.println(ws[i]);
		}
//		for(int i = 0;i < keys.length;i++) {
//			System.out.println(keys[i]+","+map.get(keys[i]));
//		}
		
		
//		int [] plan = PlanSystem.dayPlan(ws.wno);
//		for(int i = 0;i<plan.length;i++) {
//			System.out.println(plan[i]);
//		}
		//Date time = new Date();
		//ws.work(2, 10, 2,time);
		//String []set = {"�������","����״̬"};
		//String [] setvalues = {"200,200,500","2"};
		//ws.changeWork(1, set, setvalues);
		//String [] result = WorkSystem.selectInfo(1,set);
		//for(int i = 0;i<result.length;i++) {
		//	System.out.println(result[i]);
		//}
		//System.out.println(capacity.get(1));
		//System.out.println(product.get(1));
	}
}
