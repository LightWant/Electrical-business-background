package model.bll.roles;

import java.util.Map;

import model.bll.systems.SystemFather;

public abstract class Role {
	// �ý�ɫ��ϵͳ����
	protected Map<String, SystemFather> map;
	void addPosition(String key, SystemFather sys) {
		map.put(key, sys);
	}
}
