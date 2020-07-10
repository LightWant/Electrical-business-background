package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
public class HumanSystem implements SystemFather {
	static Staff staff;
	//static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String> property = new HashMap<String,String>();
	static Map<String,Float> salary = new HashMap<String,Float>();
	static {
		property.put("Ա�����","ID");
		property.put("Ա������","mingcheng");
		property.put("Ա���Ա�","sex");
		property.put("Ա��ְλ","zhiwei");
		property.put("Ա�����֤��","shenfenzhengID");
		property.put("Ա���ֻ���","shouji");
		property.put("Ա������","mail");
		property.put("Ա��סַ","zhuzhi");
		property.put("Ա����ְ����","ruzhiTIME");
		property.put("Ա���ڸ�״̬","zaigangSTAGE");
		property.put("Ա������","gongzi");
		property.put("Ա������","pass");
		
		//���ʺ�ְλ�Ķ�Ӧ��ϵ��û��
		
	}
	public static String[] toSqlProperty(String[] str) {
		String [] result = new String [str.length];
		for(int i = 0;i<str.length;i++) {
			result[i] = property.get(str[i]);
		}
		return result;
	}
	public void setStaff(Staff staff) {
		HumanSystem.staff = staff;
	}
	//���Ա��
	public int addWorker(String name,String sex,String zw,String sfzid,String tel,String mail,String addr,Date rztime,String state,String password) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(rztime);
		String parm[] = {"mingchen","sex","zhiwei","shenfenzhengID","shouji","mail","zhuzhi","ruzhiTIME","zaigangSTAGE","gongzi","pass"};
		String value[] = {name,sex,zw,sfzid,tel,mail,addr,timestr,state,Float.toString(salary.get(zw)),password};
		conn.insert(parm, value, "YuanGong");
		return findID(parm,value);
	}
	//ɾ��Ա������ֱ��ɾ�������ڸ�״̬תΪ��ְ����
	public void dismiss(int ID) {
		String [] goal = {"zaigangSTAGE"};
		String [] goalvalue =  {"��ְ"};
		
		changeInfo(ID,goal,goalvalue);
	}
	//����Ա�޸�Ա����Ϣ
	public void changeInfo(int ID,String[]goal,String[]goalvalues) {
		
		String goalparms [] = toSqlProperty(goal);
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		conn.update(goalparms, goalvalues, parms, values,"YuanGong");
		
	}
//�Լ��޸��Լ���Ϣ
	//�����ɫ����
	public int addRole(int ID,int role) {
		String []parms = {"yuangongID","jueseID"};
		String []values = {Integer.toString(ID),Integer.toString(role)};
		String [][] result = conn.select(parms, parms, values, "YuanGongJueSe");
		if(result==null||result.length==0) {
			conn.insert(parms, values, "YuanGongJueSe");
			return 1;
		}
		else return 0;//��ʾ�Ѿ��иý�ɫ��
	}
	
	//���������ζԸò��ŵ�Ա����Ϣ���в鿴
	public String[][] search(String systemName) {
		String sql = "select * from YuanGong where zhiwei like '"+systemName+"%'";
		return conn.get(sql);
	}
	
	private static int findID(String[]parms,String[] values) {
		String [] goalParms = {"ID"};
		String [][]result = conn.select(goalParms, parms, values, "YuanGong");
		return Integer.parseInt(result[0][0]);
	}
}
