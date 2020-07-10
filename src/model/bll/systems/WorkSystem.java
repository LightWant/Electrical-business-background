package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
public class WorkSystem implements SystemFather  {
	//private static Connsql conn;
	static Map<Integer, String> product;
	static Map<Integer, String> capacity;
	static Map<String,String>property = new HashMap<String,String>();
	@SuppressWarnings("unused")
	private static Staff staff;

	static {
		product = new HashMap<Integer,String>();
		capacity = new HashMap<Integer,String>();
		String[] goalParms = {"chejianID","chejianchanpingID"};
		String[] parms = null;
		String[] values = null;
		
		String[][]cjstr = conn.select(goalParms, parms, values, "CheJian");
		goalParms[1] = "chejianchanneng";
		String[][]cjcn = conn.select(goalParms, parms, values, "CheJian");
		for(int i = 0 ;i<cjstr.length;i++) {
			product.put(Integer.parseInt(cjstr[i][0]),cjstr[i][1]);
			capacity.put(Integer.parseInt(cjcn[i][0]),cjcn[i][1]);
		}
		property.put("������","chejianID");
		property.put("�������α��","chejianzhurenID");
		property.put("�����Ʒ���","chejianchanpingID");
		property.put("�������","chejianchanneng");
		property.put("����״̬","chejianSTAGE");
	}
	//ת�������ݿ�������
	public static String [] toSqlProperty(String [] parms) {
		String [] result = new String[parms.length];
		for(int i = 0;i<parms.length;i++) {
			result[i] = property.get(parms[i]);
		}
		return result;
	}
	
	public void setStaff(Staff staff) {
		WorkSystem.staff = staff;
	}
	
	//pass�������Ըı�ʱ���²�Ʒ�Ͳ�������
	public static void refresh() {
		String[] goalParms = {"chejianID","chejianchanpingID"};
		String[] parms = null;
		String[] values = null;
		
		String[][]cjstr = conn.select(goalParms, parms, values, "CheJian");
		goalParms[1] = "chejianchanneng";
		String[][]cjcn = conn.select(goalParms, parms, values, "CheJian");
		for(int i = 0 ;i<cjstr.length;i++) {
			product.put(Integer.parseInt(cjstr[i][0]),cjstr[i][1]);
			capacity.put(Integer.parseInt(cjcn[i][0]),cjcn[i][1]);
		}
	}
	//ͨ�����ԣ���������
	public int work(int chanpinID,int num,int zjyID,Date time, int wno) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(time);
		String cpidstr = Integer.toString(chanpinID);
		String numstr=  Integer.toString(num);
		String zjyidstr = Integer.toString(zjyID);
		String cjid = Integer.toString(wno);
		String [] parms = {"zhijianyuanID","chanpinID","shengchanNUM","shengyuNUM","shengchanTIME","chejianID"};
		String [] values = {zjyidstr,cpidstr,numstr,numstr,timestr,cjid};
		conn.insert(parms, values, "ShengChan"); 
		int scpc = findSCPC(chanpinID,timestr);
		//System.out.println("get ��������Ϊ:"+scpc);
		//�������ϸ�ڱ�
		workUseMaterial(scpc,chanpinID,num, wno);
		return scpc;//������������
	}
	//ͨ�����ԣ���������ϸ�����
	public void workUseMaterial(int scpc,int cpID,int num, int wno) {
		//System.out.println("WorkUseMaterial");
		String []peifang = ProductSystem.findPeiFang(cpID);
		String [] ylstr = peifang[0].split(",");
		String [] ylnumstr = peifang[1].split(",");
		int cjID = wno;
		//int [] yl = new int[ylstr.length];
		//int [] ylnum = new int [ylnumstr.length];
		for(int i = 0;i<ylstr.length;i++) {
			int ylID = Integer.parseInt(ylstr[i]);
			int ylnum = Integer.parseInt(ylnumstr[i])*num;
			//System.out.println(""+ylID+""+ylnum);
			conn.executeUYL(scpc,ylID,ylnum,cjID);//��ÿһ���䷽�е�ԭ�϶�ִ��useMaterial�洢����
		}
		
	}
	//pass�������г���id 
	public static int[] selectAllWork() {
		String []goal = {"chejianID"};
		String [][]result = conn.select(goal, null, null, "CheJian");
		int [] ids = new int[result[0].length];
		for(int i = 0;i<result[0].length;i++) {
			ids[i] = Integer.parseInt(result[0][i]);
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
	public int findSCPC(int cpID,String timestr){
		String cpidstr = Integer.toString(cpID);
		String []goalParms = {"shengchanpiciID"};
		String []parms = {"chanpinID","shengchanTIME"};
		String []values = {cpidstr,timestr};
		String [][] result = conn.select(goalParms, parms, values, "ShengChan");
		return Integer.parseInt(result[0][0]);
	}
	//pass�޸�ĳ��������
	public void changeWork(int ID,String[] set,String[] setvalues) {
		String[] parms = {"chejianID"};
		String [] setparms = toSqlProperty(set);
		String[] values = {Integer.toString(ID)};
		conn.update(setparms, setvalues, parms, values, "CheJian");//����ĳԭ����Ϣ
		//���²��ܺͲ�Ʒ
		WorkSystem.refresh();
	}
	
	public static int chanNeng(String chanpinID) {
		return 0;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		WorkSystem ws = new WorkSystem(new Staff(1,"123456"),1);
//		int [] plan = PlanSystem.dayPlan(ws.wno);
//		for(int i = 0;i<plan.length;i++) {
//			System.out.println(plan[i]);
//		}
//		//Date time = new Date();
//		//ws.work(2, 10, 2,time);
//		//String []set = {"�������","����״̬"};
//		//String [] setvalues = {"200,200,500","2"};
//		//ws.changeWork(1, set, setvalues);
//		//String [] result = WorkSystem.selectInfo(1,set);
//		//for(int i = 0;i<result.length;i++) {
//		//	System.out.println(result[i]);
//		//}
//		//System.out.println(capacity.get(1));
//		//System.out.println(product.get(1));
//	}
}
