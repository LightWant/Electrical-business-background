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
	
		
		<form class="layui-form" action="" id="form2">
			<div class="layui-form-item">
				<label class="layui-form-label" style="font-20px">查询配方</label>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="flb1">产品名称</label>
				  <div class="layui-input-block">
					
						<select id="fdname1" name="fdname1"  required lay-verify="required" >
						<%Map<Integer,String> map = ProductSystem.selectAllProduct();
						if(map!=null&&map.size()!=0){
							Object[]keys = map.keySet().toArray();
						
							for(Object key:keys){%>
							<option value=<%=key%>><%=map.get(key)%></option>
						<%}}
						%>
					   </select>
				  </div>
			</div>
			  
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo1">查询该产品信息</button>
				</div>
			</div>
			<div class="layui-form-item">
				<div id="Gl_Table">
        			<table 
        				class="layui-hide" lay-filter="Table" 
        				id="Table" style="max-height:600px; height:600px;">
        			 </table>
    			</div>
   			</div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
AddData = [];
layui.use(['form','layer','table'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var table = layui.table;
  $ = layui.jquery;
 
  table.render({
	    elem: '#Table',
	    height:300
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
	    	//产品名称,产品单位,原料名称,原料数量,原料单位
	      , {field:'产品名称', width:'20%', title: '产品名称'}
	      , {field:'产品单位', width:'20%', title: '产品单位'}
	      , {field:'原料名称', width:'20%', title: '原料名称'}
	      , {field:'原料数量', width:'20%', title: '原料数量'}
	      , {field:'原料单位', width:'20%', title: '原料单位'}
	    ]],
	    data:AddData
	  });
  
  form.on('submit(formDemo1)', function(data){
	  //原料信息
	  var da={
				name:document.getElementById("fdname1").value
		};
	  
	  //console.log(JSON.stringify(da));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.getPfInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(data){       
	            
            	 //弹出原料信息
	        	window.AddData=[];
            	if(data!=null && data.length > 0){
            		for(var i = 0;i < data.length;i++){
	                	var get = data[i].split(",");
	                	var da = {
	                			产品名称:get[0],产品单位:get[1],原料名称:get[2],原料数量:get[3],原料单位:get[4]
	                	};
	                	
		        		window.AddData.unshift(da);
            		}
            	}else{
            		layer.msg('该产品暂无配方');
            	}
   				table.reload('Table',{
   					data : window.AddData
   				});
				form.render();
	        },
            error:function (data) {
                alert("系统出错!")
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
