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
				<label class="layui-form-label" style="font-size:20px"><b>查询信息</b></label>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="flb1">员工ID</label>
				  <div class="layui-input-block">
						<select id="ygid" name="ygid"  required lay-verify="required" >
						<option value="全部" selected>全部</option>
						<%Integer [] ids = HumanSystem.findAllStaff();
						Calendar cal = Calendar.getInstance();
						if(ids!=null)
						for(Integer key:ids){%>
							<option value=<%=key%>><%= cal.get(Calendar.YEAR)+String.format("%03d",key)%></option>
						<%}%>
					   </select>
				  </div>
			<div class="layui-form-item">
				  <label class="layui-form-label"></label>
				  <div class="layui-form-item"></div>
				  <div class="layui-input-block">
				  	<button class="layui-btn" lay-submit lay-filter="formDemo1">查询</button>
				  </div>
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
		<form class="layui-form" action="" id="form2">
		  	  <div class="layui-form-item">
		  	  	<label class="layui-form-label" style="font-size:20px"><b>修改信息</b></label>
		  	  </div>
		  	 
			  <div class="layui-form-item">
			  		
					<label class="layui-form-label" id = "flb2" >员工ID</label>
					<div class="layui-input-block" >
						<select id="ygid1" name="ygid1"  required lay-verify="required" lay-filter="selStaff">
							<%
							if(ids!=null)
							for(Integer key:ids){%>
								<option value=<%=key%>><%= cal.get(Calendar.YEAR)+String.format("%03d",key)%></option>
							<%}%>
						</select>
				   	</div>
			</div>
			<div class="layui-form-item">
				 <div class="layui-inline">
				 
					<label class="layui-form-label" >基础工资</label>
				    <div class="layui-input-inline">
				    	<input id="gz" name="gz" min="0" lay-verify="num" class="layui-input" autocomplete="off">
				    </div>
				    
				    
				    <div class="layui-inline">
				  		<label class="layui-form-label" >在岗状态</label>
					    <div class="layui-input-inline">
					    	<select id="zgzt" name="zgzt" >
					    		<option value="在岗" selected>在岗</option>
					    		<option value="请假">请假</option>
					    		<option value="离职">离职</option>
					    	</select>
					    </div>
					</div>
				 </div>
			 </div>
			 <div class="layui-form-item">
				 <div class="layui-inline">
					 <div class="layui-container"> 
					 
						  <div class="layui-row">
						  	<div class="layui-col-md2">
						  		<label class="layui-form-label" ></label>
						  	</div>
						    <div class="layui-col-md3">
						      <button class="layui-btn" lay-submit lay-filter="formDemo3">修改基础工资</button>
						    </div>
						    <div class="layui-col-md3">
						    	<button class="layui-btn" lay-submit lay-filter="formDemo5">修改在岗状态</button>
						    </div>
						    
						  </div>
						  
					 </div>
					
				 </div>
			 </div>
			 <div class="layui-form-item">
			 <div class="layui-container"> 
			 <div class="layui-row">
			 	<div class="layui-col-md2">
			  		<label class="layui-form-label" ></label>
			  	</div> 	
			    <div class="layui-col-md6">
			    	<button class="layui-btn" lay-submit lay-filter="formDemo6">重置密码</button>
			    </div>
				  
			</div>
			</div>
			</div> 
		</form>
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
AddData = [];
//Demo
layui.use(['form','layer','table'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var table = layui.table;
  $ = layui.jquery;
 //工资验证
  form.verify({
	  num: function(value){
		  if(value!=""){
			  if(!new RegExp('^[+]{0,1}(\d+)$').test(value.toString()) && !(new RegExp('^(0|[1-9](\.[0-9]+|[0-9]*))$').test(value)) ){
				 return '工资格式不正确！';
			  }
			  else if(value<2000){
		        return '工资要大于2000！';
		      }
		  }
	   }
  	  
  	});
  //biaoge 
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
	    	//编号,姓名,性别,职位,身份证,手机,邮箱,住址,入职时间,在岗状态,工资
	       {field:'编号', width:'9%', title: '编号'}
	      , {field:'姓名', width:'9%', title: '姓名'}
	      , {field:'性别', width:'8%', title: '性别'}
	      , {field:'职位', width:'14%', title: '职位'}
	      , {field:'身份证', width:'14%', title: '身份证'}
	      , {field:'手机', width:'14%', title: '手机'}
	      , {field:'邮箱', width:'14%', title: '邮箱'}
	      , {field:'住址', width:'14%', title: '住址'}
	      , {field:'入职时间', width:'11%', title: '入职时间'}
	      , {field:'在岗状态', width:'10%', title: '在岗状态'}
	      , {field:'工资', width:'10%', title: '工资'}
	      
	    ]],
	    data:AddData
	  }); 
  
  form.on('submit(formDemo1)', function(data){
	  //查询员工信息
	 
	  var da={
				name:data.field.ygid
		};
	  
	  //alert("查询id:"+JSON.stringify(da));
		$.ajax({   
	        url:'${pageContext.request.contextPath}/staffInfo.getStaffInfo',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(result){   
	        	//alert("success");
	             
            	if(result == null){
            		layer.alert("没有该员工信息！");
            	}  
            	else{
            		window.AddData = [];
            		if(result==null || result.length == 0){
            			
            		}
            		else{
            			for(var i = 0;i < result.length;i++){
            				var get=eval("("+result[i]+")");
            				var da = {
            						编号:changeToCode(get['id']),姓名:get['mingchen'],性别:get['sex'],职位:get['zhiwei']
            						,身份证:get['shenfenzhengID'],手机:get['shouji'],邮箱:get['mail'],住址:get['zhuzhi']
            						,入职时间:get['ruzhiTIME'],在岗状态:get['zaigangSTAGE'],工资:get['gongzi']
            				}
            				window.AddData.unshift(da);  
            			}
            		}
            		table.reload('Table',{
    					data : window.AddData
    				});
    				form.render();
            	}
	        },
            error:function (data) {
                alert("系统出错!")
            }           
	    }) ;
    // return "addMat";
    return false;
  });
  
  form.on('submit(formDemo3)', function(data){
	  //原料信息
	  if(data.field.gz==""){
		  alert("请先输入修改工资！");
		  return null;
	  }
	  var da={
				name:data.field.ygid1+","+data.field.gz
		};
	  
		$.ajax({   
	        url:'${pageContext.request.contextPath}/staffInfo.changeGz',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(res){       
	             //alert("success");
            	 //弹出原料库存信息
            	 
           		if(res==true){
           		  layer.msg('修改成功！');   
           		}
           		else{
           			layer.msg('修改失败！');   
           		}
           		 
	        },
            error:function (data) {
            	layer.msg('系统出错！');   
            }           
	    }) ;
    // return "addMat";
    return false;
  });
  form.on('submit(formDemo4)', function(data){
	 //职位
	  if(data.field.zhiwei==""){
		  alert("请先输入修改职位！");
		  return null;
	  }
	  var da={
				name:data.field.ygid1+","+data.field.zhiwei
		};
	  $.ajax({   
	        url:'${pageContext.request.contextPath}/staffInfo.changeZhiwei',     
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),    
	        
	        success:function(res){       
	        	//alert("success");
            	 //弹出原料库存信息
            	 
           		if(res==true){
           		  layer.open({
           			title: '修改工资'
           			,content: '修改成功！'
           			});   
           		}
           		else{
           			alert("修改失败！");
           		}
	        },error:function(re){
	        	alert("系统错误！");
	        }
	  });
	  return false;
  });
  form.on('submit(formDemo5)', function(data){
		 //职位
		  if(data.field.zgzt==""){
			  alert("请先输入修改在岗状态！");
			  return null;
		  }
		  var da={
					name:data.field.ygid1+","+data.field.zgzt
			};
		  $.ajax({   
		        url:'${pageContext.request.contextPath}/staffInfo.changeZgzt',     
		        type :'post',       
		        contentType : 'application/json;charset=utf-8',
		        data:JSON.stringify(da),    
		        
		        success:function(res){       
		        	//alert("success");
	            	 //弹出原料库存信息
	            	 
	           		if(res==true){
	           		  layer.open({
	           			title: '修改在岗状态'
	           			,content: '修改成功！'
	           			});   
	           		}
	           		else{
	           			alert("修改失败！");
	           		}
		        },error:function(re){
		        	alert("系统错误！");
		        }
		  });
		  return false;
	  });
  form.on('submit(formDemo6)', function(data){
	  var id = data.field.ygid1;
	  layer.open({
	        content:'确定要重置编号为'+id+'的员工密码吗？'
	        ,btn: '确定'
	        ,btnAlign: 'c' //按钮居中
	        ,shade: 0 //不显示遮罩
	        ,yes: function(){
	        	layer.closeAll();
	        	var da = {
	  				  name:id
	  		  }
	  		  $.ajax({   
	  		        url:'${pageContext.request.contextPath}/staffInfo.resetPass',     
	  		        type :'post',       
	  		        contentType : 'application/json;charset=utf-8',
	  		        data:JSON.stringify(da),    
	  		        
	  		        success:function(res){      
	  		        	if(res){
	  		        		layer.open({
	  		        			title:'重置密码',
	  		        			content:'成功！'
	  		        		});
	  		        	}
	  		        	else{
	  		        		layer.open({
	  		        			title:'重置密码',
	  		        			content:'失败'
	  		        		});
	  		        	}
	  		        },error:function(re){
	  		        	alert("系统错误！");
	  		        }
	  		  });
	          
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
