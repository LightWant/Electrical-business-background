<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:useBean id="command" class="model.bll.objects.Staff" scope="request"></jsp:useBean>
    
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Layui HTML</title>
    <link rel="stylesheet" href="layui/css/layui.css">
</head>
<body style="background: url(<%=request.getContextPath()%>/image/timg.jpg); background-size:100% 100% ; background-attachment: fixed" >
	
	<h2 align="center"><font color=red>用户登录</font></h2>
	<form:form method="POST" action="mylogin">
		<table align="center" border="1">
			<tr>
				<td>账号：</td>
				<td><form:input type="number" path="id"></form:input></td>
			</tr>
			<tr>
				<td>密&nbsp;&nbsp;码：</td>
				<td><form:input type="password" path="password" /></td>
			</tr>
			<tr>
				<td><input type="submit" value=url(<%=request.getContextPath()%>/image/timg.jpg)/></td>
				<td><input type="reset" value="重置 " /></td>
			</tr>
		</table>
	</form:form>
<button class="layui-btn layui-btn-warm"  type="button" id="btn">一个标准的按钮</button>
<button class="layui-btn layui-btn-warm"  type="button" id="btn1">一个标准的按钮</button>
<button class="layui-btn layui-btn-warm"  type="button" id="btn2">一个标准的按钮</button>
<button class="layui-btn layui-btn-warm"  type="button" id="btn3" onclick="fun1">一个标准的按钮</button>
<script type="text/javascript" src="/js/jquery-3.4.1.min.js"></script>
<script src="layui/layui.js" type="text/javascript" charset="utf-8"></script>

<script type="text/javascript">
function fun1(){
	$.ajax({
        type : 'post',
        url : '/jsonsource.do',
        //设置contentType类型为json
        contentType : 'application/json;charset=utf-8',
        //json数据
        data : {id:"01",username:"reader001",password:"psw001"},
    
        success : function(data) {
        	layer.msg("ok");
        },error:function(s) {
        	console.log("sdasd");
        }
		});
}
</script>
<script>
	layui.use(['jquery','layer'], function() {
		layer=layui.layer,
		$=layui.jquery;
		$(document).on('click','#btn',function() {
			layer.msg("${pageContext.request.contextPath}/jsonsource.do");
			$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/jsonsource',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : '{"id":"01","username":"reader001","password":"psw001"}',
            
                success : function(data) {
                	layer.msg("ok");
                },error:function(s) {
                	console.log("sdasd");
                }
      		});
		});
		$(document).on('click','#btn1',function() {
			layer.msg("/jsonsource");
			$.ajax({
                type : 'post',
                url : '/jsonsource.do',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : {id:"01",username:"reader001",password:"psw001"},
            
                success : function(data) {
                	layer.msg("ok");
                },error:function(s) {
                	console.log("sdasd");
                }
      		});
		});
		$(document).on('click','#btn2',function() {
			layer.msg("layui/jsonsource.do");
			$.ajax({
                type : 'post',
                url : 'layui/jsonsource',
                //设置contentType类型为json
                contentType : 'application/json;charset=utf-8',
                //json数据
                data : {id:"01",username:"reader001",password:"psw001"},
            
                success : function(data) {
                	layer.msg("ok");
                },error:function(s) {
                	console.log("sdasd");
                }
      		});
		});
	});
</script>
 
</body>