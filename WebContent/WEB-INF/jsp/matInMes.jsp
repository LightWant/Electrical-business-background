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
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
	<div class="layui-layout layui-layout-admin">
		<div class="layui-side" aria-hidden="true" hidden="true"> </div>

		<div class="layui-body" >
			
			<form class="layui-form" action="" id="myForm">
				<div class="layui-container"> 
					<div class="layui-row">
							<div class="layui-form-item">
								<div id="Gl_Table">
				        			<table 
				        				class="layui-hide" lay-filter="Table" 
				        				id="Table" style="max-height:600px; height:600px;">
				        			 </table>
				    			</div>
			    			</div>
						<!--<div class="layui-col-md6">
							<div class="layui-form-item">
								<div id="Gl_Table">
				        			<table 
				        				class="layui-hide" lay-filter="Table" 
				        				id="Table1" style="max-height:600px; height:600px;">
				        			 </table>
				    			</div>
			    			</div>
						</div>-->
					</div>
				</div>
				
    		</form>
    		<form class="layui-form" action="" >
    			<div class="layui-form-item">
					<label class="layui-form-label layui-inline">入库批次编号:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="rkpcid" id="rkpcid">
			     	</div>
			     	<button type="button" class="layui-btn" lay-submit lay-filter="formDemo">查询该入库批次的信息</button>
			  
			     	<div class="layui-inline">
			     		<label class="layui-form-label layui-inline">入库负责人ID:</label>
			     		<div class="layui-input-item layui-inline">
			           		<select id="rkfzr" name="rkfzr"  lay-filter="selStaff">
								<%Integer [] ids = HumanSystem.findAllStaff();
								Calendar cal = Calendar.getInstance();
								if(ids!=null)
								for(Integer key:ids){%>
									<option value=<%=key%>><%= cal.get(Calendar.YEAR)+String.format("%03d",key)%></option>
								<%}%>
							</select>
			     		</div>
			     		<button type="button" class="layui-btn" lay-submit lay-filter="formDemo1">查询该员工负责的入库信息</button>
			     	</div>
				</div>
    		</form>
    		<form class="layui-form" action="" >
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-container"> 
							<div class="layui-row">
								<div class="layui-col-md4">
									<label class="layui-form-label layui-inline">起始时间:</label>
									<div class="layui-input-item layui-inline">
							            <input type="text" name="begindate" id="begindate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
							     	</div>
								</div>
								<div class="layui-col-md4">
									<label class="layui-form-label layui-inline">结束时间:</label>
									<div class="layui-input-item layui-inline">
							    	    <input type="text" name="endate" id="endate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
									</div>
								</div>
								<div class="layui-col-md4">
									<button type="button" class="layui-btn" lay-submit lay-filter="formDemo2">该时间段的入库信息</button>
								</div>
							</div>
						</div>
						
					</div>
				</div>
		</form>
 
	  </div>
	</div>
	
	<div id="openProductBox" style="display: none; padding: 10px;">
		<table id="openProductTable" lay-filter="openProductTable"></table>
	</div>
<!--<script src="../src/layui.js"></script>-->
</body>

<script src="layui/layui.js"></script>
<script>
//表格数据缓存
	var AddData=[];
	var AddData1=[];
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form','laydate', 'layer'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate = layui.laydate
        layer = layui.layer;
        
        form.render();
        
        laydate.render({elem: '#begindate'});
        laydate.render({elem: '#endate'});
        
		element.init();
		//入库批次输入验证
		form.verify({
			  num: function(value){
				  if(value!=""){
					  if(!new RegExp('^[+]{0,1}(\d+)$').test(value.toString())){
						 return '原料入库批次只能是数字！';
					  }
				  }
			   }
		  	  
		  	});
		
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
		//入库批次ID,进货批次来源,原料名称,入库数量,负责人ID,入库时间,剩余数量
		    ,cols: [[	        
		      ,	{field:'入库批次ID',  width:'14%', title: '入库批次ID'}
		      , {field:'进货批次来源', width:'14%', title: '进货批次来源'}
		      , {field:'原料名称', width:'14%', title: '原料名称'}
		      , {field:'入库仓库', width:'14%', title: '入库仓库'}
		      , {field:'入库数量', width:'14%', title: '入库数量'}
		      , {field:'负责人ID', width:'14%', title: '负责人ID'}
		      , {field:'入库时间', width:'16%', title: '入库时间'}
		      , {field:'剩余数量', width:'14%', title: '剩余数量'}
		    ]],
		    data:AddData
		  });
		/*table.render({
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
		      ,	{field:'生产批次ID',  width:'25%', title: '生产批次ID'}
		      , {field:'使用原料出库批次ID', width:'25%', title: '产品ID'}
		      , {field:'原料使用数量', width:'25%', title: '生产数量'}
		      , {field:'原料单位', width:'25%', title: '原料单位'}
		    ]],
		    data:AddData1
		  });*/
		 
		
        /*JQuery 限制文本框只能输入数字*/
        $("#rkpcid").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        //提交，刷新表单
        form.on('submit(formDemo)', function(data){
          //  console.log(data.field);
          	var rkpc = data.field.rkpcid
          	if(rkpc == ""||rkpc == null){
          		alert("请先输入查询的原料入库批次ID！");
          		return false;
          	}
            var da = {
            	name:data.field.rkpcid
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatInInfoViapc',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
	                	var get = data.split(",");
	                	var da = {
	                			入库批次ID:get[0],进货批次来源:get[1],原料名称:get[2],入库仓库:get[3],入库数量:get[4],负责人ID:changeToCode(get[5]),入库时间:get[6],剩余数量:get[7]
	                	};
		        		window.AddData.unshift(da);
                	}
	    				table.reload('Table',{
	    					data : window.AddData
	    				});
    				form.render();
                },error:function(s) {
                	layer.msg("error :"+s);
                }
      		});
        
            return false;
        });
        
        form.on('submit(formDemo1)', function(data){
        	var rkfzr = data.field.rkfzr
          	if(rkfzr == ""||rkfzr == null){
          		alert("请先输入查询入库负责人id！");
          		return false;
          	}
            var da = {
            	name:data.field.rkfzr
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatInInfoViaFzr',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			入库批次ID:get[0],进货批次来源:get[1],原料名称:get[2],入库仓库:get[3],入库数量:get[4],负责人ID:changeToCode(get[5]),入库时间:get[6],剩余数量:get[7]
		                	};
		                	
			        		window.AddData.unshift(da);
	                	}
                	}
	    				table.reload('Table',{
	    					data : window.AddData
	    				});
    				form.render();
                },error:function(s) {
                	layer.msg("error :"+s);
                }
      		});
        
            return false;
        });
        form.on('submit(formDemo2)', function(data){
        	var begin = data.field.begindate
        	var end = data.field.endate
          	if(begin == "" || end == ""|| begin == null || end == null){
          		alert("请先输入完整的起始时间！");
          		return false;
          	}
            var da = {
            	name:begin+","+end
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatInInfoViaTime',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			入库批次ID:get[0],进货批次来源:get[1],原料名称:get[2],入库仓库:get[3],入库数量:get[4],负责人ID:changeToCode(get[5]),入库时间:get[6],剩余数量:get[7]
		                	};
		                	
			        		window.AddData.unshift(da);
	                	}
                	}
	    				table.reload('Table',{
	    					data : window.AddData
	    				});
    				form.render();
                },error:function(s) {
                	layer.msg("error :"+s);
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
</html>
