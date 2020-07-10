package model.bll.systems;
import java.text.ParseException;
//import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.CallStack;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

import java.sql.SQLException;
//import model.dal.*;
public class MaterialSystem implements SystemFather {
	
	Staff staff;
	static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>();
	static String [] danwei = {"��","����","��"};
	static {
		property.put("ԭ�ϱ��", "yuanliaoID");
		property.put("ԭ������", "yuanliaoname");
		property.put("ԭ�ϱ�����", "baozhiqi");
		property.put("ԭ�ϴ�����", "dabaoguige");
		property.put("ԭ�ϵ�λ", "danwei");
		property.put("�ֿ���", "cangkuID");
		property.put("�ֿ����", "cangkuzhurenID");
		property.put("�ֿ�״̬", "cangkuSTATE");
		property.put("�ֿ����", "cangkuTYPE");
		property.put("�ֿ�������", "cangkuSIZE");
		property.put("�ֿ�ʣ������", "leftSIZE");
	}
	public MaterialSystem(){
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	public MaterialSystem(Staff staff){
		this.staff = staff;
		
	}
	static String[] toSqlProperty(String[]parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	//ͨ������
	public int addMaterial(String name,int dabaoguige,int savetime,String dw) {
		System.out.println("�����ԭ����:"+name);
		Map<Integer,String > map = selectAllMaterial();
		Object[] keys = map.keySet().toArray();
		for(int i = 0;i<keys.length;i++) {
			System.out.println("����ԭ����:"+map.get(keys[i]));
			if(map.get(keys[i]).equals(name.trim())) {
				System.out.println("���");
				return -1;//��ʾ����
			}
			
		}
		String [] para = {"yuanliaoname","dabaoguige","baozhiqi","danwei"};
		String [] value = {name,Integer.toString(dabaoguige),Integer.toString(savetime),dw};
		try{
			conn.insert(para,value, "YuanLiao");
		}catch (Exception e) {
			return -3;//��ʾ����ʧ��
		}
		String [] goalParms = {"yuanliaoID"};
		String [][] result = conn.select(goalParms, para, value, "YuanLiao");
		return Integer.parseInt(result[0][0]);//����id
	}
	//ͨ������ ԭ�Ͻ���
	public  int stock(int ID,int num,int ghsID,float price,Date sctime) {
		Date time=new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sctimestr = format.format(sctime);
//		//����,Ӧ�øĵ�������,���ؽ������α��
//		int ID = this.findMaterialID(ylname);
//		int ghsID = this.findSupplyID(ghsname);
//		if(ID<0||ghsID<0) {
//			return -1;//����������ԭ��
//		}
//		if(ghsID<0) {
//			return -2;//�����̲�����
//		}
		String [] parms = {"yuanliaoID","Ytime","Ynum","shengyuNUM","gonghuoshangID","yuangongID","YMONEY","shengchanTime"};
		String [] values = {Integer.toString(ID),timestr,Integer.toString(num),Integer.toString(num),Integer.toString(ghsID),Integer.toString(this.staff.getId()),Float.toString(price),sctimestr};
		try {
			conn.insert(parms, values, "YuanLiaoJinHuo");
		}catch(Exception e) {
			return -3;//����ʧ��
		}
		String [] goal = {"max(jinhuoID)"};
		String [] para = {"Ytime"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoJinHuo");
		
		CallStack.printCallStatck();
		return Integer.parseInt(result[0][0]);//�������ɵ�ID 
		
	}
	//ͨ������ ԭ�����
	public int inStorage(int ckID,int num,int jhID) {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sID = Integer.toString(this.staff.getId());
		String ck = Integer.toString(ckID);
		String numstr = Integer.toString(num);
		String jinhuo = Integer.toString(jhID);
		String [] parms = {"cangkuID","rukuTIME","yuanliaoNUM","shengyuNUM","rukufuzerenID","jinhuoID"};
		String [] values = {ck,timestr,numstr,numstr,sID,jinhuo};
		
		try {
			conn.insert(parms, values, "YuanLiaoRuKu");
		}catch(Exception e) {
			return -1;//����ʧ��
		}
		String [] goal = {"max(rukupiciID)"};
		String [] para = {"rukuTIME"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoRuKu");
		return Integer.parseInt(result[0][0]);//�������ɵ�ID 
	}
	//3��OutStorage()ԭ�ϳ���,ͨ������
	public int outStorage(int rukuID,int cjID ,int num) {
		//ʱ������δ���
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String sID = Integer.toString(this.staff.getId());
		String rk = Integer.toString(rukuID);
		String cj = Integer.toString(cjID);
		String numstr = Integer.toString(num);
		String [] parms = {"yuangongID","rukupiciID","chukuTIME","chukuchejianID","chukuNUM","shengyuNUM"};
		String [] values = {sID,rk,timestr,cj,numstr,numstr};
		
		try {
			conn.insert(parms, values, "YuanLiaoChuKu");
		}catch(Exception e) {
			return -1;//����ʧ��
		}
		
		String [] goal = {"max(chukupiciID)"};
		String [] para = {"chukuTIME"};
		String [] value = {timestr};
		String [][] result = conn.select(goal, para, value, "YuanLiaoChuKu");
		return Integer.parseInt(result[0][0]);//�������ɵ�ID 
	}
	//4����ʾ����ԭ�ϣ�ͨ�����ԡ�
	//����������α�ţ�ԭ�ϱ�ţ�ԭ������
	public static String[][] expire() {
		//�����������ID��ԭ��ID,ԭ������
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "expireMaterial");
		
	}
	//ͨ�����ԣ�����10����ڵ�ԭ��
	String[][] comingExpire(){
		//�����������ID��ԭ��ID,ԭ������
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "ComingExMaterial");
	}
	//add    ��ȡĳԭ�ϵ������������� 0  ���������
	public static Integer[] selectAllRkpcViaName(String name) {
		//û�ж�Ӧ��������εĻ�����null
		String [] goalParms = {"rukupiciID"};
		String [] parms = {name}; 
		String [][] get = conn.selectTable(goalParms, parms, "findAllRkpc");
		if(get == null || get.length == 0) {
			return null;
		}
		Integer [] result = new Integer [get.length];
		
		for(int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(get[i][0]);
		}
		return result;
	}
	//ͨ�����ԣ�ԭ������
	public void destroyMaterial(int[]rukupiciID) {
		Date time = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String yuangongID = Integer.toString(this.staff.getId());
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
	//�������еĹ���ԭ��
	public void destroyMaterialAll() {
		//Date time = new Date();
		String [][]result = expire();
		if(result == null||result.length==0)
			return;
		else {
			int[]rkpc = new int[result.length];
			for(int i = 0;i<result.length;i++) {
				rkpc[i] = Integer.parseInt(result[i][0]);
			}
			this.destroyMaterial(rkpc);
		}
	}
	//5��ͨ�����ԣ��ֿ�״̬�����ֿ�ʹ��������ֶ���ϵͳ�и��Ĳֿ�״̬������ֿ���Ϣ�鿴��״̬����ѯĳ��״̬�ֱ�����Щ�ֿ⡣
	void changeStorageState(int id,String state) {
		String[] setparms = {"cangkuSTATE"};
		String[] values = {state};
		String[] parms = {"cangkuID"};
		String[] goal = {Integer.toString(id)};
		conn.update(setparms,values,parms,goal,"YuanLiaoCangKu"); 
	}
	//6��ͨ�����ԣ�ԭ����Ϣ�޸ģ�������Ϣ���޸�
	void changeMaterial(int ID,String[] set,String[] setvalues) {
		String[] parms = {"yuanliaoID"};
		String[] values = {Integer.toString(ID)};
		String[] setparms = toSqlProperty(set);
		conn.update(setparms, setvalues, parms, values, "YuanLiao");//����ĳԭ����Ϣ
	}
	//ͨ������,����ѯ
	public static int Storage(int id) {
		String[]parms = {Integer.toString(id)};
		int result;
		try {
			result = Integer.parseInt(conn.selectScale(parms, "MaterialStorage"));
		}catch(NumberFormatException e) {
			return 0;
		}
		return result;
	}
	//pass����ĳ����Ʒ����ϢĳЩ
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"yuanliaoID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "YuanLiao");
		return result[0];
	}
	
	//pass����ĳ����Ʒ��ȫ����Ϣ
	public static String selectAllInfo(int ID) {
		//System.out.println("select all material");
		String [] parms = {"yuanliaoID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "YuanLiao");
		if(result==null || result.length == 0 )return null; 
		String per = result[0][result[0].length-1];
		for(int i = 1;i < result[0].length-1;i++) {
			per+= "," + result[0][i];
		}
		per += "," + MaterialSystem.Storage(ID);
		System.out.println(per);
		return per;
	}
	public static String [] selectAllMatInfo() {
		Map<Integer,String>map = MaterialSystem.selectAllMaterial();
		if(map == null||map.size() == 0)return null;
		Object [] ids = map.keySet().toArray();
		String [] result = new String [ids.length];
		for(int i = 0;i < ids.length ;i++) {
			result[i] = selectAllInfo((Integer)ids[i]);
		}
		return result;
	}
	//pass����ĳ�ֿ�ĳЩ��Ϣ
	public static String[] selectStorageInfo(int ID,String[]goal) {
		String [] parms = {"cangkuID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "YuanLiaoCangKu");
		return result[0];
	}
	
	//pass����ĳ�ֿ�������Ϣ
	public static String[] selectAllStorageInfo(int ID) {
		String [] parms = {"cangkuID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "YuanLiaoCangKu");
		if(result==null||result.length == 0)
			return null;
		return result[0];
	}
	//pass ��������ԭ�����ơ�id
	public static Map<Integer,String> selectAllMaterial(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"yuanliaoID","yuanliaoname"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "YuanLiao");
		if(result != null)
			for(int i = 0; i < result.length; i++) {
				map.put(Integer.parseInt(result[i][0]), result[i][1]);
			}
		return map;
	}
	//pass  add ��������ԭ������
	public static String[] selectAllMaterialName() {
		Map<Integer,String> map = MaterialSystem.selectAllMaterial();
		if(map.size()==0)
			return null;
		Object [] keys = map.keySet().toArray();
		String [] result = new String[keys.length];
		for(int i = 0; i < keys.length; i++) {
			result[i] = map.get(keys[i]);
		}
		return result;
	}
	//pass
	public static String[] selectAllGhsName() {
		String [] goalParms = {"gonghuoshangNAME"};
		String [] parms = null;
		String [] values = null;
		String [][] result1 = conn.select(goalParms, parms, values, "GongHuoShang");
		if(result1 == null||result1.length == 0) {
			return null;
		}
		String [] result = new String[result1.length];
		for(int i = 0;i < result1.length;i++) {
			result[i] = result1[i][0];
		}
		return result;
	}
	//pass ,��������ԭ�ϲֿ�id ���
	public static Map<Integer,String> selectAllStorage(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		String [] goalParms = {"cangkuID","cangkuTYPE"};
		String [] parms = null;
		String [] values = null;
		
		String [][] result = conn.select(goalParms, parms, values, "YuanLiaoCangKu");
		if(result!=null&&result.length>0)
			for(int i = 0;i<result.length;i++) {
				map.put(Integer.parseInt(result[i][0]), result[i][1]);
			}
		return map;
	}
	public static Integer[] selectAllStorageId() {
		Map<Integer,String> map = MaterialSystem.selectAllStorage();
		if(map.size()==0) {
			return null;
		}
		Object keys[] = map.keySet().toArray();
		Integer result[] = new Integer[keys.length];
		for(int i = 0;i < keys.length;i++) {
			result[i] = Integer.parseInt(keys[i].toString());
		}
		return result;
	}
	//ɾ��ԭ��
	public boolean deleteMaterial(int ID) {
		String []parms = {"yuanliaoID"};
		String []values = {Integer.toString(ID)};
		return conn.delete(parms, values, "YuanLiao");
		
	}
	// change
	public Object[] deleteMaterials(int[] ID) {
		String []parms = {"yuanliaoID"};
		ArrayList<Integer> result = new ArrayList<Integer>();
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			if(!conn.delete(parms, values, "YuanLiao")) {
				result.add(ID[i]);
			}
		}
		return result.toArray();
	}
	//��Ӳֿ�
	public static int addStorage(int managerid,String type,String state,int num) {
		String [] parms = {"cangkuzhurenID","cangkuTYPE","cangkuSTATE","cangkuSIZE"};
		String idstr = Integer.toString(managerid);
		String numstr = Integer.toString(num);
		String [] values = {idstr,type,state,numstr};
		int flag = conn.insert(parms, values, "YuanLiaoCangKu");
		if(flag < 0)return flag;//-1˵���ظ��� -2˵������ʧ��
		String [] goalParms = {"cangkuID"};
		String [][] result = conn.select(goalParms, parms, values, "YuanLiaoCangKu");
		if(result!=null&&result.length!=0) {
			return Integer.parseInt(result[0][0]);
		}
		else {
			return -3;//����ɹ����ǲ�ѯ���ʧ��
		}
	}
	//ɾ���ֿ�
	public static boolean deleteStorage(int ID) {
		String []parms = {"cangkuID"};
		String []values = {Integer.toString(ID)};
		return conn.delete(parms, values, "YuanLiaoCangKu");
	}
	public void deleteStorages(int [] ID) {
		String []parms = {"cangkuID"};
		for(int i = 0;i<ID.length;i++) {
			String []values = {Integer.toString(ID[i])};
			conn.delete(parms, values, "YuanLiaoCangKu");
		}
		
	}
	//7�����ֲ�ѯ
	
	//����Ĺ��ܺ���
	public static int findMaterialID(String name) {
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
	public static String findMaterialName(int id) {
		String [] goal = {"yuanliaoname"};
		String [] parms = {"yuanliaoID"};
		String idstr = Integer.toString(id);
		String [] values = {idstr};
		String [][] result = conn.select(goal, parms, values, "YuanLiao");
		if(result == null || result.length == 0)
			return null;
		else {
			System.out.println("findName : "+result[0][0]);
			return result[0][0];
		}
	}
	//pass
	public static Integer[] findJhpc(int id) {
		//String [] goalParms= {"jinhuoID"};
		//String [] parms = {"yuanliaoID"};
		//String idstr = Integer.toString(id);
		//String [] values = {idstr};
		String sql = "select jinhuoID from YuanLiaoJinHuo where yuanliaoID = "+id+" and shengyuNUM > 0";
		String [][] result = conn.get(sql);
		if(result==null||result.length==0) {
			return null;
		}
		Integer [] pc = new Integer[result.length];
		for(int i = 0;i < result.length ;i++) {
			pc[i] = Integer.parseInt(result[i][0]);
		}
		return pc;
	}
	//pass
	public static Integer[] jhpcLeft(int id) {
		Integer [] jhpc = MaterialSystem.findJhpc(id);
		if(jhpc==null)
			return null;
		else {
			String [] goalParms= {"shengyuNUM"};
			String [] parms = {"jinhuoID"};
			Integer [] result = new Integer[jhpc.length];
			for(int i = 0;i < jhpc.length; i++) {
				String pcstr = Integer.toString(jhpc[i]);
				String [] values = {pcstr};
				String [][]get = conn.select(goalParms, parms, values, "YuanLiaoJinHuo");
				if(get==null||get.length == 0) {
					result[i] = 0;
				}
				else {
					result[i] = Integer.parseInt(get[0][0]);
				}
			}
			return result;
		}
		
	}
	//Pass
	public static Map<Integer,Integer> jhpcLeftMap(int id) {
		Integer [] jhpc = MaterialSystem.findJhpc(id);
		Integer [] left = MaterialSystem.jhpcLeft(id);
		if(jhpc==null)
			return null;
		else {
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for(int i = 0; i < jhpc.length;i++) {
				map.put(jhpc[i], left[i]);
			}
			return map;
		}
	}
	//pass ���ݽ���������������ε�ʣ������
	public static Integer[] rkpcLeft(int ylid) {
		Integer [] rkpc = MaterialSystem.findRkpcViayl(ylid);
		if(rkpc==null)
			return null;
		else {
			String [] goalParms= {"shengyuNUM"};
			String [] parms = {"rukupiciID"};
			Integer [] result = new Integer[rkpc.length];
			for(int i = 0;i < rkpc.length; i++) {
				String pcstr = Integer.toString(rkpc[i]);
				String [] values = {pcstr};
				String [][]get = conn.select(goalParms, parms, values, "YuanLiaoRuKu");
				if(get==null||get.length == 0) {
					result[i] = 0;
				}
				else {
					result[i] = Integer.parseInt(get[0][0]);
				}
			}
			return result;
		}
		
	}
	//pass ���ݽ��������Ҷ�Ӧ���������
	public static Integer[] findRkpc(int id) {
		//String [] goalParms= {"rukupiciID"};
		//String [] parms = {"jinhuoID"};
		//String idstr = Integer.toString(id);
		//String [] values = {idstr};
		String sql = "select rukupiciID from YuanLiaoRuKu where jinhuoID = "+id+" and shengyuNUM > 0";
		String [][] result = conn.get(sql);
		if(result==null||result.length==0) {
			return null;
		}
		Integer [] pc = new Integer[result.length];
		for(int i = 0;i < result.length ;i++) {
			pc[i] = Integer.parseInt(result[i][0]);
		}
		return pc;
	}
	public static Integer[] findRkpcViayl(int id) {
		String sql = "select YuanLiaoRuKu.rukupiciID " + 
				"from YuanLiao,YuanLiaoJinHuo,YuanLiaoRuKu " + 
				"where YuanLiao.yuanliaoID = YuanLiaoJinHuo.yuanliaoID and YuanLiaoJinHuo.jinhuoID = YuanLiaoRuKu.jinhuoID " + 
				"and YuanLiaoRuKu.shengyuNUM > 0 and YuanLiao.yuanliaoID = "+id;
		String [][] result = conn.get(sql);
		if(result==null||result.length==0) {
			return null;
		}
		System.out.println("��"+id+"���������");
		Integer [] pc = new Integer[result.length];
		for(int i = 0;i < result.length ;i++) {
			pc[i] = Integer.parseInt(result[i][0]);
			System.out.println("rkpc:"+pc[i]);
		}
		return pc;
	}
	//pass
	public static Map<Integer,Integer> rkpcLeftMap(int ylid) {
		System.out.println("��ylid = "+ylid+"����ʣ���������������");
		Integer [] rkpc = MaterialSystem.findRkpcViayl(ylid);
		Integer [] left = MaterialSystem.rkpcLeft(ylid);
		if(rkpc==null)
			return null;
		else {
			
			Map<Integer,Integer> map = new HashMap<Integer,Integer>();
			for(int i = 0; i < rkpc.length;i++) {
				System.out.println("����"+rkpc[i]+",ʣ��"+left[i]);
				map.put(rkpc[i], left[i]);
			}
			return map;
		}
	}
	//pass
	public static int findSupplyID(String name) {
		String [] goal = {"gonghuoshangID"};
		String [] para = {"gonghuoshangNAME"};
		String [] value = {name};
		String[][] result = conn.select(goal, para, value, "GongHuoShang");
		if(result == null||result.length == 0) {
			return -1;//û�ҵ�
		}
		int ID = Integer.parseInt(result[0][0]);
		
		return ID;
	}
	//������Ϣ
	//ͨ��pc
	public static String getStockInfoViapc(String id) {
		String [] parms = {id};
		String [][] all = conn.selectTable(null, parms, "getStockInfoViaPc");
		if(all == null|| all.length == 0)return null;
		String re = all[0][0];
		for(int i = 1;i < all[0].length ;i++) {
			re += ","+all[0][i];
		}
		return re;
	}
	//ͨ��������
	public static String[] getStockInfoViaFzr(String id) {
		String [] parms = {id};
		String [][] all = conn.selectTable(null, parms, "getStockInfoViaFzr");
		if(all == null|| all.length == 0)return null;
		String [] result = new String [all.length];
		for(int i = 0;i < all.length ;i++) {
			String re = all[i][0];
			for(int j = 1;j < all[i].length ;j++) {
				re += "," + all[i][j];
			}
			result[i] = re;
		}
		return result;
	}
	//ͨ��time
	public static String[] getStockInfoViaTime(String begin,String end) {
		String [] parms = {begin,end};
		String [][] all = conn.selectTable(null, parms, "getStockInfoViaTime");
		if(all == null|| all.length == 0)return null;
		String [] result = new String [all.length];
		for(int i = 0;i < all.length ;i++) {
			String re = all[i][0];
			for(int j = 1;j < all[i].length ;j++) {
				re += "," + all[i][j];
			}
			result[i] = re;
		}
		return result;
	}
	//����ԭ�������Ϣ����������
	public static String getInInfoViapc(String pc) {
		String [] parms = {pc};
		String [][] all = conn.selectTable(null, parms, "getMatInfoViapc");
		if(all == null|| all.length == 0)return null;
		String re = all[0][0];
		for(int i = 1;i < all[0].length ;i++) {
			re += ","+all[0][i];
		}
		return re;
	}
	//���ݸ�����
	public static String[] getInInfoViaFzr(String fzr) {
		String [] parms = {fzr};
		String [][] all = conn.selectTable(null, parms, "getMatInfoViaFzr");
		if(all == null|| all.length == 0)return null;
		String [] result = new String [all.length];
		for(int i = 0;i < all.length ;i++) {
			String re = all[i][0];
			for(int j = 1;j < all[i].length ;j++) {
				re += "," + all[i][j];
			}
			result[i] = re;
		}
		return result;
	}
	//����ʱ��
	public static String[] getInInfoViaTime(String begin,String end) {
		String [] parms = {begin,end};
		String [][] all = conn.selectTable(null, parms, "getMatInfoViaTime");
		if(all == null|| all.length == 0)return null;
		String [] result = new String [all.length];
		for(int i = 0;i < all.length ;i++) {
			String re = all[i][0];
			for(int j = 1;j < all[i].length ;j++) {
				re += "," + all[i][j];
			}
			result[i] = re;
		}
		return result;
	}
	//ԭ�ϳ�����Ϣ
	//����
	public static String getMatOutInfoViapc(String pc) {
		String [] parms = {pc};
		String [][] all = conn.selectTable(null, parms, "getMatOutInfoViapc");
		if(all == null|| all.length == 0)return null;
		String re = all[0][0];
		for(int i = 1;i < all[0].length ;i++) {
			re += ","+all[0][i];
		}
		return re;
	}
	//���ݸ�����
		public static String[] getMatOutInfoViaFzr(String fzr) {
			String [] parms = {fzr};
			String [][] all = conn.selectTable(null, parms, "getMatOutInfoViaFzr");
			if(all == null|| all.length == 0)return null;
			String [] result = new String [all.length];
			for(int i = 0;i < all.length ;i++) {
				String re = all[i][0];
				for(int j = 1;j < all[i].length ;j++) {
					re += "," + all[i][j];
				}
				result[i] = re;
			}
			return result;
		}
		//����ʱ��
		public static String [] getMatOutInfoViaTimeper(String cj,String begin,String end) {
			String [] parms = {cj,begin,end};
			String [][] all = conn.selectTable(null, parms, "getMatOutInfoViaTime");
			if(all == null|| all.length == 0)return null;
			String [] result = new String [all.length];
			for(int i = 0;i < all.length ;i++) {
				String re = all[i][0];
				for(int j = 1;j < all[i].length ;j++) {
					re += "," + all[i][j];
				}
				result[i] = re;
			}
			
			return result;
		}
		public static String[] getMatOutInfoViaTime(String cj,String begin,String end) {
			if(!cj.equals("ȫ��")) return getMatOutInfoViaTimeper(cj,begin,end);
			else {
				int [] cjid = WorkSystem.selectAllWork();
				ArrayList<String> row = new ArrayList<String>();
				for(int i = 0;i < cjid.length;i++) {
					String [] per = getMatOutInfoViaTimeper(cjid[i]+"",begin,end);
					if(per == null) continue;
					for(int j = 0 ;j < per.length ;j++) {
						row.add(per[j]);
					}
				}
				if(row.size() == 0)return null;
				String [] result = new String[row.size()];
				for(int i = 0;i < row.size(); i++){
					result[i] = row.get(i);
				}
				return result;
			}
		}
		
		//ԭ�����ټ�¼
		public static String[] getMatDelInfoViaFzr(String fzr) {
			String [] parms = {fzr};
			String [][] all = conn.selectTable(null, parms, "getMatDelViaFzr");
			if(all == null|| all.length == 0)return null;
			String [] result = new String [all.length];
			for(int i = 0;i < all.length ;i++) {
				String re = all[i][0];
				for(int j = 1;j < all[i].length ;j++) {
					re += "," + all[i][j];
				}
				result[i] = re;
			}
			return result;
		}
		//����ʱ��
		public static String [] getMatDelInfoViaTimeper(String ck,String begin,String end) {
			String [] parms = {ck,begin,end};
			String [][] all = conn.selectTable(null, parms, "getMatDelViaTime");
			if(all == null|| all.length == 0)return null;
			String [] result = new String [all.length];
			for(int i = 0;i < all.length ;i++) {
				String re = all[i][0];
				for(int j = 1;j < all[i].length ;j++) {
					re += "," + all[i][j];
				}
				result[i] = re;
			}
			
			return result;
		}
		public static String[] getMatDelInfoViaTime(String ck,String begin,String end) {
			if(!ck.equals("ȫ��")) return getMatOutInfoViaTimeper(ck,begin,end);
			else {
				Integer [] ckid = MaterialSystem.selectAllStorageId();
				ArrayList<String> row = new ArrayList<String>();
				for(int i = 0;i < ckid.length;i++) {
					String [] per = getMatDelInfoViaTimeper(Integer.toString(ckid[i]),begin,end);
					if(per == null) continue;
					for(int j = 0 ;j < per.length ;j++) {
						row.add(per[j]);
					}
				}
				if(row.size() == 0)return null;
				String [] result = new String[row.size()];
				for(int i = 0;i < row.size(); i++){
					result[i] = row.get(i);
				}
				return result;
			}
		}
		
		//������
				public static String[][] getJinHuoFlow() {
					String sql = 
						"select jinhuoID,YMONEY*Ynum, YMONEY*Ynum,Ytime from YuanLiaoJinHuo";
					return addFlowType(conn.get(sql));
				}
				public static String[][] getBuyMoneySum(String t1, String t2) {
					String sql = 
							"select jinhuoID,YMONEY*Ynum, YMONEY*Ynum,Ytime from YuanLiaoJinHuo"
							+ " where Ytime >= '"+t1+"' and Ytime <= '"+t2+"'";
					return conn.get(sql);
				}
				//����ϵͳ��Ա����
				public static String[][] getFlow(String yuangongID) {
					String sql = 
						"select jinhuoID,YMONEY*Ynum, YMONEY*Ynum,Ytime from YuanLiaoJinHuo"
						+ " where yuangongID = '" + yuangongID +"'";
					
					return conn.get(sql);
				}
				//����ʱ��ɸѡ������
				public static String[][] getDingDanFlow(String format, String format2) {
					String sql = 
							"select jinhuoID,YMONEY*Ynum, YMONEY*Ynum,Ytime"
							+ " from YuanLiaoJinHuo where Ytime >= '"+format+"' and Ytime <= '"+format2+"'";
						
					return addFlowType(conn.get(sql));
				}
				//������۶����ֶ�
				private static String[][] addFlowType(String[][] s) {
					if(s == null) return null;
					String[][] res = new String[s.length][s[0].length+1];
					for(int i = 0; i < s.length; i++) {
						for(int j = 0; j < s[i].length; j++)
							res[i][j] = s[i][j];
						res[i][s[i].length] = "ԭ�϶���";
					}
					return res;
				}
				
				public static String[][] GongHuoShangSelectAllGongHuoShang() {
					String sql = "select * from GongHuoShang";
					return conn.get(sql);
				}
				public static String[][] GongHuoShangSelectGongHuoShang(
						Integer integer, String string) {
					String sql = "select * from GongHuoShang ";
					Boolean first = true;
					if(integer != null && integer.toString().length() > 0) {
						if(first) {
							sql += " where ";
							first = false;
						}
						sql += " gonghuoshangID = " + integer.toString();
					}
					if(string != null &&  string.length() > 0 ) {
						if(first) {
							sql += " where ";
							first = false;
						}
						else {
							sql += " and ";
						}
						sql += "  fuzerenNAME = '" + string +"'";
					}
					System.out.println(sql);
					return conn.get(sql);
				}
}
