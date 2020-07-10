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
    <script src="layui/layui.js"></script>
</head>

<body class="layui-layout-body" style="overflow: hidden"> 
	<div class="layui-layout layui-layout-admin">
		<div class="layui-side" aria-hidden="true" hidden="true"> </div>

		<div class="layui-body" >
			<form class="layui-form" action="" id="myForm">
				<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:600px; height:600px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
			    	<div class="layui-input-block">
			      	<button type="button" class="layui-btn" lay-submit lay-filter="selectAllKeHu">加载所有</button>
<!-- 			      <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button> -->
			    	</div>
				</div>
    			
	   			<div class="layui-form-item">
					<label class="layui-form-label layui-inline">供货商ID:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="kehuID" id="dingdanID">
			     	</div>
			     	<div class="layui-inline">
			     		<label class="layui-form-label layui-inline">供货商名称:</label>
			     		<div class="layui-input-item layui-inline">
			           		<input type="text" class="layui-input" name="kehuNAME" id="kehuID">
			     		</div>
			     	</div>
				</div>
	
				<div class="layui-form-item">
				   <div class="layui-input-block">
				     <button type="button" class="layui-btn" lay-submit lay-filter="selectKeHu">提交</button>
				     <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
				   </div>
				</div>
		</form>
	  </div>
	</div>
	
	<div id="openProductBox" style="display: none; padding: 10px;">
		<table id="openProductTable" lay-filter="openProductTable"></table>
	</div>
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
		    	{field:'No',  width:'10%', title: 'No.', sort:true}
		      , {field:'供货商ID', width:'10%', title: '供货商ID'}
		      , {field:'供货商名称', width:'15%', title: '供货商名称'}
		      , {field:'供货商负责人', width:'15%', title: '供货商负责人'}
		      , {field:'电话', width:'15%', title: '电话'}
		      , {field:'邮箱', width:'15%', title: '邮箱'}
		      , {field:'地址', width:'15%', title: '地址'}
		    ]],
		    data:AddData
		  });

        /*JQuery 限制文本框只能输入数字*/
        $(".start-fee").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        //提交，刷新表单
        form.on('submit(selectAllKeHu)', function(data){
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/GongHuoShang.selectAllGongHuoShang',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
               // data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	for(i = data.length-1; i >=0; i--) {
                		var da = [
                        	{No:i, 供货商ID:"",供货商名称:"",供货商负责人:"",
                        		电话:"",邮箱:"",地址:""},
           		         ];
                		da["No"] = i+1;
                		da["供货商ID"] = changeToCode(data[i][0]);
                		da["供货商名称"] = data[i][1];
                		da["供货商负责人"] = data[i][2];
                		da["电话"] = data[i][3];
                		da["邮箱"] = data[i][4];
                		da["地址"] = data[i][5];
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
        
      //提交，刷新表单
        form.on('submit(selectKeHu)', function(data){
        	console.log(data);
        	var da = {
        		 kehuID:changeToID(data.field.kehuID)
        		,kehumingchen:data.field.kehuNAME
        	}
        	
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/GongHuoShang.selectGongHuoShang',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	console.log(data);
                	window.AddData=[];
                	for(i = data.length-1; i >=0; i--) {
                		var da = [
                        	{No:i, 供货商ID:"",供货商名称:"",供货商负责人:"",
                        		电话:"",邮箱:"",地址:""},
           		         ];
                		da["No"] = i+1;
                		da["供货商ID"] = changeToCode(data[i][0]);
                		da["供货商名称"] = data[i][1];
                		da["供货商负责人"] = data[i][2];
                		da["电话"] = data[i][3];
                		da["邮箱"] = data[i][4];
                		da["地址"] = data[i][5];
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
