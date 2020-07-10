package model.bll.systems;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import model.bll.objects.Staff;
import model.dal.*;
import model.dal.Configuration;

public class PlanSystem implements SystemFather {
	@SuppressWarnings("unused")
	private static Staff staff;
	public static Connsql conn = new Connsql(Configuration.user,Configuration.password);
	
	public void setStaff(Staff staff) {
		PlanSystem.staff = staff;
	}
	//���ҵ�ǰ��ִ�е������ƻ�������ͨ��
	public static String[] findProductPlan() {
		String[] goalParms = {"chanpinID","chanpinNUM","leftday"};
		String[] parms = null;
		String[][]result = conn.selectTable(goalParms, parms, "findNowPlan");
		return result[0];
	}
	//pass,����workno����ĵ��������ƻ�
	public static int[] dayPlan(int workno) {
		String[] goalParms = {"jihuachanpinNUM"};
		String[] parms = {"chejianID","Ctime"};
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String timestr = format.format(date);
		String[] values = {Integer.toString(workno),timestr};
		String[][]result = conn.select(goalParms, parms, values, "CheJianRiJiHua");
		if(result != null&&result.length>0) {
			String[]re = result[0][0].split(",");
			int [] plan = new int[re.length];
			for(int i = 0;i<re.length;i++) {
				plan[i] = Integer.parseInt(re[i]);
			}
			return plan;
		}
		
		String []allplan = findProductPlan();
		String []proid;
		String []pronum;
		proid = allplan[0].split(",");//ȫ���ƻ���Ʒ��str
		pronum = allplan[1].split(",");//ȫ���ƻ���Ʒ��Ӧ��num str
		int leftday = Integer.parseInt(allplan[2]);//�ƻ���Ӧ��ʣ������

		Map<Integer,Integer>idleftnum = new HashMap<Integer,Integer>();
		for(int i = 0;i<proid.length;i++) {
			int id = Integer.parseInt(proid[i]);
			int num = Integer.parseInt(pronum[i]);
			int leftavenum = (num - ProductSystem.Storage(id))/leftday+1;//id[i]��Ʒ��������������ȥ��ǰ���
			idleftnum.put(id,leftavenum);
		}
		String castr = WorkSystem.capacity.get(workno);
		String[] castrs = castr.split(",");//�ó���Ĳ���str
		String prod = WorkSystem.product.get(workno);
		String [] prods = prod.split(",");//�ó���Ĳ�Ʒstr
		int [] plan = new int[castrs.length];//�ó��䵱�յļƻ�
		String plannum = "";
		//int index = 0;
		for(int i = 0;i<prods.length;i++) {
			int nowid = Integer.parseInt(prods[i]);
			if(idleftnum.containsKey(nowid)) {
				int cap = Integer.parseInt(castrs[i]);
				plan[i] = idleftnum.get(nowid)>cap?cap:idleftnum.get(nowid);//Ӳ����ƽ���������ڲ��ܵĻ������ܣ�����average
				plannum += Integer.toString(plan[i]);
				
			}
			else {
				plan[i] = 0;
				plannum += "0";
			}
			if(i< prods.length-1)
				plannum += ",";
		}
		//sql����ó��䵱�ռƻ�
		String [] para = {"chejianID","Ctime","jihuachanpinNUM"};
		String [] value = {Integer.toString(workno),timestr,plannum};
		conn.insert(para, value, "CheJianRiJiHua");
		return plan;
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}

}
