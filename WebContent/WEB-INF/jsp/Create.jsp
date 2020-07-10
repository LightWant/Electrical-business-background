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
				<label  class="layui-form-label" id = "creatlb" style="font-20px"><b>生产产品</b></label>
			</div>
	  		
		        <!-- <div class="layui-form-item">
					<label  class="layui-form-label" id = "creatlb" style="font-20px"><b>自动生产</b></label>
				</div> -->
				<div class="layui-container"> 
					<div class="layui-row">
					<div class="layui-form-item">
						<div class="layui-col-md6">
							
					   		<label  class="layui-form-label"  >产品名称</label>
			       			<div class="layui-input-block" >
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
					   	</div>
					   	<div class="layui-col-md6">
					   		
					   		<label  class="layui-form-label"  >产品数量</label>
			       			<div class="layui-input-block" >
			       				<input id="num" name="num" min="0" required lay-verify="required|number|num" class="layui-input" autocomplete="off">
					   		</div>
					   	</div>
					</div>
					</div>
					<div class="layui-row">
					<div class="layui-form-item">
						<div class="layui-col-md6">
							<label  class="layui-form-label">质检员编号</label>
			       			<div class="layui-input-block" >
			       			<select id="zjyid" name="zjyid" required lay-verify="required" >
					     		<%Integer [] person= HumanSystem.findAllStaff();
					     			if(person!=null){
					     				
					     				for(Integer key : person){%>
					     					<option value=<%=key%>><%=key%></option>
					     		<%}}
					     		%> 
					     	</select>
					     	</div>
						</div>
						<div class="layui-col-md6">
							<label  class="layui-form-label">车间编号</label>
			       			<div class="layui-input-block" >
			       			<select id="cjid" name="cjid" required lay-verify="required" >
					     		<%int [] work= WorkSystem.selectAllWork();
					     			if(work!=null){
					     				
					     				for(int key : work){%>
					     					<option value=<%=key%>><%=key%></option>
					     		<%}}
					     		%> 
					     	</select>
					     	</div>
						</div>
					   	
					</div>
					</div>
					<div class="layui-row">
					<div class="layui-form-item">
						<label  class="layui-form-label">质检记录</label>
						<div class="layui-input-block" >
							<input id="zhijian" name="zhijian" required lay-verify="required" class="layui-input" placeholder="(长度小于50)" autocomplete="off">
						</div>
					</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md5">
					   		<div class="layui-input-block" >
								<button class="layui-btn" id = "btn-add" lay-submit lay-filter="formDemo">生产</button>
							</div>
					   	</div>
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
		<form class="layui-form" action="" id="form1">
			<div class="layui-form-item">
				<label  class="layui-form-label" id = "deslb" style="font-20px" ><b>次品销毁</b></label>
			</div>
			<div class="layui-form-item">
		   		<label  class="layui-form-label" >产品名称</label>
       			<div class="layui-input-block" >
       			<select id="cpname1" name="cpname1" required lay-verify="required" lay-filter="selPro">
		     		<%
		     			if(pro!=null){
		     				Object [] keys = pro.keySet().toArray();
		     				for(Object key : keys){%>
		     					<option value=<%=pro.get(key)%>><%=pro.get(key)%></option>
		     		<%}}
		     		%> 
		     	</select>
		   		</div>
		   	</div>
		   	<div class="layui-form-item">
		   		<label  class="layui-form-label" id = "scpcnumlb" >未入库的生产批次</label>
       			<div class="layui-input-block" >
	       			<select id="scpc" name="scpc" required lay-verify="required" >
			     		
			     	</select>
		   		</div>
		   	</div>
	   		<div class="layui-form-item">
			   		
		   		<div class="layui-input-block" >
					<button class="layui-btn" id = "btn-add" lay-submit lay-filter="formDemo1">销毁</button>
				</div>
       		</div>
		   	
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
AddData = [];
AddData1 = []
layui.use(['form','layer','table'],function(){
	var form = layui.form;
	var layer = layui.layer;
	var table = layui.table;
	$ = layui.jquery;
	
	 //输入数量验证
	 form.verify({
		  num: function(value){
		      if(value<=0){
		        return '生产数量要大于0！';
		      }
		   }
	 });
	 //table
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
		//入库批次ID，产品ID，产品名称，入库数量，负责人ID，入库时间，剩余数量
		    ,cols: [[	 
		      ,	{field:'生产批次ID',  width:'30%', title: '生产批次ID'}
		      ,	{field:'使用原料的出库批次',  width:'45%', title: '使用原料的出库批次'}
		      , {field:'使用数量', width:'25%', title: '使用数量'}
		    ]],
		    data:AddData
		  });
	 //监听选择销毁的产品
	 form.on('select(selPro)',function(data){
		 var da = {
				 name:data.value
		 };
	 	console.log(data);
		 $.ajax({   
		        url:'${pageContext.request.contextPath}/Create.notscpc',
		        type :'post',       
		        contentType : 'application/json;charset=utf-8',
		        data:JSON.stringify(da),   
		        success:function(res){
		        	$("#scpc").empty();
		        	if(res!=null&&res.length>0){
		        		for(var i = 0;i<res.length;i++){
		        			$("#scpc").append(new Option(res[i],res[i]));
		        		}
		        		form.render('select');
		        	}
		        	else{
		        		alert("该产品暂无可在车间销毁的生产批次！");
		        	}
		        	},error:function(r){
		        		$("#scpc").empty();
		        		alert("系统错误!");
		        	}
		 })
		 return false;
	 });
	 
	form.on('submit(formDemo)', function(data){
		//int work(int chanpinID,int num,int zjyID,Date time,int wno)
		var da={
			cpname:data.field.cpname,
			zjyid:data.field.zjyid+","+data.field.zhijian,
			cjid:data.field.cjid,
			num:data.field.num
		};
		console.log(JSON.stringify(da));
		 $.ajax({   
		        url:'${pageContext.request.contextPath}/Create.shengchan',
		        type :'post',       
		        contentType : 'application/json;charset=utf-8',
		        data:JSON.stringify(da),   
		        success:function(res){   
		        	if(res == null ||res.length == 0){
		        		console.log("wu");
		        	}
		        	if(res!=null && res.length > 0){
		        		
		        		for(var i = 0;i < res.length;i++){
		        			var get = res[i].split(",");
		        			var da = {
		        					生产批次ID:get[0],使用原料的出库批次:get[1],使用数量:get[2]
		        			};
		        			console.log(JSON.stringify(da));
		        			window.AddData.unshift(da);
		        		}
		        		table.reload('Table',{
	    					data : window.AddData
	    				});
    					form.render();
    					layer.msg('生产成功!');
		        	}
		        	else {
		        		layer.msg("车间中原料剩余量不足以生产该数量的该产品！");
		        	}
		        },error:function(s){
		        	alert("系统错误!");
		        }
		 });
		 return false;
	});
	form.on('submit(formDemo1)', function(data){
		var da={
			name:data.field.scpc
		};
		$.ajax({   
	        url:'${pageContext.request.contextPath}/Create.destory',
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){   
	        	if(res == 1){
	        		layer.msg('销毁成功！');
	        		//alert(document.getElementById("cpname1").value);
	        		var daa = {
		       	 			name:document.getElementById("cpname1").value
		       	 	}
		       		 $.ajax({   
		       		        url:'${pageContext.request.contextPath}/Create.notscpc',
		       		        type :'post',       
		       		        contentType : 'application/json;charset=utf-8',
		       		        data:JSON.stringify(daa),   
		       		        success:function(res){
		       		        	$("#scpc").empty();
		       		        	if(res!=null&&res.length>0){
		       		        		for(var i = 0;i<res.length;i++){
		       		        			$("#scpc").append(new Option(res[i],res[i]));
		       		        		}
		       		        		form.render('select');
		       		        	}
		       		        	else{
		       		        		//layer.msg("该产品暂无未入库的生产批次！");
		       		        	}
		       		        	},error:function(r){
		       		        		$("#scpc").empty();
		       		        		layer.msg("系统错误!");
		       		        	}
		       		 	});
	        	}
	        	else if(res == -1){
	        		layer.msg("已经销毁过了！");
	        	}
	        	else{
	        		layer.msg("插入数据失败");
	        	}
	        	
	        },error:function(r){
	        	layer.msg("系统错误!");
	        }
	    });
		return false;
	});
});
</script>
   
</div>
</body>
</html>
