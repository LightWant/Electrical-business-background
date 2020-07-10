package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.*;

public class ProductFactory implements SystemFactory{
	public SystemFather getFactory(Staff staff) {
		SystemFather factory = new ProductSystem();
		factory.setStaff(staff);
		return factory;
	}
}
