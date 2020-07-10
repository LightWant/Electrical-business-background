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
		</style>
</head>

<body class="layui-layout-body"
style="background: url(<%=request.getContextPath()%>/image/wef.jpg); background-size:100% 100% ; background-attachment: fixed;overflow: hidden"> 
<div class="layui-layout layui-layout-admin">
<div class="layui-side" aria-hidden="true" hidden="true"> 
</div>

<div class="layui-body" >
<form class="layui-form" action="" id="myForm">
  <div class="layui-form-item">
  	<div class="layui-inline">
		 <label class="layui-form-label">姓名</label>
		<div class="layui-input-inline">
        	<input type="text" class="layui-input" id="name" readonly>
    	</div>
    </div>
    <div class="layui-inline">
    	 <label class="layui-form-label">ID</label>
		<div class="layui-input-inline">
        	<input type="text" class="layui-input" id="id" readonly>
    	</div>
    </div>
   </div>

   <div class="layui-form-item">
	   <div class="layui-inline">
		   <label class="layui-form-label">性别</label>
			<div class="layui-input-inline">
	        	<input type="text" class="layui-input" id="sex" readonly>
	    	</div>
	   </div>
	   <div class="layui-inline">
	    	<label class="layui-form-label">身份证</label>
		    <div class="layui-input-inline">
		      <input type="text" readonly name="identity" 
		      lay-verify="identity" placeholder=""
		       class="layui-input" id="identity">
		    </div>
	   </div>
		
   </div>
   <div class="layui-form-item">
		<label class="layui-form-label">职位</label>
		<div class="layui-input-block">
        	<input type="text" class="layui-input" id="zhiwei" readonly>
    	</div>
   </div>
   
    <div class="layui-form-item">
	    <div class="layui-inline">
	      <label class="layui-form-label">手机</label>
	      <div class="layui-input-inline">
	        <input type="tel" id="phone" name="phone" readonly lay-verify="required|phone" class="layui-input">
	      </div>
	    </div>
	 
	    <div class="layui-inline">
	      <label class="layui-form-label">邮箱</label>
	      <div class="layui-input-inline">
	        <input type="text" id="mail" name="email" readonly lay-verify="email" class="layui-input">
	      </div>
	    </div>
    </div>
    <div class="layui-form-item">
    	<div class="layui-inline">
      		<label class="layui-form-label">住址</label>
      		<div class="layui-input-inline">
        	<input type="text" id="zhuzhi" class="layui-input" id="confirm" readonly>
    		</div>
    	</div>
    	<div class="layui-inline">
      		<label class="layui-form-label">入职时间</label>
      		<div class="layui-input-inline">
        		<input type="text" id="ruzhiTIME" class="layui-input" lay-verify="date" id="confirm" readonly>
    		</div>
    	</div>
   </div>
   
   <div class="layui-form-item">
    	<div class="layui-inline">
      <label class="layui-form-label">在岗状态</label>
      <div class="layui-input-inline">
        <input type="text" id="state" class="layui-input" id="confirm" readonly>
    	</div>
    	</div>
    	<div class="layui-inline">
      	<label class="layui-form-label">工资</label>
      	<div class="layui-input-inline">
        <input type="text" id="gongzi" class="layui-input" id="confirm" readonly>
    	</div>
    	</div>
   </div>

	<div class="layui-form-item">
	<div class="layui-inline">
      	<div class="layui-input-block layui-input-inline">
        	<input type="password" id="password" required lay-verify="required" 
        	placeholder="请输入密码" autocomplete="off" class="layui-input">
    	</div>
    	</div>
    	<div class="layui-inline">
      		<div class="layui-input-block layui-input-inline">
	 		 <button type="button" class="layui-btn" id="changePassword">修改密码</button>
	 		</div>
    	</div>
    	
   </div>
</form>
    </div>
</div>
<!--<script src="../src/layui.js"></script>-->

<script src="layui/layui.js"></script>
<script>
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form;
		element.init();
		
		$.ajax({
            type : 'post',
            url : '${pageContext.request.contextPath}/YuanGong.action',
            contentType : 'application/json;charset=utf-8',
            data : JSON.stringify({id:$("#id").val()}),
            success : function(data) {
            	var dataObj=eval("("+data+")");
            	//console.log(data);
            	//console.log(dataObj);	
            	$("#name").val(dataObj['mingchen']);
            	$("#id").val(dataObj['id']);
            	$("#sex").val(dataObj['sex']);
            	$("#zhiwei").val(dataObj['zhiwei']);
            	$("#identity").val(dataObj['shenfenzhengID']);
            	$("#phone").val(dataObj['shouji']);
            	$("#mail").val(dataObj['mail']);
            	$("#zhuzhi").val(dataObj['zhuzhi']);
            	$("#ruzhiTIME").val(dataObj['ruzhiTIME']);
            	$("#state").val(dataObj['zaigangSTAGE']);
            	$("#gongzi").val(dataObj['gongzi']);
            },error:function(s) {
            	layer.alert('连接失败！');
            }
  		});
		
		$(document).on('click','#changePassword',function() {
			var da = {
				id:$('#id').val()
			   ,password:$('#password').val()
			};
			
			$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/login.changePassWord',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : JSON.stringify(da),
                success : function(data) {
                	console.log(data);
                	self.location="http://localhost:8080/FoodFactory/login";
                },error:function(s) {
                	layer.msg("error");
                }
      		});
			return false;
		});
    });
</script>
</body>
</html>
