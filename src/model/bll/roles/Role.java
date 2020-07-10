package model.bll.roles;

import java.util.Map;

import model.bll.systems.SystemFather;

public abstract class Role {
	// 该角色子系统集合
	protected Map<String, SystemFather> map;
	void addPosition(String key, SystemFather sys) {
		map.put(key, sys);
	}
}
