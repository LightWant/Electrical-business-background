<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="command" class="model.bll.objects.Staff" scope="request"></jsp:useBean>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>财务管理子系统员工</title>
  <link rel="stylesheet" href="layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
    <div class="layui-logo">财务管理子系统会计</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
    </ul>
    <ul class="layui-nav layui-layout-right">
      <li class="layui-nav-item">
        <a href="javascript:;" >
          <img src="http://t.cn/RCzsdCq" class="layui-nav-img">
         	<label id="yuangongvalue"> 个人面板 </label>
        </a>
        <dl class="layui-nav-child">
          <dd><a href="http://localhost:8080/FoodFactory/YuanGong" id="yuangong">基本资料</a></dd>
<!--           <dd><a id="changePassword">修改密码</a></dd> -->
        </dl>
      </li>
      <li class="layui-nav-item"><a id="out">退出</a></li>
    </ul>
  </div>
  
  <div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      <!-- 左侧导航区域（可配合layui已有的垂直导航） -->
      <ul class="layui-nav layui-nav-tree"  lay-filter="test">
        <li class="layui-nav-item layui-nav-itemed">
          <a class="" href="javascript:;">业务流水</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" id="yuangong">员工流水</a></dd>
            <dd><a href="javascript:;" id="dingdan">订单流水</a></dd>
            <dd><a href="javascript:;" id ="jinhuo">进货流水</a></dd>
          </dl>
        </li>
        <li class="layui-nav-item">
          <a href="javascript:;">报表</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" id="MoneySum">盈亏报表</a></dd>
        <!--     <dd><a href="">超链接</a></dd> -->
          </dl>
        </li>
       
      </ul>
    </div>
  </div>
  
  <div class="layui-body">
	<!-- <div id="content"></div> -->
	<iframe frameborder="0" scrolling="yes" 
	style="width:100%;height:100%" src="" id="content"></iframe>
  </div>
  
  <div class="layui-footer">
    <!-- 底部固定区域 -->
    © yxw.com
  </div>
</div>
<script src="layui/layui.js"></script>
<script>

//JavaScript代码区域
layui.use(['jquery','layer','element', 'form'], function(){
 /*  var element = layui.element; */
  var $ = layui.jquery
  , layer=layui.layer
  , element = layui.element
  , form = layui.form;
 
  element.init();
  $(document).on("click", "#yuangong", function() {
	 // console.log(document.getElementById("yuangongvalue").value);
	  $("iframe").attr("src",'${pageContext.request.contextPath}/Yuangongflow', function(){
		  layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
	  });
	  var t = $("iframe").attr("src");
	  if(confirmEnding(t, 'login')) {
		  self.location = t;
	  }
  });
  
  $(document).on("click", "#dingdan", function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/Dingdanflow', function(){
		  layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
	  });
	  var t = $("iframe").attr("src");
	  if(confirmEnding(t, 'login')) {
		  self.location = t;
	  }
  });
  
  $(document).on('click','#jinhuo',function() {
		//layer.msg("${pageContext.request.contextPath}/jsonsource.do");
	  $("iframe").attr("src",'${pageContext.request.contextPath}/Jinhuoflow', function(){
		  layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
	  });
	  var t = $("iframe").attr("src");
	  if(confirmEnding(t, 'login')) {
		  self.location = t;
	  }
  });
  
  $(document).on('click','#MoneySum',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/MoneySum', function(){
		  layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
   	  });
	  var t = $("iframe").attr("src");
	  if(confirmEnding(t, 'login')) {
		  self.location = t;
	  }
  });
  
  $(document).on('click','#out',function() {
	  $.ajax({
          type : 'post',
          url : '${pageContext.request.contextPath}/logout',
          contentType : 'application/json;charset=utf-8',
          data : "logout",
          success : function(data) {
			self.location = "http://localhost:8080/FoodFactory/login";
          },error:function(s) {
          	layer.msg("err");
          }
   	});
  });
});
</script>
</body>
</html>