<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.bll.systems.*,java.util.Map,java.util.*,java.text.*,java.io.*" %>

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

.layui-textarea {
	height:20px;
	width:200px;
}
.layui-form-select dl { 
	max-height:200px; 
}
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
<div class="layui-layout layui-layout-admin">
<div class="layui-side" aria-hidden="true" hidden="true"> 

</div>

<div class="layui-body" >
<div class="layui-fluid"> 
			
		<form class="layui-form" action="" id="form1">
			<div class="layui-form-item">
				<div id="Gl_Table">
        			 <table 
        				class="layui-hide" lay-filter="Table" 
        				id="Table" style="max-height:500px; height:500px;">
        			 </table> 
        			 
    			</div>
   			</div>
   			<div class="layui-form-item">
   				<label  class="layui-form-label">车间编号</label>
      			<div class="layui-input-block" >
	      			<select id="cjid" name="cjid" required lay-verify="required" >
		     		<%int [] work= WorkSystem.selectAllWork();
		     			if(work!=null){
		     				
		     				for(int key : work){%>
		     					<option value=<%=key%>><%=key%></option>
		     		<%}}
		     		%> 
		     		</select>
	     		</div>
	     	</div>
	     	<div class="layui-form-item">
	     		<div class="layui-input-block" >
	     			<button class="layui-btn" id = "btn-add" lay-submit lay-filter="formDemo">查询该车间今日计划</button>
	     		</div>
	     	</div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
var AddData=[
    { 产品名称:"",产品数量:""},
     ];
layui.use(['form','table','layer'],function(){
	var form = layui.form;
	var layer = layui.layer;
	var table = layui.table;
	$ = layui.jquery;
	//表格渲染
	table.render({
	    elem: '#Table',
	    height:200
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
	    	//{field:'No',  width:'20%', title: 'No.', sort:true},
	      	{field:'产品名称',  width:'50%', title: '产品名称',edit: 'text'}
	      ,{field:'产品数量', width:'50%', title: '产品数量',edit: 'text'}
	    ]],
	    data:AddData
	});
	form.on('submit(formDemo)', function(data){
		var da = {
				name:data.field.cjid
		};
		$.ajax({
			url:'${pageContext.request.contextPath}/dayPlan.getDayPlan',
	        //在此继续
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){  
	        	if(res!=null&&res.length!=0){
		        	for(key in res){
		        		var data1={产品名称:key,产品数量:res[key]};
		        		window.AddData.unshift(data1);
		        	}
		        	table.reload('Table',{
						data : window.AddData
					});
	        	}
	        	else{
	        		alert("今日本车间没有生产计划！");
	        	}
	        },error:function(s){
	        	alert("系统出错！");
	        }
		});
		
		return false;
	});
	
});
</script>
   
</div>
</body>
</html>
