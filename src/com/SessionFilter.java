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
        
    	//�����˵�uri
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

      //�����uri
        String uri = request.getRequestURI();
      //��̬��Դֱ�ӷ���
        for(String s : Static){
            if(uri.indexOf(s) != -1){
                //uri�а���������uri���򲻽��й���
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
        
        //�Ƿ����
        boolean doFilter = true;
        for(String s : Dynamic) {
            if(uri.indexOf(s) != -1){
                //uri�а���������uri���򲻽��й���
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
        
        //�Ƿ����
		Boolean flag = false;
		Object obj = request.getSession().getAttribute("staff");
		if(obj == null) {
			flag = true;
			System.out.println("obj NULL!");
		}

        if(doFilter || flag){
            System.out.println("doFilter>>>");
            //���˲���
            //��session�л�ȡ��½��ʵ��
            if(flag){
                System.out.println("doFilter>>>obj is null");
                boolean isAjaxRequest = isAjaxRequest(request);
                if(isAjaxRequest || uri.endsWith(".toGetJSP")) {
                	System.out.println("doFilter>>>ajax! or JSP!");
                    response.setCharacterEncoding("UTF-8");
                    response.sendError(HttpStatus.UNAUTHORIZED.value(),"���Ѿ�̫��ʱ��û�в���,��ˢ��ҳ��");
                    return ;
                }else{
                    System.out.println("doFilter>>>http request");
                    response.sendRedirect("./login");
                    //��ת����¼ҳ�棬�����ĳ�login
                    return ;
                }
            }else{
                Staff staff = (Staff) obj;
                System.out.println("doFilter>>>username>>"+staff.getMingchen());
                  // ���session�д��ڵ�¼��ʵ�壬�����  
                filterChain.doFilter(request, response);
            }
        }else{

            System.out.println("no Filter>>> ");
            //��ִ�й��˲���
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
