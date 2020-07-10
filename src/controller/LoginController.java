package controller;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.ui.ModelMap;

import com.alibaba.fastjson.JSON;
import model.bll.objects.*;
import model.bll.systems.FinanceSystem;
import model.bll.systems.HumanSystem;
import model.bll.systems.MaterialSystem;
import model.bll.systems.PlanSystem;
import model.bll.systems.SoldSystem;
import model.dal.MD5;

@SessionAttributes("staff")
@Controller
public class LoginController {
	@RequestMapping(value = "/login",method = RequestMethod.GET)
	public ModelAndView login() {
		return new ModelAndView("login","command",new Staff());
	}
	
	@RequestMapping(value = "/YuanGong",method = RequestMethod.GET)
	public String YuanGong() {
		return "YuanGong";
	}
	@RequestMapping(value = "/layui",method = RequestMethod.GET)
	public String layui() {
		return "layui";
	}

//	@RequestMapping(value = "/Plan_staff",method = RequestMethod.GET)
//	public String Plan_staff() {
//		return "Plan_staff";
//	}

	@RequestMapping(value = "/MyStaff",method = RequestMethod.GET)
	public String MyStaff() {
		return "MyStaff";
	}
	
	@RequestMapping(value = "/GongHuoShang",method = RequestMethod.GET)
	public String GongHuoShang() {
		return "GongHuoShang";
	}
	@RequestMapping(value = "/login.action",method = RequestMethod.POST,produces="text/plain;charset=utf-8") 
	public @ResponseBody String dologin(@RequestBody Staff staff, HttpServletRequest request, ModelMap model) {
		staff.check();
		if(staff.getState() != "OK") {
			return staff.getState();
		}
		//model.addAttribute("staff"+staff.getId(), staff);
		model.addAttribute("staff", staff);
		//request.getSession(true).setAttribute("staff"+staff.getId(),staff);
		//System.out.println("staff"+staff.getId());
		String url = "http://localhost:8080/FoodFactory/";
		return url+"MyStaff";
		
//		if(staff.getZhiwei().startsWith("销售")) return url+"Sold_staff";
//		if(staff.getZhiwei().startsWith("财务会计")) return url+"Finance_staff";
//		if(staff.getZhiwei().startsWith("计划")) return url+"Plan_staff";
//		return "";
	}
	
	
	@RequestMapping(value = "/MyStaff.getRoles",method = RequestMethod.POST) 
	public @ResponseBody String[][] MyStaffGetRoles(HttpSession session) {
		Staff staff = (Staff)session.getAttribute("staff");
		String[][] ans = HumanSystem.getRoles(staff.getId());
		
		return ans;
	}
	
	@RequestMapping(value = "/YuanGong.action",method = RequestMethod.POST,produces="text/plain;charset=utf-8") 
	@ResponseBody
	public String YuanGongAction(HttpSession session,HttpServletRequest request,HttpServletResponse responses) {
		String id = request.getParameter("id");
		System.out.println("staff"+id);
		//Staff staff = (Staff)session.getAttribute("staff"+id);
		Staff staff = (Staff)session.getAttribute("staff");
//		if(staff == null)
//			System.out.println("NULL STAFF");
//		else
//			System.out.println(staff.getId());
	//	System.out.println(JSON.toJSONString(staff));
		return JSON.toJSONString(staff);
	}
	
	@RequestMapping(value = "/login.changePassWord",method = RequestMethod.POST,produces="text/plain;charset=utf-8") 
	@ResponseBody
	public String loginChangePassWord(@RequestBody Staff staff) {
		int id = staff.getId();
		String pass = MD5.encrypt(staff.getPassword());
		String[] goal = {"员工密码"};
		String[] val = {pass};
		System.out.println(pass);
		HumanSystem.changeInfo(id, goal, val);
		return "ok";
	}

	@RequestMapping(value = "/jsonsource",method = RequestMethod.POST) 
	public @ResponseBody User jsonSource2(@RequestBody User user){
		System.out.println(user.getId()+"222");
        return user;
	}
	
	@RequestMapping(value = "/mylogin",method = RequestMethod.POST)
	public String addStaff(@ModelAttribute("SpringWeb")Staff staff,ModelMap model) {
		staff.check();
		String state = staff.getState();
		//System.out.println(staff.getId()+","+staff.getPassword());
		//System.out.println("state:"+state);
		if(state!=null&&state.equals("OK")) {
			model.addAttribute("id",staff.getId());
			return "home";
		}
		else return "noperson";
	}
	
	
	@RequestMapping(value = "/MoneySum.doSold",method = RequestMethod.POST) 
	public @ResponseBody String[][] MoneySumDoSold(@RequestBody Dingdan time){
		String[][] res = FinanceSystem.getSoldMoneySum(time.getTihuoTime1(),
				time.getTihuoTime2());
        return res;
	}
	
	@RequestMapping(value = "/logout",method = RequestMethod.POST) 
	public @ResponseBody String logout(HttpSession session, @RequestBody String nothing){
		System.out.println("logout!");
		//session.removeAttribute("staff");
		session.invalidate();
        return "ok";
	}
	
	@RequestMapping(value = "/welcome",method = RequestMethod.GET)
	public String welcome() {
		return "welcome";
	}
	
}
