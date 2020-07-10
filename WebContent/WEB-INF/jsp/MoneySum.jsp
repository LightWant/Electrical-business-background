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
    				<label class="layui-form-label" style="font-size:20px">收入记录</label>
    			</div>
				<div class="layui-form-item">
					<div id="Gl_Table1">
	        			<table 
	        				class="layui-hide" lay-filter="Table1" 
	        				id="Table1" style="max-height:400px; height:200px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
    				<label class="layui-form-label" style="font-size:20px">支出记录</label>
    			</div>
    			
    			<div class="layui-form-item">
    		
					<div id="Gl_Table2">
	        			<table 
	        				class="layui-hide" lay-filter="Table2" 
	        				id="Table2" style="max-height:400px; height:200px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
					<label class="layui-form-label layui-inline" style="font-size:20px">时间筛</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="tihuo1" id="tihuo1" required>
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" class="layui-input" name="tihuo2" id="tihuo2" required>
					</div>
					<div class="layui-input-item layui-inline">
						<button type="button" lay-submit 
						class="layui-btn" lay-filter="formDemo">确定</button>
					</div>
				</div>
    			
    			<div class="layui-form-item">
				  	<div class="layui-inline">
						 <label class="layui-form-label">收入</label>
						<div class="layui-input-inline">
				        	<input type="text" class="layui-input" id="name" readonly>
				    	</div>
				    </div>
				    <div class="layui-inline">
				    	 <label class="layui-form-label">支出</label>
						<div class="layui-input-inline">
				        	<input type="text" class="layui-input" id="id" readonly>
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
	var AddData=[
	        {No:1, 订单ID:"",应付金额:"",实付金额:"",下单时间:""},
	         ];
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form','laydate'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate=layui.laydate;
		element.init();
		
		function flushTable(tablename, AddData) {
			table.render({
			    elem: tablename,
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
			    	{field:'No',  width:'20%', title: 'No.', sort:true}
			      ,	{field:'订单ID',  width:'20%', title: '订单ID'}
			      , {field:'应付金额', width:'20%', title: '应付金额'}
			      , {field:'实付金额', width:'20%', title: '实付金额'}
			      , {field:'下单时间', width:'20%', title: '下单时间'}
			    ]],
			    data:AddData
			});
		}
		flushTable('#Table1', AddData);
		flushTable('#Table2', AddData);
		
		laydate.render({elem: '#tihuo1'});
		laydate.render({elem: '#tihuo2'});

		form.on('submit(formDemo)', function(data) {
           	var da = {
           		tihuoTime1:data.field.tihuo1 ,
                tihuoTime2:data.field.tihuo2
           	};

           	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/MoneySum.doSold',
                contentType : 'application/json;charset=utf-8',
                data : JSON.stringify(da),
                success : function(data) {
					//console.log(data);
                	var AddData=[];
                	var sum = 0;
                	for(i = 0; i < data.length; i++) {
                		var da={No:i+1, 
                				订单ID:changeToCode(data[i][0]),
                				应付金额:data[i][1],
                				实付金额:data[i][2],
                				下单时间:data[i][3]};
            	        AddData.unshift(da);
            	        sum = sum + parseFloat(data[i][2]);
                	}
                	flushTable('#Table1', AddData);
                //	console.log(sum);
                	$('#name').val(sum);
                },error:function(s) {
                	layer.msg("err");
                }
         	});
           	
           	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/MoneySum.doBuy',
                contentType : 'application/json;charset=utf-8',
                data : JSON.stringify(da),
                success : function(data) {
                	var AddData=[];
                	var sum = 0;
                	for(i = 0; i < data.length; i++) {
                		var da={No:i+1, 
                				订单ID:changeToCode(data[i][0]),
                				应付金额:data[i][1],
                				实付金额:data[i][2],
                				下单时间:data[i][3]};
            	        AddData.unshift(da);
            	        sum = sum + parseFloat(data[i][2]);
                	}
                	flushTable('#Table2', AddData);
                	$('#id').val(sum);
					//console.log(data);
                },error:function(s) {
                	layer.msg("err");
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
