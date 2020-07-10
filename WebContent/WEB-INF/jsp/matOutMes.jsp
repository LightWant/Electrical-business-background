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
						<!--<div class="layui-col-md6">
							<div class="layui-form-item">
								<div id="Gl_Table">
				        			<table 
				        				class="layui-hide" lay-filter="Table" 
				        				id="Table1" style="max-height:600px; height:600px;">
				        			 </table>
				    			</div>
			    			</div>
						</div>-->
					</div>
				</div>
				
    		</form>
    		<form class="layui-form" action="" >
    			<div class="layui-form-item">
					<label class="layui-form-label layui-inline">出库批次编号:</label>
					<div class="layui-input-item layui-inline">
			           <input type="text" class="layui-input" name="ddid" id="ddid">
			     	</div>
			     	<button type="button" class="layui-btn" lay-submit lay-filter="formDemo">查询批次的信息</button>
			  
			     	<div class="layui-inline">
			     		<label class="layui-form-label layui-inline">出库负责人ID:</label>
			     		<div class="layui-input-item layui-inline">
			           		<select id="rkfzr" name="rkfzr"  lay-filter="selStaff">
								<%Integer [] ids = HumanSystem.findAllStaff();
								Calendar cal = Calendar.getInstance();
								if(ids!=null)
								for(Integer key:ids){%>
									<option value=<%=key%>><%= cal.get(Calendar.YEAR)+String.format("%03d",key)%></option>
								<%}%>
							</select>
			     		</div>
			     		<button type="button" class="layui-btn" lay-submit lay-filter="formDemo1">查询该员工负责的入库信息</button>
			     	</div>
				</div>
    		</form>
    		<form class="layui-form" action="" >
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-container"> 
							<div class="layui-row">
								<div class="layui-col-md3">
									<label class="layui-form-label layui-inline">出库车间ID:</label>
									<div class="layui-input-item layui-inline">
										 <select id="cjid" name="cjid"  required lay-verify="required" >
											 <option value="全部" selected>全部</option>
											 <%int [] cj = WorkSystem.selectAllWork();
											 if(cj!=null&&cj.length != 0){
												 for(int i = 0;i<cj.length;i++){%>
													<option value=<%=cj[i]%>><%=cj[i] %></option>
												 <%}} %>
										 </select>
							        </div>
							    </div>
							    <div class="layui-col-md3">
									<label class="layui-form-label layui-inline">起始时间:</label>
									<div class="layui-input-item layui-inline">
							            <input type="text" name="begindate" id="begindate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
							     	</div>
								</div>
								<div class="layui-col-md3">
									<label class="layui-form-label layui-inline">结束时间:</label>
									<div class="layui-input-item layui-inline">
							    	    <input type="text" name="endate" id="endate" lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
									</div>
								</div>
								<div class="layui-col-md3">
									<button type="button" class="layui-btn" lay-submit lay-filter="formDemo2">该时间段该车间的出库信息</button>
								</div>
							</div>
						</div>
						
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
		    	//出库批次ID,出库原料,出库数量,出库车间ID,出库时间,出库负责人ID,原料单位
		      ,	{field:'出库批次ID',  width:'14%', title: '出库批次ID'}
		      , {field:'出库原料', width:'14%', title: '出库原料'}
		      , {field:'出库数量', width:'14%', title: '出库数量'}
		      , {field:'出库车间ID', width:'14%', title: '出库车间ID'}
		      , {field:'出库时间', width:'14%', title: '出库时间'}
		      , {field:'出库负责人ID', width:'16%', title: '出库负责人ID'}
		      , {field:'原料单位', width:'14%', title: '原料单位'}
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
        $("#ddid").keyup(function () {
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).bind("paste", function () {  //CTR+V事件处理
            $(this).val($(this).val().replace(/[^0-9]/g, ''));
        }).css("ime-mode", "disabled"); //CSS设置输入法不可用
        
        //提交，刷新表单
        form.on('submit(formDemo)', function(data){
          //  console.log(data.field);
          	var dd = data.field.ddid
          	if(dd == ""||dd == null){
          		alert("请先输入查询出库批次！");
          		return false;
          	}
            var da = {
            	name:data.field.ddid
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatOutInfoViapc',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
                		console.log(data);
	                	
	                	var get = data.split(",");
	                	var da = {
	                			出库批次ID:get[0],出库原料:get[1],出库数量:get[2],出库车间ID:get[3],出库时间:get[4],出库负责人ID:changeToCode(get[5]),原料单位:get[6]
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
        	var rkfzr = data.field.rkfzr
          	if(rkfzr == ""||rkfzr == null){
          		alert("请先输入查询出库负责人id！");
          		return false;
          	}
            var da = {
            	name:data.field.rkfzr
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatOutInfoViaFzr',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			出库批次ID:get[0],出库原料:get[1],出库数量:get[2],出库车间ID:get[3],出库时间:get[4],出库负责人ID:changeToCode(get[5]),原料单位:get[6]
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
        form.on('submit(formDemo2)', function(data){
        	var cjid = data.field.cjid
        	var begin = data.field.begindate
        	var end = data.field.endate
          	if(cjid == "" || cjid == null||begin == "" || end == ""|| begin == null || end == null){
          		alert("请先输入完整的查询信息！（车间ID、起始时间、结束时间）");
          		return false;
          	}
            var da = {
            	name:cjid+","+begin+","+end
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatOutInfoViaTime',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null){
                		
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			出库批次ID:get[0],出库原料:get[1],出库数量:get[2],出库车间ID:get[3],出库时间:get[4],出库负责人ID:changeToCode(get[5]),原料单位:get[6]
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
