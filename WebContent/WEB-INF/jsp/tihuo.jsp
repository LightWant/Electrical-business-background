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
<div class="layui-side" aria-hidden="true" hidden="true"> 

</div>

<div class="layui-body" >
<form class="layui-form" action="" id="myForm">
  <div class="layui-form-item">
    <label class="layui-form-label">客户名称</label>
    	<div class="layui-input-block" >
            <select name="kehumingchen" lay-filter="kehumingcheng" id="kehumingcheng" required lay-verify="required" >
	        	<%String[]keys = SoldSystem.selectAllKeHuMingCheng();
					for(Object key:keys){%>
					<option value="<%=key%>"><%=key%></option>
				<%} %>
      		</select>
    	</div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">订单号</label>
    <div class="layui-input-block" >
    	<select name="dingdanID" lay-filter="dingdanID" id="dingdanID" required lay-verify="required">
    	</select>
    </div>
  </div>

  <div class="layui-form-item">
	<div id="Gl_Table">
		<table 
			class="layui-hide" lay-filter="Table" 
			id="Table" style="max-height:600px; height:600px;">
		 </table>
	</div>
  </div>
    			

  <div class="layui-form-item">
      <label class="layui-form-label">金额</label>
      <div class="layui-input-inline">
          <input 
           type="text" class="layui-input start-fee"
           name="activity_apply_money" 
           readonly>
      </div>
      <div class="layui-form-mid">元</div>
  </div>

  <div class="layui-form-item">
    <label class="layui-form-label">收款类型</label>
    	<div class="layui-input-block" >
            <select name="shoukuanTYPE" lay-filter="kehumingcheng" id="kehumingcheng" required lay-verify="required" >
					<option value="银行卡转账">银行卡转账</option>
					<option value="支付宝">支付宝</option>
					<option value="微信">微信</option>
					<option value="现金">现金</option>
      		</select>
    	</div>
  </div>

  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
<!--       <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button> -->
    </div>
  </div>
</form>
    </div>
</div>
<!--<script src="../src/layui.js"></script>-->

<script src="layui/layui.js"></script>
<script>
	var AddData=[];
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form;
		element.init();
		
        /*JQuery 限制文本框只能输入数字*/
        $(".priority,.start-fee,.activity_ratio,.activity_card_money").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        
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
		    	{field:'No',  width:'20%', title: 'No.', sort:true}
		      , {field:'产品ID', width:'20%', title: '产品ID'}
		      , {field:'产品名称', width:'20%', title: '产品名称'}
		      , {field:'需要数量', width:'20%', title: '需要数量'}
		      , {field:'库存数量', width:'20%', title: '库存数量'}
		    ]],
		    data:AddData
		  });
        
        function flushMoney(message) {
        	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/tihuo.getmoney',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : message,
                success : function(data) {
                	//console.log("sca "+data);
                	$('.start-fee').val(data);
                },error:function(s) {
                	console.log("err");
                }
      		});
        	return false;
        }
        
        function flushSlect(message) {
        	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/tihuo.getDingDanIDWithKeHuMingCheng',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : message,
                success : function(data) {
                	if(data==undefined || data==null || data=="") {
                		$("#dingdanID").empty();
                		form.render('select');
                		return;
                	}
                	var html="";
                	for(var i=0;i<data.length;i++){
                         html+="<option value="+data[i]+">"+changeToCode(data[i])+"</option>";
                     }
                	$("#dingdanID").empty();
                    $("#dingdanID").append(html);
                    form.render('select');
                    
                    flushMoney(data[0]);
                },error:function(s) {
                	console.log("err");
                }
      		});
        	
        	return false;
        }
        
      //下拉框联动
        form.on('select(kehumingcheng)', function(data) {
        	var message=$("select[id=kehumingcheng").val();
        	//console.log(message);
         	
        	flushSlect(message);
        	return false;
        });
        
        //更新金额、细节表
        form.on('select(dingdanID)', function(data) {
        	var message=$("select[id=dingdanID").val();
        	//console.log(message);
        	
         	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/tihuo.getXiJieAndKuCun',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : message,
                success : function(data) {
                	
                	window.AddData=[];
                	for(i = data.length-1; i >=0; i--) {
                		var da = [
                        	{No:i, 产品ID:"",产品名称:"",需要数量:"",库存数量:""},
           		         ];
                		da["No"] = i+1;
                		da["产品ID"] = changeToCode(data[i][0]);
                		da["产品名称"] = data[i][1];
                		da["需要数量"] = data[i][2];
                		da["库存数量"] = data[i][3];
                		window.AddData.unshift(da);
                	}
    				table.reload('Table',{
    					data : window.AddData
    				});
    				form.render();
    				
    				flushMoney(message);
                },error:function(s) {
                	console.log("err");
                }
      		});
         	
        	return false;
        });
        
      //监听提交
        form.on('submit(formDemo)', function(data){
        	var message=$("select[id=dingdanID").val();
        	if(message == null || message.length == 0) {
        		layer.alert("请选择订单！");
        		return false;
        	}
        	
        	for(i = 0; i < window.AddData.length; i++) {
        		if(parseInt(window.AddData[2]) > parseInt(window.AddData[3])) {
        			layer.msg("库存不足");
        			return false;
        		}
        	}
          	console.log(JSON.stringify(data.field));
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/tihuo.do',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(data.field),
                success : function(data) {
                	$("#myForm")[0].reset();
    				layer.msg("ok");
    				window.AddData=[];
    				table.reload('Table',{
    					data : window.AddData
    				});
    				flushSlect(message);
    				form.render();
                },error:function(s) {
                	layer.msg("error");
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
        	if(s == "000")
        		return "0151564156";
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
</body>
</body>
</html>
