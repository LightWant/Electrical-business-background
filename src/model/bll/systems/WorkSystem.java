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
	//private int wno;//车间编号
	static {
		conn = new Connsql(Configuration.user,Configuration.password);
		product = new HashMap<Integer,String>();
		capacity = new HashMap<Integer,String>();
		String [] goal = {"chejianID"};
		String [][] cj = conn.select(goal,null,null,"CheJian");//所有的车间ID
		if(cj!=null&&cj.length!=0) {
			//如果有车间
			for(int i = 0 ; i < cj.length; i++) {
				//对每个车间计算产能和产品
				String cjid = cj[i][0];
				String[] goalParms = {"chanpinID","channeng"};
				String[] parms = {"chejianID"};
				String[] values = {cjid};
				String[][]cjstr = conn.select(goalParms, parms, values, "CheJianXiJie");
				if(cjstr == null || cjstr.length == 0) {
					//如果该车间不生产产品，跳过
					continue;
				}
				String cp=cjstr[0][0] ,cn = cjstr[0][1];//该车间的产品string、产能string
				
				for(int j = 1; j < cjstr.length; j++) {
					cp += ","+cjstr[j][0];
					cn += ","+cjstr[j][1];
				}
				product.put(Integer.parseInt(cjid), cp);// 产品map中添加该车间的产品
				capacity.put(Integer.parseInt(cjid), cn); // 产品map中添加该车间的产能
			}
		}
		property.put("车间编号","chejianID");
		property.put("车间主任编号","chejianzhurenID");
		property.put("车间产品编号","chejianchanpinID");
		property.put("车间产能","chejianchanneng");
		property.put("车间状态","chejianSTAGE");
		
	}
	public WorkSystem() {
		
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	//获得某个产品的产能
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
	//转换成数据库属性名
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
	//pass车间属性改变时更新产品和产能属性
	public static void refresh() {
		String [] goal = {"chejianID"};
		String [][] cj = conn.select(goal,null,null,"CheJian");//所有的车间ID
		if(cj!=null&&cj.length!=0) {
			//如果有车间
			for(int i = 0 ; i < cj.length; i++) {
				//对每个车间计算产能和产品
				String cjid = cj[i][0];
				String[] goalParms = {"chanpinID","channeng"};
				String[] parms = {"chejianID"};
				String[] values = {cjid};
				String[][]cjstr = conn.select(goalParms, parms, values, "CheJianXiJie");
				if(cjstr == null || cjstr.length == 0) {
					//如果该车间不生产产品，跳过
					continue;
				}
				String cp=cjstr[0][0] ,cn = cjstr[0][1];//该车间的产品string、产能string
				
				for(int j = 1; j < cjstr.length; j++) {
					cp += cjstr[j][0];
					cn += cjstr[j][1];
				}
				product.put(Integer.parseInt(cjid), cp);// 产品map中添加该车间的产品
				capacity.put(Integer.parseInt(cjid), cn); // 产品map中添加该车间的产能
			}
		}
	}
	//通过测试，生产过程
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
		if(state<0)return null;//-1 重复项，-2插入失败
		int scpc = findSCPC(chanpinID,timestr);
		//System.out.println("get 生产批次为:"+scpc);
		
		//添加生产细节表
		workUseMaterial(scpc,chanpinID,num,wno);
		String [] xjparms = {"shengchanpiciID"};
		String [] xjvalue = {""+scpc};
		String [][] xjresult = conn.select(null, xjparms, xjvalue, "ShengChanXiJie");
		if(xjresult == null||xjresult.length == 0) return null;//-3查找细节失败
		//添加质检记录
		String [] pa = {"shengchanpici","chejianID","zhijianTIME","zhijianrenyuanID","zhijianSTATE"};
		String [] val = {""+scpc,cjid,timestr,zjyidstr,zhijian};
		if(conn.insert(pa, val, "ZhiJianJiLu")<0) {
			System.out.println("插入质检记录失败");
			return null;
		}
		return xjresult;//返回生产细节，批次、使用的出库批次、数量
		//return scpc;//返回生产批次
	}
	//通过测试，生产批次细节添加
	public static void workUseMaterial(int scpc,int cpID,int num,int wno) {
		//System.out.println("WorkUseMaterial");
		String [][]peifang = ProductSystem.findPeiFang(cpID);
		String [] ylstr = new String [peifang.length];
		//= peifang[0].split(",");
		String [] ylnumstr = new String [peifang.length];
		//peifang[1].split(",");
		System.out.println("产品iD为"+cpID+",配方如下：\n");
		for(int i = 0 ;i < peifang.length; i++) {
			ylstr[i] = peifang[i][0];
			System.out.print("原料ID："+ylstr[i]);
			ylnumstr[i] = peifang[i][1];
			System.out.println(",数量："+ylnumstr[i]);
		}
		int cjID = wno;
		//int [] yl = new int[ylstr.length];
		//int [] ylnum = new int [ylnumstr.length];
		System.out.println("生产需要的数量：");
		for(int i = 0;i<ylstr.length;i++) {
			int ylID = Integer.parseInt(ylstr[i]);
			int ylnum = Integer.parseInt(ylnumstr[i])*num;
			System.out.println("ylid:"+ylID);
			System.out.println("ylnum:"+ylnum);
			//System.out.println(""+ylID+""+ylnum);
			conn.executeUYL(scpc,ylID,ylnum,cjID);//对每一个配方中的原料都执行useMaterial存储过程
		}
		
	}
	//pass查找所有车间id 
	public static int[] selectAllWork() {
		String []goal = {"chejianID"};
		String [][]result = conn.select(goal, null, null, "CheJian");
		int [] ids = new int[result.length];
		for(int i = 0;i<result.length;i++) {
			ids[i] = Integer.parseInt(result[i][0]);
		}
		return ids;
	}
	//pass查找某个车间的某些信息
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"chejianID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "CheJian");
		return result[0];
	}
	
	//pass查找某个车间的全部信息
	public static String[] selectAllInfo(int ID) {
		String [] parms = {"chejianID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "CheJian");
		return result[0];
	}
	//pass 根据产品ID 和时间查找生产批次,内部调用
	public static int findSCPC(int cpID,String timestr){
		String cpidstr = Integer.toString(cpID);
		String []goalParms = {"shengchanpiciID"};
		String []parms = {"chanpinID","shengchanTIME"};
		String []values = {cpidstr,timestr};
		String [][] result = conn.select(goalParms, parms, values, "ShengChan");
		return Integer.parseInt(result[0][0]);
	}
	//找到某产品的没有入库的生产批次
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
	//pass修改某车间属性
	public static int changeWork(int ID,String[] setvalues,String[]power,String [] prod) {
		System.out.println("changework");
		System.out.println("zrid:"+setvalues[0]+"state : "+setvalues[1]);
		System.out.println("number of pro:"+prod.length);
		//更新车间表内容
		String[] parms = {"chejianID"};
		String [] setparms = {"chejianzhurenID","chejianSTAGE"};
		String[] values = {Integer.toString(ID)};
		if(!conn.update(setparms, setvalues, parms, values, "CheJian"))//更新某原料信息
		{
			return -1;//更新车间表失败
		}
		String [] delset = {"chejianID"};
		String [] delval = {Integer.toString(ID)};
		//删除车间细节中该车间的记录
 		if(!conn.delete(delset, delval, "chejianxijie")) {
 			return -2;//删除细节失败
 		}
 		for(int i = 0;i < prod.length;i++) {
 			String [] set = {"chejianID","chanpinID","channeng"};
 			String [] val = {Integer.toString(ID),prod[i],power[i]};
 			//添加新的车间细节
 			int state = conn.insert(set, val, "chejianxijie");
 			if(state < 0)
 				return -3;//插入新记录失败
 		}
		
		//更新产能和产品
		WorkSystem.refresh();
		return 1;//更新成功 
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
//	获得某车间的生产日计划
//	public Map<Integer,Integer> dayPlan(int wno) {
//		return PlanSystem.dayPlan(wno);
//	}
	//pass 查找某原料在某车间的出库批次剩余数量大于0 的出库批次和剩余数量
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
	
	//来自生产车间的产品销毁（生产不合格），暂不测试，逻辑和原料一样
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
	//查找生产批次信息
	//根据批次
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
	//时间+车间
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
		if(cjid.equals("全部")) {
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
	//获得质检信息
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
	//获得某车间的所有信息
	public static String[] getWorkInfo(String cjid) {
		String sql = "";
		if(cjid.equals("全部")) {
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
					String state = result[i][j].equals("1")?"使用中":"空闲";
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
//		Map<Integer,Integer> map = WorkSystem.findCkpc("苹果", 1);
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
		//String []set = {"车间产能","车间状态"};
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
