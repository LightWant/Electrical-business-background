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
    				
			     	<div class="layui-inline">
			     		<label class="layui-form-label layui-inline">销毁负责人ID:</label>
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
			     		<button type="button" class="layui-btn" lay-submit lay-filter="formDemo1">查询该员工负责的销毁信息</button>
			     	</div>
				</div>
    		</form>
    		<form class="layui-form" action="" >
				<div class="layui-form-item">
					<div class="layui-inline">
						<div class="layui-container"> 
							<div class="layui-row">
								<div class="layui-col-md3">
									<label class="layui-form-label layui-inline">仓库ID:</label>
									
									<div class="layui-input-item layui-inline">
							            <select id="ckid" name="ckid"  lay-filter="selStaff">
							            	<option value="全部" selected>全部</option>
											<%Integer [] ckids = MaterialSystem.selectAllStorageId();
											if(ckids!=null)
											for(Integer key:ckids){%>
												<option value=<%=key%>><%=key %></option>
											<%}%>
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
									<button type="button" class="layui-btn" lay-submit lay-filter="formDemo2">该时间段的销毁信息</button>
								</div>
							</div>
							<div class="layui-row">
								
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
		    	//原料名称,入库批次ID,销毁数量,销毁时间,销毁负责人ID
		       {field:'原料名称', width:'16%', title: '原料名称'}
		      , {field:'入库批次ID', width:'10%', title: '入库批次ID'}
		      , {field:'销毁数量', width:'14%', title: '销毁数量'}
		      , {field:'销毁时间', width:'18%', title: '销毁时间'}
		      , {field:'销毁负责人ID', width:'12%', title: '销毁负责人ID'}
		      , {field:'销毁仓库ID', width:'10%', title: '销毁仓库ID'}
		      , {field:'原料单位', width:'10%', title: '原料单位'}
		    ]],
		    data:AddData
		  });
        //提交，刷新表单
       
        form.on('submit(formDemo1)', function(data){
        	var rkfzr = data.field.rkfzr
          	if(rkfzr == ""||rkfzr == null){
          		alert("请先输入查询销毁负责人id！");
          		return false;
          	}
            var da = {
            	name:data.field.rkfzr
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatDelViaFzr',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data :  JSON.stringify(da),
                success : function(data) {
                	//console.log(data);
                	window.AddData=[];
                	if(data!=null&&data.length>0){
	                	for(var i = 0;i < data.length;i++){
		                	var get = data[i].split(",");
		                	var da = {
		                			原料名称:get[0],入库批次ID:get[1],销毁数量:get[2],销毁时间:get[3],销毁负责人ID:changeToCode(get[4]),销毁仓库ID:get[5],原料单位:get[6]
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
        	var begin = data.field.begindate
        	var end = data.field.endate
        	var ckid = data.field.ckid
          	if(begin == "" || end == ""|| begin == null || end == null){
          		alert("请先输入完整的起始时间！");
          		return false;
          	}
        	
            var da = {
            	name:ckid+","+begin+","+end
            };
            $.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/Material_Staff.getMatDelViaTime',
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
		                			原料名称:get[0],入库批次ID:get[1],销毁数量:get[2],销毁时间:get[3],销毁负责人ID:changeToCode(get[4]),销毁仓库ID:get[5],原料单位:get[6]
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
