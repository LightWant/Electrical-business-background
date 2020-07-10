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
	
		<!-- int inStorage(int ckID,int num,int jhID)  -->
		<form class="layui-form" action="" id="form2">
			
			<div class="layui-form-item">
					<label class="layui-form-label" id = "deslb" >原料名称</label>
					<div class="layui-input-block" >
						<select id="ylname" name="ylname"  required lay-verify="required"  lay-filter="selMat">
						<%String[]names = MaterialSystem.selectAllMaterialName();
						if(names!=null)
						for(String key:names){%>
							<option value=<%=key%>><%=key %></option>
						<%}%>
						</select>
				   	</div>
			   </div>
			   
			   <div class="layui-form-item">
				   	<label class="layui-form-label" id = "jhpclb" >进货批次</label>
					<div class="layui-input-block" >
				     	 <select  required lay-verify="required" id="jhpc" name="jhpc">
				     		
				     	</select>
				   	</div>
			   	</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="numlb">入库数量</label>
				  <div class="layui-input-block">
						<input id="num" name="num" min="0" required lay-verify="required|number|num" class="layui-input" autocomplete="off">
				  </div>
			</div>
			<div>
				  <label class="layui-form-label"  id="numlb">仓库ID</label>
				  <div class="layui-input-block">
						<select id="ckid" name="ckid"  required lay-verify="required" >
						<%Integer []ckid = MaterialSystem.selectAllStorageId();
						if(ckid!=null)
						for(Integer key:ckid){%>
							<option value=<%=key%>><%=key %></option>
						<%}%>
						</select>
				  </div>
			</div>
			<div class="layui-form-item"></div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo">入库</button>
					<button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
				</div>
			</div>
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form
  ,layer = layui.layer
  $ = layui.jquery;
  form.on('select(selMat)',function(data){
	  var da={
			  name:data.value//选择的原料名称
	  };
	  $.ajax({
		  url:'${pageContext.request.contextPath}/inStorage.findjhpc',     
	      type :'post',       
	      contentType : 'application/json;charset=utf-8',
	      data:JSON.stringify(da),
		  success:function(res){   
			  $("#jhpc").empty();
				  if(res==null||res.length == 0){
					  $("#jhpc").empty();
			        	 layer.alert("该原料暂无可入库的进货批次！");
				  }
				  else{
					var i;
		        	for(i = 0;i<res.length;i++){
		        		$("#jhpc").append(new Option(res[i],res[i]));
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
  
 
  //自定义验证规则
  form.verify({
	  num: function(value){
	      if(value<=0){
	        return '入库数量要大于0！';
	      }
	   }
  	});
  
  form.on('submit(formDemo)', function(data){
	 //console.log("add");
    //layer.msg(JSON.stringify(data.field));
    if(data.field.num == null || data.field.num.length == 0 || data.field.num.length > 8) {
    	layer.alert('数量不合法！');
    	return false;
    }
    
    var da = {
   		ylname:data.field.ylname,
   		jhpc:data.field.jhpc,
   		num:data.field.num,
   		ckid:data.field.ckid
    };
    console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/inStorage.instore',   
        //该写这儿了
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        success:function(res){    
        	if(res.indexOf("left")>=0){
        		var left = res.substr(res.indexOf("left")+"left".length);
        		layer.alert("不能入库比剩余数量多的原料，该批次仅剩"+left);
        	}
        	else if(res == "no"){
        		layer.alert("入库失败!");
        	}
        	else {     
            	 //更新删除表
            	 layer.open({
        			title: '入库成功！'
        			,content: '入库批次为：'+res
        			,yes: function(){
    	     	       layer.closeAll();
    	     	       var dad = {
    	        				name:data.field.ylname
    	        		};
    	     	       //更新批次
    	     	      $.ajax({
    	     			  url:'${pageContext.request.contextPath}/inStorage.findjhpc',     
    	     		      type :'post',       
    	     		      contentType : 'application/json;charset=utf-8',
    	     		      data:JSON.stringify(dad),
    	     			  success:function(res){   
    	     				  $("#jhpc").empty();
    	     					  if(res==null||res.length == 0){
    	     						  $("#jhpc").empty();
    	     				        	 layer.alert("该原料暂无进货批次！");
    	     					  }
    	     					  else{
    	     						var i;
    	     			        	for(i = 0;i<res.length;i++){
    	     			        		$("#jhpc").append(new Option(res[i],res[i]));
    	     			        	}
    	     					  }
    	     					  layui.form.render("select");//重新渲染select
    	     		         },      
    	     		         
    	     		         error:function (data) {
    	     		        	 layer.alert("系统出错！");
    	     		         }      
    	     		  });
    	     	       
        			}
        		});   
            }
        },              
        error:function (s) {
            layer.alert("系统出错!");
        }           
        
    }) ;         
        
    //return "addMat";
    return false;
  });
});
</script>
   
</div>
</body>
</html>
