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
	//查找当前在执行的生产计划，测试通过
	public static String[] findProductPlan() {
		String[] goalParms = {"chanpinID","chanpinNUM","leftday"};
		String[] parms = null;
		String[][]result = conn.selectTable(goalParms, parms, "findNowPlan");
		return result[0];
	}
	//pass,生成workno车间的当日生产计划
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
		proid = allplan[0].split(",");//全部计划产品的str
		pronum = allplan[1].split(",");//全部计划产品对应的num str
		int leftday = Integer.parseInt(allplan[2]);//计划对应的剩余天数

		Map<Integer,Integer>idleftnum = new HashMap<Integer,Integer>();
		for(int i = 0;i<proid.length;i++) {
			int id = Integer.parseInt(proid[i]);
			int num = Integer.parseInt(pronum[i]);
			int leftavenum = (num - ProductSystem.Storage(id))/leftday+1;//id[i]产品总生产的数量减去当前库存
			idleftnum.put(id,leftavenum);
		}
		String castr = WorkSystem.capacity.get(workno);
		String[] castrs = castr.split(",");//该车间的产能str
		String prod = WorkSystem.product.get(workno);
		String [] prods = prod.split(",");//该车间的产品str
		int [] plan = new int[castrs.length];//该车间当日的计划
		String plannum = "";
		//int index = 0;
		for(int i = 0;i<prods.length;i++) {
			int nowid = Integer.parseInt(prods[i]);
			if(idleftnum.containsKey(nowid)) {
				int cap = Integer.parseInt(castrs[i]);
				plan[i] = idleftnum.get(nowid)>cap?cap:idleftnum.get(nowid);//硬座的平均数量大于产能的话按产能，否则按average
				plannum += Integer.toString(plan[i]);
				
			}
			else {
				plan[i] = 0;
				plannum += "0";
			}
			if(i< prods.length-1)
				plannum += ",";
		}
		//sql插入该车间当日计划
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
