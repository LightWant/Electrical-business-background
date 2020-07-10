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
					<div class="layui-input-block">
						<button class="layui-btn" id="btn-add" layui-btn-fluid>添加一行产品数据</button>
					</div>
				</div>
    			
		  		<div class="layui-form-item">
					<label class="layui-form-label layui-inline">时间范围:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" required lay-verify="required"  readonly class="layui-input" name="tihuo1" id="tihuo1">
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" required lay-verify="required" class="layui-input" name="tihuo2" id="tihuo2">
					</div>
				</div>
		  		
		  		<div class="layui-form-item"> <div class="layui-input-block" >
		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo1">确定生成</button>
		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo2">保存</button>
		  			<button type="button" class="layui-btn" id="formDemo3">清空</button>
		  		</div> </div>
	  		</form>
		</div>
	</div>
</body>
<script src="layui/layui.js"></script>
<script>
    //JavaScript代码区域
    var AddData=[];
    
    layui.use(['element','table','jquery', 'form','laydate','util'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate = layui.laydate
        ,util = layui.util;;
		element.init();
        
        $.ajax({
            type : 'post',
            url : '${pageContext.request.contextPath}/WorkPlan.startTime',
            contentType : 'application/json;charset=utf-8',
        //    data : JSON.stringify(da),
            success : function(data) {
            	console.log(data);
            	$('#tihuo1').val(data);
            	laydate.render({
            		elem: '#tihuo2'
            		,min: data
            	});
            },error:function(s) {
            	console.log("err");
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
		    ,cols: [[	        
		    	{field:'No',  width:'15%', title: 'No.', sort:true}
		      ,	{field:'产品ID',  width:'25%', title: '产品ID',edit:true}
		      , {field:'产品名称', width:'30%', title: '产品名称',edit:true}
		      , {field:'产品数量', width:'30%', title: '产品数量', edit:true}
		    ]],
		    data:AddData
		 });
      //监听单元格编辑
		 table.on('edit(Table)', function(obj){
		    var value = obj.value //得到修改后的值
	        ,data = obj.data //得到所在行所有键值
	        ,field = obj.field; //得到字段
	        console.log(window.AddData);
	        window.AddData[data.No-1][field] = value;
	       // layer.msg('[ID: '+ data.No +'] ' + field + ' 字段更改为：'+ value);
		 });
     // 新增一行
		$("#btn-add").off("click").on("click",function(){
			var data1={No:1,产品ID:"",产品名称:"",产品数量:""};
			if(window.AddData[0]!=null&&window.AddData[0].hasOwnProperty("No"))
				data1['No'] = 1+Math.max(window.AddData[0]['No'],window.AddData[window.AddData.length-1]['No']);
			
			window.AddData.push(data1);
			//local.setItem('AddData', local.setItem('AddData', JSON.stringify(AddData)));
			table.reload('Table',{
				data : window.AddData
			});
			console.log(window.AddData);
			return false;
		});
        
		//重新生成
        form.on('submit(formDemo1)', function(data) {
        	layer.confirm('确定生成？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
            	var da = {
            		tihuoTime1:data.field.tihuo1 ,
                	tihuoTime2:data.field.tihuo2
            	};
            	console.log(da);
            	$.ajax({
                    type : 'post',
                    url : '${pageContext.request.contextPath}/WorkPlan.getPlan',
                    contentType : 'application/json;charset=utf-8',
                    data : JSON.stringify(da),
                    success : function(data) {
                    	//console.log(data);
//                     	window.AddData=[];
						var ma = 0;
						if(window.AddData[0]!=null&&window.AddData[0].hasOwnProperty("No"))
            				ma = Math.max(window.AddData[0]['No'],window.AddData[window.AddData.length-1]['No']);
            			//console.log(ma);
                    	for(i = 0; i < data.length; i++) {
                    		var da = {No:i+1+ma, 产品ID:"",产品名称:"",产品数量:""};
                    		
                    		da["产品ID"] = changeToCode(data[i][0]);
                    		da["产品名称"] = data[i][1];
                    		da["产品数量"] = data[i][2];
                    		window.AddData.push(da);
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
      //清空
        $("#formDemo3").on('click', function() {
        	layer.confirm('确定清空？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
            	window.AddData = []
    			table.reload('Table',{
    				data : window.AddData
    			});
    			form.render();
                layer.close(index);
            }, function(index){
            	layer.close();
            });
            return false;
        });
      
        function checkNum(input){
        	var reg=/^[1-9]+[0-9]*]*$/; 
        	if(!reg.test(input)){ 
        		return false;    
        	}
        	return true;
        }
      //保存
        form.on('submit(formDemo2)', function(data) {
        	layer.confirm('确定保存？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
            	var ids = new Array();
            	var mingchens = new Array();
            	var nums = new Array();
            	for(i = 0; i < window.AddData.length; i++) {
            		
            		ids.unshift(changeToID(window.AddData[i]['产品ID']));
            		mingchens.unshift(window.AddData[i]['产品名称']);
            		nums.unshift(window.AddData[i]['产品数量']);
            		if(!checkNum(window.AddData[i]['产品数量'])) {
                		layer.alert("第"+(i+1)+"行产品数量有误！");
                		return false;
                	}
            		if(!checkNum(window.AddData[i]['产品ID'])) {
                		layer.alert("第"+(i+1)+"行产品ID有误！");
                		return false;
                	}
            		if(!checkNum(changeToID(window.AddData[i]['产品ID']))) {
                		layer.alert("第"+(i+1)+"行产品ID有误！");
                		return false;
                	}
            	}
            	var da = {
            		chanpinID:ids
            		,mingchens:mingchens
            		,chanpinNUM:nums
            		,tihuoTime1:data.field.tihuo1
                	,tihuoTime2:data.field.tihuo2
            	};
            	console.log(JSON.stringify(da));
            	$.ajax({
                    type : 'post',
                    url : '${pageContext.request.contextPath}/WorkPlan.savePlan',
                    contentType : 'application/json;charset=utf-8',
                    data:JSON.stringify(da),
                    success : function(data) {
						layer.msg(data);                     	
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
