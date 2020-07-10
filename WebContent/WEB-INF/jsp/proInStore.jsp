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
					<label class="layui-form-label" >产品名称</label>
					<div class="layui-input-block" >
						<select id="cpname" name="cpname"  required lay-verify="required"  lay-filter="selMat">
						<%Map<Integer,String> map = ProductSystem.selectAllProduct();
						if(map!=null&&map.size()!=0){
							Object[]keys = map.keySet().toArray();
						
							for(Object key:keys){%>
							<option value=<%=map.get(key)%>><%=map.get(key)%></option>
						<%}}
						%>
						</select>
				   	</div>
			   </div>
			   
			   <div class="layui-form-item">
				   	<label class="layui-form-label"  >生产批次</label>
					<div class="layui-input-block" >
				     	 <select  required lay-verify="required" id="scpc" name="scpc">
				     		
				     	</select>
				   	</div>
			   	</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="numlb">入库数量</label>
				  <div class="layui-input-block">
						<input id="num" name="num"  required lay-verify="required|number|num" class="layui-input" autocomplete="off">
				  </div>
			</div>
			<div>
				  <label class="layui-form-label"  id="numlb">仓库ID</label>
				  <div class="layui-input-block">
						<select id="ckid" name="ckid"  required lay-verify="required" >
						<%Integer[] ids = ProductSystem.selectAllStorageID();
						if(ids!=null&&ids.length!=0){
						
						for(Integer key:ids){%>
							<option value=<%=key%>><%=key %></option>
						<%}}%>
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
layui.use(['form', 'layedit', 'laydate', 'layer'], function(){
  var form = layui.form
  ,layer = layui.layer
  $ = layui.jquery;
  form.on('select(selMat)',function(data){
	  var da={
			  name:data.value//选择的原料名称
	  };
	  $.ajax({
		  url:'${pageContext.request.contextPath}/Product_Staff.findscpc',     
	      type :'post',       
	      contentType : 'application/json;charset=utf-8',
	      data:JSON.stringify(da),
		  success:function(res){   
			  $("#scpc").empty();
				  if(res==null||res.length == 0){
					  $("#scpc").empty();
			        	 layer.alert("该产品暂无可以入库的生产批次！");
				  }
				  else{
					var i;
		        	for(i = 0;i<res.length;i++){
		        		$("#scpc").append(new Option(res[i],res[i]));
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
    var da = {
    		ylname:data.field.cpname,
    		jhpc:data.field.scpc,
    		num:data.field.num,
    		ckid:data.field.ckid
    };
    //layer.alert(JSON.stringify(da));
    //console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/Product_Staff.instore',   
        //该写这儿了
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        success:function(res){   
        	if(res.indexOf("left")>=0){
        		var left = res.substr(res.indexOf("left")+"left".length);
        		layer.alert("不能入库比剩余数量多的数量，该批次剩余"+left);
        	}
        	else if(res=="insert"||res == "find"){  
        		layer.alert("入库失败！");
            	 //更新删除表
        	}
        	else{
        		layer.open({
        			title: '入库成功！'
        			,content: '入库批次为：'+res
        			,yes: function(){
  	     	          layer.closeAll();
  	     	       var dad = {
  	        				name:data.field.cpname
  	        		};
  	        		$.ajax({
  	        			  url:'${pageContext.request.contextPath}/Product_Staff.findscpc',     
  	        		      type :'post',       
  	        		      contentType : 'application/json;charset=utf-8',
  	        		      data:JSON.stringify(dad),
  	        			  success:function(res){   
  	        				  $("#scpc").empty();
  	        					  if(res==null||res.length == 0){
  	        						  $("#scpc").empty();
  	        				        	 layer.alert("该产品暂无可以入库的生产批次！");
  	        					  }
  	        					  else{
  	        						var i;
  	        			        	for(i = 0;i<res.length;i++){
  	        			        		$("#scpc").append(new Option(res[i],res[i]));
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
        		//更新批次表
        		
        	}
        
        },  error:function (s) {
                layer.alert("系统出错!")
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
