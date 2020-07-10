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
				<div class="layui-inline">
					 <label class="layui-form-label">车间ID</label>
					 <div class="layui-input-inline">
					 <select id="cjid" name="cjid"  required lay-verify="required" >
						 <option value="全部" selected>全部</option>
						 <%int [] cj = WorkSystem.selectAllWork();
						 if(cj!=null&&cj.length != 0){
							 for(int i = 0;i<cj.length;i++){%>
								<option value=<%=cj[i]%>><%=cj[i] %></option>
							 <%}} %>
					 </select>
					 </div>
					 <button class="layui-btn" lay-submit lay-filter="formDemo1">查询该车间信息</button>
				</div>
			</div>
			<div class="layui-form-item">
				<div id="Gl_Table">
        			 <table 
        				class="layui-hide" lay-filter="Table" 
        				id="Table" style="max-height:500px; height:500px;">
        			 </table> 
        			 
    			</div>
   			</div>
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
var AddData=[];
layui.use(['form','layer','table','laydate'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var laydate = layui.laydate;
  var table = layui.table;
  $ = layui.jquery;
//表格渲染

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
	    	//车间编号,车间主任编号,车间状态,生产产品名称,该产品最大产能,单位
	    	//{field:'No',  width:'20%', title: 'No.', sort:true},
	    	{field:'车间编号', width:'16%', title: '车间编号'}
	      ,{field:'车间主任编号',  width:'18%', title: '车间主任编号'}
	      ,{field:'车间状态',  width:'16%', title: '车间状态'}
	      ,{field:'生产产品名称', width:'16%', title: '生产产品名称'}
	      ,{field:'该产品最大产能', width:'18%', title: '该产品最大产能'}
	      ,{field:'单位', width:'16%', title: '单位'}
	    ]],
	    data:AddData
	});
//显示
  form.on('submit(formDemo1)', function(data){
	  //原料信息
	  var da={
				name:document.getElementById("cjid").value
		};
	  //alert(JSON.stringify(data));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/workInfo.getWorkInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(res){   
	        	window.AddData = []
	        	if(res!=null&&res.length!=0){
		        	for(var i = 0;i<res.length;i++){
		        		var get = res[i].split(",");
		        		var data1={
		        				车间编号:get[0],车间主任编号:changeToCode(get[1]),车间状态:get[2],生产产品名称:get[3],该产品最大产能:get[4],单位:get[5]
		        		};
		        		window.AddData.unshift(data1);
		        	}
		        	table.reload('Table',{
						data : window.AddData
					});
	        	}
	        },
            error:function (data) {
                alert("系统出错!");
            }           
	    }) ;
    // return "addMat";
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
   
</div>
</body>
</html>
