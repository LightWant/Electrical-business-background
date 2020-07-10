package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.CallStack;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

public class HumanSystem implements SystemFather{
	Staff staff;
	static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String> property = new HashMap<String,String>();
	//static Map<String,Float> salary = new HashMap<String,Float>();
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
		
		//工资和职位的对应关系还没加,手动输入吧 不加了
		
	}
	public HumanSystem() {
		
	}
	public void setStaff(Staff staff) {
		this.staff = staff;
	}
	static String[] toSqlProperty(String[] str) {
		String [] result = new String [str.length];
		for(int i = 0;i<str.length;i++) {
			result[i] = property.get(str[i]);
		}
		return result;
	}
	public HumanSystem(Staff staff) {
		this.staff = staff;
	}
	//添加员工
	public static int addWorker(String name,String sex,String zw,String sfzid,String tel,String mail,String addr,Date rztime,String state,String password,float gongzi) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(rztime);
		String parm[] = {"mingchen","sex","shenfenzhengID","shouji","mail","zhuzhi","ruzhiTIME","zaigangSTAGE","pass","gongzi"};
		String value[] = {name,sex,sfzid,tel,mail,addr,timestr,state,password,Float.toString(gongzi)};
		int flag = conn.insert(parm, value, "YuanGong");
		if(flag<0)return flag;
		int newid = findID(parm,value);
		String parms[]= {"yuangongID","jueseID"};
		String [] values = {""+newid,zw};
		flag = conn.insert(parms, values, "YuanGongJueSe");
		if(flag<0)return flag;
		return newid;
	}
	//删除员工，不直接删除，将在岗状态转为离职即可
	public void dismiss(int ID) {
		String [] goal = {"za igangSTAGE"};
		String [] goalvalue =  {"离职"};
		
		changeInfo(ID,goal,goalvalue);
	}
	//管理员修改员工信息
	public static boolean changeInfo(int ID,String[]goal,String[]goalvalues) {
		
		String goalparms [] = toSqlProperty(goal);
		String [] parms = {"ID"};
		String [] values = {Integer.toString(ID)};
		return conn.update(goalparms, goalvalues, parms, values,"YuanGong");
		
	}
	//查询某员工额信息
	public static String [] findInfo(int id) {
		String [] parms = {"ID"};
		String [] values = {Integer.toString(id)};
		String [][] result =conn.select(null, parms, values, "yuangong");
		if(result == null || result.length == 0) {
			return null;
		}
		else {
			return result[0];
		}
	}
//自己修改自己信息
	//授予角色属性,待修改
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
	//删除某员工的角色
	public int deleteRole(int ID,int role) {
		String []parms = {"yuangongID","jueseID"};
		String []values = {Integer.toString(ID),Integer.toString(role)};
		String [][] result = conn.select(parms, parms, values, "YuanGongJueSe");
		if(result==null||result.length==0) {
			
			return 0;//表示该员工本来就没有该角色
		}
		conn.delete(parms, values, "YuanGongJueSe");
		return 1;
	}
	
	public String[][] search(String systemName) {
		String sql = "select * from YuanGong where zhiwei like '"+systemName+"%'";
		return conn.get(sql);
	}
	
	public static int findID(String[]parms,String[] values) {
		String [] goalParms = {"ID"};
		String [][]result = conn.select(goalParms, parms, values, "YuanGong");
		return Integer.parseInt(result[0][0]);
	}
	public static Integer [] findAllStaff() {
		String [] goalParms = {"ID"};
		String [] parms = null;
		String [] values = null;
		String [][] result = conn.select(goalParms, parms, values, "YuanGong");
		if(result == null || result.length==0) {
			
			return null;
		}
		else {
			Integer [] ids = new Integer[result.length];
			for(int i = 0;i < result.length;i++) {
				ids[i] = Integer.parseInt(result[i][0]);
			}
			return ids;
 		}
	}
	//
	public static String findName(Integer id) {
		System.out.println("查询员工id:"+id);
		String [] goalParms = {"mingchen"};
		String [] parms = {"ID"};
		String idstr = Integer.toString(id);
		String [] values = {idstr};
		String [][] result = conn.select(goalParms, parms, values, "YuanGong");
		if(result == null||result.length == 0)
			return null;
		else {
			return result[0][0];
		}
	}
	//查找所有的角色
	public static String[][] selectAllRole() {
		String [] parms = {"jueseID","jueseNAME"};
		String [][] result = conn.select(parms, null, null, "JueSe");
		if(result == null || result.length == 0)return null;
		return result;
	}
	//查找所有的权限
	public static String [][] selectAllFunction(){
		String [] parms = {"quanxianID","quanxianNAME"};
		String result [][] = conn.select(parms, null, null, "QuanXian");
		if(result == null || result.length == 0)return null;
		return result;
	}
	//修改员工角色
	public static boolean changeRole(String [] info) {
		String ygid = info[0];
		String [] parms = {"yuangongID"};
		String [] values = {ygid};
		if(!conn.delete(parms, values, "YuanGongJueSe")) return false;
		if(info != null && info.length > 1) {
			for(int i = 1;i < info.length ;i++) {
				String [] parm = {"yuangongID","jueseID"};
				String [] value = {ygid,info[i]};
				if(conn.insert(parm, value, "YuanGongJueSe") < 0)
					return false;
			}
		}
		
		return true;
	}
	//修改角色权限
	public static boolean changeRoleInfo(String [] info) {
		String js = info[0];
		String [] parms = {"jueseID"};
		String [] values = {js};
		if(!conn.delete(parms, values, "QuanXianFenPei")) return false;
		if(info.length<=1) return false;
		for(int i = 1;i < info.length ;i++) {
			String [] parm = {"jueseID","quanxianID"};
			String [] value = {js,info[i]};
			if(conn.insert(parm, value, "QuanXianFenPei")<0)return false;
		}
		return true;
		
	}
	
	//获得去权限
	public static String[][] getRoles(Integer id) {
		String sql  = "select jueseID from YuanGongJueSe where yuangongID ="+id.toString();
		String sql1 = "select quanxianID from QuanXianFenPei where jueseID in ("+sql+")";
		String sql2 = "select daquanxianNAME,quanxianURL,quanxianNAME from QuanXian where quanxianID in ("+sql1+") "
				+ " order by daquanxianNAME";
		System.out.println(sql2);
		return conn.get(sql2);
	}
	
	
	public static String [][] getRoleInfo(String roleid) {
		String sql = "select jueseNAME,quanxianNAME " + 
				"from JueSe,QuanXianFenPei,QuanXian " + 
				"where JueSe.jueseID = QuanXianFenPei.jueseID and QuanXianFenPei.quanxianID = QuanXian.quanxianID " + 
				"and JueSe.jueseID = " + roleid;
		String [][] result = conn.get(sql);
		if(result == null || result.length == 0)return null;
		else return result;
	}



	//所有员工ID
	public static String[] selectAllId() {
		String sql = "select ID from YuanGong";
		String ret[][] = conn.get(sql);
		String[] res = new String[ret.length];
		
		for(int i = 0; i < res.length; i++) res[i] = ret[i][0];
		return res;
	}
}
