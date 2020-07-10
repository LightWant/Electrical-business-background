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
.input:-ms-input-placeholder{  /* Internet Explorer 10-11 */
    color:red;
font-size:20px;
}
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
	<div class="layui-layout layui-layout-admin">
		<div class="layui-side" aria-hidden="true" hidden="true"> 
		</div>
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
					<label class="layui-form-label layui-inline">时间范围:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" required lay-verify="required"  class="layui-input" name="tihuo1" id="tihuo1">
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" required lay-verify="required" class="layui-input" name="tihuo2" id="tihuo2">
					</div>
				</div>
		  		
		  		<div class="layui-form-item"> <div class="layui-input-block" >
		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo1">重新生成</button>
<!-- 		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo2">加载修改</button> -->
		  		</div> </div>
	  		</form>
		</div>
	</div>
</body>
<script src="layui/layui.js"></script>
<script>
    //JavaScript代码区域
    var AddData=[];
    
    layui.use(['element','table','jquery', 'form','laydate'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate = layui.laydate;
		element.init();

		laydate.render({elem: '#tihuo1'});
        laydate.render({elem: '#tihuo2'});
		
        table.render({
		    elem: '#Table',
		    height:250
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
		    	{field:'No',  width:'15%', title: 'No.'}
		      ,	{field:'产品ID',  width:'25%', title: '产品ID'}
		      , {field:'产品名称', width:'30%', title: '产品名称'}
		      , {field:'产品数量', width:'30%', title: '产品数量'}
		    ]],
		    data:AddData
		 });
        
		//重新生成
        form.on('submit(formDemo1)', function(data) {
        	layer.confirm('重新生成?', {
                btn: ['确定','取消'] //按钮
            }, function(index){
            	var da = {
            		tihuoTime1:data.field.tihuo1 ,
                	tihuoTime2:data.field.tihuo2
            	};
            	console.log(da);
            	$.ajax({
                    type : 'post',
                    url : '${pageContext.request.contextPath}/SoldPlan.getPlan',
                    contentType : 'application/json;charset=utf-8',
                    data : JSON.stringify(da),
                    success : function(data) {
                    	//console.log(data);
//                     	window.AddData=[];
                    	for(i = data.length-1; i >=0; i--) {
                    		var da = [
                            	{No:i, 产品ID:"",产品名称:"",产品数量:""},
               		         ];
                    		da["No"] = i+1;
                    		da["产品ID"] = changeToCode(data[i][2]);
                    		da["产品名称"] = data[i][0];
                    		da["产品数量"] = data[i][1];
                    		window.AddData.unshift(da);
                    	}
        				table.reload('Table',{
        					data : window.AddData
        				});
        				form.render();
                    },error:function(s) {
                    	console.log("err");
                    }
          		});
                layer.close(index);
            }, function(index){
            	layer.close();
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
