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
	  	  	<label class="layui-form-label">添加原料</label>
	  	  </div>
	  	 
		  <div class="layui-form-item">
				<label class="layui-form-label" id = "mingchenglb" >原料名称</label>
				<div class="layui-input-block" >
			     	<input id="mingcheng" name="mingcheng" type="text" required lay-verify="required" class="layui-input" autocomplete="off">
			   	</div>
		   </div>
		  <!--   <div class="layui-form-item">
			   	<label class="layui-form-label" id = "dabaoguigelb" >打包规格</label>
				<div class="layui-input-block" >
			     	<input id="dbgg" name="dbgg " type="number" required lay-verify="required" class="layui-input" autocomplete="off">
			   	</div>
		   </div>  -->
		   <div class="layui-form-item">
			   	<label class="layui-form-label" id = "dbgglb" >打包规格</label>
				<div class="layui-input-block" >
			     	<input id="dbgg" name="dbgg" type="number" min="1" required lay-verify="required" class="layui-input" autocomplete="off">
			   	</div>
		   </div>
		   <div class="layui-form-item">
			   	<label class="layui-form-label" id = "baozhiqilb" >保质期</label>
				<div class="layui-input-block" >
			     	<input id="baozhiqi" name="baozhiqi" type="number" min="1" required lay-verify="required" class="layui-input" autocomplete="off">
			   	</div>
		   </div>
		   
		   <div class="layui-form-item">
			   	<label class="layui-form-label" id = "danweilb" >单位</label>
				<div class="layui-input-block" >
			     	<select  required lay-verify="required" id="danwei" name="danwei">
			     		<option value="克" selected>克</option>
			     		<option value="两">两</option>
			     		<option value="斤">斤</option>
			     		<option value="公斤">公斤</option>
			     	</select>
			   	</div>
		   	</div>
		           
		  
		  <div class="layui-form-item">
		    <div class="layui-input-block">
		      <button class="layui-btn" lay-submit lay-filter="formDemo">添加</button>
		      <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
		    </div>
		  </div>
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
layui.use('form', function(){
  var form = layui.form;
  $ = layui.jquery;
  //var checkStatus1 = table.checkStatus('addMattable');
  //var data = checkStatus1.data;
  //监听提交
  //$(function(){
//	   //输入框的值改变时触发
//	  $("#inputid").on("input",function(e){
//	    //获取input输入的值
//	    console.log(e.delegateTarget.value);
//	  });
//	});
  form.on('submit(formDemo)', function(data){
	 console.log("add");
    //layer.msg(JSON.stringify(data.field));
    var da = {
    		mingcheng:data.field.mingcheng,
    		dbgg:data.field.dbgg,
    		baozhiqi:data.field.baozhiqi,
    		danwei:data.field.danwei
    };
    console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/addMat',     
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        success:function(res){       
             if(res>0){     
            	 //更新删除表
            	 layer.alert("添加原料成功！原料编号为 : " + changeToCode(res));   
                }                
            else if(res == -1) {   
            	layer.alert("系统中已存在名称为"+document.getElementById("mingcheng").value+"的原料，请更换名称！");   
            }
        	else{
        		layer.alert("插入失败！");
        	}},              
            error:function (s) {
            	layer.alert("系统出错!")
            }           
    }) ;         
        
    //return "addMat";
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
