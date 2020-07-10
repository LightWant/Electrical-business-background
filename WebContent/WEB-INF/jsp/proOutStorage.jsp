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
			<div class="layui-container"> 
				<div class="layui-row">
					
						<div class="layui-form-item">
							<label class="layui-form-label" >订单ID</label>
							<div class="layui-input-block" >
								<select id="ddid" name="ddid"  required lay-verify="required"  lay-filter="selMat">
								<%Integer [] ids = ProductSystem.getDingdanId();
								Calendar cal = Calendar.getInstance();
								if(ids!=null&&ids.length!=0){
									
									for(Integer id:ids){%>
									<option value=<%=id%>><%=cal.get(Calendar.YEAR)+String.format("%03d",id)%></option>
								<%}}
								%>
								</select>
						   	</div>
					   </div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<label class="layui-form-label" style="font-20px">订单内容</label>
						
					</div>
					<div class="layui-col-md6">
						<label class="layui-form-label" style="font-20px">交付细节</label>
						
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<div id="Gl_Table">
			        			 <table 
			        				class="layui-hide" lay-filter="Table" 
			        				id="Table" style="max-height:500px; height:500px;">
			        			 </table> 
			        			 
			    			</div>
		    			</div>
	    			</div>
	    			<div class="layui-col-md6">
						<div class="layui-form-item">
							<div id="Gl_Table">
			        			 <table 
			        				class="layui-hide" lay-filter="Table1" 
			        				id="Table1" style="max-height:500px; height:500px;">
			        			 </table> 
			        			 
			    			</div>
		    			</div>
	    			</div>
				</div>
				<div class="layui-row">
					<div class="layui-form-item">
						<button class="layui-btn" lay-submit lay-filter="formDemo1">确认交付</button>
					</div>
				</div>
				
				
			</div>
			
			   
			
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
var AddData=[
     ];
var AddData1=[
    ];
layui.use(['form', 'layedit', 'table','laydate'], function(){
  var form = layui.form
  ,table = layui.table
  ,layer = layui.layer
  $ = layui.jquery;
//订单交付来源table
  
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
	       {field:'产品名称',  width:'40%', title: '产品名称'}
	      ,{field:'入库批次来源',  width:'30%', title: '入库批次来源'}
	      ,{field:'交付数量',  width:'30%', title: '交付数量'}
	      
	    ]],
	    data:AddData
	});
//订单细节table
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
	      ,{field:'预订数量',  width:'50%', title: '预订数量'}
	      
	    ]],
	    data:AddData
	});
  
  
  form.on('select(selMat)',function(data){
	  AddData = [];
	  AddData1 = [];
	  var da={
			  name:data.value//选择的订单ID
	  };
	  //alert("订单细节");
	  $.ajax({
		  url:'${pageContext.request.contextPath}/Product_Staff.finddd',     
	      type :'post',       
	      contentType : 'application/json;charset=utf-8',
	      data:JSON.stringify(da),
		  success:function(res){   
			  console.log("map length "+Object.keys(res).length);
			  if(res == null||Object.keys(res).length == 0){
				  layer.msg('该订单没有内容！');
				  AddData = [];
				  AddData1 = [];
				//重新渲染表格
				  table.reload('Table',{
						data : window.AddData
					});
				  table.reload('Table1',{
						data : window.AddData1
					});
			  }
			  else{
				  
				  for(var key in res){
					  var data1 = {
					      	产品名称:key
					      	,预订数量:res[key] 
					  };
					  window.AddData.unshift(data1);
				  }
				//重新渲染表格
				  table.reload('Table',{
						data : window.AddData
					});
				
				  //判断交付
				  $.ajax({
					  url:'${pageContext.request.contextPath}/Product_Staff.ddjf',     
				      type :'post',       
				      contentType : 'application/json;charset=utf-8',
				      data:JSON.stringify(da),
				      success:function(res){   
				    	  if(res==null||res.length == 0){
				    		  //alert('当前库存无法交付该订单，请延期取货时间！' );
				    		  layer.msg('当前库存无法交付该订单，请延期取货时间！' );
				    	  }
				    	  else{
				    		  for(var i = 0;i < res.length ;i++){
				    			  var per = res[i].split(",");
				    			  var data2 ={
				    				 产品名称:per[0]
				    				,入库批次来源:per[1]
				    				, 交付数量:per[2]
				    			  };
				    			  console.log(JSON.stringify(data2));
				    			  window.AddData1.unshift(data2);
				    		  }
				    		  table.reload('Table1',{
									data : window.AddData1
								});
				    	  }
				      },error:function(re){
				    	  layer.msg("系统错误！");
				      }
				  });
			  }
			  
         },      
         
         error:function (data) {
        	 layer.msg("系统出错！");
         }      
	  });
	  //alert("订单交付");
	  /*console.log("adddata lenght "+window.AddData.length);
	  if(window.AddData.length<=0){
		  layer.open({
				 content:'该订单没有内容！' 
			  });
		  return false;
	  }*/
	  
	  
	  return false;
  })
  
 
 
  form.on('submit(formDemo1)', function(data){
	 //console.log("add");
    //layer.msg(JSON.stringify(data.field));
    if(AddData1.length <= 0){
    	layer.msg('该订单无法交付！');
    	return false;
    }
    var da = {
    		name:data.field.ddid
    };
    console.log(JSON.stringify(da));
    //alert(JSON.stringify(da));
    //console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/Product_Staff.asurejf',   
        //该写这儿了
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        success:function(res){   
        	if(res){
        		layer.msg('交付成功！');
        		window.AddData = [];
        		window.AddData1 = [];
        		table.reload('Table',{
					data : window.AddData
				});
        		table.reload('Table1',{
					data : window.AddData1
				});
        		$("#ddid").empty();
        		var dad= {
        				name:""
        		};
        		$.ajax({   
        	        url:'${pageContext.request.contextPath}/Product_Staff.getddid',   
        	        //该写这儿了
        	        type :'post',       
        	        contentType : 'application/json;charset=utf-8',
        	        data:JSON.stringify(dad),   
        	        success:function(res){  
        	        	if(res==null||res.length == 0){
        	        		layer.msg("已经没有可以交付的订单！");
        	        	}
        	        	else{
        	        		for(var i = 0;i<res.length;i++){
        	        			$("#ddid").append(new Option(changeToCode(res[i]),res[i]));
        	        		}
        	        		form.render('select');
        	        	}
        	        },error:function(re){
        	        	layer.msg("查找订单出错！");
        	        }
        		});
        		
        	}
        	else{
        		layer.open({
        			title:'产品交付',
        			content:'交付失败！'
        		});
        	}
        
        },  error:function (s) {
                alert("系统出错!")
            }           
    }) ;         
        
    //return "addMat";
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
