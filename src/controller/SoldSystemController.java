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
public class SoldSystemController {
	@RequestMapping(value = "/Sold_staff",method = RequestMethod.GET)
	public String Sold_staff() {
		return "Sold_staff";
	}
	@RequestMapping(value = "/createOrder",method = RequestMethod.GET)
	public String createOrder() {
		return "createOrder";
	}
	@RequestMapping(value = "/tihuo",method = RequestMethod.GET)
	public String tihuo() {
		return "tihuo";
	}
	@RequestMapping(value = "/buhuo",method = RequestMethod.GET)
	public String buhuo() {
		return "buhuo";
	}
	@RequestMapping(value = "/tuihuo",method = RequestMethod.GET)
	public String tuihuo() {
		return "tuihuo";
	}
	@RequestMapping(value = "/SoldPlan",method = RequestMethod.GET)
	public String SoldPlan() {
		return "SoldPlan";
	}
	
	@RequestMapping(value = "/fixPlan",method = RequestMethod.GET)
	public String fixPlan() {
		return "fixPlan";
	}
	@RequestMapping(value = "/selectDingDan",method = RequestMethod.GET)
	public String selectDingDan() {
		return "selectDingDan";
	}
	@RequestMapping(value = "/selectKeHu",method = RequestMethod.GET)
	public String selectKeHu() {
		return "selectKeHu";
	}
	@RequestMapping(value = "/TuiDIng",method = RequestMethod.GET)
	public String TuiDing() {
		return "TuiDing";
	}
	
	@RequestMapping(value = "/selectTuiHuo",method = RequestMethod.GET)
	public String selectTuiHuou() {
		return "selectTuiHuo";
	}
	@RequestMapping(value = "/fixPlan.savePlan",method = RequestMethod.POST) 
	public @ResponseBody String fixPlanSavePlan(@RequestBody SoldPlanXiJie xijie) {
		SoldSystem.fixPlanSavePlan(xijie.getId(), 
				xijie.getChanpinID(), xijie.getChanpinNUM());
		return "ok";
	}
	@RequestMapping(value = "/fixPlan.getXiJie",method = RequestMethod.POST) 
	public @ResponseBody String[][] fixPlanGetXiJie(@RequestBody String id) {
		String[][] ans = SoldSystem.fixPlanGetXiJie(id);
		return ans;
	}
	@RequestMapping(value = "/SoldPlan.getPlan",method = RequestMethod.POST) 
	public @ResponseBody String[][] getPlan(@RequestBody Dingdan time) {
		String[][] ans = SoldSystem.getPlan(time.getTihuoTime1(), time.getTihuoTime2());
		return ans;
	}
	
	@RequestMapping(value = "/TuiDing.getMoney",method = RequestMethod.POST) 
	public @ResponseBody String[][] TuiDingGetMoney(@RequestBody String id) {
		String[][] ans = SoldSystem.TuiDing(id);
		return ans;
	}
	
	@RequestMapping(value = "/SoldPlan.getNowPlan",method = RequestMethod.POST) 
	public @ResponseBody String[][] SoldPlanGetNowPlan() {
		
		return SoldSystem.readNowPlan();
	}
	
	@RequestMapping(value = "/SoldPlan.savePlan",method = RequestMethod.POST) 
	public @ResponseBody String SoldPlanSavePlan(@RequestBody YuJi yuji) {
		SoldSystem.savePlan(yuji.getChanpinID(), yuji.getChanpinNUM());
		return "ok";
	}
	
	//生成新订单
		@RequestMapping(value = "/createNewOrder",method = RequestMethod.POST) 
		public @ResponseBody String createNewOrder(HttpSession session, @RequestBody Dingdan dingdan ){
			Staff staff = (Staff)session.getAttribute("staff");
			SoldSystem.newOrder(dingdan.getKehumingchen(), 
					dingdan.getShifuMONEY(),
					dingdan.getChanpinmingchen(), 
					dingdan.getChanpinnum(), 
					dingdan.getTihuoTime(), staff.getId());
	        return "ok";
		}
		
		@RequestMapping(value = "/tihuo.getDingDanIDWithKeHuMingCheng",method = RequestMethod.POST) 
		public @ResponseBody String[] getDingDanIDWithKeHuMingCheng(@RequestBody String name){
			String[] res = SoldSystem.getDingDanIDWithKeHuMingCheng(name);
			//System.out.println(name);
	        return res;
		}
		
		@RequestMapping(value = "/tuihuo.getDingDanIDWithKeHuMingCheng",method = RequestMethod.POST) 
		public @ResponseBody String[] TuigetDingDanIDWithKeHuMingCheng(@RequestBody String name){
			String[] res = SoldSystem.TuigetDingDanIDWithKeHuMingCheng(name);
			System.out.println(name);
	        return res;
		}
		
		@RequestMapping(value = "/tihuo.getmoney",method = RequestMethod.POST) 
		public @ResponseBody String tihuoGetmoney(@RequestBody String dingdanID){
	        return SoldSystem.getMoney(dingdanID);
		}
		
		@RequestMapping(value = "/tihuo.getXiJieAndKuCun",method = RequestMethod.POST) 
		public @ResponseBody String[][] tihuoGetXiJieAndKuCun(@RequestBody String dingdanID){
	        return SoldSystem.tihuoGetXiJieAndKuCun(dingdanID);
		}
		
		@RequestMapping(value = "/tuihuo.getmoney",method = RequestMethod.POST) 
		public @ResponseBody String tuihuoGetmoney(@RequestBody String dingdanID){
	        return SoldSystem.getTuiMoney(dingdanID);
		}
		@RequestMapping(value = "/tihuo.do",method = RequestMethod.POST) 
		public @ResponseBody String dotihuo(HttpSession session, @RequestBody Dingdan tihuo){
			//System.out.println(tihuo.getDingdanID());
			Staff staff = (Staff)session.getAttribute("staff");
			SoldSystem.tihuo(tihuo.getKehumingchen(),
					tihuo.getDingdanID(),
					tihuo.getActivity_apply_money()
					,tihuo.getShoukuanTYPE()
					,staff.getId().toString());
	        return "ok";
		}
		
		@RequestMapping(value = "/tuihuo.do",method = RequestMethod.POST) 
		public @ResponseBody String dotuihuo(HttpSession session, @RequestBody Dingdan tuihuo){
			//System.out.println(tihuo.getDingdanID());
			Staff staff = (Staff)session.getAttribute("staff");
			SoldSystem.tuihuo(staff.getId().toString(),
					tuihuo.getDingdanID(),
					tuihuo.getActivity_apply_money());
	        return "ok";
		}
		
		@RequestMapping(value = "/buhuo.getDingDanXiJieWithDingdanID",method = RequestMethod.POST) 
		public @ResponseBody String[][] getDingDanXiJieWithDingdanID(@RequestBody String options){
//			String[][] res = {{"奶油","1"}, {"鸡肉","2"}};
//			System.out.println(options);
			String[][] res = SoldSystem.getDingDanXiJieWithDingdanID(options);
	        return res;
		}
		
		@RequestMapping(value = "/selectDingDan.xijie",method = RequestMethod.POST) 
		public @ResponseBody String[][] selectDingDanXijie(@RequestBody String options){
			//System.out.println(options.getShifuMONEY() );
	        return SoldSystem.searchXiJie(options);
		}
		
		@RequestMapping(value = "/selectDingDan.submit",method = RequestMethod.POST) 
		public @ResponseBody String[][] selectDingDanSubmit(@RequestBody Dingdan options){
			//System.out.println(options.getShifuMONEY() );
	        return SoldSystem.search(options);
		}
		@RequestMapping(value = "/selectKeHu.selectAllKeHu",method = RequestMethod.POST) 
		public @ResponseBody String[][] selectKeHuSelectAllKeHu(){
			
	        return SoldSystem.selectAllKeHu();
		}
		
		@RequestMapping(value = "/selectKeHu.selectKeHu",method = RequestMethod.POST) 
		public @ResponseBody String[][] selectKeHuSelectKeHu(@RequestBody Dingdan kehu){
			if(kehu == null) return SoldSystem.selectKeHu(null, null);
			String id = null, name = null;
			if(kehu.getKehuID() != null) id = kehu.getKehuID().toString();
			if(kehu.getKehumingchen() != null) name = kehu.getKehumingchen().toString();
	        return SoldSystem.selectKeHu(id, name);
		}
		
		@RequestMapping(value = "/selectTuiHuo.submit",method = RequestMethod.POST) 
		public @ResponseBody String[][] selectTuiHuoSubmit(@RequestBody Dingdan dingdan){
			 return SoldSystem.selectTuiHuo(dingdan.getDingdanID(),dingdan.getKehuID(),
					 dingdan.getXiadanTime1(), dingdan.getXiadanTime2());
		}
		
}
