package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
public class ProductSystem implements SystemFather  {
	static Staff staff;
	//static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>(); 
	static String [] danwei = {"斤","公斤","克","包"};
	public ProductSystem() {
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
	
	public void setStaff(Staff staff) {
		ProductSystem.staff = staff;
	}
	
	public static String[] toSqlProperty(String[]parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	//通过测试 添加新产品
	public int addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi,String dw) {
		Map<Integer,String> result = selectAllProduct();
		Object[] keys = result.keySet().toArray();
		for(int i = 0;i<keys.length;i++) {
			if(name.trim() == result.get(keys[i])) return -1;//表示重名
		}
		boolean find = false;
		for(int i =0;i<danwei.length;i++) {
			if(dw.trim()==danwei[i]) {
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
		String ylstr = Integer.toString(yuanliao[0]);
		String ylnum = Integer.toString(ynum[0]);
		for(int i = 1;i<yuanliao.length;i++) {
			ylstr +=","+Integer.toString(yuanliao[i]);
			ylnum +=","+Integer.toString(ynum[i]);
		}
		//插入产品表
		String []parms = {"mingchen","dabaoguige","jiage","baozhiqi","danwei"};
		String []values = {name,gg,money,bzq,dw};
		conn.insert(parms, values, "ChanPin");
		String [] goalParms = {"ID"};
		String [][]cpid = conn.select(goalParms, parms, values, "ChanPin");
		int id = Integer.parseInt(cpid[0][0]);
		//插入配方表
		String []parm = {"chanpinID","yuanliaoID","yuanliaoNUM"};
		String idstr = Integer.toString(findProduct(name));
		String[]value = {idstr,ylstr,ylnum};
		conn.insert(parm, value,"PeiFang");
		return id;
	}
	//通过测试，产品入库
	public int inStorage(int piciID,int cangkuID,int num,Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"cangkuID","shengchanpiciID","chanpinNUM","rukufuzerenID","rukuTIME","shengyuNUM"};
		String ckID = Integer.toString(cangkuID);
		String pcID = Integer.toString(piciID);
		String fzrID = Integer.toString(ProductSystem.staff.getId());
		String rknum = Integer.toString(num);
		String synum = rknum;
		String []values = {ckID,pcID,rknum,fzrID,timestr,synum};
		
		conn.insert(parms, values, "ChanPinRuKu");
		String []goalParms = {"rukupiciID"};
		String [][]rkpc = conn.select(goalParms, goalParms, values, "ChanPinRuKu");
		return Integer.parseInt(rkpc[0][0]);
	}
	//通过测试，产品出库
	public void outStorage(int rkpcID,Date time,int ddID,int num) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"rukupiciID","chukuTIME","chukufuzerenID","dingdanID","chanpinNUM"};
		String []values = {
				Integer.toString(rkpcID),
				timestr,
				Integer.toString(ProductSystem.staff.getId()),
				Integer.toString(ddID),
				Integer.toString(num)};
		conn.insert(parms, values, "ChanPinChuKu");
		
	}
	//来自仓库的产品销毁，暂不测试，逻辑和原料一样
	public void destroyFromStorage(int rukupiciID,Date time,int num) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		String []values = {Integer.toString(rukupiciID),timestr,Integer.toString(num)};
		conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
	}
	//来自生产车间的产品销毁（生产不合格），暂不测试，逻辑和原料一样
	public void destroyFromWork(int scpc,Date time,int num) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"shengchanpiciID","xiaohuiTIME","xiaohuiNUM"};
		String []values = {Integer.toString(scpc),timestr,Integer.toString(num)};
		conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
	}
	//过期产品显示，暂不测试，逻辑和原料一样
	public String[][] expire() {
		//暂时只显示过期的，濒临过期的还未确定时间
		//返回入库批次ID，原料ID,原料名称
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "expireProduct");
		
	}
	//即将过期产品显示，暂不测试，逻辑和原料一样
	public String[][] comingExpire(){
		//返回入库批次ID，产品ID,产品名称
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "ComingExProduct");
	}
	//通过测试，查询产品库存
	public static int Storage(int id) {
		String[]parms = {Integer.toString(id)};
		int result = conn.selectScale(parms, "ProductStorage");
		return result;
	}
	//产品销毁
	public void destroy(int []rukupiciID,Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String yuangongID = Integer.toString(ProductSystem.staff.getId());
		String []parms = {"xiaohuiyuangongID","rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		for(int i = 0;i<rukupiciID.length;i++) {
			String []values = new String[4];
			values[0] = yuangongID;
			values[1] = Integer.toString(rukupiciID[i]);
			values[2] = timestr;
			String[]goal = {"shengyuNUM"};
			String[]par = {"rukupiciID"};
			String[]val = {Integer.toString(rukupiciID[i])};
			String [][] get = conn.select(goal, par, val, "ChanPinRuKu");
			values[3] = get[0][0];
			conn.insert(parms, values, "ChanPinXiaoHuiJiLu");
		}
	}
	//5）仓库状态管理：仓库使用情况、手动于系统中更改仓库状态、输入仓库信息查看其状态、查询某种状态分别有哪些仓库。
	public void changeStorageState(String state) {
		String[] setparms = {"cangkuzhuangtai"};
		String[] values = {state};
		conn.update(setparms,values,null,null,"ChanPinCangKu"); 
	}
	//6）pass 产品信息修改：输入信息，修改
	public void changeProduct(int ID,String[] set,String[] setvalues) {
		String[] parms = {"ID"};
		String [] setparms = toSqlProperty(set);
		String[] values = {Integer.toString(ID)};
		conn.update(setparms, setvalues, parms, values, "ChanPin");//更新某原料信息
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
	public static String[] selectAllInfo(int ID) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "ChanPin");
		return result[0];
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
	public void changePeiFang(int id,int[]yl,int[]ylnum) {
		String ylstr = Integer.toString(yl[0]);
		String ylnumstr = Integer.toString(ylnum[0]);
		for(int i = 1;i<yl.length;i++) {
			ylstr += ","+Integer.toString(yl[i]);
			ylnumstr += ","+Integer.toString(ylnum[i]);
		}
		String []setparms = {"yuanliaoID","yuanliaoNUM"};
		String []setvalues = {ylstr,ylnumstr};
		String []parms = {"ChanPinID"};
		String []values = {Integer.toString(id)};
		conn.update(setparms, setvalues, parms, values, "PeiFang");
	}
	//pass 根据名称找id
	public static int findProduct(String name) {
		String[]parms = {"mingchen"};
		String[]values = {name};
		String[]goal = {"ID"};
		String [][]result = conn.select(goal,parms, values,"ChanPin");
		return Integer.parseInt(result[0][0]);
	}
	//通过测试，配方查找
	public static String[] findPeiFang(int cpID) {
		String[]goalParms = {"yuanliaoID","yuanliaoNUM"};
		String[]parms = {"chanpinID"};
		String[]values = {Integer.toString(cpID)};
		String [][]result = conn.select(goalParms, parms, values, "PeiFang");
		return result[0];
	}
	//pass返回所有产品名称、id
	public static Map<Integer,String> selectAllProduct(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"ID","mingchen"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "ChanPin");
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
		for(int i = 0;i<result.length;i++) {
			map.put(Integer.parseInt(result[i][0]), result[i][1]);
		}
		return map;
	}
	//删除原料
	public void deleteProduct(int ID) {
		String []parms = {"ID"};
		String []values = {Integer.toString(ID)};
		conn.delete(parms, values, "ChanPin");
		
	}
	public void deleteProducts(int[] ID) {
		String []parms = {"ID"};
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			conn.delete(parms, values, "ChanPin");
		}
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
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		ProductSystem ps = new ProductSystem(new Staff(1,"123456"));
//		//addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi ,String dw)
//		//int []yl = {1,2};
//		//int []ylnum = {3,5};
//		//Date time = new Date();
//		String [] goal = {"仓库类别"};
//		//String [] setvalues = {"400"};
//		//
//		//ps.changeProduct(2,goal,setvalues);
//		String[] result = ProductSystem.selectStorageInfo(1,goal);
//		for(int i = 0;i<result.length;i++) {
//			System.out.println(result[i]);
//		}
//		//Object[] keyset = result.keySet().toArray();
//		//for(int i = 0;i<keyset.length;i++) {
//		//	System.out.println("id:"+Integer.parseInt(keyset[i].toString())+",name"+result.get(keyset[i]));
//			
//		//}
//		//ps.changePeiFang(2,yl,ylnum);
//		//ps.inStorage(3,1,5,time);
//		//int st = ProductSystem.Storage(2);
//		//System.out.println(st);
//		//ps.outStorage(2,time,1,5); 
//		//1订单修改状态
//	}
//	
}
