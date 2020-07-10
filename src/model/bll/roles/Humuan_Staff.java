package model.bll.roles;

import java.util.Date;

import model.bll.objects.Staff;
import model.bll.systemfactory.HumanFactory;
import model.bll.systems.HumanSystem;

// 人事管理子系统-员工
public class Humuan_Staff extends Role {
	private Staff staff;
	
	void Human_Staff(Integer id, String password) {
		staff = new Staff(id, password);
		staff.check();
		HumanFactory fac = new HumanFactory();
		addPosition("Human", fac.getFactory(staff));
	}
	
	//添加员工
	public int addWorker(String name,
			String sex,String zw,String sfzid,
			String tel,String mail,String addr,Date rztime,
			String state,String password) {
		HumanSystem sys = (HumanSystem) map.get("Human");
		return sys.addWorker(name, sex, zw, sfzid,
				tel, mail, addr, rztime, state, password);
	}

	//删除员工，不直接删除，将在岗状态转为离职即可
	public void dismiss(int ID) {
		((HumanSystem) map.get("Human")).dismiss(ID);
	}
	
	//管理员修改员工信息
	public void changeInfo(int ID,
			String[]goal,String[]goalvalues) {
		((HumanSystem) map.get("Human")).changeInfo(ID, goal, goalvalues);
	}
	
	//授予角色属性
	public int addRole(int ID,int role) {
		return ((HumanSystem) map.get("Human")).addRole(ID, role);
	}
	
	// 对该部门的员工信息进行查看
	public String[][] search(String systemName) {
		return ((HumanSystem) map.get("Human")).search(systemName);
	}
}
