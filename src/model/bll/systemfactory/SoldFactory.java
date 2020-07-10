package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.*;

public class SoldFactory implements SystemFactory{
	public SystemFather getFactory(Staff staff) {
		SystemFather factory = new SoldSystem();
		factory.setStaff(staff);
		
		return factory;
	}
}
