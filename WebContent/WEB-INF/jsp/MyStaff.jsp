<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="command" class="model.bll.objects.Staff" scope="request"></jsp:useBean>
<%@ page import="model.bll.systems.*,java.util.Map,java.util.*,java.text.*,java.io.*" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <title>山威电商后台管理</title>
  <link rel="stylesheet" href="layui/css/layui.css">
</head>
<body class="layui-layout-body">
<div class="layui-layout layui-layout-admin">
  <div class="layui-header">
    <div class="layui-logo">山威电商后台管理</div>
    <!-- 头部区域（可配合layui已有的水平导航） -->
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
  			<ul class="layui-nav layui-nav-tree"  lay-filter="test">
  				
  			</ul>
  		</div>
  </div>
  
  
  <div class="layui-body">
	<iframe frameborder="0" scrolling="yes" 
	style="width:100%;height:100%" src="" id="content"></iframe>
  </div>
  
  <div class="layui-footer" style="background: url(<%=request.getContextPath()%>/image/水果1.jpg); background-size:100% 100% ; background-attachment: fixed">
    <!-- 底部固定区域 -->
<!--     © yxw.com -->
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
  
//获取所有的菜单
  $.ajax({
      type:"POST",
      url:'${pageContext.request.contextPath}/MyStaff.getRoles',
      dataType:"json",
      success:function(data) {
          //先添加所有的主材单
          //console.log(data);
          var content;
          for(i = 0; i < data.length;) {
        	  content+='<li class="layui-nav-item">';
              content+='<a href="javascript:;">'+data[i][0]+'</a>';
              content+='<dl class="layui-nav-child">';
              //这里是添加所有的子菜单
              do {
            	  content+='<dd class="tt"><a href="javascript:;" id="'+data[i][1]+'">'+data[i][2]+'</a></dd>';
            	  i++;
              }while(i < data.length && data[i][0] == data[i-1][0])
            	  
              content+='</dl>';
          }
          content+='</li>';
         // console.log(content);
          $(".layui-nav-tree").append(content);
          element.init();
      },
      error:function(jqXHR){
          layer.msg("发生错误："+ jqXHR.status);
      }
  });
  
  //初始欢迎界面
  $("iframe").attr("src",'${pageContext.request.contextPath}/welcome', function(){
	  layui.use(['element','form'], function(){
          var element = layui.element
          , form = layui.form;
			form.render();
			element.init();
   	  });
  });
  
  function confirmEnding(str, target) {
	  var start = str.length-target.length;
	  var arr = str.substr(start,target.length);
	  if(arr == target){
	    return true;
	  }
	  return false;
  }
  
  element.on('nav(test)', function(elem){
     // console.log(elem[0].id.length);
      if(elem[0].id != null && elem[0].id.length > 0)
      $("iframe").attr("src",'${pageContext.request.contextPath}/'+elem[0].id+'.toGetJSP', function(){
		  layui.use(['element','form'], function(){
	          var element = layui.element
	          , form = layui.form;
				form.render();
				element.init();
	   	  });
	  });
     // console.log($("iframe")[0].src);
//       var t = $("iframe")[0].src;
// 	  if(confirmEnding(t, 'login')) {
// 		  self.href = t;
// 	  }
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