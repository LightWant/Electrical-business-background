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
		<div class="layui-container"> 
		  <div class="layui-row">
			  	<div class="layui-col-md6">
			  		<div class="layui-form-item">
				  		<label class="layui-form-label" >姓名</label>
				  		<div class="layui-input-block">
				  			<input id="name" name="name" required lay-verify="required" class="layui-input" autocomplete="off">
				  		</div>
			  		</div>
			  	</div>
			  	<div class="layui-col-md6">
			  		<div class="layui-form-item">
				  		<label class="layui-form-label" >性别</label>
				  		<div class="layui-input-block" id = "inputsex">
				  			<input type="radio" id="man" name="sex" value="男" title="男" checked="">
    						<input type="radio" id="woman" name="sex" value="女" title="女">
				  		</div>
			  		</div>
			  	</div>
		  </div>
		  <div class="layui-row">
		  		<div class="layui-col-md6">
			  		<div class="layui-form-item">
				  		<label class="layui-form-label" >职位</label>
				  		<div class="layui-input-block">
				  			<select id="zhiwei" name="zhiwei" required lay-verify="required">
				  				<% String [][] roles = HumanSystem.selectAllRole();
				  				if(roles!=null && roles.length > 0){
				  				int j;
				  				for(j = 0;j < roles.length;j++){%>
				  					<option value=<%=roles[j][0]%>><%=roles[j][1]%></option>
				  				<%}}
				  				%>
				  				
				  			</select>
				  		</div>
			  		</div>
			  	</div>
			  	<div class="layui-col-md6">
			  		<div class="layui-form-item">
				  		<label class="layui-form-label" >在岗状态</label>
				  		<div class="layui-input-block">
				  			<select id="zgzt" name="zgzt" required lay-verify="required">
				  				<option value="在岗" selected>在岗</option>
					    		<option value="请假">请假</option>
					    		<option value="离职">离职</option>
				  			</select>
				  		</div>
			  		</div>
			  	</div>
		  </div>

			<div class="layui-row">
				<div class="layui-form-item">
					<label class="layui-form-label" >身份证编号</label>
			  		<div class="layui-input-block">
			  			 <input type="text" id="identity" name="identity" required  lay-verify="identity|required" placeholder="" autocomplete="off" class="layui-input">
			  		</div>
			  	</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-md6">
					<div class="layui-form-item">
						<label class="layui-form-label" >手机号</label>
						<div class="layui-input-block">
					        <input type="tel" id="phone" name="phone" lay-verify="required|phone" autocomplete="off" class="layui-input">
					    </div>
					</div>
				</div>
				<div class="layui-col-md6">
					<div class="layui-form-item">
						<label class="layui-form-label" >邮箱</label>
						<div class="layui-input-block">
				        	<input type="text" id="email" name="email" lay-verify="email|required" autocomplete="off" class="layui-input">
				      	</div>
					</div>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-form-item">
					<label class="layui-form-label" >住址</label>
					<div class="layui-input-block">
				        <input type="tel" id="addr" name="addr" lay-verify="required" autocomplete="off" class="layui-input">
				    </div>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-md6">
					<div class="layui-form-item">
						<label class="layui-form-label" >入职日期</label>
						<div class="layui-input-block">
					    	<input type="text" id="date" name="date" lay-verify="date|required" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
					    </div>
					</div>
				</div>
				<div class="layui-col-md6">
					<div class="layui-form-item">
						<label class="layui-form-label" >基础工资</label>
						<div class="layui-input-block">
					    	<input id="gz" name="gz" min="0" lay-verify="num" class="layui-input" autocomplete="off">
					    </div>
					</div>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-md10">
					<div class="layui-form-item">
					</div>
				</div>
				<div class="layui-col-md2">
					<div class="layui-form-item">
						<button class="layui-btn" lay-submit lay-filter="formDemo1">确认入职</button>
					</div>
				</div>
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
 //工资验证
  form.verify({
	  num: function(value){
		  if(value!=""){
			  if(!new RegExp('^[+]{0,1}(\d+)$').test(value.toString()) && !(new RegExp('^(0|[1-9](\.[0-9]+|[0-9]*))$').test(value.toString())) ){
				 return '工资格式不正确！';
			  }
			  else if(value<2000){
		        return '工资要大于2000！';
		      }
		  }
	   }
  	  
  	});
  
  form.on('submit(formDemo1)', function(data){
	  //查询员工信息
	  //sex 不确定
	  //alert("submit");
	  //alert($('#IsPurchased inputsex[name="sex"]:checked ').val());
	  var da={
				mingchen:data.field.name
				,sex:$('#inputsex input[name="sex"]:checked ').val()
				,zhiwei:data.field.zhiwei
				,sfzid:data.field.identity
				,shouji:data.field.phone
				,mail:data.field.email
				,zhuzhi:data.field.addr
				,rztime:data.field.date
				,zgstate:data.field.zgzt
				,password:"123456"
				,gongzi:data.field.gz
		};
	  
	  //alert("查询id:"+JSON.stringify(da));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/staffInfo.addStaff',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(result){   
	        	//alert("success");
	             console.log(result);
            	if(result<0){
            		alert("添加员工失败！");
            	}  
            	else{
            		//alert("有信息");
            		layer.msg('添加成功！编号为:'+changeToCode(result)); 
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
