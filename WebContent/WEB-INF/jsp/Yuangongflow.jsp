<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.bll.systems.*,java.util.Map,java.util.*,java.text.*,java.io.*,java.text.SimpleDateFormat,java.util.Date" %>

<jsp:useBean id="command" class="model.bll.objects.Staff" scope="request"></jsp:useBean>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>

    <link rel="stylesheet" href="layui/css/layui.css">
    <script src="layui/layui.js"></script>
</head>
<style>
	.layui-body {
	   left:0;
	}
	.layui-side {
	   width:0;
	}
	.layui-side-scroll {
	   width:0;
	}
	.layui-table-cell {
	   height: 30px;
	   line-height:30px;
	}
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
	<div class="layui-layout layui-layout-admin">
		<div class="layui-side" aria-hidden="true" hidden="true"> </div>

		<div class="layui-body" >
			<form class="layui-form" action="" id="myForm">
  				<div class="layui-form-item">
    				<label class="layui-form-label">员工ID</label>
    				<div class="layui-input-block" >
      					<select name="yuangongID" lay-filter="yuangongID" id="yuangongID" required lay-verify="required" >
					        <%String[]keys = HumanSystem.selectAllId();
					        Date date = new Date();
					        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
					        String year = sdf.format(date);
					        
					        for(Object key:keys){
					        	String s = (String)key;
								while(s.length() < 3) {
					    			s = "0" + s;
					    		}%>
								
							<option value="<%=key%>"><%= year+s %></option>
						<%} %>
					    </select>
    				</div>
  				</div>
				
				<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:1000px; height:1000px;">
	        			 </table>
	    			</div>
    			</div>
		</form>
 
	  </div>
	</div>
<!--<script src="../src/layui.js"></script>-->
</body>

<script src="layui/layui.js"></script>
<script>
//表格数据缓存
	var AddData=[];
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form','laydate'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate=layui.laydate;
		element.init();
		
		function flushTable(AddData) {
			table.render({
			    elem: '#Table',
			    height:400
			// ,url:'/demo/table/user/'
			    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
			      layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
			      //,curr: 5 //设定初始在第 5 页
			      ,groups: 1 //只显示 1 个连续页码
			      ,first: false //不显示首页
			      ,last: false //不显示尾页
			      ,limit: 20
			    }
			    ,cols: [[
			    	{field:'No',  width:'8%', title: 'No.', sort:true}
			      ,	{field:'订单ID',  width:'15%', title: '订单ID'}
			      ,{field:'应付金额', width:'20%', title: '应付金额'}
			      ,{field:'实付金额', width:'20%', title: '实付金额'}
			      ,{field:'下单时间', width:'20%', title: '下单时间', sort:true}
			      ,{field:'订单类别', width:'17%', title: '订单类别'}
			    ]],
			    data:AddData
			});
		}
		flushTable(AddData);
		
		laydate.render({elem: '#tihuo1'});
		
		form.on('select(yuangongID)', function(data){
        	var message=$("select[id=yuangongID").val();
        	console.log(message);
        	console.log("asdfasdf");
         	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Yuangongflow.do',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : message,
                success : function(data) {
                	//console.log(data);
                	var AddData=[];
                	for(i = 0; i < data.length; i++) {
                		var da={No:i+1, 
                				订单ID:changeToCode(data[i][0]),
                				应付金额:data[i][1],
                				实付金额:data[i][2],
                				下单时间:data[i][3],
                				订单类别:data[i][4]};
            	        AddData.push(da);
                	}
                	flushTable(AddData);
                },error:function(s) {
                	console.log("err");
                }
      		});
        	
        	return false;
        });
		
		function changeToCode(s) {
        	s = pad(s, 3);
        	s = year() + s;
        	return s;
        }
        
        function changeToID(s) {
        	s = s.substring(4);
        	while(s.startsWith("0"))
        		s = s.substring(1);
        	return s;
        }
        
        function pad(num, n) {
       	  var len = num.toString().length;
       	  while(len < n) {
       	    num = "0" + num;
       	    len++;
       	  }
       	  return num;
       	}
        function year() {
        	var myDate = new Date();
            return myDate.getFullYear();
        }
    });
</script>
</html>
