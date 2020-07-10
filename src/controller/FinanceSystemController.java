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
public class FinanceSystemController {
	@RequestMapping(value = "/Finance_staff",method = RequestMethod.GET)
	public String Finance_staff() {
		return "Finance_staff";
	}
	
	@RequestMapping(value = "/selectShouKuan",method = RequestMethod.GET)
	public String selectShouKuan() {
		return "selectShouKuan";
	}
	@RequestMapping(value = "/Yuangongflow.do",method = RequestMethod.POST) 
	public @ResponseBody String[][] YuangongflowDo(@RequestBody String yuangongID){
		System.out.println("flow "+yuangongID);
		String[][] res = FinanceSystem.getYuanGongFlow(yuangongID);
        return res;
	}
	@RequestMapping(value = "/Dingdanflow.do",method = RequestMethod.POST) 
	public @ResponseBody String[][] DingdanflowDo(@RequestBody String nothing){
		String[][] res = FinanceSystem.getDingDanFlow1();
        return res;
	}
	@RequestMapping(value = "/Dingdanflow.filter",method = RequestMethod.POST) 
	public @ResponseBody String[][] DingdanflowFilter(@RequestBody Dingdan times){
		String[][] res = FinanceSystem.DingdanflowFilter(times.getTihuoTime1()
				,times.getTihuoTime2());
        return res;
	}
	
	@RequestMapping(value = "/Jinhuoflow.do",method = RequestMethod.POST) 
	public @ResponseBody String[][] JinhuoflowDo(@RequestBody String nothing){
		String[][] res = FinanceSystem.getJinHuoFlow();
        return res;
	}
	@RequestMapping(value = "/Jinhuoflow.filter",method = RequestMethod.POST) 
	public @ResponseBody String[][] JinhuoflowFilter(@RequestBody Dingdan times){
		String[][] res = FinanceSystem.JinhuoflowFilter(times.getTihuoTime1()
				,times.getTihuoTime2());
        return res;
	}
	
	@RequestMapping(value = "/MoneySum.doBuy",method = RequestMethod.POST) 
	public @ResponseBody String[][] MoneySumDoBuy(@RequestBody Dingdan time){
		String[][] res = FinanceSystem.getBuyMoneySum(
				time.getTihuoTime1(),
				time.getTihuoTime2() );
        return res;
	}
	@RequestMapping(value = "/selectShengChanPlan.getXiJie",method = RequestMethod.POST) 
	public @ResponseBody String[][] selectShengChanPlanGetXiJie(@RequestBody String id){
		
        return PlanSystem.getWorkPlanWithId(id);
	}
	@RequestMapping(value = "/selectShouKuan.submit",method = RequestMethod.POST) 
	public @ResponseBody String[][] selectShouKuanSubmit(@RequestBody Dingdan dingdan ){
		
        return FinanceSystem.selectShouKuanSubmit(dingdan.getDingdanID(),
        		dingdan.getKehuID(), dingdan.getXiadanTime1(), dingdan.getXiadanTime2());
	}
	
}
