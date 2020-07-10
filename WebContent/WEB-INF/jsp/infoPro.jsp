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
				<label class="layui-form-label" style="font-20px">查询信息</label>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="flb1">产品名称</label>
				  <div class="layui-input-block">
					
						<select id="fdname1" name="fdname1"  required lay-verify="required" >
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
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo1">查询该产品信息</button>
				</div>
			</div>
			<div class="layui-form-item">
			    <div class="layui-input-block">
			      <button class="layui-btn" lay-submit lay-filter="formDemo">查询全部产品信息</button>
			      
			    </div>
			  </div>
			<div class="layui-form-item">
				<div id="Gl_Table">
        			<table 
        				class="layui-hide" lay-filter="Table" 
        				id="Table" style="max-height:600px; height:600px;">
        			 </table>
    			</div>
   			</div>
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
AddData = [];
layui.use(['form','layer','table'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var table = layui.table;
  $ = layui.jquery;
 
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
	
	    ,cols: [[	   
	    	//产品编号,产品名称,打包规格,单价,保质期（天）,单位
	       {field:'产品编号', width:'15%', title: '产品编号'}
	      , {field:'产品名称', width:'14%', title: '产品名称'}
	      , {field:'打包规格', width:'14%', title: '打包规格'}
	      , {field:'单价', width:'14%', title: '单价'}
	      , {field:'保质期-天', width:'15%', title: '保质期-天'}
	      , {field:'单位', width:'14%', title: '单位'}
	      , {field:'库存', width:'14%', title: '库存'}
	    ]],
	    data:AddData
	  });
  
  form.on('submit(formDemo1)', function(data){
	  //原料信息
	  var da={
				name:document.getElementById("fdname1").value
		};
	  
	  //console.log(JSON.stringify(da));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.getProInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(data){       
	            
            	 //弹出原料信息
	        	window.AddData=[];
            	if(data!=null){
	                	var get = data.split(",");
	                	var da = {
	                			产品编号:changeToCode(get[0]),产品名称:get[1],打包规格:get[2],单价:get[3],"保质期-天":get[4],单位:get[5],库存:get[6]
	                	};
	                	
		        		window.AddData.unshift(da);
                	
            	}
    				table.reload('Table',{
    					data : window.AddData
    				});
				form.render();
	        	/*layer.open({
          		  type: 1
          		  ,title: false //不显示标题栏
          		  ,closeBtn: false
          		  ,area: '300px;'
          		  ,shade: 0.8
          		  ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
          		  ,resize: false
          		  ,btn: ['我知道啦']
          		  ,btnAlign: 'c'
          		  ,moveType: 1 //拖拽模式，0或者1
          		  ,content:'<p>产品ID:'+result[0]+'</p><br>产品名称：'+result[1]+'</br><br>打包规格：'+result[2]+'</br>'+'<br>单价：'+result[3]+'</br>'+'<br>保质期：'+result[4]+'</br>'
	     	        +'<br>单位：'+result[5]+'</br>'
          		  ,success: function(layero){
          			//layer.closeAll();
          		  }
          		});*/
	        },
            error:function (data) {
                alert("系统出错!")
            }           
	    }) ;
    // return "addMat";
    return false;
  });
  
  form.on('submit(formDemo)', function(data){
	  //原料信息
	  var da={
				name:"all"
		};
	  
		$.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.getAllProInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(res){  
	        	window.AddData=[];
	        	if(res!=null&&res.length>0){
	        		for(var i = 0;i < res.length ;i++){
			        	var get = res[i].split(",");
		            	var da = {
		            			产品编号:changeToCode(get[0]),产品名称:get[1],打包规格:get[2],单价:get[3],"保质期-天":get[4],单位:get[5],库存:get[6]
		            	};
		            	window.AddData.unshift(da);  
	        		}
	        	}
	        	table.reload('Table',{
					data : window.AddData
				});
				form.render();
	        },
            error:function (data) {
                alert("系统出错!")
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
