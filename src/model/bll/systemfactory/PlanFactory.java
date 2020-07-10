package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.*;

public class PlanFactory implements SystemFactory{
	public SystemFather getFactory(Staff staff) {
		SystemFather factory = new PlanSystem();
	
		factory.setStaff(staff);
		
		return factory;
	}
}
