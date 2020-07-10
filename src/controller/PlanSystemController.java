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
public class PlanSystemController {
	@RequestMapping(value = "/Yuangongflow",method = RequestMethod.GET)
	public String Yuangongflow() {
		return "Yuangongflow";
	}
	@RequestMapping(value = "/Dingdanflow",method = RequestMethod.GET)
	public String Dingdanflow() {
		return "Dingdanflow";
	}
	@RequestMapping(value = "/Jinhuoflow",method = RequestMethod.GET)
	public String Jinhuoflow() {
		return "Jinhuoflow";
	}
	@RequestMapping(value = "/selectShengChanPlan",method = RequestMethod.GET)
	public String selectShengChanPlan() {
		return "selectShengChanPlan";
	}
	@RequestMapping(value = "/MoneySum",method = RequestMethod.GET)
	public String MoneySum() {
		return "MoneySum";
	}
	@RequestMapping(value = "/WorkPlan",method = RequestMethod.GET)
	public String WorkPlan() {
		return "WorkPlan";
	}

	@RequestMapping(value = "/WorkPlan.getPlan",method = RequestMethod.POST) 
	public @ResponseBody String[][] WorkPlanGetPlan(@RequestBody Dingdan time) {
		String[][] ans = PlanSystem.getWorkPlan(time.getTihuoTime1(), time.getTihuoTime2());
		return ans;
	}
	
	@RequestMapping(value = "/WorkPlan.startTime",method = RequestMethod.POST) 
	public @ResponseBody String WorkPlanStartTime() throws ParseException {
		String ans = PlanSystem.getStartTime();
		return ans;
	}
	
	@RequestMapping(value = "/WorkPlan.savePlan",method = RequestMethod.POST,produces="text/plain;charset=utf-8") 
	public @ResponseBody String WorkPlanSavePlan(@RequestBody YuJi yuji) {
		return PlanSystem.savePlan(
				yuji.getChanpinID(), 
				yuji.getChanpinNUM(),
				yuji.getTihuoTime1(),
				yuji.getTihuoTime2(),
				yuji.getMingchens());
		//return "ok";
	}
	
	@RequestMapping(value = "/ShengChanJinDu.getXiJie",method = RequestMethod.POST) 
	public @ResponseBody String[][] ShengChanJinDuGetXiJie(@RequestBody String id) {
		//System.out.println(id);
		return PlanSystem.ShengChanJinDuGetXiJie(id);
	}
}
