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
				<label class="layui-form-label" style="font-size:20px"><b>修改角色功能</b></label>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="flb1">角色</label>
				  <div class="layui-input-block">
						<select id="js" name="js"  required lay-verify="required" >
						<%String [][] roles = HumanSystem.selectAllRole();
						if(roles!=null&&roles.length > 0)
						for(int i = 0; i < roles.length ;i++){%>
							<option value=<%=roles[i][0]%>><%=roles[i][1] %></option>
						<%}%>
					   </select>
				  </div>
			</div>
			<div class="layui-form-item">
				    <label class="layui-form-label">权限名称</label>
				   	<div class="layui-input-block">
				   		<%String [][] func = HumanSystem.selectAllFunction();
				   		for(int i = 0 ;i< func.length;i++){%>
				   			<input type="checkbox" name="CSDN" lay-filter="like" title=<%=func[i][1]%> value=<%=func[i][0] %>>
				   		<%}
				   		%>
				    </div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
		    		<button class="layui-btn" lay-submit lay-filter="formDemo">修改员工角色</button>
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
  
  form.on('submit(formDemo)', function(data){
	  //查询员工信息
	  var arr_box = [];
	    $('input[type=checkbox]:checked').each(function() {
	      arr_box.push($(this).val());
	    });
	  if(arr_box.length<=0){
		  alert("至少选择一个功能赋予角色！");
		  return false;
	  }
	    //数组
	  //console.log(arr_box);
	  var re = arr_box[0];
	  for(var i = 1;i < arr_box.length;i++){
		  re += "," + arr_box[i];
	  }
	  var da={
				name:data.field.js+","+re
		};
	  
	  //alert("查询id:"+JSON.stringify(da));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/changeRoleInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(result){   
	        	//alert("success");
	        	console.log(result);
	             if(result){
	            	 layer.msg('修改成功！');
	             }
	             else{
	            	 layer.msg('修改失败！');
	             }
	        },
            error:function (data) {
                alert("系统出错!")
            }           
	    }) ;
    // return "addMat";
    return false;
  });
  
  
});
</script>
   
</div>
</body>
</html>
