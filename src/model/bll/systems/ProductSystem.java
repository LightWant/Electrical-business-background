package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

//import model.dal.*;
public class ProductSystem implements SystemFather {
	Staff staff;
	static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>(); 
	static String [] danwei = {"斤","公斤","克","包"};
	static {
		property.put("产品名称", "mingchen");
		property.put("产品编号", "ID");
		property.put("产品保质期", "baozhiqi");
		property.put("产品打包规格", "dabaoguige");
		property.put("产品单价", "jiage");
		property.put("产品单位", "danwei");
		property.put("仓库类别", "cangkuleibie");
		property.put("仓库主任ID", "cangkuzurenID");
		property.put("仓库状态", "cangkuzhuangtai");
		property.put("仓库总容量", "cangkurongliang");
		property.put("仓库剩余容量", "shengyurongliang");
	}
	public ProductSystem(Staff staff) {
		this.staff = staff;
		
	}
	
	public ProductSystem() {
		
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	static String[] toSqlProperty(String[]parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	//通过测试 添加新产品
	public static int addProduct(String name,String [] yuanliao,String [] ynum,int guige,float price,int baozhiqi,String dw) {
		Map<Integer,String> result = selectAllProduct();
		Object[] keys = result.keySet().toArray();
		for(int i = 0;i<keys.length;i++) {
			if(name.trim().equals(result.get(keys[i]))) return -1;//表示重名
		}
		boolean find = false;
		for(int i =0;i<danwei.length;i++) {
			if(dw.trim().equals(danwei[i])) {
				find = true;
				break;
			}
		}
		if(!find) {
			return -2;//表示单位不合规
		}
		String gg = Integer.toString(guige);
		String money = Float.toString(price);
		String bzq = Integer.toString(baozhiqi);
		//插入产品表
		String []parms = {"mingchen","dabaoguige","jiage","baozhiqi","danwei"};
		String []values = {name,gg,money,bzq,dw};
		conn.insert(parms, values, "ChanPin");
		String [] goalParms = {"ID"};
		//String [][]cpid = conn.select(goalParms, parms, values, "ChanPin");
		Integer id = findProduct(name);
		String  idstr = Integer.toString(id);
		//插入配方表
		
		String []parm = {"chanpinID","yuanliaoID","yuanliaoNUM"};
		for(int i = 0;i<yuanliao.length;i++) {
			String ylid = Integer.toString(MaterialSystem.findMaterialID(yuanliao[i]));
			String[]value = {idstr,ylid,ynum[i]};
			if(conn.insert(parm, value,"PeiFang")<0)
				return -3;
		}
		return id;
	}
	//通过测试，产品入库
	public String inStorage(int piciID,int cangkuID,int num) {
		String sql = "select shengyuNUM " + 
				"from ShengChan " + 
				"where ShengChan.shengchanpiciID = "+piciID;
		String [][] result = conn.get(sql);
		Integer left = Integer.parseInt(result[0][0]);
		System.out.println("scpc:"+piciID+",left:"+left);
		System.out.println("入库："+num);
		if(left<num) return "left"+left;//说明剩余的比要入库的数量少
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"cangkuID","shengchanpiciID","chanpinNUM","rukufuzerenID","rukuTIME","shengyuNUM"};
		String ckID = Integer.toString(cangkuID);
		String pcID = Integer.toString(piciID);
		String fzrID = Integer.toString(this.staff.getId());
		String rknum = Integer.toString(num);
		String synum = rknum;
		String []values = {ckID,pcID,rknum,fzrID,timestr,synum};
		
		int state = conn.insert(parms, values, "ChanPinRuKu");
		if(state<0)return "insert";
		String []goalParms = {"rukupiciID"};
		String [][]rkpc = conn.select(goalParms, parms, values, "ChanPinRuKu");
		if(rkpc==null||rkpc.length == 0)return "find";//插入失败
		return rkpc[0][0];
	}
	//通过测试，产品出库
	public int outStorage(int rkpcID,int ddID,int num) {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"rukupiciID","chukuTIME","chukufuzerenID","dingdanID","chanpinNUM"};
		String []values = {Integer.toString(rkpcID),timestr,Integer.toString(this.staff.getId()),Integer.toString(ddID),Integer.toString(num)};
		return conn.insert(parms, values, "ChanPinChuKu");
		
	}
	//来自仓库的产品销毁，暂不测试，逻辑和原料一样
	public  int destroyFromStorage(int rukupiciID) {
		Integer ygid = this.staff.getId();
		String [] goal = {"shengyuNUM"};
		String [] parm = {"rukupiciID"};
		String scpcstr = Integer.toString(rukupiciID);
		String [] value = {scpcstr};
		String [][] result1 = conn.select(goal, parm, value, "ChanPinRuKu");
		int num = Integer.parseInt(result1[0][0]);
		if(num<0)num = 0;
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"xiaohuiyuangongID","rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		String []values = {""+ygid,Integer.toString(rukupiciID),timestr,Integer.toString(num)};
		int state = conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
		return state;
	}
	
	
	//过期产品显示，暂不测试，逻辑和原料一样
	public static String[][] expire() {
		//暂时只显示过期的，濒临过期的还未确定时间
		//返回入库批次ID，原料ID,原料名称
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "expireProduct");
		
	}
	//即将过期产品显示，暂不测试，逻辑和原料一样
	public String[][] comingExpire(){
		//返回入库批次ID，产品ID,产品名称
		String[] goal = {"rukupiciID","shengyuNUM"};
		
		String[] values = null;
		return conn.selectTable(goal, values, "ComingExProduct");
	}
	//通过测试，查询产品库存
	public static int Storage(int id) {
		String[]parms = {Integer.toString(id)};
		int result = Integer.parseInt(conn.selectScale(parms, "ProductStorage"));
		System.out.println("id："+id+",库存:"+result);
		return result;
	}
	//产品销毁
	public boolean destroy(Integer[] rkpc) {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String yuangongID = Integer.toString(this.staff.getId());
		String []parms = {"xiaohuiyuangongID","rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		for(int i = 0;i<rkpc.length;i++) {
			String []values = new String[4];
			values[0] = yuangongID;
			values[1] = Integer.toString(rkpc[i]);
			values[2] = timestr;
			String[]goal = {"shengyuNUM"};
			String[]par = {"rukupiciID"};
			String[]val = {Integer.toString(rkpc[i])};
			String [][] get = conn.select(goal, par, val, "ChanPinRuKu");
			values[3] = get[0][0];
			int state = conn.insert(parms, values, "ChanPinXiaoHuiJiLu");
			if(state <  0)return false;
		}
		return true;
	}
	//5）仓库状态管理：仓库使用情况、手动于系统中更改仓库状态、输入仓库信息查看其状态、查询某种状态分别有哪些仓库。
	public void changeStorageState(String state) {
		String[] setparms = {"cangkuzhuangtai"};
		String[] values = {state};
		conn.update(setparms,values,null,null,"ChanPinCangKu"); 
	}
	//6）pass 产品信息修改：输入信息，修改
	public static boolean changeProduct(int ID,String[] set,String[] setvalues) {
		String[] parms = {"ID"};
		String [] setparms = toSqlProperty(set);
		String[] values = {Integer.toString(ID)};
		return conn.update(setparms, setvalues, parms, values, "ChanPin");//更新某原料信息
	}
	//pass查找某个产品的信息某些
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "ChanPin");
		return result[0];
	}
	
	//pass查找某个产品的全部信息
	public static String selectAllInfo(int ID) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "ChanPin");
		if(result == null||result.length == 0) return null;
		int kucun = ProductSystem.Storage(ID);
		String peresult = result[0][0];
		for(int i = 1;i < result[0].length-1;i++) {
			peresult += "," + result[0][i];
		}
		peresult+=","+kucun;
		return peresult;
	}
	public static String [] selectAllProInfo(){
		Map<Integer,String>map = ProductSystem.selectAllProduct();
		if(map == null||map.size()==0)return null;
		Object[] ids = ProductSystem.selectAllProduct().keySet().toArray();
		String result[] = new String[ids.length];
 		for(int i = 0 ;i < ids.length ;i++) {
			int id = (Integer) ids[i];
			result[i] = ProductSystem.selectAllInfo(id);
		}
 		return result;
	}
	//pass查找某仓库某些信息
	public static String[] selectStorageInfo(int ID,String[]goal) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "ChanPinCangKu");
		return result[0];
	}
	
	//pass查找某仓库所有信息
	public static String[] selectAllStorageInfo(int ID) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "ChanPinCangKu");
		return result[0];
	}
	//7)通过测试，产品配方修改，
	public static  boolean changePeiFang(int id,Integer[] ylid,Integer[] ylnum) {
		//String ylstr = Integer.toString(ylid[0]);
		//String ylnumstr = Integer.toString(ylnum[0]);
//		for(int i = 1;i<ylid.length;i++) {
//			ylstr += ","+Integer.toString(ylid[i]);
//			ylnumstr += ","+Integer.toString(ylnum[i]);
//		}
		String [] parms = {"chanpinID"};
		String [] values = {""+id};
		//String sql = "delete from PeiFang where chanpinID = "+id;
		if(!conn.delete(parms, values, "PeiFang"))return false;
		System.out.println("删除配方成功");
		String []setparms = {"chanpinID","yuanliaoID","yuanliaoNUM"};
		for(int i = 0;i<ylid.length;i++) {
			String [] value = {""+id,""+ylid[i],""+ylnum[i]};
			if(conn.insert(setparms, value, "PeiFang")<0) return false;
			System.out.println("插入"+(i+1)+"条配方");
		}
		System.out.println("插入配方成功");
		return true;
	}
	//pass 根据名称找id
	public static int findProduct(String name) {
		System.out.println("find cpname:" + name);
		String[]parms = {"mingchen"};
		String[]values = {name};
		String[]goal = {"ID"};
		String [][]result = conn.select(goal,parms, values,"ChanPin");
		return Integer.parseInt(result[0][0]);
	}
	// 根据id找名称
	public static String findProductName(int id) {
		String[]parms = {"ID"};
		String idstr = Integer.toString(id);
		String[]values = {idstr};
		String[]goal = {"mingchen"};
		String [][]result = conn.select(goal,parms, values,"ChanPin");
		if(result == null||result.length==0)return null;
		return result[0][0];
	}
	//通过测试，配方查找
	public static String[][] findPeiFang(int cpID) {
		String[]goalParms = {"yuanliaoID","yuanliaoNUM"};
		String[]parms = {"chanpinID"};
		String[]values = {Integer.toString(cpID)};
		String [][]result = conn.select(goalParms, parms, values, "PeiFang");
		return result;
	}
	public static String [] getPeiFang(String cpid) {
		String sql = "select ChanPin.mingchen,ChanPin.danwei,yuanliaoname,Peifang.yuanliaoNUM, yuanliao.danwei " + 
				"from PeiFang,YuanLiao,ChanPin " + 
				"where PeiFang.yuanliaoID = yuanliao.yuanliaoID and ChanPin.ID = PeiFang.chanpinID " + 
				"and PeiFang.chanpinID = "+cpid;
		String [][]result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		String all[] = new String[result.length];
		for(int i = 0;i < result.length ;i++) {
			String per = result[i][0];
			for(int j = 1;j < result[i].length ;j++) {
				per += "," + result[i][j];
			}
			all[i] = per;
		}
		return all;
	}
	//pass返回所有产品名称、id
	public static Map<Integer,String> selectAllProduct(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"ID","mingchen"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "ChanPin");
		if(result == null||result.length == 0)return null;
		for(int i = 0;i<result.length;i++) {
			map.put(Integer.parseInt(result[i][0]), result[i][1]);
		}
		return map;
	}
	//pass返回所有产品仓库的id 类别
	public static Map<Integer,String>selectAllStorage(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"ID","cangkuleibie"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "ChanPinCangKu");
		if(result==null||result.length == 0)return null;
		for(int i = 0;i<result.length;i++) {
			map.put(Integer.parseInt(result[i][0]), result[i][1]);
		}
		return map;
	}
	//返回所有仓库ID
	public static Integer [] selectAllStorageID(){
		//Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"ID"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "ChanPinCangKu");
		if(result==null||result.length == 0)return null;
		Integer[] ids = new Integer[result.length];
		for(int i = 0;i<result.length;i++) {
			ids[i] = Integer.parseInt(result[i][0]);
		}
		return ids;
	}
	//删除仓库
	public void deleteStorage(int ID) {
		String []parms = {"ID"};
		String []values = {Integer.toString(ID)};
		conn.delete(parms, values, "ChanPinCangKu");
	}
	public void deleteStorages(int [] ID) {
		String []parms = {"ID"};
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			conn.delete(parms, values, "ChanPinCangKu");
		}
		
	}
	
	
	//获得某产品的剩余数量》0的入库批次
	public static Integer[] getRkpc(int id) {
		String sql = "select rukupiciID " + 
				"from ChanPinRuKu,ChanPin,ShengChan " + 
				"where ChanPinRuKu.shengchanpiciID = ShengChan.shengchanpiciID and ShengChan.chanpinID = ChanPin.ID " + 
				"and id = " +id+" and ChanPinRuKu.shengyuNUM>0";
		String [][]result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		Integer rkpc[] = new Integer[result.length];
		for(int i = 0;i<result.length;i++) {
			rkpc[i] = Integer.parseInt(result[i][0]);
		}
		return rkpc;
	}
	//获得某产品的剩余数量》0的生产批次
	public static Integer[] getScpc(int id) {
		String sql = "select ShengChan.shengchanpiciID " + 
				"from ShengChan,ChanPin " + 
				"where ShengChan.chanpinID = ChanPin.ID and shengyuNUM>0 and chanpin.ID = "+id;
		String [][]result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		Integer scpc[] = new Integer[result.length];
		for(int i = 0;i<result.length;i++) {
			scpc[i] = Integer.parseInt(result[i][0]);
		}
		return scpc;
	}
	//获得某产品的剩余数量》0的入库批次对应的剩余数量
	public static Integer[] getScpcLeft(int id) {
		String sql = "select ShengChan.shengyuNUM " + 
				"from ShengChan,ChanPin " + 
				"where ShengChan.chanpinID = ChanPin.ID and shengyuNUM>0 and chanpin.ID = "+id;
		String [][]result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		Integer left[] = new Integer[result.length];
		for(int i = 0;i<result.length;i++) {
			left[i] = Integer.parseInt(result[i][0]);
		}
		return left;
	}
	public static Integer [] getDingdanId() {
		String sql = "select dingdanID from DingDan where dingdanSTATE = '待提货'";
		
		String [][] result = conn.get(sql);
		if(result == null||result.length == 0)return null;
		Integer [] id = new Integer[result.length];
		for(int i = 0;i < id.length; i++) {
			id[i] = Integer.parseInt(result[i][0]);
		}
		return id;
	}
	//获取订单细节
	public static Map<String,Integer>getDdxj(int id){
		String sql = "select chanpinID,chanpinNUM from DingDanXiJie where dingdanID = "+id;
		String [][] cp = conn.get(sql);
		System.out.println("找订单"+id+"的订货细节");
		
		if(cp==null||cp.length == 0)return null;
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(int i = 0; i < cp.length ;i++) {
			String cpname = ProductSystem.findProductName(Integer.parseInt(cp[i][0]));
			System.out.println("产品ID："+cp[i][0]+"产品name:"+cpname+",订货数量:"+Integer.parseInt(cp[i][1]));
			map.put(cpname, Integer.parseInt(cp[i][1]));
		}
		return map;
	}
	//判断某订单是否能交付
	public static boolean IsFinDingdan(int id) {
		System.out.println("判断是否能完成"+id+"订单");
		String sql = "select chanpinID,chanpinNUM from DingDanXiJie where dingdanID = "+id;
		
		String [][] cp = conn.get(sql);
		if(cp==null||cp.length == 0) {
			System.out.println("judge find len = 0,没有订单细节");
			return false;
		}
		//Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for(int i = 0; i < cp.length ;i++) {
			Integer cpid = Integer.parseInt(cp[i][0]);
			Integer need = Integer.parseInt(cp[i][1]);
			Integer left = ProductSystem.Storage(cpid);
			System.out.println("need "+cpid+"产品 "+need+"个");
			System.out.println("have "+cpid+"产品 "+left+"个");
			if(left<need)return false;
		}
		return true;
		
	}
	//获得某订单的支付细节
	public static String [] jiaofuProduct(int ddid) {
		Map<String,Integer> ddxj = ProductSystem.getDdxj(ddid);
		if(ddxj==null)return null;
		ArrayList<String> rresult = new ArrayList<String>();
		for(String cpname:ddxj.keySet()) {
			String cpidstr = Integer.toString(ProductSystem.findProduct(cpname));
			String  numstr = ""+ddxj.get(cpname);
			String [] parms = {cpidstr,numstr};
			String [][] peresult = conn.selectTable(null, parms, "outProduct");
			
			for(int i = 0 ;i<peresult.length;i++) { 
				String perstr = cpname + ","+peresult[i][0] + "," + peresult[i][1];
				System.out.println("per 交付信息："+perstr);
				rresult.add(perstr);
			}
		}
		String [] result = new String[rresult.size()];
		for(int i = 0;i<rresult.size();i++) {
			result[i] = rresult.get(i);
		}
		return result;
	}
	//完成订单的修改状态
	public static boolean overDD(int ddid) {
		//String sql = "update  DingDan set dingdanSTATE = \'完成\'  where dingdanID = "+ddid;
		String [] setvalues = {"完成"};
		String [] setparms = {"dingdanSTATE"};
		String [] parms = {"dingdanID"};
		String [] values = {""+ddid};
		return conn.update(setparms, setvalues, parms, values, "DingDan");
	
	}
	//获取产品入库批次信息
	public static String getRkpcInfo (int rkpc) {
		String values [] = {""+rkpc};
		System.out.println("根据ID入库批次");

		String [][] result = conn.selectTable(null,  values, "getRkpcInfo");
		if(result == null || result.length == 0 )return null;
		String rstr = result[0][0];
		for(int i = 1;i<result[0].length;i++)
			rstr += "," + result[0][i];
		System.out.println("结果："+rstr);
		return rstr;
		
		//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
	}
	//根据负责人查入库批次细节
	public static String[] getRkpcInfoViaFzr (int fzr) {
		String values [] = {""+fzr};
		String [][] result = conn.selectTable(null,  values, "getRkpcInfoViaFzr");
		System.out.println("根据负责人差入库批次");

		if(result == null || result.length == 0 )return null;
		
		String [] all = new String[result.length];
		for(int i = 0;i < result.length;i++) {
			String rstr = result[i][0];
			for(int j = 1;j<result[i].length;j++)
				rstr += "," + result[i][j];
			System.out.println(i+"结果:"+rstr);
			all[i] = rstr;
		}
		return all;
		
		//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
	}
	//根据起始时间差入库批次细节
	public static String[] getRkpcInfoViaTime(String begin,String end) {
		String values [] = {begin,end};
		String [][] result = conn.selectTable(null,  values, "getRkpcInfoViaTime");
		System.out.println("根据时间差入库批次");
		
		if(result == null || result.length == 0 )return null;
	
		String [] all = new String[result.length];
		for(int i = 0;i < result.length;i++) {
			String rstr = result[i][0];
			for(int j = 1;j<result[i].length;j++)
				rstr += "," + result[i][j];
			System.out.println(i+"结果:"+rstr);
			all[i] = rstr;
		}
		return all;
		
	}
	
	//获取产品出库批次信息，根据订单
		public static String[] getCkpcInfoViaDD (int dd) {
			String values [] = {""+dd};
			System.out.println("根据ID入库批次");

			String [][] result = conn.selectTable(null,  values, "getCkpcInfoViaDD");
			if(result == null || result.length == 0 )return null;
			String [] all = new String[result.length];
			for(int i = 0;i < result.length;i++) {
				String rstr = result[i][0];
				for(int j = 1;j<result[i].length;j++)
					rstr += "," + result[i][j];
				System.out.println(i+"结果:"+rstr);
				all[i] = rstr;
			}
			return all;
			
			//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
		}
		//根据负责人查出库批次细节，根据负责人
		public static String[] getCkpcInfoViaFzr (int fzr) {
			String values [] = {""+fzr};
			String [][] result = conn.selectTable(null,  values, "getCkpcInfoViaFzr");
			System.out.println("根据负责人差入库批次");

			if(result == null || result.length == 0 )return null;
			
			String [] all = new String[result.length];
			for(int i = 0;i < result.length;i++) {
				String rstr = result[i][0];
				for(int j = 1;j<result[i].length;j++)
					rstr += "," + result[i][j];
				System.out.println(i+"结果:"+rstr);
				all[i] = rstr;
			}
			return all;
			
			//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
		}
		//根据起始时间差出库批次细节
		public static String[] getCkpcInfoViaTime(String begin,String end) {
			String values [] = {begin,end};
			String [][] result = conn.selectTable(null,  values, "getCkpcInfoViaTime");
			System.out.println("根据时间差入库批次");
			
			if(result == null || result.length == 0 )return null;
		
			String [] all = new String[result.length];
			for(int i = 0;i < result.length;i++) {
				String rstr = result[i][0];
				for(int j = 1;j<result[i].length;j++)
					rstr += "," + result[i][j];
				System.out.println(i+"结果:"+rstr);
				all[i] = rstr;
			}
			return all;
			
		}
		//查询销毁记录
		//根据负责人查销毁批次细节，根据负责人
		public static String[] getXhInfoViaFzr (int fzr) {
			String values [] = {""+fzr};
			String [][] result = conn.selectTable(null,  values, "getXhjlViaFzr");
			System.out.println("根据负责人差入库批次");

			if(result == null || result.length == 0 )return null;
			
			String [] all = new String[result.length];
			for(int i = 0;i < result.length;i++) {
				String rstr = result[i][0];
				for(int j = 1;j<result[i].length;j++)
					rstr += "," + result[i][j];
				System.out.println(i+"结果:"+rstr);
				all[i] = rstr;
			}
			return all;
			
			//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
		}
		//根据起始时间差出库批次细节
		public static String[] getXhjlViaTime(String ckid,String begin,String end) {
			String values [] = {ckid,begin,end};
			String [][] result = conn.selectTable(null,  values, "getXhjlViaTime");
			System.out.println("根据时间差销毁批次");
			
			if(result == null || result.length == 0 )return null;
		
			String [] all = new String[result.length];
			for(int i = 0;i < result.length;i++) {
				String rstr = result[i][0];
				for(int j = 1;j<result[i].length;j++)
					rstr += "," + result[i][j];
				System.out.println(i+"结果:"+rstr);
				all[i] = rstr;
			}
			return all;
			
		}
		//根据销毁批次查询
		public static String getXhInfo (int xhpc) {
			String values [] = {""+xhpc};
			System.out.println("根据ID入库批次");

			String [][] result = conn.selectTable(null,  values, "getXhjl");
			if(result == null || result.length == 0 )return null;
			String rstr = result[0][0];
			for(int i = 1;i<result[0].length;i++)
				rstr += "," + result[0][i];
			System.out.println("结果："+rstr);
			return rstr;
			
			//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
		}
	public Staff getStaff() {
		return this.staff;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ProductSystem ps = new ProductSystem(new Staff(1,"123456"));
		//addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi ,String dw)
		//int []yl = {1,2};
		//int []ylnum = {3,5};
		//Date time = new Date();
		String [] goal = {"仓库类别"};
		//String [] setvalues = {"400"};
		//
		//ps.changeProduct(2,goal,setvalues);
		String[] result = ProductSystem.selectStorageInfo(1,goal);
		for(int i = 0;i<result.length;i++) {
			System.out.println(result[i]);
		}
		//Object[] keyset = result.keySet().toArray();
		//for(int i = 0;i<keyset.length;i++) {
		//	System.out.println("id:"+Integer.parseInt(keyset[i].toString())+",name"+result.get(keyset[i]));
			
		//}
		//ps.changePeiFang(2,yl,ylnum);
		//ps.inStorage(3,1,5,time);
		//int st = ProductSystem.Storage(2);
		//System.out.println(st);
		//ps.outStorage(2,time,1,5); 
		//1订单修改状态
	}
	
}
