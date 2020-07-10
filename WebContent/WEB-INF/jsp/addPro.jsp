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

.layui-textarea {
	height:20px;
	width:200px;
}
.layui-form-select dl { 
	max-height:200px; 
}
</style>
<body class="layui-layout-body" style="overflow: hidden"> 
<div class="layui-layout layui-layout-admin">
<div class="layui-side" aria-hidden="true" hidden="true"> 

</div>

<div class="layui-body" >
<div class="layui-fluid"> 
	
		<form class="layui-form" action="" id="form1">
		
			<div class="layui-container"> 
				<div class="layui-row">
					<div class="layui-form-item">
				  	  	<label class="layui-form-label" style="font-size:20px">添加产品</label>
				  	</div>
			  	</div>
				<div class="layui-row">
					<div class="layui-col-md6">
						<div class="layui-form-item">
							<label class="layui-form-label" id = "mingchenglb" >产品名称</label>
							<div class="layui-input-block" >
						     	<input id="mingcheng" name="mingcheng" type="text" required lay-verify="required" class="layui-input" autocomplete="off">
						   	</div>
					   </div>
					</div>
					<div class="layui-col-md6">
						<div class="layui-form-item">
						   	<label class="layui-form-label" id = "danweilb" >单位</label>
							<div class="layui-input-block" >
						     	<select  required lay-verify="required" id="danwei" name="danwei">
						     		<option value="克" selected>克</option>
						     		<option value="斤">斤</option>
						     		<option value="公斤">公斤</option>
						     	</select>
						   	</div>
					   	</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md4">
						<div class="layui-form-item">
						   	<label class="layui-form-label" id = "dbgglb" >打包规格</label>
							<div class="layui-input-block" >
						     	<input id="dbgg" name="dbgg"  required lay-verify="required|mm" class="layui-input" autocomplete="off">
						   	</div>
					   </div>
					</div>
					<div class="layui-col-md4">
						<div class="layui-form-item">
						   	<label class="layui-form-label" id = "baozhiqilb" >保质期</label>
							<div class="layui-input-block" >
						     	<input id="baozhiqi" name="baozhiqi"  min="1" required lay-verify="required|number|num" class="layui-input" autocomplete="off">
						   	</div>
					   </div>
					</div>
					<div class="layui-col-md4">
						<div class="layui-form-item">
						   	<label class="layui-form-label">单价</label>
							<div class="layui-input-block" >
						     	<input id="price" name="price" required lay-verify="required|fudian" class="layui-input" autocomplete="off">
						   	</div>
					   </div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-col-md4">
						<div class="layui-form-item">
							<label class="layui-form-label">配方原料</label>
							<div class="layui-input-block" >
								<select id="ylname" name="ylname">
									<% Map<Integer,String>map = MaterialSystem.selectAllMaterial();
									if(map!=null&&map.size()!=0){
									Object[] keys = map.keySet().toArray();
									for(Object key:keys){%>
										<option value=<%=map.get(key)%>><%=map.get(key)%></option>
									<%}}
									%>
								</select>
							</div>
						</div>
					</div>
					<div class="layui-col-md4">
						<div class="layui-form-item">
							<label class="layui-form-label">原料数量</label>
							<div class="layui-input-block" >
								<input id="ylnum" name="ylnum"  required lay-verify="required|number" class="layui-input" autocomplete="off">
							</div>
						</div>
					</div>
					<div class="layui-col-md4">
						<div class="layui-form-item">
							<div class="layui-input-block" >
								<button class="layui-btn" lay-submit lay-filter="formDemo">添加一行配方记录</button>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-row">
					<div class="layui-form-item">
					    <div class="layui-input-block">
					      <button class="layui-btn" lay-submit lay-filter="formDemo1">添加该产品</button>
					      <button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
					    </div>
					  </div>
				</div>
				<div class="layui-row">
					<div class="layui-form-item">
					<div id="Gl_Table">
	        			 <table 
	        				class="layui-hide" lay-filter="Table" 
	        				id="Table" style="max-height:500px; height:500px;">
	        			 </table> 
	        			 
	    			</div>
    			</div>
   			</div>
			</div>
			
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
var AddData=[
     ];
layui.use(['form','table','layer'], function(){
  var form = layui.form;
  var table = layui.table;
  var layer = layui.layer;
  $ = layui.jquery;
  //输入验证
  form.verify({
	  num: [
			  /^(0|\+?[1-9][0-9]*)$/
			  ,'保质期只能是正整数哦（天数）！'
		   ]
		  
    ,mm: [
		  /^(0|\+?[1-9][0-9]*)$/
		  ,'打包规格只能是正整数哦！'
	   ]
  	,fudian:[/^((0{1}\.\d{1,2})|([1-9]\d*\.{1}\d{1,2})|([1-9]+\d*)|0)$/,
			  '单价是保留两位小数的正数哦！'
		]
  	  
  	});
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
	    	//{field:'No',  width:'20%', title: 'No.', sort:true},
	    	{field:'原料名称',  width:'50%', title: '原料名称'}
	      ,{field:'配方数量',  width:'50%', title: '配方数量'}
	    ]],
	    data:AddData
	});
  form.on('submit(formDemo)', function(data){
	  var name = data.field.ylname;
	  var find = 0;
	  for(var i = 0; i < window.AddData.length;i++){
		  if(window.AddData[i]['原料名称']==name){
			  find = 1;
			  layer.alert("您已经添加过"+name+'的配方数量了！欲修改请先删除！（删除请双击相应单元行）');
		  	  break;
		  }
	  }
	  if(find == 0){
		  var da={
				  原料名称 : data.field.ylname
				  , 配方数量 : data.field.ylnum
		  };
		  window.AddData.unshift(da);
		  table.reload('Table',{
		  	data : window.AddData
		  });
	  }
	  return false;
  });
  //双击行删除
  table.on('rowDouble(Table)', function(obj){
	  var data = obj.data;
	    //layer.alert(JSON.stringify(data));
	    var flag = 1;
	    for(var i = 0;i < window.AddData.length;i++){
	    	//layer.alert(window.AddData[i]['产品名称']);
	    	//layer.alert(window.AddData[i]['相应产能']);
	    	if(window.AddData[i]['原料名称']==data['原料名称']&&window.AddData[i]['配方数量']==data['配方数量']&&data['配方数量']!=""){
	    		//layer.alert("find");
	    		window.AddData.splice(i,1);
	    		flag = 0;
	    		//删除选中的行
	    	}
	    }
	    //layer.alert("删除该行");
	    //重新渲染表格
	    if(flag == 0){
	    table.reload('Table',{
			data : window.AddData
			});
	    }
	    //标注选中样式
	    obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
	  });
  form.on('submit(formDemo1)', function(data){
	 console.log("add");
	 if(AddData.length<=0){
		 layer.alert("请先输入配方信息！");
		 return false;
	 }
	 var ylname = AddData[0]['原料名称'];
	 var ylnum = AddData[0]['配方数量'];
	 for(var i = 1;i < AddData.length;i++){
		 ylname += ","+ AddData[i]['原料名称'];
		 ylnum += "," + AddData[i]['配方数量'];
	 }
    //layer.msg(JSON.stringify(data.field));
    var da = {
    		mingcheng:data.field.mingcheng,
    		dbgg:data.field.dbgg,
    		baozhiqi:data.field.baozhiqi,
    		danwei:data.field.danwei,
    		price:data.field.price,
    		ylname:ylname,
    		ylnum:ylnum
    };
    console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/Product_Staff.addPro',     
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        
        success:function(res){       
             if(res>0){     
            	 //更新删除表
            	 layer.alert("添加产品成功！产品编号为 : " + changeToCode(res));   
                }                
            else if(res == -1) {   
                layer.alert("系统中已存在名称为"+document.getElementById("mingcheng").value+"的产品，请更换名称！");   
            }
        	else{
        		layer.alert("插入失败！");
        	}},              
            error:function (s) {
                layer.alert("系统出错!")
            }           
    }) ;         
        
    //return "addMat";
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
//删除原料
/*layui.use('form', function(){
	  var form = layui.form;
	  $ = layui.jquery;
	  //var checkStatus1 = table.checkStatus('addMattable');
	  //var data = checkStatus1.data;
	  //监听提交
	  
	});*/
</script>
   
</div>
</body>
</html>
