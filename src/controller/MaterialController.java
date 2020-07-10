package controller;
import java.util.Date;
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
public class MaterialController {
	//功能页面跳转
	@RequestMapping(value = "/addMat",method = RequestMethod.GET)
	public String addMat() {
		return "addMat";
	}
	@RequestMapping(value="/delMat",method=RequestMethod.GET)
	public String delMatPage() {
		return "delMat";
	}
	@RequestMapping(value="/matInfo",method=RequestMethod.GET)
	public String infoMat() {
		return "matInfo";
	}
	@RequestMapping(value="/expireMat",method=RequestMethod.GET)
	public String expireMat() {
		return "expireMat";
	}
	@RequestMapping(value="/stock",method=RequestMethod.GET)
	public String stock() {
		return "stock";
	}
	@RequestMapping(value="/inStorage",method=RequestMethod.GET)
	public String inStorage() {
		return "inStorage";
	}
	@RequestMapping(value="/outStorage",method=RequestMethod.GET)
	public String outStorage() {
		return "outStorage";
	}
	@RequestMapping(value="/addStorage",method=RequestMethod.GET)
	public String addStorage() {
		return "addStorage";
	}
	
	@RequestMapping(value="/matStockMes",method=RequestMethod.GET)
	public String matStockMes() {
		return "matStockMes";
	}
	@RequestMapping(value="/matInMes",method=RequestMethod.GET)
	public String matInMes() {
		return "matInMes";
	}
	
	@RequestMapping(value="/matOutMes",method=RequestMethod.GET)
	public String matOutMes() {
		return "matOutMes";
	}
	@RequestMapping(value="/matDelMes",method=RequestMethod.GET)
	public String matDelMes() {
		return "matDelMes";
	}
	//post
	@SuppressWarnings("unused")
	@RequestMapping(value="/addMat",method=RequestMethod.POST)
	public @ResponseBody Integer addMaterial(HttpSession session , @RequestBody Material material) {
		//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
		if(material == null)
			System.out.println("mat null");
		else {
			System.out.println("mat name "+material.getMingcheng());
			System.out.println("mat dbgg "+material.getDbgg());
			System.out.println("mat bzq "+material.getBaozhiqi());
			System.out.println("mat dw "+material.getDanwei());
		}
		Staff staff = (Staff) session.getAttribute("staff");
		MaterialSystem ms = new MaterialSystem(staff);
		if(ms==null)
			System.out.println("ms null");
		else
			System.out.println("ms isnot null");
		int state = ms.addMaterial(material.getMingcheng(), Integer.parseInt(material.getDbgg().toString()), Integer.parseInt(material.getBaozhiqi().toString()),material.getDanwei());
		return state;
	}
	
	
	@RequestMapping(value="/getName",method=RequestMethod.POST)
	public @ResponseBody String [] getAllName(@RequestBody delMat delmat) {
		String names[] = MaterialSystem.selectAllMaterialName();
		return names;
	}
	@RequestMapping(value="/getRkpc",method=RequestMethod.POST)
	public @ResponseBody Integer[] getRkpc(@RequestBody delMat delmat) {
		Integer rkpc[] = MaterialSystem.selectAllRkpcViaName(delmat.getName());
		return rkpc;
	}
	@RequestMapping(value="/destroyRkpc",method=RequestMethod.POST)
	public @ResponseBody boolean destroyRkpc(HttpSession session,@RequestBody delMat delmat) {
		System.out.println("destroyRkpc");
		Integer rkpc = Integer.parseInt(delmat.getName());
		System.out.println("rkpc = "+rkpc);
		//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		MaterialSystem ms = new MaterialSystem(staff);
		int [] rkpcs = {rkpc};
		ms.destroyMaterial(rkpcs);
		return true;
	}
	@RequestMapping(value="/getMatInfo",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String getMatInfo(@RequestBody delMat delmat) {
		String name = delmat.getName();
		int id = MaterialSystem.findMaterialID(name);
		return MaterialSystem.selectAllInfo(id);
	}
	@RequestMapping(value="/getAllMatInfo",method=RequestMethod.POST)
	public @ResponseBody String [] getAllMatInfo(@RequestBody delMat delmat) {
		return MaterialSystem.selectAllMatInfo();
	}
	@RequestMapping(value="/destroyAll",method=RequestMethod.GET)
	public @ResponseBody Integer destroyAll(HttpSession session) {
		//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		System.out.println("destroyAll");
		String [][] result = MaterialSystem.expire();
		if(result==null||result.length==0||result[0][0].equals("null")) {
			System.out.println("不过期");
			return 0;
		}
		MaterialSystem ms = new MaterialSystem(new Staff(1,MD5.encrypt("123456")));
		try {
			ms.destroyMaterialAll();
		}catch(Exception E) {
			System.out.println("no");
			return -1;
			
		}
		System.out.println("ok");
		return 1;
	}
	@RequestMapping(value="/stock",method=RequestMethod.POST)
	public @ResponseBody Integer addStock(HttpSession session, @RequestBody Stock mystock) {
		//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		MaterialSystem ms = new MaterialSystem(staff);
		String ylname = mystock.getYlname();
		Integer ylid = MaterialSystem.findMaterialID(ylname);
		String ghsname = mystock.getGhsname();
		Integer ghsid = MaterialSystem.findSupplyID(ghsname);
		Integer num = mystock.getNum();
		Float price = mystock.getPrice();
		Date date = mystock.getScrq();
		Integer state = ms.stock(ylid, num, ghsid, price, date);
		return state;//-3表示插入失败，否则为进货批次id
	}
	@RequestMapping(value="/inStorage.findjhpc",method=RequestMethod.POST)
	public @ResponseBody Integer[] findJhpc(@RequestBody delMat ylname) {
		String name = ylname.getName();
		Integer ylid = MaterialSystem.findMaterialID(name);
		return MaterialSystem.findJhpc(ylid);
	}
	@RequestMapping(value="/inStorage.jhpcleft", method=RequestMethod.POST)
	public @ResponseBody Integer[] leftNum(@RequestBody delMat ylname) {
		String name = ylname.getName();
		Integer ylid = MaterialSystem.findMaterialID(name);
		return MaterialSystem.jhpcLeft(ylid);
	}
	@RequestMapping(value="/inStorage.instore", method=RequestMethod.POST)
	public @ResponseBody String inStorage(HttpSession session, @RequestBody inStore store) {
		System.out.println(store==null);
		String ylname = store.getYlname();
		Integer ylid = MaterialSystem.findMaterialID(ylname);
		Integer jhpc = store.getJhpc();
		Integer ckid = store.getCkid();
		Integer num = store.getNum();
		Map<Integer,Integer> map = MaterialSystem.jhpcLeftMap(ylid);
		Integer left = map.get(jhpc);
		if(left<num)
			return "left"+left;//表示该进货批次剩余数量比待入库数量小
		//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		MaterialSystem ms = new MaterialSystem(new Staff(1,MD5.encrypt("123456")));
		int state = ms.inStorage(ckid, num, jhpc);//0表示插入失败，反之为该入库批次
		if(state == -1)return "no";
		else return Integer.toString(state);
	}
	@RequestMapping(value="/outStorage.findrkpcviayl",method=RequestMethod.POST)
	public @ResponseBody Integer[] findRkpcviayl(@RequestBody delMat ylname) {
		System.out.println("getrkpc");
		Integer ylid = MaterialSystem.findMaterialID(ylname.getName());
		return MaterialSystem.findRkpcViayl(ylid);
	}
	@RequestMapping(value="/outStorage.outstore",method=RequestMethod.POST)
	public @ResponseBody String outStore(HttpSession session, @RequestBody outStorage outstore) {
		String ylname = outstore.getYlname();
		Integer ylid = MaterialSystem.findMaterialID(ylname);
		System.out.println("ylname:"+ylname);
		//Integer jhpc = outstore.getJhpc();
		Integer rkpc = outstore.getRkpc();
		Integer num = outstore.getNum();
		Integer cjid = outstore.getCjid();
		Map<Integer,Integer>map = MaterialSystem.rkpcLeftMap(ylid);
		Integer left = map.get(rkpc);
		if(left<num) {
			return "left"+left;//表示剩余数量不足
		}
		else {
			//MaterialSystem ms = (MaterialSystem) session.getAttribute("materialsystem");
			Staff staff = (Staff) session.getAttribute("staff");
			MaterialSystem ms = new MaterialSystem(new Staff(1,MD5.encrypt("123456")));
			int state = ms.outStorage(rkpc, cjid, num);
			if(state == -1) return "no";//表示插入失败
			else return ""+state;//返回出库批次ID
		}
	}
	@RequestMapping(value="/addStorage",method=RequestMethod.POST)
	public @ResponseBody Integer addStorage(@RequestBody Storage storage) {
		Integer id = storage.getManagerid();
		String type = storage.getType();
		String state = storage.getState();
		Integer num = storage.getNum();
		
		return  MaterialSystem.addStorage(id, type, state, num);
		///-1有重复 -2插入失败 -3 查找编号失败 否则为仓库编号
	}
	@RequestMapping(value="/addStorage.delStorage",method=RequestMethod.POST)
	public @ResponseBody int delStorage(@RequestBody delMat ckidstr) {
		Integer id = Integer.parseInt(ckidstr.getName());
		boolean flag = MaterialSystem.deleteStorage(id);
		if(flag)return 1;
		else {
			System.out.println("flag = false");
			return 0;
		}
	}
	@RequestMapping(value="/addStorage.infoStorage",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String infoStorage(@RequestBody delMat ckidstr) {
		Integer id = Integer.parseInt(ckidstr.getName());
		String [] result = MaterialSystem.selectAllStorageInfo(id);
		if(result == null)
			return null;
		else {
			String zridstr = result[1];
			Integer zrid = Integer.parseInt(zridstr);
			String name = HumanSystem.findName(zrid);
			String str = "";
			str += id+"仓库的信息:\r\n";
			str += "仓库主任编号:"+zrid+",\r\n";
			str += "仓库主任名称:"+name+",\r\n";;
			str += "仓库类型:"+result[2]+",\r\n";;
			str += "仓库状态:"+result[3]+",\r\n";;
			str += "仓库容量:"+result[4]+",\r\n";;
			return str;
		}
		///-1有重复 -2插入失败 -3 查找编号失败 否则为仓库编号
	}
	@RequestMapping(value="/addStorage.findStorage",method=RequestMethod.POST)
	@ResponseBody Integer[] findStorage(@RequestBody delMat delmat) {
		Integer [] ids = MaterialSystem.selectAllStorageId();
		return ids;
	}
	
	
	@RequestMapping(value="/Material_Staff.getStockInfoViapc",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	@ResponseBody String getStockInfoViapc(@RequestBody delMat delmat) {
		String id = delmat.getName();
		String result = MaterialSystem.getStockInfoViapc(id);
		if(result == null) {
			System.out.println("没有该批次！");
		}
		else {
			System.out.println("该批次信息为："+result);
		}
		return result;
	}
	@RequestMapping(value="/Material_Staff.getJhpcInfoviafzr",method=RequestMethod.POST)
	@ResponseBody String[] getJhpcInfoviafzr(@RequestBody delMat delmat) {
		String id = delmat.getName();
		String [] result = MaterialSystem.getStockInfoViaFzr(id);
		if(result == null) {
			System.out.println("没有该员工负责的进货");
			return null;
		}
		else {
			System.out.println("负责数量："+result.length);
			return result;
		}
	}
	@RequestMapping(value="/Material_Staff.getJhpcInfoviaTime",method=RequestMethod.POST)
	@ResponseBody String[] getJhpcInfoviaTime(@RequestBody delMat delmat) {
		String [] time = delmat.getName().split(",");
		String [] result = MaterialSystem.getStockInfoViaTime(time[0],time[1]);
		if(result == null) {
			System.out.println("没有该时间段的进货");
			return null;
		}
		else {
			System.out.println("进货数量："+result.length);
			return result;
		}
	}
	@RequestMapping(value="/Material_Staff.getMatInInfoViapc",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	@ResponseBody String getMatInInfoViapc(@RequestBody delMat delmat) {
		String id = delmat.getName();
		String result = MaterialSystem.getInInfoViapc(id);
		if(result == null) {
			System.out.println("没有该批次！");
		}
		else {
			System.out.println("该批次信息为："+result);
		}
		return result;
	}
	
	@RequestMapping(value="/Material_Staff.getMatInInfoViaFzr",method=RequestMethod.POST)
	@ResponseBody String[] getMatInInfoViaFzr(@RequestBody delMat delmat) {
		String fzr = delmat.getName();
		String [] result = MaterialSystem.getInInfoViaFzr(fzr);
		if(result == null) {
			System.out.println("没有该时间段的入库");
			return null;
		}
		else {
			System.out.println("入库数量："+result.length);
			return result;
		}
	}
	@RequestMapping(value="/Material_Staff.getMatInInfoViaTime",method=RequestMethod.POST)
	@ResponseBody String[] getMatInInfoViaTime(@RequestBody delMat delmat) {
		String [] time = delmat.getName().split(",");
		String [] result = MaterialSystem.getInInfoViaTime(time[0],time[1]);
		if(result == null) {
			System.out.println("没有该时间段的入库");
			return null;
		}
		else {
			System.out.println("入库数量："+result.length);
			return result;
		}
	}
	
	@RequestMapping(value="/Material_Staff.getMatOutInfoViapc",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	@ResponseBody String getMatOutInfoViapc(@RequestBody delMat delmat) {
		String id = delmat.getName();
		String result = MaterialSystem.getMatOutInfoViapc(id);
		if(result == null) {
			System.out.println("没有该批次！");
		}
		else {
			System.out.println("该批次信息为："+result);
		}
		return result;
	}
	@RequestMapping(value="/Material_Staff.getMatOutInfoViaFzr",method=RequestMethod.POST)
	@ResponseBody String[] getMatOutInfoViaFzr(@RequestBody delMat delmat) {
		String fzr = delmat.getName();
		String [] result = MaterialSystem.getMatOutInfoViaFzr(fzr);
		if(result == null) {
			System.out.println("没有该时间段的入库");
			return null;
		}
		else {
			System.out.println("入库数量："+result.length);
			return result;
		}
	}
	
	@RequestMapping(value="/Material_Staff.getMatOutInfoViaTime",method=RequestMethod.POST)
	@ResponseBody String[] getMatOutInfoViaTime(@RequestBody delMat delmat) {
		String [] time = delmat.getName().split(",");
		String [] result = MaterialSystem.getMatOutInfoViaTime(time[0],time[1],time[2]);
		if(result == null) {
			System.out.println("没有该时间段的入库");
			return null;
		}
		else {
			System.out.println("入库数量："+result.length);
			return result;
		}
	}
	
	@RequestMapping(value="/Material_Staff.getMatDelViaFzr",method=RequestMethod.POST)
	@ResponseBody String[] getMatDelViaFzr(@RequestBody delMat delmat) {
		String fzr = delmat.getName();
		String [] result = MaterialSystem.getMatDelInfoViaFzr(fzr);
		if(result == null) {
			System.out.println("没有该时间段的销毁");
			return null;
		}
		else {
			System.out.println("xiaohui数量："+result.length);
			return result;
		}
	}
	
	@RequestMapping(value="/Material_Staff.getMatDelViaTime",method=RequestMethod.POST)
	@ResponseBody String[] getMatDelViaTime(@RequestBody delMat delmat) {
		String [] time = delmat.getName().split(",");
		String [] result = MaterialSystem.getMatDelInfoViaTime(time[0],time[1],time[2]);
		if(result == null) {
			System.out.println("没有该时间段的入库");
			return null;
		}
		else {
			System.out.println("入库数量："+result.length);
			return result;
		}
	}
	
	@RequestMapping(value="/GongHuoShang.selectGongHuoShang",method=RequestMethod.POST)
	@ResponseBody String[][] GongHuoShangSelectGongHuoShang(@RequestBody Dingdan dingdan) {
		//System.out.println(dingdan.getKehumingchen());
		return MaterialSystem.GongHuoShangSelectGongHuoShang(dingdan.getKehuID() , dingdan.getKehumingchen());
	}
	@RequestMapping(value="/GongHuoShang.selectAllGongHuoShang",method=RequestMethod.POST)
	@ResponseBody String[][] GongHuoShangSelectAllGongHuoShang() {
		return MaterialSystem.GongHuoShangSelectAllGongHuoShang();
	}
}
