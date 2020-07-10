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
    				<label class="layui-form-label">客户名称</label>
    				<div class="layui-input-block" >
      					<select name="kehumingchen" required lay-verify="required" >
					        <%String[]keys = SoldSystem.selectAllKeHuMingCheng();
							for(Object key:keys){%>
								<option value="<%=key%>"><%=key%></option>
							<%} %>
					    </select>
    				</div>
  				</div>
				
				<div class="layui-form-item">
					<div id="Gl_Table">
	        			<table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:500px; height:500px;">
	        			 </table>
	    			</div>
    			</div>
    			
    			<div class="layui-form-item">
    				<label class="layui-form-label layui-inline">产品名称</label>
    				<div class="layui-input-item layui-inline" >
      					<select name="chanpinmingchen" id="chanpinmingchen" >
					        <%String[][]ming = SoldSystem.selectAllChanPinMingCheng();
							if(ming != null)
					        for(int i = 0; i < ming.length; i++){
								String key = ming[i][0];
								String danjia = ming[i][1];
								String danwei = ming[i][2];
								danjia = danjia.substring(0,danjia.length()-3);
							%>	
								<option value="<%=key+" "+danjia+" "+danwei%>"><%=key+" "+danjia+" 元/"+danwei%></option>
							<%} %>
					    </select>
    				</div>
    				<div class="layui-inline">
	    				<label class="layui-form-label layui-inline">产品数量</label>
	    				<div class="layui-input-item layui-inline" >
	    					<input type="text"  class="layui-input start-fee" id="chanpinNUM">
	    				</div>
    				</div>
  				</div>
    			
				<div class="layui-form-item">
					<div class="layui-input-block">
						<button class="layui-btn" id="btn-add" layui-btn-fluid>添加一行产品数据</button>
					</div>
				</div>
				
<!-- 				<div class="layui-form-item"> -->
<!-- 					<label class="layui-form-label layui-inline">删除行No:</label> -->
<!-- 					<div class="layui-input-item layui-inline"> -->
<!-- 			           <input type="text"  class="layui-input start-fee" name="seleteNo"> -->
<!-- 			     	</div> -->
<!-- 					<div class="layui-input-item layui-inline"> -->
<!-- 						<button class="layui-btn" id="btn-add" layui-btn-fluid>删除</button> -->
<!-- 					</div> -->
<!-- 				</div> -->
<%-- 		</form> --%>
<%--     	<form class="layui-form" action="" id="myForm"> --%>
			  <div class="layui-form-item">
			      <label class="layui-form-label ">付款金额</label>
			      <div class="layui-input-inline">
			          <input 
			           type="text" class="layui-input start-fee"
			           name="activity_apply_money" 
			           placeholder="请输入金额"
			           required>
			      </div>
			      <div class="layui-form-mid">元</div>
			  </div>
			  
			  <div class="layui-form-item">
			      <label class="layui-form-label ">应付金额</label>
			      <div class="layui-input-inline">
			          <input  type="text" class="layui-input start-fee"
			           name="money" id="money" readonly>
			      </div>
			      <div class="layui-form-mid">元</div>
			  </div>
			  
			 <div class="layui-form-item">
				<label class="layui-form-label layui-inline">提货时间:</label>
				<div class="layui-input-item layui-inline">
		           <input type="text" class="layui-input" name="tihuo1" id="tihuo1">
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
		    	{field:'No',  width:'20%', title: 'No.', sort:true}
		      ,	{field:'产品名称',  width:'40%', title: '产品名称'}
		      ,{field:'产品数量', width:'40%', title: '产品数量'}
		    ]],
		    data:AddData
		  });
		
		var myDate = new Date();
		//myDate.setTime(myDate.getTime()-24*60*60*1000);
		laydate.render({elem: '#tihuo1',min: myDate.toLocaleTimeString()});
		
		$('#money').val("0");
		// 新增一行
		$("#btn-add").off("click").on("click",function(){
			console.log( $('#kehumingchen').val() );
			if(!checkNum( $('#chanpinNUM').val() )) {
				layer.alert("请输入数量！");
				return false;
			}
			
			var st = 1;
			var s = $('#chanpinmingchen').val().split(' ');
			console.log(s);
			if(window.AddData != null && window.AddData.length != 0)
				st = window.AddData[0]["No"]+1;
			var data1={No:st,产品名称:s[0],产品数量:$('#chanpinNUM').val()};

			window.AddData.unshift(data1);
			//local.setItem('AddData', local.setItem('AddData', JSON.stringify(AddData)));
			table.reload('Table',{
				data : window.AddData
			});
			
			var prenum = parseInt($('#money').val());
			var nowmoney = parseFloat(s[1]) * parseInt($('#chanpinNUM').val());
			$('#money').val(prenum+nowmoney);
		//	console.log(window.AddData);
			return false;
		});
		
		 //监听单元格编辑
		 table.on('edit(Table)', function(obj){
		    var value = obj.value //得到修改后的值
	        ,data = obj.data //得到所在行所有键值
	        ,field = obj.field; //得到字段
	        //console.log(window.AddData);
	        window.AddData[window.AddData.length-data.No][field] = value;
	       // layer.msg('[ID: '+ data.No +'] ' + field + ' 字段更改为：'+ value);
		 });
		
        /*JQuery 限制文本框只能输入数字*/
        $(".priority,.start-fee,.activity_ratio,.activity_card_money").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
     // 监听keyup事件
//         $(document).on('keyup', 'td[data-field="产品数量"]>input.layui-table-edit', function (event) {
//         	validateNum(event,this);
//         });

        /**
         * 为输入框校验合法数字
         * @param event
         * @param obj
         */
        function validateNum(event, obj) {
        	//响应鼠标事件，允许左右方向键移动
        	event = window.event || event;
        	if (event.keyCode == 37 | event.keyCode == 39) {
        		return;
        	}
        	var t = obj.value.charAt(0);
        	//先把非数字的都替换掉，除了数字和.
        	obj.value = obj.value.replace(/[^\d.]/g, "");
        	//必须保证第一个为数字而不是.
        	obj.value = obj.value.replace(/^\./g, "");
        	//保证只有出现一个.而没有多个.
        	obj.value = obj.value.replace(/\.{2,}/g, ".");
        	//保证.只出现一次，而不能出现两次以上
        	obj.value = obj.value.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".");
        
        	var value = obj.value //得到修改后的值
	        ,data = obj.data //得到所在行所有键值
	        ,field = obj.field; //得到字段
	        console.log(window.AddData);
	        window.AddData[window.AddData.length-data.No][field] = value;
        }
        
        function checkNum(input){
        	var reg=/^[1-9]+[0-9]*]*$/; 
        	if(!reg.test(input)){ 
        		return false;    
        	}
        	return true;
        }
        
        //提交，刷新表单
        form.on('submit(formDemo)', function(data) {
        	if(parseInt(data.field.activity_apply_money) > parseInt($('#money').val())) {
        		layer.alert('付款多余价格！');
        		return false;
        	}
            //layer.msg(JSON.stringify(data.field));
            var cpmc = new Array();
            var cpsl = new Array();
            for(i = 0; i < AddData.length; i++) {
            	cpmc[i] = window.AddData[i]["产品名称"];
            	cpsl[i] = window.AddData[i]["产品数量"];
            	if(!checkNum(cpsl[i])) {
            		layer.alert("第"+(i+1)+"行产品数量有误！");
            		return false;
            	}
            }
            var da = {
                 kehumingchen:data.field.kehumingchen,
                 shifuMONEY:data.field.activity_apply_money,
                 chanpinmingchen:cpmc,
                 chanpinnum:cpsl,
                 tihuoTime:data.field.tihuo1
            };
            console.log(JSON.stringify(da));
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/createNewOrder',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	$("#myForm")[0].reset();
                	AddData=[
        		        {No:1,产品名称:"",产品数量:""},
        		         ];
                	//var data1={产品名称:"",产品数量:""};
    				//AddData.unshift(data1);
    				table.reload('Table',{
    					data : AddData
    				});
    				layer.msg("ok");
    				form.render();
                },error:function(s) {
                	layer.msg("error");
                }
      		});
        
            return false;
        });
    });
</script>
</html>
