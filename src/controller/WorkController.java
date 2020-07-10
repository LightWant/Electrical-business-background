package controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.bll.objects.*;
import model.bll.systems.*;
import model.dal.MD5;
@SessionAttributes("staff")
@Controller
public class WorkController {
	@RequestMapping(value="/Create",method=RequestMethod.GET)
	public String Create() {
		return "Create";
	}
	@RequestMapping(value="/dayPlan",method=RequestMethod.GET)
	public String dayPlan() {
		return "dayPlan";
	}
	@RequestMapping(value="/workInfo",method=RequestMethod.GET)
	public String workInfo() {
		return "workInfo";
	}
	@RequestMapping(value="/workInfoChange",method=RequestMethod.GET)
	public String infoChange() {
		return "workInfoChange";
	}
	@RequestMapping(value="/produceInfo",method=RequestMethod.GET)
	public String productInfo() {
		return "produceInfo";
	}
	
	@RequestMapping(value="/zhijianInfo",method=RequestMethod.GET)
	public String zhijianInfo() {
		return "zhijianInfo";
	}
	//post
	@RequestMapping(value="/Create.findyl",method=RequestMethod.POST)
	public @ResponseBody String [] findyl(@RequestBody delMat delmat) {
		String []name = MaterialSystem.selectAllMaterialName();
		return name;
	}
	@RequestMapping(value="/ShengChanJinDu",method=RequestMethod.GET)
	public String ShengChanJinDu() {
		return "ShengChanJinDu";
	}
	//忘记在哪儿用到了，，找了半天没找到，暂时注释
	/*@RequestMapping(value="/Create.findylckpc",method=RequestMethod.POST)
	public @ResponseBody Integer [] findylckpc(@RequestBody delMat yl) {
		String name = yl.getName();
		//Staff staff = (Staff)session.getAttribute("staff");
		System.out.println("原料名:"+name);
		Staff staff = new Staff(7,MD5.encrypt("123456"));
		String title = staff.getZhiwei();
		int index = title.indexOf("车间");
		String cjidstr = title.substring(index+"车间".length(),index+"车间".length()+1);
		Integer cjid = Integer.parseInt(cjidstr);
		Map<Integer,Integer>map = WorkSystem.findCkpc(name, cjid);
		if(map ==null)
			return null;
		Object[] keys = map.keySet().toArray();
		Integer [] pc = new Integer[keys.length];
		for(int i = 0;i<keys.length;i++) {
			pc[i] = Integer.parseInt(keys[i].toString());
		}
		
		for(int i = 0;i<keys.length;i++) {
			System.out.println("批次"+pc[i]+",剩余"+map.get(pc[i]));
		}
		
		return pc;
	}
	@RequestMapping(value="/Create.findylckpcleft",method=RequestMethod.POST)
	public @ResponseBody Map<Integer,Integer> findylckpcleft(HttpSession session,@RequestBody delMat yl) {
		String name = yl.getName();
		//Staff staff = (Staff)session.getAttribute("staff");
		Staff staff = new Staff(7,MD5.encrypt("123456"));
		String title = staff.getZhiwei();
		int index = title.indexOf("车间");
		String cjidstr = title.substring(index+"车间".length(),index+"车间".length()+1);
		Integer cjid = Integer.parseInt(cjidstr);
		Map<Integer,Integer>map = WorkSystem.findCkpc(name, cjid);
		if(map ==null)
			return null;
		return map;
	}*/
	@RequestMapping(value="/Create.shengchan",method=RequestMethod.POST)
	public @ResponseBody String [] shengChan(@RequestBody Shengchan sc) {
		String cpname = sc.getCpname();
		Integer cpid = ProductSystem.findProduct(cpname);
		String zjyid = sc.getZjyid().split(",")[0];
		String zhijian = sc.getZjyid().split(",")[1];
		Integer cjid = Integer.parseInt(sc.getCjid());
		Integer num = sc.getNum();
		System.out.println("cpname:"+cpname);
		System.out.println("zjyid:"+zjyid);
		System.out.println("num:"+num);
		//Staff staff = (Staff)session.getAttribute("staff");
		
		String [][] peifang = ProductSystem.findPeiFang(cpid);
		if(peifang == null||peifang.length == 0) return null;
		Integer [] ylid = new Integer[peifang.length];
		Integer [] ylnum = new Integer[peifang.length];
		int state = 1;
		for(int i = 0; i < peifang.length; i++) {
			ylid[i] = Integer.parseInt(peifang[i][0]);
			String ylname = MaterialSystem.findMaterialName(ylid[i]);
			Map<Integer,Integer> map = WorkSystem.findCkpc(ylname, cjid);
			if(map == null||map.size() == 0) {
				state = 0;
				break;
			}
			Object [] key = map.keySet().toArray();
			int sum = 0;
			for(int j = 0;j < key.length;j++) {
				sum += map.get(key[j]);
			}
			System.out.println("原料"+ylid[i]+"还剩"+sum);
			
			ylnum[i] = Integer.parseInt(peifang[i][1]);
			System.out.println("需要"+(num*ylnum[i]));
			if(ylnum[i]*num>sum) {
				
				state = 0;//车间库存不足
				break;
			}
		}
		if(state == 0) {
			System.out.println("车间库存不足");
			return null;//车间库存不足
		}
		String [][] scxj = WorkSystem.work(cpid, num, zjyid, cjid,zhijian);
		if(scxj == null)return null;
		String [] result = new String [scxj.length];
		for(int i = 0;i < scxj.length; i++) {
			String per = scxj[i][0];
			for(int j = 1;j < scxj[i].length;j++) {
				per += ","+scxj[i][j];
			}
			result[i] = per;
		}
		return result;
		//return pc;//-1重复项 -2 插入失败 否则为编号
	}
	@RequestMapping(value="/Create.notscpc",method=RequestMethod.POST)
	public @ResponseBody Integer[] notScpc(@RequestBody delMat yl) {
		System.out.println("controller not in");
		String ylname = yl.getName();
		Integer [] scpc = WorkSystem.findSCPCNotIn(ylname);
		if(scpc == null||scpc.length == 0)
			return null;
		System.out.println(ylname+"未入库的生产批次:");
		for(int i = 0;i<scpc.length;i++) {
			System.out.print(scpc[i]+",");
		}
		return scpc;
	}
	@RequestMapping(value="/Create.destory",method=RequestMethod.POST)
	public @ResponseBody Integer destory(HttpSession session,@RequestBody delMat scpcstr) {
		Integer scpc = Integer.parseInt(scpcstr.getName());
		//WorkSystem ws = (WorkSystem)session.getAttribute("worksystem");
		Staff staff = (Staff) session.getAttribute("staff");
		WorkSystem ws = new WorkSystem(staff);
		
		int state = ws.destroyFromWork(scpc);
		
		return state;//-1重复 -2 插入失败 1 成功
	}
	@RequestMapping(value="/dayPlan.getDayPlan",method=RequestMethod.POST)
	public @ResponseBody Map<String,Integer> getDayPlan(@RequestBody delMat delmat) {
		//获得车间id
		//Staff staff = (Staff)session.getAttribute("staff");
		Integer cjid = Integer.parseInt(delmat.getName());
		//获得当日计划
		Map<Integer,Integer>rawplan = PlanSystem.dayPlan(cjid);
		if(rawplan==null||rawplan.size()==0)return null;//当日没有生产计划
		Map<String ,Integer>plan = new HashMap<String,Integer>();
		Object [] keys = rawplan.keySet().toArray();
		for(int i = 0;i < keys.length;i++) {
			String cpname = ProductSystem.findProductName(Integer.parseInt(keys[i].toString()));
			System.out.println("cpname:"+cpname+",cpnum"+rawplan.get(keys[i]));
			plan.put(cpname, rawplan.get(keys[i]));
		}
		return plan;
	}
	@RequestMapping(value="/workInfo.getWorkInfo",method=RequestMethod.POST)
	public @ResponseBody String[] getWorkInfo(@RequestBody delMat info) {
		String cjid = info.getName();
		String [] result = WorkSystem.getWorkInfo(cjid);
		if(result!=null) {
			System.out.println("车间 = "+cjid+",的细节length："+result.length);
			for(int i = 0;i < result.length ;i++) {
				System.out.println(result[i]);
			}
		}
		else
			System.out.println("车间 = "+cjid+",查细节失败");
		return result;
	}
	@RequestMapping(value="/workInfoChange.changeWorkInfo",method=RequestMethod.POST)
	public @ResponseBody Integer changeWorkInfo(@RequestBody changeWork changework) {
		System.out.println("controller:changWorkInfo");
		//获得车间id
		
		Integer cjid = Integer.parseInt(changework.getCjid());
		Integer zrid = changework.getCjzr();
		String state = changework.getState();
		String astate;
		if(state.equals("使用中")) {
			astate = "1";//1表使用中，0表示空闲
		}
		else {
			astate = "0";
		}
		String [] setvalues = {Integer.toString(zrid),astate};
		String  [] prodstr = changework.getCpname().split(",");
		String [] prod = new String[prodstr.length];
 		String  [] powerstr = changework.getPower().split(",");
 		System.out.println("pro lenght : "+prodstr.length+", power length:"+powerstr.length);
		for(int i = 0;i<prodstr.length;i++) {
			System.out.println("pro name:"+prodstr[i]);
			System.out.println("power:"+powerstr[i]);
			prod[i] = Integer.toString(ProductSystem.findProduct(prodstr[i]));
			System.out.println("pro id:"+prod[i]);
		}
		int flag = WorkSystem.changeWork(cjid, setvalues, powerstr, prod);
		if(flag < 0) {
			if(flag == -1)
				System.out.println("更新失败");
			else if(flag == -2)
				System.out.println("删除失败");
			else
				System.out.println("插入更新失败");
			return -1;
		}
		else {
			System.out.println("插入成功！");
			return 1;
		}
	}
	@RequestMapping(value="/workInfoChange.changeWorkstate",method=RequestMethod.POST)
	public @ResponseBody Integer changeWorkstate(HttpSession session,@RequestBody delMat statel) {
		System.out.println("controller:changWorkInfo");
		//获得车间id
		String []info = statel.getName().split(",");
		Integer cjid = Integer.parseInt(info[0]);
		String state = info[1];
		String flag ;
		if(state.equals("使用中")) {
			flag = "1";
			
		}
		else {
			flag = "0";
		}
		if(WorkSystem.changeWorkstate(cjid, flag)) {
			return 1;
		}
		else return 0;
		
	}
	
	@RequestMapping(value="/Work_Staff.getWorkInfoViapc",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String getWorkInfoViapc(@RequestBody delMat dd) {
	
		String scpc= dd.getName();
		
		String result = WorkSystem.getScxjViaPc(scpc);
		if(result!=null)
			System.out.println("dd = "+dd+",的出库细节："+result);
		else
			System.out.println("dd = "+dd+",查询出库细节失败");
		return result;
	}
	
	@RequestMapping(value="/Work_Staff.getScxjViaTime",method=RequestMethod.POST)
	public @ResponseBody String[] getScxjViaTime(@RequestBody delMat dd) {
		
		String [] info = dd.getName().split(",");
		
		String[] result = WorkSystem.getScxjViaTime(info[0],info[1],info[2]);
		if(result!=null)
			System.out.println("dd = "+dd+",的出库细节length："+result.length);
		else
			System.out.println("dd = "+dd+",查询出库细节失败");
		return result;
	}
	@RequestMapping(value="/Work_Staff.getZjInfoViapc",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String getZjInfoViapc(@RequestBody delMat dd) {
	
		String pc = dd.getName();
		
		String result = WorkSystem.getZjInfoViapc(pc);
		if(result!=null)
			System.out.println("dd = "+dd+",的质检细节："+result);
		else
			System.out.println("dd = "+dd+",查询质检细节失败");
		return result;
	}
	
	@RequestMapping(value="/Work_Staff.getZjInfoViaTimecj",method=RequestMethod.POST)
	public @ResponseBody String[] getZjInfoViaTimecj(@RequestBody delMat dd) {
		System.out.println(dd.getName());
		String []info = dd.getName().split(",");
		
		String [] result = WorkSystem.getZjInfoViaTimecj(info[0], info[1], info[2]);
		if(result!=null)
			System.out.println("dd = "+dd+",的质检细节："+result.length);
		else
			System.out.println("dd = "+dd+",查询质检细节失败");
		return result;
	}
	@RequestMapping(value="/Work_Staff.getZjInfoViaTimezjy",method=RequestMethod.POST)
	public @ResponseBody String[] getZjInfoViaTimezjy(@RequestBody delMat dd) {
		
		String []info = dd.getName().split(",");
		
		String [] result = WorkSystem.getZjInfoViaTimery(info[0], info[1], info[2]);
		if(result!=null)
			System.out.println("dd = "+dd+",的质检细节："+result.length);
		else
			System.out.println("dd = "+dd+",查询质检细节失败");
		return result;
	}
}
