package model.bll.systemfactory;

import model.bll.objects.Staff;
import model.bll.systems.SystemFather;

public interface SystemFactory {
	SystemFather getFactory( Staff staff);
}
