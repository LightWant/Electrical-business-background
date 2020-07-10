package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.*;

public class HumanFactory implements SystemFactory{
	public SystemFather getFactory( Staff staff) {
		SystemFather factory = new HumanSystem();
	
		factory.setStaff(staff);
		
		return factory;
	}
}
