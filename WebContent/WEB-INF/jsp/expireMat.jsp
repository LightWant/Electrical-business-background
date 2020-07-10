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

		<table class="layui-table" lay-filter="Table" id="extable" page:true,>
			  <thead>
				    <tr>
					      <!--  <th lay-data="{checkbox:true}"></th> -->
					      <th lay-data="{field:'rkpcid', width:80 sort:true}">入库批次ID</th>
					      <th lay-data="{field:'ylid', width:180}">原料ID</th>
					      <th lay-data="{field:'ylname', width:180}">原料名称</th>
					      <th lay-data="{field:'leftnum', width:180}">剩余数量</th>
				    </tr>
			  </thead>
			  <!--  正在编写 -->
			  <%String[][] result = MaterialSystem.expire();
			  Calendar cal = Calendar.getInstance();

			  if(result!=null)
				  for(int i = 0;i<result.length;i++){%>
					  <tr>
						  <td data-field='rkpcid'  data-edit='text'><div class='layui-table-cell laytable-cell-1-rkpcid '><%=result[i][0] %></div></td>
						  <td data-field='ylid'  data-edit='text'><div class='layui-table-cell laytable-cell-1-ylcid '><%=cal.get(Calendar.YEAR)+String.format("%03d",Integer.parseInt(result[i][1]))%></div></td>
						  <td data-field='ylname'  data-edit='text'><div class='layui-table-cell laytable-cell-1-ylname '><%=result[i][2] %></div></td>
						  <td data-field='leftnum'  data-edit='text'><div class='layui-table-cell laytable-cell-1-leftnum '><%=result[i][3] %></div></td>
					  </tr>
				  <%}%>
		</table>
		<form class="layui-form" action="" id="form1">
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo">一键销毁</button>
				</div>
			</div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>

var AddData=[];
layui.use(['form','table','layer'], function(){
	  var table = layui.table; 
	  var form = layui.form;
	  var layer = layui.layer;
	  
	  $ = layui.jquery;
	  form.on('submit(formDemo)',function(){
		  $.ajax({   
		        url:'${pageContext.request.contextPath}/destroyAll',     
		        type :'get',       
		        contentType : 'application/json;charset=utf-8',  
		        success:function(res){       
		             if(res==1){
		            	 layer.alert("销毁成功！");
		            	 table.reload('Table',{
			   				data : window.AddData
			   			 });
		            	 
		            	 form.render();
		            	 //table.render();
		           	 }     
		             else if(res==0){
		            	 layer.alert("没有过期原料!！");
		 			 }
		             else{
		            	 layer.alert("销毁出故障！");
		             }
		        },
		        error:function(s){
		        	layer.alert("系统故障！");
		        }
		        
		    }) ;    
		  return false;
	  });
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
