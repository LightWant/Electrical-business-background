package model.bll.systems;

import model.bll.objects.Staff;
import model.dal.Configuration;
import model.dal.Connsql;

public interface SystemFather {
	final Connsql conn = new Connsql(Configuration.user,Configuration.password);
	void setStaff(Staff staff);
}
