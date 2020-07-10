package model.bll.roles;

import java.util.Date;

import model.bll.objects.Staff;
import model.bll.systemfactory.HumanFactory;
import model.bll.systems.HumanSystem;

// ���¹�����ϵͳ-Ա��
public class Humuan_Staff extends Role {
	private Staff staff;
	
	void Human_Staff(Integer id, String password) {
		staff = new Staff(id, password);
		staff.check();
		HumanFactory fac = new HumanFactory();
		addPosition("Human", fac.getFactory(staff));
	}
	
	//���Ա��
	public int addWorker(String name,
			String sex,String zw,String sfzid,
			String tel,String mail,String addr,Date rztime,
			String state,String password) {
		HumanSystem sys = (HumanSystem) map.get("Human");
		return sys.addWorker(name, sex, zw, sfzid,
				tel, mail, addr, rztime, state, password);
	}

	//ɾ��Ա������ֱ��ɾ�������ڸ�״̬תΪ��ְ����
	public void dismiss(int ID) {
		((HumanSystem) map.get("Human")).dismiss(ID);
	}
	
	//����Ա�޸�Ա����Ϣ
	public void changeInfo(int ID,
			String[]goal,String[]goalvalues) {
		((HumanSystem) map.get("Human")).changeInfo(ID, goal, goalvalues);
	}
	
	//�����ɫ����
	public int addRole(int ID,int role) {
		return ((HumanSystem) map.get("Human")).addRole(ID, role);
	}
	
	// �Ըò��ŵ�Ա����Ϣ���в鿴
	public String[][] search(String systemName) {
		return ((HumanSystem) map.get("Human")).search(systemName);
	}
}
