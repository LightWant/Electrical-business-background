package controller;
import java.util.ArrayList;
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
public class ProductController {
	@RequestMapping(value="/addPro",method=RequestMethod.GET)
	public String getAddPro() {
		return "addPro";
	}
	
	@RequestMapping(value="/delPro",method=RequestMethod.GET)
	public String getDelpro() {
		return "delPro";
	}
	
	@RequestMapping(value="/infoPro",method=RequestMethod.GET)
	public String getInfoPro() {
		return "infoPro";
	}
	
	@RequestMapping(value="/expPro",method=RequestMethod.GET)
	public String getExpPro() {
		return "expPro";
	}
	@RequestMapping(value="/proInStore",method=RequestMethod.GET)
	public String proInStore() {
		return "proInStore";
	}
	@RequestMapping(value="/proOutStorage",method=RequestMethod.GET)
	public String proOutStore() {
		return "proOutStorage";
	}
	@RequestMapping(value="/proStorage",method=RequestMethod.GET)
	public String proStorage() {
		return "proStorage";
	}
	@RequestMapping(value="/infoChangePro",method=RequestMethod.GET)
	public String infoChangePro() {
		return "infoChangePro";
	}
	@RequestMapping(value="/proInMes",method=RequestMethod.GET)
	public String proInMes() {
		return "proInMes";
	}
	
	@RequestMapping(value="/proOutMes",method=RequestMethod.GET)
	public String proOutMes() {
		return "proOutMes";
	}
	
	@RequestMapping(value="/proDelMes",method=RequestMethod.GET)
	public String proDelMes() {
		return "proDelMes";
	}
	@RequestMapping(value="/peifangInfo",method=RequestMethod.GET)
	public String peifangInfo() {
		return "peifangInfo";
	}
	//POST
	@RequestMapping(value="/Product_Staff.addPro",method=RequestMethod.POST)
	public @ResponseBody Integer addPro(@RequestBody Product product) {
		String name = product.getMingcheng();
		String danwei = product.getDanwei();
		Integer dbgg = product.getDbgg();
		Integer bzq = product.getBaozhiqi();
		Float price = product.getPrice();
		String ylname = product.getYlname();
		String ylnum = product.getYlnum();
		String [] ylnames = ylname.split(",");
		
		String [] ylnums = ylnum.split(",");
		//int addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi,String dw) {
		return ProductSystem.addProduct(name, ylnames, ylnums, dbgg, price, bzq, danwei);
	}
	@RequestMapping(value="/Product_Staff.getRkpc",method=RequestMethod.POST)
	public @ResponseBody Integer[] getRkpc(@RequestBody delMat proname) {
		String name = proname.getName();
		Integer id = ProductSystem.findProduct(name);
		Integer rkpc [] = ProductSystem.getRkpc(id);
		return rkpc;
	}
	
	@RequestMapping(value="/Product_Staff.getName",method=RequestMethod.POST)
	public @ResponseBody String[] getName(@RequestBody delMat proname) {
		Map<Integer,String> map = ProductSystem.selectAllProduct();
		if(map == null||map.size() == 0)return null;
		Object[] keys = map.keySet().toArray();
		String names[] = new String[keys.length];
		for(int i = 0;i < keys.length;i++) {
			names[i] = map.get(keys[i]);
		}
		return names;
 	}
	
	@RequestMapping(value="/Product_Staff.destroyRkpc",method=RequestMethod.POST)
	public @ResponseBody int destroyRkpc(HttpSession session,@RequestBody delMat rkpcstr) {
		Integer rkpc = Integer.parseInt(rkpcstr.getName());
		Staff staff = (Staff) session.getAttribute("staff");
		//ProductSystem ps = (ProductSystem)session.getAttribute("productsystem");
		ProductSystem ps = new ProductSystem(staff);
		
		return ps.destroyFromStorage(rkpc);
	}
	@RequestMapping(value="/Product_Staff.getProInfo",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String getProInfo(@RequestBody delMat proname) {
		String name = proname.getName();
		Integer id = ProductSystem.findProduct(name);
		String result = ProductSystem.selectAllInfo(id);
		return result;
	}
	@RequestMapping(value="/Product_Staff.getAllProInfo",method=RequestMethod.POST)
	public @ResponseBody String [] getAllProInfo(@RequestBody delMat proid) {
		return ProductSystem.selectAllProInfo();
	}
	@RequestMapping(value="/Product_Staff.findscpc",method=RequestMethod.POST)
	public @ResponseBody Integer[] findscpc(@RequestBody delMat proname) {
		Integer id = ProductSystem.findProduct(proname.getName());
		return ProductSystem.getScpc(id);
	}
	@RequestMapping(value="/Product_Staff.instore",method=RequestMethod.POST)
	public @ResponseBody String instore(HttpSession session,@RequestBody inStore cprk) {
		String cpname = cprk.getYlname();
		System.out.println("cpname"+cpname);
		Integer scpc = cprk.getJhpc();
		System.out.println("scpc"+scpc);
		Integer num = cprk.getNum();
		System.out.println("num"+num);
		Integer ckid = cprk.getCkid();
		System.out.println("ckid"+ckid);
		//ProductSystem ps = (ProductSystem)session.getAttribute("productsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		ProductSystem ps = new ProductSystem(staff);
		return ps.inStorage(scpc,ckid,num);
		
	}
	@RequestMapping(value="/Product_Staff.finddd",method=RequestMethod.POST)
	public @ResponseBody Map<String,Integer> finddxj(@RequestBody delMat ddidstr) {
		Integer ddid = Integer.parseInt(ddidstr.getName());
		System.out.println("findddxj");
		Map<String,Integer> map = ProductSystem.getDdxj(ddid);
		
		if(map==null||map.size() == 0) {
			System.out.println("订单"+ddid+"没有订单细节");
			return null;
		}
		System.out.println("map size:"+map.size());
		return map;
	}
	@RequestMapping(value="/Product_Staff.ddjf",method=RequestMethod.POST)
	public @ResponseBody String [] ddjf(@RequestBody delMat ddidstr) {
		//判断能否交付订单
		System.out.println("ddjf");
		Integer ddid = Integer.parseInt(ddidstr.getName());
		if(!ProductSystem.IsFinDingdan(ddid))
			return null;
		return ProductSystem.jiaofuProduct(ddid);
	}
	//确认交付
	@RequestMapping(value="/Product_Staff.asurejf",method=RequestMethod.POST)
	public @ResponseBody boolean asurejf(HttpSession session,@RequestBody delMat ddidstr) {
		//ProductSystem ps = (ProductSystem) session.getAttribute("productsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		ProductSystem ps = new ProductSystem(new Staff(1,MD5.encrypt("123456")));
		Integer ddid = Integer.parseInt(ddidstr.getName());
		String [] jf = ProductSystem.jiaofuProduct(ddid);
		//outStorage(int rkpcID,int ddID,int num)
		for(int i = 0;i < jf.length ;i++) {
			
			String [] parm = jf[i].split(",");
			int state = ps.outStorage(Integer.parseInt(parm[1]),ddid,Integer.parseInt(parm[2]));
			if(state < 0 )return false;
		}
		return ProductSystem.overDD(ddid);
	}
	@RequestMapping(value="/Product_Staff.getexpire",method=RequestMethod.POST)
	public @ResponseBody String []  getexpire(@RequestBody delMat nouse) {
		String [][]result = ProductSystem.expire();
		if(result == null||result.length == 0)return null;
		String [] newresult = new String[result.length];
		for(int i = 0 ;i < result.length; i++) {
			newresult[i] = result[i][0]+","+result[i][1]+","+result[i][2]+","+result[i][3];
			System.out.println("过期:"+newresult[i]);
		}
		return newresult;
	}
	
	@RequestMapping(value="/Product_Staff.destroyAll",method=RequestMethod.POST)
	public @ResponseBody String destroyAll(HttpSession session) {
		//boolean destroy(int []rukupiciID)
		//ProductSystem ps = (ProductSystem) session.getAttribute("productsystem");
		Staff staff = (Staff) session.getAttribute("staff");
		ProductSystem ps = new ProductSystem(staff);
		String [][] expire = ProductSystem.expire();
		if(expire==null||expire.length == 0)return "0";//没有过期
		Integer[] rkpc = new Integer[expire.length];
		for(int i = 0;i < expire.length;i++) {
			rkpc[i] = Integer.parseInt(expire[i][0]);
		}
		boolean flag = ps.destroy(rkpc);//false插入失败
		if(!flag) return "no";
		return ""+ps.getStaff().getId();
	}
	
	@RequestMapping(value="/Product_Staff.getddid",method=RequestMethod.POST)
	public @ResponseBody Integer []  getDDid(@RequestBody delMat nouse) {
		return ProductSystem.getDingdanId();
	}
	@RequestMapping(value="/Product_Staff.changeinfo",method=RequestMethod.POST)
	public @ResponseBody boolean changeinfo(@RequestBody delMat info) {
		String cinfo = info.getName();
		String [] content = cinfo.split(",");
		int cpid = ProductSystem.findProduct(content[0]);
		String [] set = {content[1]};
		String [] setvalues = {content[2]};
		return ProductSystem.changeProduct(cpid,set, setvalues);
	}
	@RequestMapping(value="/Product_Staff.changePeifang",method=RequestMethod.POST)
	public @ResponseBody boolean changePeifang(@RequestBody Product product) {
		String name = product.getMingcheng();
		int cpid = ProductSystem.findProduct(name);
		String ylname = product.getYlname();
		String ylnumstr = product.getYlnum();
		String [] ylnames = ylname.split(",");
		String [] ylnums = ylnumstr.split(",");
		Integer[] ylid = new Integer[ylnames.length];
		Integer[] ylnum = new Integer[ylnums.length];
		for(int i = 0;i<ylnames.length;i++) {
			ylid[i] = MaterialSystem.findMaterialID(ylnames[i]);
			ylnum[i] = Integer.parseInt(ylnums[i]);
		}
		
		
		//int addProduct(String name,int [] yuanliao,int[] ynum,int guige,float price,int baozhiqi,String dw) {
		return ProductSystem.changePeiFang(cpid,ylid,ylnum) ;
		// 
	}
	@RequestMapping(value="/Product_Staff.selRkpcInfo",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String selRkpcInfo(@RequestBody delMat rkpc) {
		//入库批次，产品ID，产品名称，入库数量，负责人，时间，剩余
		
		int rkpcid= Integer.parseInt(rkpc.getName());
		
		String result = ProductSystem.getRkpcInfo(rkpcid);
		if(result!=null)
			System.out.println("rkpc = "+rkpc+",的入库细节是："+result);
		else
			System.out.println("rkpc = "+rkpc+",查询入库细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selRkpcInfoviafzr",method=RequestMethod.POST)
	public @ResponseBody String[] selRkpcInfoViaFzr(@RequestBody delMat fzr) {
		int fzrid = Integer.parseInt(fzr.getName());
		String [] result = ProductSystem.getRkpcInfoViaFzr(fzrid);
		if(result!=null)
			System.out.println("fzrid = "+fzrid+",的入库细节数量："+result.length);
		else
			System.out.println("fzrid = "+fzrid+",查询入库细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selRkpcInfoviaTime",method=RequestMethod.POST)
	public @ResponseBody String[] selRkpcInfoviaTime(@RequestBody delMat time) {
		String [] info = time.getName().split(",");
		String begin = info[0];
		String end = info[1];
		String [] result = ProductSystem.getRkpcInfoViaTime(begin, end);
		if(result!=null)
			System.out.println("time ,的入库细节数量："+result.length);
		else
			System.out.println("time ,查询入库细节失败");
		return result;
	}
	
	@RequestMapping(value="/Product_Staff.selCkpcInfoViaDD",method=RequestMethod.POST)
	public @ResponseBody String [] selCkpcInfoViaDD(@RequestBody delMat dd) {
	
		int ddid= Integer.parseInt(dd.getName());
		
		String [] result = ProductSystem.getCkpcInfoViaDD(ddid);
		if(result!=null)
			System.out.println("dd = "+dd+",的出库细节数量："+result.length);
		else
			System.out.println("dd = "+dd+",查询出库细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selCkpcInfoviafzr",method=RequestMethod.POST)
	public @ResponseBody String[] selCkpcInfoviafzr(@RequestBody delMat fzr) {
		int fzrid = Integer.parseInt(fzr.getName());
		String [] result = ProductSystem.getCkpcInfoViaFzr(fzrid);
		if(result!=null)
			System.out.println("fzrid = "+fzrid+",的chu库细节数量："+result.length);
		else
			System.out.println("fzrid = "+fzrid+",查询chu库细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selCkpcInfoviaTime",method=RequestMethod.POST)
	public @ResponseBody String[] selCkpcInfoviaTime(@RequestBody delMat time) {
		String [] info = time.getName().split(",");
		String begin = info[0];
		String end = info[1];
		String [] result = ProductSystem.getCkpcInfoViaTime(begin, end);
		if(result!=null)
			System.out.println("time ,的chu库细节数量："+result.length);
		else
			System.out.println("time ,查询chu库细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selXhInfo",method=RequestMethod.POST,produces="text/plain;charset=utf-8")
	public @ResponseBody String selXhInfo(@RequestBody delMat xhpc) {
		int pc = Integer.parseInt(xhpc.getName());
		String result = ProductSystem.getXhInfo(pc);
		if(result == null)
			System.out.println("销毁批次信息查询失败,没有该销毁批次");
		else
			System.out.println("销毁信息："+result);
		return result;
	}
	@RequestMapping(value="/Product_Staff.selXhViaFzr",method=RequestMethod.POST)
	public @ResponseBody String[] selXhViaFzr(@RequestBody delMat fzr) {
		int fzrid = Integer.parseInt(fzr.getName());
		String [] result = ProductSystem.getXhInfoViaFzr(fzrid);
		if(result!=null)
			System.out.println("fzrid = "+fzrid+",的销毁细节数量："+result.length);
		else
			System.out.println("fzrid = "+fzrid+",查询xiaohui细节失败");
		return result;
	}
	@RequestMapping(value="/Product_Staff.selXhViaTime",method=RequestMethod.POST)
	public @ResponseBody String[] selXhViaTime(@RequestBody delMat time) {
		String [] info = time.getName().split(",");
		String ckid = info[0];
		System.out.println("ckid = "+ckid);
		String begin = info[1];
		String end = info[2];
		if(!ckid.equals("全部")) {
			String [] result = ProductSystem.getXhjlViaTime(ckid,begin, end);
			if(result!=null)
				System.out.println("time ,的chu库细节数量："+result.length);
			else
				System.out.println("time ,查询chu库细节失败");
			return result;
		}
		else {
			Integer[] ckids = ProductSystem.selectAllStorageID();
			ArrayList<String>all = new ArrayList<String>();
			for(int i = 0;i<ckids.length;i++) {
				String ckstr = ""+ckids[i];
				String [] result = ProductSystem.getXhjlViaTime(ckstr,begin, end);
				if(result == null || result.length == 0)
					continue;
				for(int j = 0;j < result.length ;j++) {
					all.add(result[j]);
				}
			}
			String [] allall = new String[all.size()];
			for(int i = 0;i < all.size();i++) {
				allall[i] = all.get(i);
			}
			return allall;
		}
	}
	@RequestMapping(value="/Product_Staff.getPfInfo",method=RequestMethod.POST)
	public @ResponseBody String[] getPfInfo(@RequestBody delMat cp) {
		String cpid = cp.getName();

		String [] result = ProductSystem.getPeiFang(cpid);
		if(result == null||result.length == 0) return null;
		for(int i = 0;i < result.length ;i++) {
			System.out.println(result[i]);
		}
		return result;
	}
	
}
