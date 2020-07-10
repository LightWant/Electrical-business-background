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
    				<label class="layui-form-label">计划选择</label>
    				<div class="layui-input-block" >
      					<select name="jinhua" lay-filter="jinhua" required lay-verify="required" >
					        <%String[][]keys = SoldSystem.selectSoldPlan();
					        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
							if(keys != null)
					        for(int i = 0; i < keys.length; i++){%>
								<option value="<%=keys[i][0]%>"><%=keys[i][0]+"&nbsp;&nbsp;&nbsp;&nbsp;"+keys[i][1]+"&nbsp;"+keys[i][2]%></option>
							<%} %>
					    </select>
    				</div>
  				</div>
	  					 
		  		<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:1000px; height:1000px;">
	        			 </table>
	    			</div>
    			</div>

		  		<div class="layui-form-item"> <div class="layui-input-block" >
<!-- 		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo1">重新生成</button> -->
<!-- 		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo2">加载修改</button> -->
		  			<button type="button" lay-submit class="layui-btn" lay-filter="formDemo3">确认修改</button>
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
		    height:350
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
		      , {field:'产品数量', width:'30%', title: '产品数量',edit:true}
		    ]],
		    data:AddData
		 });
        
        //下拉框选择
        form.on('select(jinhua)', function(data) {
			 console.log(data.value);
			 $.ajax({
               type : 'post',
               url : '${pageContext.request.contextPath}/fixPlan.getXiJie',
               //设置contentType类型为json
               contentType : 'application/json;charset=utf-8',
               //json数据
               data : data.value,
               success : function(data) {
	               	console.log(data);
	                window.AddData = [];
	               	for(i = 0; i < data.length; i++) {
	               		var da = {No:i+1, 产品ID:"",产品名称:"",产品数量:""};
	               		
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
       		return false;
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
      //保存
        form.on('submit(formDemo3)', function(data) {
        	layer.confirm('确认修改？', {
                btn: ['确定','取消'] //按钮
            }, function(index){
				//console.log(data);
            	var ids = new Array();
            	var nums = new Array();
            	for(i = 0; i < window.AddData.length; i++) {
            		ids.unshift(changeToID(window.AddData[i]['产品ID']));
            		nums.unshift(window.AddData[i]['产品数量']);
            	}
            	var da = {
            		id:data.field.jinhua,
            		chanpinID:ids,
            		chanpinNUM:nums
            	};
            	
            	console.log(da);
            	$.ajax({
                    type : 'post',
                    url : '${pageContext.request.contextPath}/fixPlan.savePlan',
                    contentType : 'application/json;charset=utf-8',
                    //data : JSON.stringify(da),
                    data:JSON.stringify(da),
                    success : function(data) {
						layer.msg("ok");                     	
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
