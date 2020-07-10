package model.bll.systems;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.CallStack;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

//import model.dal.*;
//import model.dal.Configuration;

public class PlanSystem implements SystemFather{
	public Staff staff;
	public static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	public PlanSystem() {
		
	}
	public PlanSystem(Staff staff) {
		this.staff = staff;
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	//查找当前在执行的生产计划，测试通过
	public static String[][] findProductPlan() {
		String[] goalParms = {"chanpinID","chanpinNUM","leftday"};
		String[] parms = null;
		String[][]result = conn.selectTable(goalParms, parms, "findNowPlan");
		return result;
	}
	//pass,生成workno车间的当日生产计划
	public static Map<Integer,Integer> dayPlan(int workno) {
		String[] goalParms = {"chanpinID,chanpinNUM"};
		String[] parms = {"chejianID","Ctime"};
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String timestr = format.format(date);
		String[] values = {Integer.toString(workno),timestr};
		String[][]result = conn.select(goalParms, parms, values, "CheJianRiJiHua");
		//如果已经存在改天的计划，直接返回
		if(result != null&&result.length>0) {
			//String[]re = result[0][0].split(",");
			Map<Integer,Integer> plan = new HashMap<Integer,Integer>();
			for(int i = 0;i<result.length;i++) {
				plan.put(Integer.parseInt(result[i][0]),Integer.parseInt(result[i][1]));
			}
			return plan;
		}
		
		//不存在则生成改天计划
		String [][]allplan = findProductPlan();
		int leftday = Integer.parseInt(allplan[0][2]);//计划对应的剩余天数
		//int [] id = new int[proid.length];//全部计划产品
		//int [] num = new int[pronum.length];//全部计划产品对应的num
		//int [] leftavenum = new int[pronum.length];//全部计划产品对应的剩余数量的一天平均数
		Map<Integer,Integer>idleftnum = new HashMap<Integer,Integer>();
		for(int i = 0;i<allplan.length;i++) {
			int id = Integer.parseInt(allplan[i][0]);//0代表产品编号
			int num = Integer.parseInt(allplan[i][1]);//1代表产品数量
			int leftavenum = (num - ProductSystem.Storage(id))/leftday+1;//id[i]产品总生产的数量减去当前库存
			idleftnum.put(id,leftavenum);
		}
		String castr = WorkSystem.capacity.get(workno);
		String[] castrs = castr.split(",");//该车间的产能str
		String prod = WorkSystem.product.get(workno);
		String [] prods = prod.split(",");//该车间的产品str
		//int [] plan = new int[castrs.length];//该车间当日的计划
		Map<Integer,Integer> plan = new HashMap<Integer,Integer>();
		//String plannum = "";
		ArrayList<Integer> plancp = new ArrayList<Integer>();
		ArrayList<Integer> plannum = new ArrayList<Integer>();
		//int index = 0;
		for(int i = 0;i<prods.length;i++) {
			int nowid = Integer.parseInt(prods[i]);
			if(idleftnum.containsKey(nowid)) {
				int cap = Integer.parseInt(castrs[i]);
				
				int pnum = idleftnum.get(nowid)>cap?cap:idleftnum.get(nowid);//硬座的平均数量大于产能的话按产能，否则按average
				plan.put(Integer.parseInt(prods[i]),pnum);
				
				plancp.add(Integer.parseInt(prods[i]));
				plannum.add(pnum);
				
			}
		}
		
		//sql插入该车间当日计划
		String [] para = {"chejianID","Ctime","chanpinID","chanpinNUM"};
		for(int i = 0;i<plancp.size();i++) {
			String [] value = {Integer.toString(workno),timestr,plancp.toString(),plannum.toString()};
			conn.insert(para, value, "CheJianRiJiHua");
		}
		
		return plan;
	}

	//生产计划
	public static String[][] getWorkPlan(Date tihuoTime1, Date tihuoTime2) {
		//预期销售计划
		//成品库存情况
		//车间生产力
		//订单
		
		//需要生产的产品数量
		String[][] dingDan = SoldSystem.getFutureChanpinNum();
		if(dingDan == null) {
			return null;
		}
//		int[] shengChanLi = new int[dingDan.length];
//		for(int i = 0; i < dingDan.length; i++) {
//			shengChanLi[i] = WorkSystem.chanNeng(Integer.parseInt(dingDan[i][0]));
//		}
		String[][] yuJi = SoldSystem.readNowPlan();
	
		Map<Integer,Integer> plan = new HashMap<Integer,Integer>();
		if(dingDan != null)
		for(int i = 0; i < dingDan.length; i++) {
			//Integer tmp = plan.get(Integer.parseInt(dingDan[i][0]));
			plan.put(Integer.parseInt(dingDan[i][0]),  Integer.parseInt(dingDan[i][1])); 
		}
		
		if(yuJi != null)
		for(int i = 0; i < yuJi.length; i++) {
			if(!plan.containsKey(Integer.parseInt(yuJi[i][2]))) {
				plan.put(Integer.parseInt(yuJi[i][2]), Integer.parseInt((yuJi[i][1])));
				continue;
			}
			Integer tmp = plan.get(Integer.parseInt(yuJi[i][2]));
			plan.replace(Integer.parseInt(yuJi[i][2]),  Math.max(Integer.parseInt((yuJi[i][1])),tmp)); 
		}
		
		int cnt = 0;
		for(Integer key:plan.keySet()) {
			Integer tmp = plan.get(key);
			int kuCun = ProductSystem.Storage(tmp);
			if(tmp - kuCun <= 0) {
				cnt++;
				plan.replace(key, tmp - kuCun);
			}
			else
				plan.replace(key, tmp - kuCun);
		}
		
		String[][] ans = new String[plan.size()-cnt][3];
		int i = 0;
		for(Integer key:plan.keySet()) {
			if(plan.get(key) == null || plan.get(key) <= 0)
				continue;
			ans[i][0] = key.toString();
			ans[i][1] = conn.get("select mingchen from ChanPin where ID = "+key.toString())[0][0];
			ans[i][2] = plan.get(key).toString();
			i++;
		}
		
		return ans;
	}
	//取当前时间和已有计划结束时间的最大值
	public static String getStartTime() throws ParseException {
		String sql = "select max(endTIME) from ShengChanJiHua";
		String date = conn.get(sql)[0][0];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = df.parse(date);
		
		if(d1.getTime() > (new Date()).getTime()) {
			return date;
		}
		return df.format(new Date());
	}
	public static String savePlan(String[] chanpinID, String[] chanpinNUM,
			Date date, Date date2, String[] mingchens) {
		if(chanpinID != null) {
			if(mingchens == null) {
				return "产品数据有误！";
			}
			for(int i = 0; i < chanpinID.length; i++) {
				String sql = "select mingchen from ChanPin where ID = "+chanpinID[i];
				String[][]tmp = conn.get(sql);
				if(tmp == null || !tmp[0][0].equals(mingchens[i])) {
					//System.out.println(tmp[0][0]+" "+mingchens[i]);
					return "第"+(chanpinID.length-i)+"行产品数据有误！";
				}
			}
		}
		else if(mingchens != null && mingchens.length != 0) {
			return "产品数据有误！";
		}
		
		String[] parms = {"startTIME", "endTIME"};
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String[] values = {df.format(date), df.format(date2)};
		conn.insert(parms, values, "ShengChanJiHua");
		String sql = "select jihuaID from ShengChanJiHua where startTime = '"+df.format(date)+"' "
				+ "and endTIME = '"+df.format(date2)+"'";
		String id = conn.get(sql)[0][0];
		
		String[] parms2 = {"jihuaID","chanpinID", "chanpinNUM"};
		String[] values2 = new String[3];
		for(int i = 0; i < chanpinID.length; i++) {
			values2[0] = id;
			values2[1] = chanpinID[i];
			values2[2] = chanpinNUM[i];
			conn.insert(parms2, values2, "ShengChanJiHuaXiJie");
		}
		
		return "ok";
	}
	public static String[][] selectAllJiHua() {
		String sql = "select * from ShengChanJiHua";
		return conn.get(sql);
	}
	public static String[][] getWorkPlanWithId(String id) {
		System.out.println(id);
		String sql = "select chanpinID,mingchen,sum(chanpinNUM) from ShengChanJiHuaXiJie, ChanPin"
				+ " where ChanPin.ID=chanpinID and jihuaID ="+id+" "
						+ " group by chanpinID,mingchen";
		return conn.get(sql);
	}
	
	public static String[][] selectWorkPlan() {
		String sql = "select * from ShengChanJiHua";
		return conn.get(sql);
	}
	public static String[][] ShengChanJinDuGetXiJie(String id) {
		String sql = "select chanpinID, chanpinNUM from ShengChanJiHuaXiJie where jihuaID = "+id;
		String[][] res = conn.get(sql);
//		for(int i = 0; i  < res.length; i++)
//			System.out.println(res[i][0] + " " + res[i][1]);
//		System.out.println(sql);
		sql = "select chanpinID, shengchanNUM from ShengChan, ShengChanJiHua"
				+ " where shengchanTIME >= startTIME and shengchanTIME <= endTIME"
				+ " and jihuaID = "+id;
		String[][] res2 = conn.get(sql);
//		for(int i = 0; i  < res2.length; i++)
//			System.out.println(res2[i][0] + " " + res2[i][1]);
//		System.out.println(sql);
		
		Map<String, Integer> map1 = getMap(res);
		Map<String, Integer> map2 = getMap(res2);
		Map<String, String> map3 = new HashMap<String,String>();
		
//		for(String key : map1.keySet())
//			System.out.println(map1.get(key));
//		for(String key : map2.keySet())
//			System.out.println("2 "+map2.get(key));
		if(res != null)
			for(int i = 0; i < res.length; i++) map3.put(res[i][0], res[i][1]);
		if(res2 != null)
			for(int i = 0; i < res2.length; i++) map3.put(res2[i][0], res2[i][1]);
		
		Set<String> st = new TreeSet<>();

		for(String key : map1.keySet()) st.add(key);
		for(String key : map2.keySet()) st.add(key);
		
		String[][] ans = new String[st.size()][4];
		
		int i = 0;
		for(String key : st) {
//			System.out.println("st "+key);
			ans[i][0] = key;
			ans[i][1] = map3.get(key);
			if(map1.containsKey(key))
				ans[i][2] = map1.get(key).toString();
			else
				ans[i][2] = "0";
			if(map2.containsKey(key))
				ans[i][3] = map2.get(key).toString();
			else
				ans[i][3] = "0";
			i++;
		}
		
//		for(int i1 = 0; i1 < ans.length; i1++)
//			System.out.println(ans[i1][0] + " " + ans[i1][1]);
		System.out.print("ss");
		CallStack.printCallStatck();
		return ans;
	}
	
	private static Map<String, Integer> getMap(String[][] ret) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(ret != null)
			for(int i = 0; i < ret.length; i++) {
				if(map.containsKey(ret[i][0])) {
					Integer tmp = map.get(ret[i][0]);
					map.put(ret[i][0], tmp + Integer.parseInt(ret[i][1])  );
				}
				else {
					map.put(ret[i][0], Integer.parseInt(ret[i][1]));
				}
			}
		return map;
	}
}
