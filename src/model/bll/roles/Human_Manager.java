package model.bll.roles;

import model.bll.objects.Staff;
import model.bll.systemfactory.HumanFactory;
import model.bll.systems.HumanSystem;

public class Human_Manager extends Role {
	private Staff staff;
	
	Human_Manager(Integer id, String password) {
		staff = new Staff(id, password);
		staff.check();
		HumanFactory fac = new HumanFactory();
		addPosition("Human", fac.getFactory(staff));
	}
	
	// 查询人员信息
	public String[][] search() {
		return ((HumanSystem) map.get("Human")).search("人事");
	}
}
