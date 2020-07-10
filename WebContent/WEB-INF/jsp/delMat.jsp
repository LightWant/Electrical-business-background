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
		  	  	<label class="layui-form-label" style="font-20px">原料销毁</label>
		  	  </div>
		  	 
			  <div class="layui-form-item">
					<label class="layui-form-label" id = "deslb" >原料名称</label>
					<div class="layui-input-block" >
						<select id="desname" name="desname"  required lay-verify="required"  lay-filter="selMat">
						<%String[]names = MaterialSystem.selectAllMaterialName();
						if(names!=null)
						for(String key:names){%>
							<option value=<%=key%>><%=key %></option>
						<%}%>
						</select>
				   	</div>
			   </div>
			   
			   <div class="layui-form-item">
				   	<label class="layui-form-label" id = "rkpclb" >入库批次</label>
					<div class="layui-input-block" >
				     	 <select  required lay-verify="required" id="rkpc" name="rkpc">
				     		
				     	</select>
				   	</div>
			   	</div>
			           
			  
			  <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn" lay-submit lay-filter="formDemo">销毁</button>
			      <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
			    </div>
			  </div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
layui.use(['form','layer'], function(){
  var form = layui.form;
  var layer = layui.layer;
  $ = layui.jquery;
  //var checkStatus1 = table.checkStatus('addMattable');
  //var data = checkStatus1.data;
  //监听提交
  form.on('select(selMat)',function(data){
	  var da={
			  name:data.value//选择的原料名称
	  };
	  $.ajax({
		  url:'${pageContext.request.contextPath}/getRkpc',     
	      type :'post',       
	      contentType : 'application/json;charset=utf-8',
	      data:JSON.stringify(da),
		  success:function(res){   
			  $("#rkpc").empty();
				  if(res==null||res.length == 0){
					  $("#rkpc").empty();
			        	 layer.alert("该原料已无可销毁库存！");
				  }
				  else{
					var i;
		        	for(i = 0;i<res.length;i++){
		        		$("#rkpc").append(new Option(res[i],res[i]));
		        	}
				  }
				  layui.form.render("select");//重新渲染select
	         },      
	         
	         error:function (data) {
	        	 layer.alert("系统出错！");
	         }      
	  });
	  return false;
  })

  form.on('submit(formDemo)',function(data){
	  if(document.getElementById("rkpc").value==null||document.getElementById("rkpc").value==""){
		  layer.alert("请先选择入库批次！");
		  return false;
	  }
	  var da = {
			  name:document.getElementById("rkpc").value
	  };
	  $.ajax({
		  url:'${pageContext.request.contextPath}/destroyRkpc',     
	      type :'post',       
	      contentType : 'application/json;charset=utf-8',
	      data:JSON.stringify(da),
		  success:function(res){   
			  layer.open({
				  content:"该入库批次的原料销毁成功！"
			  });
			  var dad = {
					  name:data.field.desname
			  };
			  $.ajax({
				  url:'${pageContext.request.contextPath}/getRkpc',     
			      type :'post',       
			      contentType : 'application/json;charset=utf-8',
			      data:JSON.stringify(dad),
				  success:function(res){   
					  $("#rkpc").empty();
						  if(res==null||res.length == 0){
							  $("#rkpc").empty();
					        	 layer.alert("该原料已无可销毁库存！");
						  }
						  else{
							var i;
				        	for(i = 0;i<res.length;i++){
				        		$("#rkpc").append(new Option(res[i],res[i]));
				        	}
						  }
						  layui.form.render("select");//重新渲染select
			         },      
			         
			         error:function (data) {
			        	 layer.alert("系统出错！");
			         }      
			  });
		  },
		  error:function(re){
			  layer.alert("系统出错！");
		  }
	  });
	  return false;
  })
});
//删除原料
/*layui.use('form', function(){
	  var form = layui.form;
	  $ = layui.jquery;
	  //var checkStatus1 = table.checkStatus('addMattable');
	  //var data = checkStatus1.data;
	  //监听提交
	  
	});*/
</script>
   
</div>
</body>
</html>
