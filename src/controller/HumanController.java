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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import model.bll.objects.*;
import model.bll.systems.*;
import model.dal.MD5;

@SessionAttributes("staff")
@Controller
public class HumanController {
	@RequestMapping(value="/changeRole",method=RequestMethod.GET)
	public String changeRole() {
		return "changeRole";
	}
	@RequestMapping(value="/changeStaffInfo",method=RequestMethod.GET)
	public String changeStaffInfo() {
		return "changeStaffInfo";
	}
	@RequestMapping(value="/staffInfo",method=RequestMethod.GET)
	public String staffInfo() {
		return "staffInfo";
	}
	@RequestMapping(value="/inStaff",method=RequestMethod.GET)
	public String inStaff() {
		return "inStaff";
	}
	@RequestMapping(value="/changeRoleInfo",method=RequestMethod.GET)
	public String changeRoleInfo() {
		return "changeRoleInfo";
	}
	@RequestMapping(value="/infoRole",method=RequestMethod.GET)
	public String infoRole() {
		return "infoRole";
	}
	//post
//	@RequestMapping(value="/staffInfo.getStaffInfo",method=RequestMethod.POST)
//	public @ResponseBody String[] getStaffInfo(@RequestBody delMat staffid) {
//		Integer id = Integer.parseInt(staffid.getName());
//		String [] info = HumanSystem.findInfo(id);
//		return info;
//	}
	@RequestMapping(value="/staffInfo.getStaffInfo",method=RequestMethod.POST)
	public @ResponseBody String[] getStaffInfo(@RequestBody delMat staffid) {
		if(staffid.getName().equals("全部")) {
			Integer [] ids = HumanSystem.findAllStaff();
			if(ids==null||ids.length == 0)return null;
			String result[] = new String [ids.length];
			for(int i = 0;i <ids.length ;i++) {
				int id = ids[i];
				Staff staff = new Staff();
				staff.setId(id);
				staff.getinfo();
				result[i] = JSON.toJSONString(staff);
			}
			return result;
		}
		Integer id = Integer.parseInt(staffid.getName());
		Staff staff = new Staff();
		staff.setId(id);
		staff.getinfo();
		String result[] = {JSON.toJSONString(staff)};
		System.out.println(JSON.toJSONString(staff));
		return result;
	}

	@RequestMapping(value="/staffInfo.changeGz",method=RequestMethod.POST)
	public @ResponseBody boolean changeGz(@RequestBody delMat gongzi) {
		String [] get = gongzi.getName().split(",");
		String gz = get[1];
		Integer ygid = Integer.parseInt(get[0]);
		String [] goal = {"员工工资"};
		String [] value = {gz};
		return HumanSystem.changeInfo(ygid, goal, value);
	}
	@RequestMapping(value="/staffInfo.changeZhiwei",method=RequestMethod.POST)
	public @ResponseBody boolean changeZhiwei(@RequestBody delMat zhiwei) {
		String [] get = zhiwei.getName().split(",");
		String zw = get[1];
		Integer ygid = Integer.parseInt(get[0]);
		String [] goal = {"员工职位"};
		String [] value = {zw};
		return HumanSystem.changeInfo(ygid, goal, value);
	}
	@RequestMapping(value="/staffInfo.changeZgzt",method=RequestMethod.POST)
	public @ResponseBody boolean changeZgzt(@RequestBody delMat state) {
		String [] get = state.getName().split(",");
		String zgzt = get[1];
		Integer ygid = Integer.parseInt(get[0]);
		String [] goal = {"员工在岗状态"};
		String [] value = {zgzt};
		return HumanSystem.changeInfo(ygid, goal, value);
	}
	@RequestMapping(value="/staffInfo.addStaff",method=RequestMethod.POST)
	public @ResponseBody int addStaff(@RequestBody ostaff staff) {
		String name = staff.getMingchen();
		System.out.println("name:"+name);
		String sex = staff.getSex();
		System.out.println("sex:"+sex);
		String identity = staff.getSfzid();
		System.out.println("sfz:"+identity);
		String tel = staff.getShouji();
		System.out.println("tel:"+tel);
		String zgstate = staff.getZgstate();
		System.out.println("zgstate:"+zgstate);
		String zhiwei = staff.getZhiwei();
		System.out.println("zw:"+zhiwei);
		String addr = staff.getZhuzhi();
		System.out.println("addr:"+addr);
		Date rztime = staff.getRztime();
		System.out.println("rztime:"+rztime);
		Float gz = staff.getGongzi();
		System.out.println("gz:"+gz);
		String mail = staff.getMail();
		System.out.println("mail:"+mail);
		String password = MD5.encrypt(staff.getPassword());
		System.out.println("pass:"+password);
		int flag = HumanSystem.addWorker(name, sex, zhiwei, identity, tel, mail, addr, rztime, zgstate, password, gz);
		return flag;
	}
	@RequestMapping(value="/staffInfo.resetPass",method=RequestMethod.POST)
	public @ResponseBody boolean resetPass(@RequestBody delMat idstr) {
		Integer id = Integer.parseInt(idstr.getName());
		String [] goal = {"员工密码"};
		String [] value = {MD5.encrypt("123456")};
		return HumanSystem.changeInfo(id,goal,value);
	}
	
	@RequestMapping(value="/changeRole",method=RequestMethod.POST)
	public @ResponseBody boolean changeRole(@RequestBody delMat yg) {
		if(yg.getName().contains(",")) {
			String [] info = yg.getName().split(",");
			System.out.println("info.length = "+info.length);
			System.out.println("员工角色修改信息");
			for(int i = 0;i < info.length ;i++) {
				System.out.println(info[i]);
			}
			return HumanSystem.changeRole(info);
		}
		else {
			String [] info = {yg.getName()};
			return HumanSystem.changeRole(info);
		}
	}
	
	@RequestMapping(value="/changeRoleInfo",method=RequestMethod.POST)
	public @ResponseBody boolean changeRoleInfo(@RequestBody delMat js) {
		String [] info = js.getName().split(",");
		return HumanSystem.changeRoleInfo(info);
	
	}
	
	@RequestMapping(value="/infoRole.getRoleInfo",method=RequestMethod.POST)
	public @ResponseBody String [][] getRoleInfo(@RequestBody delMat js) {
		String jsid = js.getName();
		return HumanSystem.getRoleInfo(jsid);
	
	}
}
