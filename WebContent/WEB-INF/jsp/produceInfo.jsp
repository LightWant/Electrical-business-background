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
					</div>
				</div>
				
    		</form>
    		<form class="layui-form" action="" >
    			
    			<div class="layui-form-item">
	    			<div class="layui-container"> 
						<div class="layui-row">
							<div class="layui-col-md3">
								<label class="layui-form-label">生产批次:</label>
								<div class="layui-input-block">
						           <input type="text" class="layui-input" name="scpc" id="scpc">
						     	</div>
						     	
				  			</div>
				  			<div class="layui-col-md3">
				  				<button type="button" class="layui-btn" lay-submit lay-filter="formDemo">查询该生产批次信息</button>
				  			</div>
				  		</div>
				  	</div>
			  	</div>
    		</form>
    		<form class="layui-form" action="" >
				<div class="layui-form-item">
					
						<div class="layui-container"> 
							<div class="layui-row">
								<div class="layui-col-md3">
						     		<label class="layui-form-label">车间编号：</label>
						     		<div class="layui-input-block">
						           		<select id="cjid" name="cjid"  lay-filter="selStaff">
						           			<option value="全部" selected>全部</option>
											<%int [] ids = WorkSystem.selectAllWork();
											if(ids!=null)
											for(int key:ids){%>
												<option value=<%=key%>><%=key %></option>
											<%}%>
										</select>
						     		</div>
						     		
						     	</div>
								<div class="layui-col-md3">
									<label class="layui-form-label">起始时间:</label>
									<div class="layui-input-block">
							            <input type="text" name="begindate" id="begindate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
							     	</div>
								</div>
								<div class="layui-col-md3">
									<label class="layui-form-label">结束时间:</label>
									<div class="layui-input-block">
							    	    <input type="text" name="endate" id="endate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
									</div>
								</div>
								<div class="layui-col-md3">
									<button type="button" class="layui-btn" lay-submit lay-filter="formDemo1">该时间段该车间生产信息</button>
								</div>
							</div>
						</div>
						
				</div>
		</form>
 
	  </div>
	</div>
	
<!--<script src="../src/layui.js"></script>-->
</body>

<script src="layui/layui.js"></script>
<script>
//表格数据缓存
	var AddData=[];
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
		    	//生产批次，生产车间，生产产品名称，生产数量，使用原料的出库批次，使用的原料数量，生产时间，质检员编号
		      ,	{field:'生产批次',  width:'14%', title: '生产批次'}
		      , {field:'生产车间', width:'14%', title: '生产车间'}
		      , {field:'生产产品名称', width:'14%', title: '生产产品名称'}
		      , {field:'生产数量', width:'14%', title: '生产数量'}
		      , {field:'使用原料的出库批次', width:'16%', title: '使用原料的出库批次'}
		      , {field:'使用的原料数量', width:'14%', title: '使用的原料数量'}
		      , {field:'生产时间', width:'14%', title: '生产时间'}
		      , {field:'质检员编号', width:'14%', title: '质检员编号'}
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
        $("#scpc").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        //提交，刷新表单
        form.on('submit(formDemo)', function(data){
          //  console.log(data.field);
          	var scpc = data.field.scpc
          	if(scpc == ""||scpc == null){
          		layer.alert("请先输入查询的生产批次编号！");
          		return false;
          	}
            var da = {
            	name:data.field.scpc
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Work_Staff.getWorkInfoViapc',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	
                	window.AddData=[];
                	if(data!=null){
                		get = data.split(",")
                		//生产批次，生产车间，生产产品名称，生产数量，使用原料的出库批次，使用的原料数量，生产时间，质检员编号
	                	var da = {
                			生产批次:get[0],生产车间:get[1],生产产品名称:get[2],生产数量:get[3],使用原料的出库批次:get[4],使用的原料数量:get[5],生产时间:get[6],质检员编号:changeToCode(get[7])
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
        	var cjid = data.field.cjid
        	var begin = data.field.begindate
        	var end = data.field.endate
        	console.log(cjid+","+begin+","+end);
          	if(cjid == ""||cjid == null||begin == ""||begin == null||end == ""||end == null){
          		layer.alert("请先输入相关查询信息！");
          		return false;
          	}
            var da = {
            	name:cjid+","+begin+","+end
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Work_Staff.getScxjViaTime',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	console.log(data);
                	window.AddData=[];
                	if(data!=null){
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			生产批次:get[0],生产车间:get[1],生产产品名称:get[2],生产数量:get[3],使用原料的出库批次:get[4],使用的原料数量:get[5],生产时间:get[6],质检员编号:changeToCode(get[7])
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
