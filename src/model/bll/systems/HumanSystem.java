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
		property.put("员工编号","ID");
		property.put("员工姓名","mingcheng");
		property.put("员工性别","sex");
		property.put("员工职位","zhiwei");
		property.put("员工身份证号","shenfenzhengID");
		property.put("员工手机号","shouji");
		property.put("员工邮箱","mail");
		property.put("员工住址","zhuzhi");
		property.put("员工入职日期","ruzhiTIME");
		property.put("员工在岗状态","zaigangSTAGE");
		property.put("员工工资","gongzi");
		property.put("员工密码","pass");
		
		//工资和职位的对应关系还没加
		
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
	//添加员工
	public int addWorker(String name,String sex,String zw,String sfzid,String tel,String mail,String addr,Date rztime,String state,String password) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(rztime);
		String parm[] = {"mingchen","sex","zhiwei","shenfenzhengID","shouji","mail","zhuzhi","ruzhiTIME","zaigangSTAGE","gongzi","pass"};
		String value[] = {name,sex,zw,sfzid,tel,mail,addr,timestr,state,Float.toString(salary.get(zw)),password};
		conn.insert(parm, value, "YuanGong");
		return findID(parm,value);
	}
	//删除员工，不直接删除，将在岗状态转为离职即可
	public void dismiss(int ID) {
		String [] goal = {"zaigangSTAGE"};
		String [] goalvalue =  {"离职"};
		
		changeInfo(ID,goal,goalvalue);
	}
	//管理员修改员工信息
	public void changeInfo(int ID,String[]goal,String[]goalvalues) {
		
		String goalparms [] = toSqlProperty(goal);
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		conn.update(goalparms, goalvalues, parms, values,"YuanGong");
		
	}
//自己修改自己信息
	//授予角色属性
	public int addRole(int ID,int role) {
		String []parms = {"yuangongID","jueseID"};
		String []values = {Integer.toString(ID),Integer.toString(role)};
		String [][] result = conn.select(parms, parms, values, "YuanGongJueSe");
		if(result==null||result.length==0) {
			conn.insert(parms, values, "YuanGongJueSe");
			return 1;
		}
		else return 0;//表示已经有该角色了
	}
	
	//各部门主任对该部门的员工信息进行查看
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
