package model.bll.systems;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import model.bll.objects.Staff;
import model.dal.FileIO;

public class SoldSystem implements SystemFather   {
	static Staff staff;
	//static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	static Map<String,String>property = new HashMap<String,String>();
	
	public SoldSystem() {
		property.put("�������", "dingdanID");
		property.put("�ͻ����", "kehuID");
		property.put("�µ�ʱ��", "baozhiqi");
		property.put("�˻�ʱ��", "dabaoguige");
		property.put("����״̬", "danwei");
		property.put("Ӧ�����", "cangkuID");
		property.put("ʵ�����", "cangkuzhurenID");
		property.put("��Ʒ���", "cangkuSTATE");
		property.put("��Ʒ����", "cangkuTYPE");
		property.put("�˻�ʱ��", "cangkuSIZE");
		property.put("�˻�����", "leftSIZE");

	}
	
	public void setStaff(Staff staff) {
		SoldSystem.staff = staff;
	}
	
	public String CreateOrder(String kehuID, String chanpinID,
			String chanpinNum,String dingdanSTATE,
			String tihuoTime, String xiadanTime,
			float yingfuMoney, float shifuMoney) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		tihuoTime = format.format(tihuoTime);
		xiadanTime = format.format(xiadanTime);
		
		int numParms = 6;
		String tableName = "DingDan";
		
		String parms[] = new String[numParms];
		parms[0] = "kehuID";
		parms[1] = "xiadanTime";
		parms[2] = "tihuoTime";
		parms[3] = "dingdanSTATE";
		parms[4] = "yingfuMONEY";
		parms[5] = "shifuMONEY";
		
		String values[] = new String[numParms];
		values[0] = kehuID;
		values[1] = xiadanTime;
		values[2] = tihuoTime;
		values[3] = dingdanSTATE;
		values[4] = Float.toString(yingfuMoney);
		values[5] = Float.toString(shifuMoney);
		
		//��Ϣ�Ƿ�����
		if(!judgeIfWrongMessage(parms, values, tableName)) {
			return "��Ϣ����";
		}
		//���ж��Ƿ�ӵ�
		if(!judgeIfAccept(parms, values, tableName, chanpinID)) {
			return "�޷��ӵ�";
		}
		
		conn.insert(parms, values, tableName);
		
		String [] goalParms = {"dingdanID"};
		String [][] result = conn.select(goalParms, parms, values, "YuanLiao");
		int dingdanID = Integer.parseInt(result[0][0]);
		
		parms = new String[3];
		parms[0] = "dingdanID";
		parms[1] = "chanpiniID";
		parms[2] = "chanpinNUM";
		tableName = "DingDanXiJie";
		
		values = new String[3];
		values[0] = Integer.toString(dingdanID);
		values[1] = chanpinID;
		values[2] = chanpinNum;
		
		conn.insert(parms, values, tableName);
		
		return "OK";
	}

	// �ж���Ϣ�Ƿ�����
	private boolean judgeIfWrongMessage(String[] parms, String[] values, String tableName) {
		
		return true;
	}

	private boolean judgeIfAccept(String[] parms, 
			String[] values, String tableName, String chanpinID) {
		// �õ��ò�Ʒ�ֿ��桢�������������������ĸò�Ʒ�Ķ����������ж��������Ƿ��㹻
		int kuCun = ProductSystem.Storage(Integer.parseInt(chanpinID));
		int shengChanLi = WorkSystem.chanNeng(chanpinID);
//		int kuCun = 0;
//		int shengChanLi = 0;
		int dingDanNum = productNum(values[0], values[1]);
		@SuppressWarnings("deprecation")
		long different = new Date(values[3]).getTime() - new Date(values[4]).getTime();
		
		different = different/(1000 * 60 * 60 * 24);
		
		if(kuCun + shengChanLi * different - dingDanNum >= 0)
			return true;
		return false;
	}

	// ĳ�ֲ�Ʒ������������
	private int productNum(String dingdanID, String chanpinID) {
		String parms[] = {"dingdanID", "dingdanSTATE", "chanpinID"};
		String values[] = {dingdanID, "������",chanpinID};
		String goalParms[] = {"chnapinNUM"};
		String tableName = "DingDanAndXiJie";
		int num = 0;
		
		String ret[][] = conn.select(goalParms, parms, values, tableName);
		
		if(ret == null || ret[0] == null || ret[0][0] == "NULL") {
			return 0;
		}
		for(int i = 0; i < ret[0].length; i++) {
			num += Integer.parseInt(ret[0][i]);
		}
		
		values[1] = "��ȡ��";
		ret = conn.select(goalParms, parms, values, tableName);
		if(ret == null || ret[0] == null || ret[0][0] == "NULL") {
			return 0;
		}
		for(int i = 0; i < ret[0].length; i++) {
			num += Integer.parseInt(ret[0][i]);
		}

		return num;
	}

	// ��ѯ
	public static String[][] select(String goalParms[], String parms[], String values[], String tableName) {
		return conn.select(goalParms, parms, values, tableName);
	}
	
	// Ԥ�����ۼƻ�
	public void salePlan() {
		String parms[] = {"dingdanSTATE"};
		String values[] = {"���"};
		String goalParms[] = {"chanpinID", "chnapinNUM"};
		String tableName = "DingDanAndXiJie";
		
		String ret[][] = conn.select(goalParms, parms, values, tableName);
		Map<String, Integer> map = new HashMap<String, Integer>();
		Map<String, Integer> map2 = new HashMap<String, Integer>();
		
		for(int i = 0; i < ret.length; i++) {
			if(map.containsKey(ret[i][1])) {
				Integer tmp = map.get(ret[i][1]);
				map.put(ret[i][1], tmp + 1);
				
				Integer tmp2 = map.get(ret[i][1]);
				map2.put(ret[i][1], tmp2 + 1);
			}
			else {
				map.put(ret[i][1], 1);
				map2.put(ret[i][1], 1);
			}
		}
		
		int i = 0;
		String plan[] = new String[map.size() + 1];
		Iterator<String> it = map.keySet().iterator();
		Iterator<String> it2 = map2.keySet().iterator();
		
        while(it.hasNext()){                         //������Iterator������**
            //�õ�ÿһ��key
            String key = it.next();
            it2.next();
            //ͨ��key��ȡ��Ӧ��value
            Integer value = map.get(key);
            Integer value2 = map2.get(key);
            
            int v = value.intValue();
            int v2 = value2.intValue();
            
            v = (int) (v * 1.2 / v2);
            value = v;
            plan[i] = key + " : " + value.toString();
            i = i + 1;
        }
        plan[i] = "0";

		try {
			FileIO.writeAlllines("salePlan.txt", plan);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ������Ȩ
	public boolean authorize() {
		String s[] = null;
		try {
			s = FileIO.readAlllines("salePlan.txt");
		} catch (IOException e) {
			return false;
		}
		
		if(s[s.length-1].equals("1")) {
			return true;
		}
		
		if(staff.getZhiwei().equals("�ܾ���")) {
			s[s.length-1] = "1";
			try {
				FileIO.writeAlllines("salePlan.txt", s);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		else {
			return false;
		}
	}
}
