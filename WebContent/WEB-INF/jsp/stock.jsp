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
	
		
		<form class="layui-form" action="" id="form2">
			
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="ylnamelb">原料名称</label>
				  <div class="layui-input-block">
					
						<select id="ylname" name="ylname"  required lay-verify="required" >
						<%String[]names = MaterialSystem.selectAllMaterialName();
						if(names!=null)
						for(String key:names){%>
							<option value=<%=key%>><%=key %></option>
						<%}%>
					   </select>
				  </div>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="ghslb">供货商名称</label>
				  <div class="layui-input-block">
						<select id="ghsname" name="ghsname"  required lay-verify="required" >
						<%String[]ghsn = MaterialSystem.selectAllGhsName();
						if(ghsn!=null)
							for(String key:ghsn){%>
								<option value=<%=key%>><%=key %></option>
							<%}%>
					   </select>
				  </div>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="numlb">进货数量</label>
				  <div class="layui-input-block">
						<input id="num" name="num" min="0" required lay-verify="required|number|num" class="layui-input" autocomplete="off">
				  </div>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="pricelb">单价</label>
				  <div class="layui-input-block">
						<input id="price" name="price" type = "text" required lay-verify="required|price" class="layui-input" autocomplete="off">
				  </div>
			</div>
			<div class="layui-form-item">
				  <label class="layui-form-label"  id="scrqlb">生产日期</label>
				  <div class="layui-input-block">
				 		<input type="text" name="scrq" id="scrq" lay-verify="date|required" placeholder="yyyy-MM-dd" autocomplete="off" class="layui-input">
				  </div>
			</div>
			<div class="layui-form-item">
				<div class="layui-input-block">
					<button class="layui-btn" lay-submit lay-filter="formDemo">进货</button>
					<button type="reset" id="resetButton" class="layui-btn layui-btn-primary">重置</button>
				</div>
			</div>
		</form>
		
 </div>
	
</div>
<script src="layui/layui.js"></script>
<script>
//Demo
layui.use(['form', 'layedit', 'laydate'], function(){
  var form = layui.form
  ,layer = layui.layer
  ,layedit = layui.layedit
  ,laydate = layui.laydate;
  $ = layui.jquery;

 
  //自定义验证规则
  form.verify({
	  num: function(value){
	      if(value<=0){
	        return '进货数量要大于0！';
	      }
	    }
  	  ,price:[/^((0{1}\.\d{1,2})|([1-9]\d*\.{1}\d{1,2})|([1-9]+\d*)|0)$/,
		  '单价是保留两位小数的正数哦！'
  		]
  	});
  form.on('submit(formDemo)', function(data){
	 //console.log("add");
    //layer.msg(JSON.stringify(data.field));
    var da = {
    		ylname:data.field.ylname,
    		ghsname:data.field.ghsname,
    		num:data.field.num,
    		price:data.field.price,
    		scrq:data.field.scrq
    };
    console.log(JSON.stringify(da));
    $.ajax({   
        url:'${pageContext.request.contextPath}/stock',   
        //该写这儿了
        type :'post',       
        contentType : 'application/json;charset=utf-8',
        data:JSON.stringify(da),   
        success:function(res){       
             if(res>0){     
            	 //更新删除表
            	 layer.open({
        			title: '进货成功！'
        			,content: '进货批次为：'+res
        		});   
            	 
                }     
        	else{
        		alert("插入失败！");
        	}},              
            error:function (s) {
                alert("系统出错!")
            }           
    }) ;         
        
    //return "addMat";
    return false;
  });
  
});
</script>
   
</div>
</body>
</html>
