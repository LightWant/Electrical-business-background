package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.*;

public class WorkFactory implements SystemFactory{
	public SystemFather getFactory(Staff staff) {
		SystemFather factory = new WorkSystem();
		factory.setStaff(staff);

		return factory;
	}
}
