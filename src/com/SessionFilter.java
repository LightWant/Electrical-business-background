package com;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import model.bll.objects.Staff;
import model.bll.objects.User;


public class SessionFilter extends OncePerRequestFilter{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
    	//不过滤的uri
    	String[] Dynamic = {
    			"/FoodFactory/Sold_staff",
    			"/FoodFactory/createOrder",
    			"/FoodFactory/tihuo",
				"/FoodFactory/tuihuo",
				"/FoodFactory/buhuo",
				"/FoodFactory/SoldPlan",
				"/FoodFactory/fixPlan",
				"/FoodFactory/createNewOrder",
				"/FoodFactory/selectDingDan",
				"/FoodFactory/YuanGong",
				"/FoodFactory/Finance_staff",
				"/FoodFactory/Yuangongflow",
				"/FoodFactory/Dingdanflow",
				"/FoodFactory/Jinhuoflow",
				"/FoodFactory/MoneySum",
				"/FoodFactory/logout",
				"/FoodFactory/Plan_staff",
				"/FoodFactory/WorkPlan",
				"/FoodFactory/selectShengChanPlan",
				"/FoodFactory/selectKeHu",
				"/FoodFactory/MyStaff",
				"/FoodFactory/delMat",
				"/FoodFactory/addMat",
				"/FoodFactory/matInfo",
				"/FoodFactory/expireMat",
				"/FoodFactory/stock",
				"/FoodFactory/inStorage",
				"/FoodFactory/outStorage",
				"/FoodFactory/addStorage",
				"/FoodFactory/matStockMes",
				"/FoodFactory/matInMes",
				"/FoodFactory/matOutMes",
				"/FoodFactory/matDelMes",
				"/FoodFactory/addPro",
				"/FoodFactory/delPro",
				"/FoodFactory/infoPro",
				"/FoodFactory/expPro",
				"/FoodFactory/proInStore",
				"/FoodFactory/proOutStorage",
				"/FoodFactory/proStorage",
				"/FoodFactory/infoChangePro",
				"/FoodFactory/proInMes",
				"/FoodFactory/proOutMes",
				"/FoodFactory/proDelMes",
				"/FoodFactory/Create",
				"/FoodFactory/dayPlan",
				"/FoodFactory/workInfo",
				"/FoodFactory/workInfoChange",
				"/FoodFactory/produceInfo",
				"/FoodFactory/changeRole",
				"/FoodFactory/changeStaffInfo",
				"/FoodFactory/staffInfo",
				"/FoodFactory/inStaff",
				"/FoodFactory/changeRoleInfo",
				"/FoodFactory/peifangInfo",
				"/FoodFactory/welcome",
				"/FoodFactory/GongHuoShang",
				"/FoodFactory/ShengChanJinDu",
				"/FoodFactory/zhijianInfo",
				"/FoodFactory/peifangInfo",
				"/FoodFactory/Work_Staff",
				"/FoodFactory/selectShouKuan",
				"/FoodFactory/infoRole",
				"/FoodFactory/selectTuiHuo"
    	};
        String[] Static = 
        		new String[]{"/FoodFactory/login",
        				"/FoodFactory/js/",
        				"/FoodFactory/layui",
        				"/FoodFactory/html/",
        				"/FoodFactory/jsonsource",
        				"/FoodFactory/image/",
        				"/FoodFactory/json"
        				};

      //请求的uri
        String uri = request.getRequestURI();
      //静态资源直接放行
        for(String s : Static){
            if(uri.indexOf(s) != -1){
                //uri中包含不过滤uri，则不进行过滤
                int ed = uri.indexOf("?");
                if(ed != -1) {
                	uri = uri.substring(0, ed);
                	System.out.println("ed :" + ed);
                }
                filterChain.doFilter(request, response);
                //System.out.println("static uri :" + uri);
                return;
            }
        }
        
        System.out.println("filter>>>uri>>>"+uri);
        
        //是否过滤
        boolean doFilter = true;
        for(String s : Dynamic) {
            if(uri.indexOf(s) != -1){
                //uri中包含不过滤uri，则不进行过滤
                doFilter = false;
                int ed = uri.indexOf("?");
                if(ed != -1) {
                	uri = uri.substring(0, ed);
                	System.out.println("ed :" + ed);
                }
              //  System.out.println("uri :" + uri);
                break;
            }
        }
        
        //是否过期
		Boolean flag = false;
		Object obj = request.getSession().getAttribute("staff");
		if(obj == null) {
			flag = true;
			System.out.println("obj NULL!");
		}

        if(doFilter || flag){
            System.out.println("doFilter>>>");
            //过滤操作
            //从session中获取登陆者实体
            if(flag){
                System.out.println("doFilter>>>obj is null");
                boolean isAjaxRequest = isAjaxRequest(request);
                if(isAjaxRequest || uri.endsWith(".toGetJSP")) {
                	System.out.println("doFilter>>>ajax! or JSP!");
                    response.setCharacterEncoding("UTF-8");
                    response.sendError(HttpStatus.UNAUTHORIZED.value(),"您已经太长时间没有操作,请刷新页面");
                    return ;
                }else{
                    System.out.println("doFilter>>>http request");
                    response.sendRedirect("./login");
                    //跳转到登录页面，将来改成login
                    return ;
                }
            }else{
                Staff staff = (Staff) obj;
                System.out.println("doFilter>>>username>>"+staff.getMingchen());
                  // 如果session中存在登录者实体，则继续  
                filterChain.doFilter(request, response);
            }
        }else{

            System.out.println("no Filter>>> ");
            //不执行过滤操作
            filterChain.doFilter(request, response);
        }
    }
    /**
     * is Ajax request
     * @param request
     * @return
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String header = request.getHeader("X-Requested-With");
        if(header != null && "XMLHttpRequest".equals(header)){
            //ajax request
            return true;
        }else{
            //traditional sync http request
            return false;
        }
    }

}
