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
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
	<div class="layui-layout layui-layout-admin">
		<div class="layui-side" aria-hidden="true" hidden="true"> </div>

		<div class="layui-body" >
			<form class="layui-form" action="" id="myForm">
 
				<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:1000px; height:1000px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
					<label class="layui-form-label layui-inline">时间:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="tihuo1" id="tihuo1">
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" class="layui-input" name="tihuo2" id="tihuo2">
					</div>
				</div>
				
				<div class="layui-form-item">
				    <div class="layui-input-block">
				      <button class="layui-btn" lay-submit id="btn" lay-filter="formDemo">提交</button>	     
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
    layui.use(['element','table','jquery', 'form','laydate'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate=layui.laydate;
		element.init();
		
		function flushTable(AddData) {
			table.render({
			    elem: '#Table',
			    height:400
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
			    	{field:'No',  width:'8%', title: 'No.', sort:true}
			      ,	{field:'订单ID',  width:'15%', title: '订单ID'}
			      , {field:'应付金额', width:'20%', title: '应付金额'}
			      , {field:'实付金额', width:'20%', title: '实付金额'}
			      , {field:'下单时间', width:'20%', title: '下单时间', sort:true}
			      , {field:'订单类别', width:'17%', title: '订单类别'}
			    ]],
			    data:AddData
			});
		}
		
		laydate.render({elem: '#tihuo1'});
		laydate.render({elem: '#tihuo2'});
	//	console.log("asdsd");
		$.ajax({
            type : 'post',
            url : '${pageContext.request.contextPath}/Jinhuoflow.do',
            //设置contentType类型为json
            contentType : 'application/json;charset=utf-8',
            //json数据
            data : "Jinhuo",
            success : function(data) {
            //	console.log(data);
            	//console.log("sda");
            	var AddData=[];
            	for(i = 0; i < data.length; i++) {
            		var da={No:i+1, 
            				订单ID:changeToCode(data[i][0]),
            				应付金额:data[i][1],
            				实付金额:data[i][2],
            				下单时间:data[i][3],
            				订单类别:data[i][4]};
        	        AddData.push(da);
            	}
            	flushTable(AddData);
            },error:function(s) {
            	console.log("err");
            }
  		});
		
		form.on('submit(formDemo)', function(data) {
			if(data.field.tihuo1 == null || data.field.tihuo1.length == 0) {
				layer.msg("请选择开始时间");
				return;
			}
			if(data.field.tihuo2 == null || data.field.tihuo2.length == 0) {
				layer.msg("请选择结束时间");
				return;
			}
			//console.log(data);
			var da = {
				 tihuoTime1:data.field.tihuo1
				,tihuoTime2:data.field.tihuo2
			}
			$.ajax({
	            type : 'post',
	            url : '${pageContext.request.contextPath}/Jinhuoflow.filter',
	            //设置contentType类型为json
	            contentType : 'application/json;charset=utf-8',
	            //json数据
	            data : JSON.stringify(da),
	            success : function(data) {
	            	var AddData=[];
	            	for(i = 0; i < data.length; i++) {
	            		var da={No:i+1, 
	            				订单ID:changeToCode(data[i][0]),
	            				应付金额:data[i][1],
	            				实付金额:data[i][2],
	            				下单时间:data[i][3],
	            				订单类别:data[i][4]};
	        	        AddData.push(da);
	            	}
	            	flushTable(AddData);
	            },error:function(s) {
	            	console.log("err");
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
