<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="command" class="model.bll.objects.Staff" scope="request"></jsp:useBean>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>销售管理子系统员工</title>
  <link rel="stylesheet" href="layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
    <div class="layui-logo">销售管理子系统员工</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
    <ul class="layui-nav layui-layout-left">
      <!-- <li class="layui-nav-item"><a href="">创建订单</a></li>
      <li class="layui-nav-item"><a href="">查询订单</a></li>
      <li class="layui-nav-item"><a href="">预期销售计划</a></li> -->
      <!-- <li class="layui-nav-item">
        <a href="javascript:;">其它系统</a>
        <dl class="layui-nav-child">
          <dd><a href="">邮件管理</a></dd>
          <dd><a href="">消息管理</a></dd>
          <dd><a href="">授权管理</a></dd>
        </dl>
      </li> -->
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
          <a class="" href="javascript:;">业务处理</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" id="createOrder">新建订单</a></dd>
            <dd><a href="javascript:;" id="tihuo">提货</a></dd>
            <dd><a href="javascript:;" id ="buhuo">补货</a></dd>
            <dd><a href="javascript:;" id ="tuihuo">退货</a></dd>
  <!--           <dd><a href="">超链接</a></dd> -->
          </dl>
        </li>
        <li class="layui-nav-item">
          <a href="javascript:;">销售计划</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" id="createSoldPlan">生成</a></dd>
            <dd><a href="javascript:;" id="fixSoldPlan">修改</a></dd>
        <!--     <dd><a href="">超链接</a></dd> -->
          </dl>
        </li>
        
        <li class="layui-nav-item layui-nav-itemed">
          <a class="" href="javascript:;">查询</a>
          <dl class="layui-nav-child">
            <dd><a href="javascript:;" id="select">查询订单</a></dd>
            <dd><a href="javascript:;" id="selectKeHu">客户信息</a></dd>
          </dl>
        </li>
<!--         <li class="layui-nav-item"><a href="">云市场</a></li>
        <li class="layui-nav-item"><a href="">发布商品</a></li> -->
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
 
 
  function confirmEnding(str, target) {
	  // 请把你的代码写在这里
	  var start = str.length-target.length;
	  var arr = str.substr(start,target.length);
	  if(arr == target){
	    return true;
	  }
	  return false;
	}

  element.init();
  $(document).on("click", "#createOrder", function() {
	 // console.log(document.getElementById("yuangongvalue").value);
	 
	 $("iframe").attr("src",'${pageContext.request.contextPath}/createOrder', function(){
		 var t = $("iframe").attr("src");
		  if(confirmEnding(t, 'login')) {
			  console.log(t);
			  self.href = t;
		  } 
		 layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
	  });
  });
  
  $(document).on("click", "#tihuo", function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/tihuo', function(){
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
  
  $(document).on('click','#buhuo',function() {
		//layer.msg("${pageContext.request.contextPath}/jsonsource.do");
	  $("iframe").attr("src",'${pageContext.request.contextPath}/buhuo', function(){
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
  
  $(document).on('click','#createSoldPlan',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/SoldPlan', function(){
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
  $(document).on('click','#fixSoldPlan',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/fixPlan', function(){
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
  $(document).on('click','#select',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/selectDingDan', function(){
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
  
  $(document).on('click','#tuihuo',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/tuihuo', function(){
		  layui.use(['element','form'], function() {
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
  $(document).on('click','#selectKeHu',function() {
	  $("iframe").attr("src",'${pageContext.request.contextPath}/selectKeHu', function(){
		  layui.use(['element','form'], function() {
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