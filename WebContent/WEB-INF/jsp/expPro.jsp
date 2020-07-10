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
	
		 <table 
			class="layui-hide" lay-filter="Table1" 
			id="Table1" style="max-height:500px; height:500px;">
		</table> 
		<form class="layui-form" action="" id="form1">
			<div class="layui-form-item">
				<div class="layui-form-inline">
					<div class="layui-input-inline">
						<button class="layui-btn" lay-submit lay-filter="formDemo1">显示过期产品</button>
					</div>
					<div class="layui-input-inline">
						<button class="layui-btn" lay-submit lay-filter="formDemo">一键销毁</button>
					</div>
					
				</div>
			</div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
AddData = [];
layui.use(['form','table','layer'], function(){
	  var table = layui.table; 
	  var form = layui.form;
	  var layer = layui.layer;
	  $ = layui.jquery;
	  
	  table.render({
		    elem: '#Table1',
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
		       {field:'入库批次ID',  width:'25%', title: '入库批次ID'}
		      ,{field:'产品ID',  width:'25%', title: '产品ID'}
		      ,{field:'产品名称',  width:'25%', title: '产品名称'}
		      ,{field:'剩余数量',  width:'25%', title: '剩余数量'}
		    ]],
		    data:AddData
		});
	  
	  
	  form.on('submit(formDemo1)',function(){
		  var da = {
					name:""	  
				  };
		  $.ajax({   
		        url:'${pageContext.request.contextPath}/Product_Staff.getexpire',     
		        type :'post',       
		        contentType : 'application/json;charset=utf-8',  
		        data:JSON.stringify(da),
		        success:function(res){  
		        	//alert("success");
		        	if(res!=null && res.length !=0){
		        		for(var i = 0;i < res.length ;i++){
		        			var per = res[i].split(",");
		        			var data1 = {
		        					入库批次ID:per[0]
		        					,产品ID:changeToCode(per[1])
		        					,产品名称:per[2]
		        					,剩余数量:per[3]
		        			};
		        			//alert(JSON.stringify(data1));
		        			window.AddData.unshift(data1);
		        			
		        		}
		        		table.reload('Table1',{
							data : window.AddData
						});
		        	}
		        	else{
		        		alert('没有过期产品！');
		        		
		        	}
		        },error:function(r){
		        	alert("系统错误！");
		        }
		        
	  	});
		  return false;
	  });
	  form.on('submit(formDemo)',function(){
		  var da = {
					name:""	  
				  };
		  $.ajax({   
		        url:'${pageContext.request.contextPath}/Product_Staff.destroyAll',     
		        type :'post',       
		        contentType : 'application/json;charset=utf-8',  
		        data:JSON.stringify(da),
		        
		        success:function(res){       
		             if(res!="0" && res.indexOf("no")<0){
		            	 layer.open({
		            		 content : "销毁成功！"
		            	 });
		            	 //form.render();
		            	 //table.render();
		            	 window.AddData = [];
		            	 table.reload('Table1',{
								data : window.AddData
						});
		            	 
		           	 }     
		             else if(res=="0"){
			  			alert("没有过期产品!！");
		 			 }
		             else{
		            	alert("销毁出故障！");
		             }
		        },
		        error:function(s){
		        	alert("系统故障！");
		        }
		        
		    }) ;    
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
	  /*table.render({
	    elem: '#extable'
	    ,url:'/expMat.getExpire'
	    ,contentType: 'application/json'
	    ,cols: [[
	      {type:'checkbox'}
	      ,{field:'rkpcid', width:80, title: '入库批次ID', sort: true}
	      ,{field:'ylid', width:80, title: '原料ID'}
	      ,{field:'ylname', width:80, title: '原料名称'}
	      ,{field:'leftnum', width:80, title: '剩余数量'}
	    ]]
	    ,parseData: function(res){ //res 即为原始返回的数据
	        return {
	          "code": res.code, //解析接口状态
	          "msg": res.mes, //解析提示文本
	          "count": res.count, //解析数据长度
	          "data": res.data.item //解析数据列表
	        };
	      }
	    ,page: true
	  });
	});*/
</script>

</div>
</body>
</html>
