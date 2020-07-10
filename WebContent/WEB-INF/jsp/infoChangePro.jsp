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
		<div class="layui-container"> 
				<div class="layui-row">
					<div class="layui-col-md12">
						<div class="layui-form-item">
						  <label class="layui-form-label"  id="flb1">产品名称</label>
						  <div class="layui-input-block">
							
								<select id="cpname" name="cpname"  required lay-verify="required" >
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
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<label class="layui-form-label">修改单价</label>
							 <div class="layui-input-block">
							 	<input id="price" name="price" lay-verify="fudian" class="layui-input" autocomplete="off">
							 </div>
						</div>
					</div>
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<div class="layui-input-block" >
								<button class="layui-btn" lay-submit lay-filter="formDemo">修改单价</button>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<label class="layui-form-label">修改保质期</label>
							 <div class="layui-input-block">
							 	<input id="baozhiqi" name="baozhiqi" lay-verify="num" class="layui-input" autocomplete="off">
							 </div>
						</div>
					</div>
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<div class="layui-input-block" >
								<button class="layui-btn" lay-submit lay-filter="formDemo1">修改保质期</button>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<label class="layui-form-label">修改打包规格</label>
							 <div class="layui-input-block">
							 	<input id="dbgg" name="dbgg" lay-verify="mm" class="layui-input" autocomplete="off">
							 </div>
						</div>
					</div>
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<div class="layui-input-block" >
								<button class="layui-btn" lay-submit lay-filter="formDemo2">修改打包规格</button>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md2">
						<div class="layui-form-item">
							<label class="layui-form-label">修改配方</label>
						</div>
					</div>
					<div class="layui-col-md4">
						<div class="layui-form-item">
							<label class="layui-form-label">原料名称</label>
							 <div class="layui-input-block">
							 	<select id="ylname" name="ylname"  lay-filter="selMat">
								<%String[]names = MaterialSystem.selectAllMaterialName();
								if(names!=null)
								for(String key:names){%>
									<option value=<%=key%>><%=key %></option>
								<%}%>
								</select>
							 </div>
						</div>
					</div>
					<div class="layui-col-md3">
						<div class="layui-form-item">
							<label class="layui-form-label">原料数量</label>
							 <div class="layui-input-block">
							 	<input id="ylnum" name="ylnum" lay-verify="num" class="layui-input" autocomplete="off">
							 </div>
						</div>
					</div>
					<div class="layui-col-md3">
						<div class="layui-form-item">
							<div class="layui-input-block" >
								<button class="layui-btn" lay-submit lay-filter="formDemo3">添加一行配方记录</button>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-form-item">
						<div id="Gl_Table">
		        			 <table 
		        				class="layui-hide" lay-filter="Table" 
		        				id="Table" style="max-height:500px; height:500px;">
		        			 </table> 
		        			 
		    			</div>
					</div>
					<div class="layui-row">
						<div class="layui-col-md3">
							<button class="layui-btn" lay-submit lay-filter="formDemo4">修改配方</button>
						</div>
					</div>
				</div>
		</div><!-- container 写到这儿了 -->
			
		
		</form>
	
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
var AddData=[
     ];

layui.use(['form','layer','table','laydate'], function(){
  var form = layui.form;
  var layer = layui.layer;
  var laydate = layui.laydate;
  var table = layui.table;
  $ = layui.jquery;
  form.verify({
	  num: function(value) {
			  if(value!=""&&!/^(0|\+?[1-9][0-9]*)$/.test(value))
			  	return '只能是正整数哦';
  		}
		  
    ,mm: function(value){
    	 if(value!=""&&!/^(0|\+?[1-9][0-9]*)$/.test(value))
		  return '打包规格只能是正整数哦！';
    }
  	,fudian: function(value){
  		if(value!=""&&!/^((0{1}\.\d{1,2})|([1-9]\d*\.{1}\d{1,2})|([1-9]+\d*)|0)$/.test(value))
			 return  '单价是保留两位小数的正数哦！';
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
	    	{field:'原料名称',  width:'50%', title: '原料名称'}
	      ,{field:'配方数量',  width:'50%', title: '配方数量'}
	    ]],
	    data:AddData
	});
	 form.on('submit(formDemo3)', function(data){
		  var num = data.field.ylnum
		  if(num == null || num == ""){
			  layer.msg("请先输入原料数量！");
			  return false;
		  }
		  var name = data.field.ylname;
		  var find = 0;
		  for(var i = 0; i < window.AddData.length;i++){
			  if(window.AddData[i]['原料名称']==name){
				  find = 1;
				  layer.msg("您已经添加过"+name+'的配方数量了！欲修改请先删除！（删除请双击相应单元行）');
			  	  break;
			  }
		  }
		  if(find == 0){
			  var da={
					  原料名称 : data.field.ylname
					  , 配方数量 : data.field.ylnum
			  };
			  window.AddData.unshift(da);
			  table.reload('Table',{
			  	data : window.AddData
			  });
		  }
		  return false;
	  });
	  //双击行删除
  table.on('rowDouble(Table)', function(obj){
	  var data = obj.data;
	    //alert(JSON.stringify(data));
	    var flag = 1;
	    for(var i = 0;i < window.AddData.length;i++){
	    	//alert(window.AddData[i]['产品名称']);
	    	//alert(window.AddData[i]['相应产能']);
	    	if(window.AddData[i]['原料名称']==data['原料名称']&&window.AddData[i]['配方数量']==data['配方数量']&&data['配方数量']!=""){
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
  form.on('submit(formDemo)', function(data){
	  var price = data.field.price;
	  if(price == ""){
		  layer.msg("请先输入单价！");
		  return false;
	  }
	  var da = {
			  name:data.field.cpname+",产品单价"+","+price
	  };
	  console.log(JSON.stringify(da));
	  $.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.changeinfo',   
	        //该写这儿了
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){   
	        	if(res){
	        		layer.msg('修改成功！');
	        	}
	        	else{
	        		layer.msg("修改失败！数据库异常");
	        	}
	        },error:function(re){
	        	layer.msg("系统错误！");
    		}
    });
	    // return "addMat";
	    return false;
});
  form.on('submit(formDemo1)', function(data){
	  var bzq = data.field.baozhiqi;
	  if(bzq == ""){
		  alert("请先输入保质期！");
		  return false;
	  }
	  var da = {
			  name:data.field.cpname+",产品保质期"+","+bzq
	  };
	  $.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.changeinfo',   
	        //该写这儿了
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){   
	        	if(res){
	        		layer.msg('修改成功！');
	        	}
	        	else{
	        		layer.msg("修改失败！数据库异常");
	        	}
	        },error:function(re){
	        	layer.msg("系统错误！");
      		}
      });
    // return "addMat";
    return false;
  });
  form.on('submit(formDemo2)', function(data){
  	//修改车间信息
	  var dbgg = data.field.dbgg;
	  if(dbgg == ""){
		  layer.msg("请先输入打包规格！");
		  return false;
	  }
	  var da = {
			  name:data.field.cpname+",产品打包规格"+","+dbgg
	  };
	  $.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.changeinfo',   
	        //该写这儿了
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){   
	        	if(res){
	        		layer.msg('修改成功！');
	        	}
	        	else{
	        		layer.msg("修改失败！数据库异常");
	        	}
	        },error:function(re){
	        	layer.msg("系统错误！");
    		}
    	});
   
	  
  	return false;
  });
  form.on('submit(formDemo4)', function(data){
	  if(AddData.length<=0){
		  layer.msg("请先输入配方信息！");
			 return false;
		 }
		 var ylname = AddData[0]['原料名称'];
		 var ylnum = AddData[0]['配方数量'];
		 for(var i = 1;i < AddData.length;i++){
			 ylname += ","+ AddData[i]['原料名称'];
			 ylnum += "," + AddData[i]['配方数量'];
		 }
	    //layer.msg(JSON.stringify(data.field));
	    var da = {
	    		mingcheng:data.field.cpname,
	    		dbgg:"",
	    		baozhiqi:"",
	    		danwei:"",
	    		price:"",
	    		ylname:ylname,
	    		ylnum:ylnum
	    };
	    //(JSON.stringify(da));
	    $.ajax({   
	        url:'${pageContext.request.contextPath}/Product_Staff.changePeifang',   
	        //该写这儿了
	        type :'post',       
	        contentType : 'application/json;charset=utf-8',
	        data:JSON.stringify(da),   
	        success:function(res){   
	        	if(res){
	        		layer.msg('修改成功！');
	        	}
	        	else{
	        		layer.msg("修改失败！数据库异常");
	        	}
	        },error:function(re){
	        	layer.msg("系统错误！");
    		}
    	});
  	return false;
  });
});
</script>
   
</div>
</body>
</html>
