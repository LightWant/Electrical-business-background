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
				<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:600px; height:600px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
					<label class="layui-form-label layui-inline">订单ID:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="dingdanID" id="dingdanID">
			     	</div>
			     	<div class="layui-inline">
			     		<label class="layui-form-label layui-inline">客户ID:</label>
			     		<div class="layui-input-item layui-inline">
			           		<input type="text" class="layui-input" name="kehuID" id="kehuID">
			     		</div>
			     	</div>
				</div>
    			
				<div class="layui-form-item">
					<label class="layui-form-label layui-inline">下单时间:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="xiadan1" id="xiadan1">
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" class="layui-input" name="xiadan2" id="xiadan2">
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label layui-inline">提货时间:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="tihuo1" id="tihuo1">
			     	</div>
					<div class="layui-input-item layui-inline">
			    	   <input type="text" class="layui-input" name="tihuo2" id="tihuo2">
					</div>
				</div>
				
				<div class="layui-form-item">
					<label class="layui-form-label layui-inline">订单状态:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="dingdanSTATE" id="dingdanSTATE">
			     	</div>
				</div>
   
			  <div class="layui-form-item">
			    <div class="layui-input-block">
			      <button type="button" class="layui-btn" lay-submit lay-filter="formDemo">提交</button>
			      <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
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
    //JavaScript代码区域
    layui.use(['element','table','jquery', 'form','laydate', 'layer'], function(){
        var element = layui.element
        ,table = layui.table
        ,$ = layui.jquery
        ,form = layui.form
        ,laydate = layui.laydate
        layer = layui.layer;
        
        form.render();
        
        laydate.render({elem: '#xiadan1'});
        laydate.render({elem: '#xiadan2'});
        laydate.render({elem: '#tihuo1'});
        laydate.render({elem: '#tihuo2'});
        
		element.init();
		
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
		    	{field:'No',  width:'10%', title: 'No.'}
		      ,	{field:'订单ID',  width:'8%', title: '订单ID'}
		      , {field:'客户ID', width:'8%', title: '客户ID'}
		      , {field:'下单时间', width:'16%', title: '下单时间'}
		      , {field:'提货时间', width:'16%', title: '提货时间'}
		      , {field:'订单状态', width:'16%', title: '订单状态'}
		      , {field:'应付金额', width:'13%', title: '应付金额'}
		      , {field:'实付金额', width:'13%', title: '实付金额'}
		    ]],
		    data:AddData
		  });
		 
		// 细节
		table.on('row(Table)', function(obj){
		    //console.log(obj.data.订单ID);
		    var table2 = layui.table;
		    //console.log(changeToID(obj.data.订单ID));
			$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/selectDingDan.xijie',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  changeToID(obj.data.订单ID),
                success : function(data) {
                	var da = '<div style="padding: 50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300;">';
                	da = da + "<br>产品ID 产品名称 产品数量</br>";
                	for(i = 0; i < data.length; i++) {
                		da = da + "<br>"+data[i][0] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data[i][1] + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + data[i][2] + "</br>";
                	}
                	//layer.msg(da);
                	//layer.alert(da);
                	layer.open({
                		  type: 1
                		  ,title: false //不显示标题栏
                		  ,closeBtn: false
                		  ,area: '300px;'
                		  ,shade: 0.8
                		  ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                		  ,resize: false
                		  ,btn: ['确定']
                		  ,btnAlign: 'c'
                		  ,moveType: 1 //拖拽模式，0或者1
                		  ,content:da
                		  ,success: function(layero){ 
                		  }
                	});
                },error:function(s) {
                	layer.msg("error :"+s);
                }
      		});
			
			return false;
		 });
		
        /*JQuery 限制文本框只能输入数字*/
        $(".start-fee").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        //提交，刷新表单
        form.on('submit(formDemo)', function(data){
          //  console.log(data.field);
            var da = {
            	dingdanID:changeToID(data.field.dingdanID),
            	kehuID:changeToID(data.field.kehuID) ,
            	dingdanSTATE:data.field.dingdanSTATE,
            	tihuoTime1:data.field.tihuo1 ,
            	tihuoTime2:data.field.tihuo2,
            	xiadanTime1:data.field.xiadan1,
            	xiadanTime2:data.field.xiadan2
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/selectDingDan.submit',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	for(i = data.length-1; i >=0; i--) {
                		var da = [
                        	{No:i, 订单ID:"",客户ID:"",下单时间:"",提货时间:"",订单状态:"",应付金额:"",实付金额:""},
           		         ];
                		da["No"] = i+1;
                		da["订单ID"] = changeToCode(data[i][0]);
                		da["客户ID"] = changeToCode(data[i][1]);
                		da["下单时间"] = data[i][2];
                		da["提货时间"] = data[i][3];
                		da["订单状态"] = data[i][4];
                		da["应付金额"] = data[i][5];
                		da["实付金额"] = data[i][6];
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
        	if(s == null) return null;
        	if(s.length == 0) return "";
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
