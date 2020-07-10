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
	static String [] danwei = {"��","����","��","��"};
	public ProductSystem() {
		property.put("��Ʒ����", "mingchen");
		property.put("��Ʒ���", "ID");
		property.put("��Ʒ������", "baozhiqi");
		property.put("��Ʒ������", "dabaoguige");
		property.put("��Ʒ����", "jiage");
		property.put("��Ʒ��λ", "danwei");
		property.put("�ֿ����", "cangkuleibie");
		property.put("�ֿ�����ID", "cangkuzurenID");
		property.put("�ֿ�״̬", "cangkuzhuangtai");
		property.put("�ֿ�������", "cangkurongliang");
		property.put("�ֿ�ʣ������", "shengyurongliang");
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
	//ͨ������ ����²�Ʒ
	public int addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi,String dw) {
		Map<Integer,String> result = selectAllProduct();
		Object[] keys = result.keySet().toArray();
		for(int i = 0;i<keys.length;i++) {
			if(name.trim() == result.get(keys[i])) return -1;//��ʾ����
		}
		boolean find = false;
		for(int i =0;i<danwei.length;i++) {
			if(dw.trim()==danwei[i]) {
				find = true;
				break;
			}
		}
		if(!find) {
			return -2;//��ʾ��λ���Ϲ�
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
		//�����Ʒ��
		String []parms = {"mingchen","dabaoguige","jiage","baozhiqi","danwei"};
		String []values = {name,gg,money,bzq,dw};
		conn.insert(parms, values, "ChanPin");
		String [] goalParms = {"ID"};
		String [][]cpid = conn.select(goalParms, parms, values, "ChanPin");
		int id = Integer.parseInt(cpid[0][0]);
		//�����䷽��
		String []parm = {"chanpinID","yuanliaoID","yuanliaoNUM"};
		String idstr = Integer.toString(findProduct(name));
		String[]value = {idstr,ylstr,ylnum};
		conn.insert(parm, value,"PeiFang");
		return id;
	}
	//ͨ�����ԣ���Ʒ���
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
	//ͨ�����ԣ���Ʒ����
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
	//���Բֿ�Ĳ�Ʒ���٣��ݲ����ԣ��߼���ԭ��һ��
	public void destroyFromStorage(int rukupiciID,Date time,int num) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"rukupiciID","xiaohuiTIME","xiaohuiNUM"};
		String []values = {Integer.toString(rukupiciID),timestr,Integer.toString(num)};
		conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
	}
	//������������Ĳ�Ʒ���٣��������ϸ񣩣��ݲ����ԣ��߼���ԭ��һ��
	public void destroyFromWork(int scpc,Date time,int num) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String []parms = {"shengchanpiciID","xiaohuiTIME","xiaohuiNUM"};
		String []values = {Integer.toString(scpc),timestr,Integer.toString(num)};
		conn.insert(parms,values,"ChanPinXiaoHuiJiLu");
	}
	//���ڲ�Ʒ��ʾ���ݲ����ԣ��߼���ԭ��һ��
	public String[][] expire() {
		//��ʱֻ��ʾ���ڵģ����ٹ��ڵĻ�δȷ��ʱ��
		//�����������ID��ԭ��ID,ԭ������
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "expireProduct");
		
	}
	//�������ڲ�Ʒ��ʾ���ݲ����ԣ��߼���ԭ��һ��
	public String[][] comingExpire(){
		//�����������ID����ƷID,��Ʒ����
		String[] goal = null;
		String[] values = null;
		return conn.selectTable(goal, values, "ComingExProduct");
	}
	//ͨ�����ԣ���ѯ��Ʒ���
	public static int Storage(int id) {
		String[]parms = {Integer.toString(id)};
		int result = conn.selectScale(parms, "ProductStorage");
		return result;
	}
	//��Ʒ����
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
	//5���ֿ�״̬�����ֿ�ʹ��������ֶ���ϵͳ�и��Ĳֿ�״̬������ֿ���Ϣ�鿴��״̬����ѯĳ��״̬�ֱ�����Щ�ֿ⡣
	public void changeStorageState(String state) {
		String[] setparms = {"cangkuzhuangtai"};
		String[] values = {state};
		conn.update(setparms,values,null,null,"ChanPinCangKu"); 
	}
	//6��pass ��Ʒ��Ϣ�޸ģ�������Ϣ���޸�
	public void changeProduct(int ID,String[] set,String[] setvalues) {
		String[] parms = {"ID"};
		String [] setparms = toSqlProperty(set);
		String[] values = {Integer.toString(ID)};
		conn.update(setparms, setvalues, parms, values, "ChanPin");//����ĳԭ����Ϣ
	}
	//pass����ĳ����Ʒ����ϢĳЩ
	public static String[] selectInfo(int ID,String[]goal) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "ChanPin");
		return result[0];
	}
	
	//pass����ĳ����Ʒ��ȫ����Ϣ
	public static String[] selectAllInfo(int ID) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "ChanPin");
		return result[0];
	}
	//pass����ĳ�ֿ�ĳЩ��Ϣ
	public static String[] selectStorageInfo(int ID,String[]goal) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [] goalparms = toSqlProperty(goal);
		String [][] result = conn.select(goalparms, parms, values, "ChanPinCangKu");
		return result[0];
	}
	
	//pass����ĳ�ֿ�������Ϣ
	public static String[] selectAllStorageInfo(int ID) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		String [][] result = conn.select(null, parms, values, "ChanPinCangKu");
		return result[0];
	}
	//7)ͨ�����ԣ���Ʒ�䷽�޸ģ�
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
	//pass ����������id
	public static int findProduct(String name) {
		String[]parms = {"mingchen"};
		String[]values = {name};
		String[]goal = {"ID"};
		String [][]result = conn.select(goal,parms, values,"ChanPin");
		return Integer.parseInt(result[0][0]);
	}
	//ͨ�����ԣ��䷽����
	public static String[] findPeiFang(int cpID) {
		String[]goalParms = {"yuanliaoID","yuanliaoNUM"};
		String[]parms = {"chanpinID"};
		String[]values = {Integer.toString(cpID)};
		String [][]result = conn.select(goalParms, parms, values, "PeiFang");
		return result[0];
	}
	//pass�������в�Ʒ���ơ�id
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
	//pass�������в�Ʒ�ֿ��id ���
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
	//ɾ��ԭ��
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
	//ɾ���ֿ�
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
//		String [] goal = {"�ֿ����"};
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
//		//1�����޸�״̬
//	}
//	
}
