package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
public class MaterialSystem implements SystemFather {
	static Staff staff;
	//static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>();
	static String [] danwei = {"斤","公斤","克"};
	public MaterialSystem(){
		property.put("原料编号", "yuanliaoID");
		property.put("原料名称", "yuanliaoname");
		property.put("原料保质期", "baozhiqi");
		property.put("原料打包规格", "dabaoguige");
		property.put("原料单位", "danwei");
		property.put("仓库编号", "cangkuID");
		property.put("仓库类别", "cangkuzhurenID");
		property.put("仓库状态", "cangkuSTATE");
		property.put("仓库类别", "cangkuTYPE");
		property.put("仓库总容量", "cangkuSIZE");
		property.put("仓库剩余容量", "leftSIZE");
	}
	
	public void setStaff(Staff staff) {
		MaterialSystem.staff = staff;
	}
	
	public static String[] toSqlProperty(String[]parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	//通过测验
	public int addMaterial(String name,int dabaoguige,int savetime,String dw) {
		Map<Integer,String > map = selectAllMaterial();
		Object[] keys = map.keySet().toArray();
		for(int i = 0;i<keys.length;i++) {
			if(map.get(keys[i]).equals(name.trim())) return -1;//表示重名
		}
		boolean find = false;
		for(int i = 0;i<danwei.length;i++) {
			if(danwei[i] == dw.trim()) {
				find = true;
				break;
			}
		}
		if(!find) {
			return -2;//表示单位不合规
		}
		String [] para = {"yuanliaoname","dabaoguige","baozhiqi","danwei"};
		String [] value = {name,Integer.toString(dabaoguige),Integer.toString(savetime),dw};
		try{
			conn.insert(para,value, "YuanLiao");
		}catch (Exception e) {
			return -2;//表示插入失败
		}
		String [] goalParms = {"yuanliaoID"};
		String [][] result = conn.select(goalParms, para, value, "YuanLiao");
		return Integer.parseInt(result[0][0]);//返回id
	}
	//通过测验 原料进货
	public int stock(int ID,Date time,int num,int ghsID,float price,Date sctime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sctimestr = format.format(sctime);

		String [] parms = {"yuanliaoID","Ytime","Ynum","shengyuNUM","gonghuoshangID","yuangongID","YMONEY","shengchanTime"};
		String [] values = {Integer.toString(ID),timestr,
				Integer.toString(num),Integer.toString(num),
				Integer.toString(ghsID),
				Integer.toString(MaterialSystem.staff.getId()),
				Float.toString(price),
				sctimestr};
		try {
			conn.insert(parms, values, "YuanLiaoJinHuo");
		}catch(Exception e) {
			return -3;//插入失败
		}
		String [] goal = {"max(jinhuoID)"};
		String [] para = {"Ytime"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoJinHuo");
		return Integer.parseInt(result[0][0]);//返回生成的ID 
		
	}
	//通过测试 原料入库
	public int inStorage(int ckID,Date time,int num,int jhID) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sID = Integer.toString(MaterialSystem.staff.getId());
		String ck = Integer.toString(ckID);
		String numstr = Integer.toString(num);
		String jinhuo = Integer.toString(jhID);
		String [] parms = {"cangkuID","rukuTIME","yuanliaoNUM","shengyuNUM","rukufuzerenID","jinhuoID"};
		String [] values = {ck,timestr,numstr,numstr,sID,jinhuo};
		
		try {
			conn.insert(parms, values, "YuanLiaoRuKu");
		}catch(Exception e) {
			return -1;//插入失败
		}
		String [] goal = {"max(rukupiciID)"};
		String [] para = {"rukuTIME"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoRuKu");
		return Integer.parseInt(result[0][0]);//返回生成的ID 
	}
	//3）OutStorage()原料出库,通过测试
	public int outStorage(int rukuID,Date time,int cjID ,int num) {
		//时间问题未解决
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sID = Integer.toString(MaterialSystem.staff.getId());
		String rk = Integer.toString(rukuID);
		String cj = Integer.toString(cjID);
		String numstr = Integer.toString(num);
		String [] parms = {"yuangongID","rukupiciID","chukuTIME","chukuchejianID","chukuNUM","shengyuNUM"};
		String [] values = {sID,rk,timestr,cj,numstr,numstr};
		
		try {
			conn.insert(parms, values, "YuanLiaoChuKu");
		}catch(Exception e) {
			return -1;
		}
		
		String [] goal = {"max(chukupiciID)"};
		String [] para = {"chukuTIME"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoChuKu");
		return Integer.parseInt(result[0][0]);//返回生成的ID 
	}
	//4）显示过期原料，通过测试。
	//返回入库批次编号，原料编号，原料名称
	String[][] expire() {
		//返回入库批次ID，原料ID,原料名称
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "expireMaterial");
	}
	//通过测试，还有10天过期的原料
	String[][] comingExpire(){
		//返回入库批次ID，原料ID,原料名称
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "ComingExMaterial");
	}
	//通过测试，原料销毁
	private void destroyMaterial(int[]rukupiciID,Date time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String yuangongID = Integer.toString(MaterialSystem.staff.getId());
		String []parms = {"xiaohuiyuangongID","rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		for(int i = 0;i<rukupiciID.length;i++) {
			String []values = new String[4];
			values[0] = yuangongID;
			values[1] = Integer.toString(rukupiciID[i]);
			values[2] = timestr;
			String[]goal = {"shengyuNUM"};
			String[]par = {"rukupiciID"};
			String[]val = {Integer.toString(rukupiciID[i])};
			String [][] get = conn.select(goal, par, val, "YuanLiaoRuKu");
			values[3] = get[0][0];
			conn.insert(parms, values, "YuanLiaoXiaoHuiJiLu");
		}
		
	}
	//销毁所有的过期原料
	void destroyMaterialAll(Date time) {
		String [][]result = expire();
		if(result == null||result.length==0)
			return;
		else {
			int[]rkpc = new int[result.length];
			for(int i = 0;i<result.length;i++) {
				rkpc[i] = Integer.parseInt(result[i][0]);
			}
			this.destroyMaterial(rkpc, time);
		}
	}
	//5）通过测试，仓库状态管理：仓库使用情况、手动于系统中更改仓库状态、输入仓库信息查看其状态、查询某种状态分别有哪些仓库。
	void changeStorageState(int id,String state) {
		String[] setparms = {"cangkuSTATE"};
		String[] values = {state};
		String[] parms = {"cangkuID"};
		String[] goal = {Integer.toString(id)};
		conn.update(setparms,values,parms,goal,"YuanLiaoCangKu"); 
	}
	//6）通过测试，原料信息修改：输入信息，修改
	void changeMaterial(int ID, String[] set,String[] setvalues) {
		String[] parms = {"yuanliaoID"};
		String[] values = {Integer.toString(ID)};
		String[] setparms = toSqlProperty(set);
		conn.update(setparms, setvalues, parms, values, "YuanLiao");//更新某原料信息
	}
	//通过测试,库存查询
	public static int Storage(int id) {
		String[]parms = {Integer.toString(id)};
		int result = conn.selectScale(parms, "MaterialStorage");
		return result;
	}
	//pass查找某个产品的信息某些
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"yuanliaoID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "YuanLiao");
		return result[0];
	}
	
	//pass查找某个产品的全部信息
	public static String[] selectAllInfo(int ID) {
		String [] parms = {"yuanliaoID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "YuanLiao");
		return result[0];
	}
	//pass查找某仓库某些信息
	public static String[] selectStorageInfo(int ID,String[]goal) {
		String [] parms = {"cangkuID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "YuanLiaoCangKu");
		return result[0];
	}
	
	//pass查找某仓库所有信息
	public static String[] selectAllStorageInfo(int ID) {
		String [] parms = {"cangkuID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "YuanLiaoCangKu");
		return result[0];
	}
	//pass 返回所有原料名称、id
	public static Map<Integer,String> selectAllMaterial(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"yuanliaoID","yuanliaoname"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "YuanLiao");
		for(int i = 0;i<result.length;i++) {
			map.put(Integer.parseInt(result[i][0]), result[i][1]);
		}
		return map;
	}
	//pass ,返回所有原料仓库id 类别
	public static Map<Integer,String> selectAllStorage(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"cangkuID","cangkuTYPE"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "YuanLiaoCangKu");
		for(int i = 0;i<result.length;i++) {
			map.put(Integer.parseInt(result[i][0]), result[i][1]);
		}
		return map;
	}
	//删除原料
	public void deleteMaterial(int ID) {
		String []parms = {"yuanliaoID"};
		String []values = {Integer.toString(ID)};
		conn.delete(parms, values, "YuanLiao");
		
	}
	public void deleteMaterials(int[] ID) {
		String []parms = {"yuanliaoID"};
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			conn.delete(parms, values, "YuanLiao");
		}
	}
	//删除仓库
	public void deleteStorage(int ID) {
		String []parms = {"cangkuID"};
		String []values = {Integer.toString(ID)};
		conn.delete(parms, values, "YuanLiaoCangKu");
	}
	public void deleteStorages(int [] ID) {
		String []parms = {"cangkuID"};
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			conn.delete(parms, values, "YuanLiaoCangKu");
		}
		
	}
	//7）各种查询
	
	//必须的功能函数
	int findMaterialID(String name) {
		String [] goal = {"yuanliaoID"};
		String [] para = {"yuanliaoname"};
		String [] value = {name};
		String[][] result = conn.select(goal, para, value, "YuanLiao");
		if(result == null||result.length==0) {
			return -1;
		}
		int ID = Integer.parseInt(result[0][0]);
		
		return ID;
	}
	int findSupplyID(String name) {
		String [] goal = {"gonghuoshangID"};
		String [] para = {"gonghuoshangNAME"};
		String [] value = {name};
		String[][] result = conn.select(goal, para, value, "GongHuoShang");
		if(result == null||result.length == 0) {
			return -1;//没找到
		}
		int ID = Integer.parseInt(result[0][0]);
		
		return ID;
	}
	
//	public static void main(String[] args) throws ParseException {
//		// TODO Auto-generated method stub
//		MaterialSystem ms = new MaterialSystem(new Staff(1,"123456"));
////		String[]parm = {"baozhiqi"};
////		String[]value = {"200"};
//		//Date time = new Date();
//		String [] goal = {"仓库总容量","仓库状态","仓库类别"};
//		String [] result = MaterialSystem.selectStorageInfo(1,goal);
//		for(int i = 0;i<result.length;i++) {
//			System.out.println(result[i]);
//		}
//		//int get = MaterialSystem.Storage(2);
//		//System.out.print(get);
//		//ms.outStorage(3,time,1,50);
//		//Map<Integer,String >result = MaterialSystem.selectAllMaterial();
//		//Object[] keyset = result.keySet().toArray();
//		//for(int i = 0;i<keyset.length;i++) {
//		//	System.out.println("id:"+Integer.parseInt(keyset[i].toString())+",name"+result.get(keyset[i]));
//			
//		//}
////		Date date = new Date();
////		System.out.println(date);
////		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
////		Date product = format.parse("2019-10-23 10:00:00");
////		ms.inStorage(1,date,70,8) ;
////		float price = (float) 12.5;
////		String [][]result = ms.comingExpire();
////		for(int i = 0;i<result.length;i++) {
////			for(int j = 0;j<result[0].length;j++) {
////				System.out.print(result[i][j]);
////			}
////			System.out.println();
////		}
////		
////		int []pc = {2};
////		ms.destroyMaterial(pc, date);
//	}
	
	
}
