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
				<div class="layui-inline">
				<label class="layui-form-label" style="font-size:12px"><b>修改车间状态</b></label>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">车间ID</label>
					 <div class="layui-input-inline">
						 <select id="cjid1" name="cjid1" required lay-verify="required" >
				     		<%int [] work= WorkSystem.selectAllWork();
				     			if(work!=null){
				     				
				     				for(int key : work){%>
				     					<option value=<%=key%>><%=key%></option>
				     		<%}}
				     		%> 
				     	</select>
					 </div>
					<label class="layui-form-label" >车间状态</label>
					 <div class="layui-input-inline">
					 	<select id="cjstate" name="cjstate" required lay-verify="required" >
					 		<option value="使用中" selected>使用中</option>
					 		<option value="空闲" >空闲</option>
					 	</select>
					 </div>
				<div class="layui-inline">
					<button class="layui-btn" lay-submit lay-filter="formDemo3">修改当前车间状态</button>
				</div>
				</div>
			</div>
		
		</form>
		
		<form class="layui-form" action="" id="form1">
			<div class="layui-form-item">
				<div class="layui-inline">
				<label class="layui-form-label" style="font-size:12px"><b>修改车间信息</b></label>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">车间ID</label>
					 <div class="layui-input-inline">
						 <select id="cjid" name="cjid" required lay-verify="required" >
					     		<%
					     			if(work!=null){
					     				
					     				for(int key : work){%>
					     					<option value=<%=key%>><%=key%></option>
					     		<%}}
					     		%> 
					     	</select>
					 </div>
					 <label class="layui-form-label">车间负责人ID</label>
					 <div class="layui-input-inline">
						 <select id="zrid" name="zrid"  required lay-verify="required" >
							 <%Integer [] cj = HumanSystem.findAllStaff();
							 Calendar cal = Calendar.getInstance();

							 if(cj!=null&&cj.length != 0){
								 for(int i = 0;i<cj.length;i++){%>
									<option value=<%=cj[i]%>><%= cal.get(Calendar.YEAR)+String.format("%03d",cj[i])%></option>
								 <%}
							 }
							 %>
						 </select>
					 </div>
					 <label class="layui-form-label">车间状态</label>
					 <div class="layui-input-inline">
					 	<select id="cjstate1" name="cjstate1" required lay-verify="required" >
					 		<option value="使用中" selected>使用中</option>
					 		<option value="空闲" >空闲</option>
					 	</select>
					 </div>
				</div>
			</div>
			<div class="layui-form-item">
				<div class="layui-inline">
					<label class="layui-form-label">生产产品</label>
					<div class="layui-input-inline">
						<select id="cpname" name="cpname" required lay-verify="required" lay-filter="sel">
				     		<%Map<Integer,String>pro = ProductSystem.selectAllProduct();
				     			if(pro!=null){
				     				Object [] keys = pro.keySet().toArray();
				     				for(Object key : keys){%>
				     					<option value=<%=pro.get(key)%>><%=pro.get(key)%></option>
				     		<%}}
				     		%> 
				     	</select>
					</div>
					<label class="layui-form-label">相应产能</label>
					<div class="layui-input-inline">
						 <input id="cn" name="cn" lay-verify="required|num|number" autocomplete="off" class="layui-input">
					</div>
				</div>
				<div class="layui-inline">
					<button class="layui-btn" lay-submit lay-filter="formDemo1">添加一行产能信息</button>
				</div> 
			</div>
			<div class="layui-form-item">
				<div id="Gl_Table">
        			 <table 
        				class="layui-hide" lay-filter="Table" 
        				id="Table" readonly="true" style="max-height:500px; height:500px;">
        			 </table> 
    			</div>
   			</div>
   			<div class="layui-form-item">
   				<div class="layui-input-block" >
					<button class="layui-btn" id = "btn-add" lay-submit lay-filter="formDemo2">修改当前车间信息</button>
				</div>
   			</div>
	</form>
	
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
var AddData=[
    {产品名称:"",相应产能:""},
     ];

layui.use(['form','layer','table','laydate'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var laydate = layui.laydate;
  var table = layui.table;
  $ = layui.jquery;
  form.verify({
	  num: function(value){
	      if(value<=0){
	        return '产能要大于0！';
	      }
	   }
 });
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
	       {field:'产品名称',  width:'50%', title: '产品名称'}
	      ,{field:'相应产能', width:'50%', title: '相应产能'}
	    ]],
	    data:AddData
	});
	 table.on('rowDouble(Table)', function(obj){
		 //alert("double");
		    var data = obj.data;
		    //alert(JSON.stringify(data));
		    var flag = 1;
		    for(var i = 0;i < window.AddData.length;i++){
		    	//alert(window.AddData[i]['产品名称']);
		    	//alert(window.AddData[i]['相应产能']);
		    	if(window.AddData[i]['产品名称']==data['产品名称']&&window.AddData[i]['相应产能']==data['相应产能']&&data['相应产能']!=""&&data['产品名称']!=""){
		    		//alert("find");
		    		window.AddData.splice(i,1);
		    		flag = 0;
		    		//删除选中的行
		    	}
		    }
		    //alert("删除该行");
		    //重新渲染表格
		    if(flag == 0){
		    table.reload('Table',{
				data : window.AddData
				});
		    }
		    //标注选中样式
		    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
		  });
	 
  form.on('submit(formDemo1)', function(data){
	  //
	  var cpname = data.field.cpname;
	  for(var i = 0 ;i < window.AddData.length;i++){
		  if(window.AddData[i]['产品名称']==cpname){
			  alert("已添加"+cpname+"的产能，要修改请先删除！(双击所在行即可删除)");
			  return false;
		  }
	  }
	  var da={
				产品名称:data.field.cpname,
				相应产能:data.field.cn
		};
	  
	  window.AddData.unshift(da);
	  //重新渲染表格
	  table.reload('Table',{
		data : window.AddData
		});
    // return "addMat";
    return false;
  });
  form.on('submit(formDemo2)', function(data){
  	//修改车间信息
  	
  	if(AddData.length>1){
  		
	  	var prod = AddData[0]['产品名称'];
	  	
	  	var power = AddData[0]['相应产能'];
	  	for(var i = 1;i < AddData.length;i++){
	  		prod += ","+AddData[i]['产品名称'];
	  		power += ","+AddData[i]['相应产能'];
	  	}
	  	//alert("prod:"+prod+",power:"+power);
	  	var da = {
	  			cjid:data.field.cjid,
	  			cjzr:data.field.zrid,
	  			state:data.field.cjstate1,
	  			cpname:prod,
	  			power:power
	  	};
	  	//alert(JSON.stringify(da));
	  	$.ajax({   
	        url:'${pageContext.request.contextPath}/workInfoChange.changeWorkInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(res){ 
	        	//alert("res:"+res);
	        	if(res>0){
	        		//alert("res:"+res);
	        		layer.msg("修改成功！");
	        		
	        	}else{
	        		layer.msg("修改失败！");
	        	}
	        },error:function(s){
	        	layer.msg("系统错误");
	       	}
	    });
	  	
	  	
  	}
  	else{
  		alert("请本车间添加产能信息!");
  	}
  	return false;
  });
  form.on('submit(formDemo3)', function(data){
  	var da = {
  			name:data.field.cjid1+","+data.field.cjstate
  	};
  	$.ajax({   
        url:'${pageContext.request.contextPath}/workInfoChange.changeWorkstate',     
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),    
        
        success:function(res){ 
        	if(res>0){
        		layer.msg('成功！');
        	}
        	else{
        		layer.msg('失败！');
        	}
        },error:function(r){
        	layer.msg("系统错误");
        	
        }
     });
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
