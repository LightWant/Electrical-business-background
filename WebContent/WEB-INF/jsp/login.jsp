<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <link rel="stylesheet" href="layui/css/layui.css">
        <style type="text/css">
      		.container{
      			width: 420px;
      			height: 320px;
		 		 min-height: 320px;  
		 		 max-height: 320px;  
		 		 position: absolute;   
		 		 top: 0;  
		 		 left: 0;  
		 		 bottom: 0;  
		 		 right: 0;  
		 		 margin: auto;  
		 		 padding: 20px;  
		 		 z-index: 130;  
		 		 border-radius: 8px;  
		 		 background-color: #fff;  
		 		 box-shadow: 0 3px 18px rgba(100, 0, 0, .5); 
		 		 font-size: 16px;
      		}
      		.close{
      			background-color: white;
      			border: none;
      			font-size: 18px;
      			margin-left: 410px;
      			margin-top: -10px;
      		}
 
        	.layui-input{
        		border-radius: 5px;
        		width: 300px;
        		height: 40px;
        		font-size: 15px;
        	}
        	.layui-form-item{
        		margin-top: 25px;
        		margin-left: -20px;
        		height:50px;
        	}
			#logoid{ 
				margin-top: -16px;
		 		 padding-left:150px; 
		 		 padding-bottom: 15px;
			}
			.layui-btn{
				margin-left: -50px;
				border-radius: 5px;
        		width: 350px;
        		height: 40px;
        		font-size: 15px;
			}
			.verity{
				width: 120px;
			}
			.font-set{
				font-size: 13px;
				text-decoration: none; 
				margin-left: 120px;
			}
			a:hover{
			 text-decoration: underline; 
			}
			 #canvas {
		        float: right;
		        display: inline-block;
		        border: 1px solid #ccc;
		        border-radius: 5px;
		        cursor: pointer;
			 }
        </style>
    </head>
    <body style="background: url(<%=request.getContextPath()%>/image/盘子.jpg); background-size:100% 100% ; background-attachment: fixed">
    	<form class="layui-form" action="">
    		<div class="container"
    		style="background: url(<%=request.getContextPath()%>/image/盘子.jpg); background-size:100% 100% ; background-attachment: fixed">
			  	<div class="layui-form-item">
			    	<label class="layui-form-label">用户ID</label>
			    
			    	<div class="layui-input-block">
			      		<input type="text" name="id" id="id" required  lay-verify="required" placeholder="请输入用户名" autocomplete="off" class="layui-input">
			    	</div>
			    </div>
			
				<div class="layui-form-item">
				    <label class="layui-form-label">密 &nbsp;&nbsp;码</label>
				    <div class="layui-input-inline">
				      <input type="password" name="password" id="password" required lay-verify="required" placeholder="请输入密码" autocomplete="off" class="layui-input">
				    </div>
				</div>
				<div class="layui-form-item">
			    	<label class="layui-form-label layui-inline">验证码</label>
			    	<div class="layui-input-item layui-inline">
			      		<input id="code" type="text" name="title" id='ver' required  lay-verify="required" placeholder="请输入验证码" autocomplete="off" class="layui-input verity">
					</div>
					<div class="layui-input-item layui-inline">
			    		<canvas id="canvas" width="100" height="33"></canvas>
					</div>
				</div>
	 
				 <div class="layui-form-item">
				    <div class="layui-input-block">
				      <button class="layui-btn" lay-submit id="btn" lay-filter="formDemo">登陆</button>	     
				    </div>
				 </div>
			</div>
		</form>
    </body>

<script src="layui/layui.js"></script>
<script>

layui.use(['element','table','jquery', 'form', 'layer'], function(){
    var element = layui.element
    ,table = layui.table
    ,$ = layui.jquery
    ,form = layui.form
    , layer = layui.layer;
	element.init();
	
	var show_num = [];
    draw(show_num);
    
    $('#id').val('2019002');
    $('#password').val('123456');
    console.log(show_num.join(""));
    $('#ver').val(show_num.join(""));

    $("#canvas").on('click',function(){
        draw(show_num);
    })
    
    form.on('submit(formDemo)', function(data) {

    	var val = $("#code").val().toLowerCase();
    	
        var num = show_num.join("");
        if(val==''){
        	layer.alert('请输入验证码！');
        }else if(val == num) {
        	var da = {
        		id:changeToID(data.field.id),
                password:data.field.password,
            };
        	//layer.alert(da);
        	$.ajax({
                type : 'post',
                url : '${pageContext.request.contextPath}/login.action',
                contentType : 'application/json;charset=utf-8',
                data :  JSON.stringify(da),
                success : function(data) {
                	if(data == "NOT EXIST" || data == "Wrong Password") {
                		layer.alert(data);
                        $("#code").val('');
                        draw(show_num);
                	}
                	else {
       
                  		self.location.href=data;//确定按钮跳转地址
                	}
                },error:function(s) {
                	layer.alert('连接失败！');
                    $("#code").val('');
                    draw(show_num);
                }
      		});
        }else{
        	layer.alert('验证码错误！请重新输入！');
            $("#code").val('');
            draw(show_num);
        }
    	return false;
    });
    
    function draw(show_num) {
	    var canvas_width=$('#canvas').width();
	    var canvas_height=$('#canvas').height();
	    var canvas = document.getElementById("canvas");//获取到canvas的对象，演员
	    var context = canvas.getContext("2d");//获取到canvas画图的环境，演员表演的舞台
	    canvas.width = canvas_width;
	    canvas.height = canvas_height;
	    var sCode = "A,B,C,E,F,G,H,J,K,L,M,N,P,Q,R,S,T,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0";
	    var aCode = sCode.split(",");
	    var aLength = aCode.length;//获取到数组的长度

	    for (var i = 0; i <= 3; i++) {
	        var j = Math.floor(Math.random() * aLength);//获取到随机的索引值
	        var deg = Math.random() * 30 * Math.PI / 180;
	        //产生0~30之间的随机弧度
	        var txt = aCode[j];//得到随机的一个内容
	        show_num[i] = txt.toLowerCase();
	        var x = 10 + i * 20;//文字在canvas上的x坐标
	        var y = 20 + Math.random() * 8;//文字在canvas上的y坐标
	        context.font = "bold 23px 微软雅黑";
	
	        context.translate(x, y);
	        context.rotate(deg);
	
	        context.fillStyle = randomColor();
	        context.fillText(txt, 0, 0);
	
	        context.rotate(-deg);
	        context.translate(-x, -y);
	    }
	    for (var i = 0; i <= 5; i++) { //验证码上显示线条
	        context.strokeStyle = randomColor();
	        context.beginPath();
	        context.moveTo(Math.random() * canvas_width, Math.random() * canvas_height);
	        context.lineTo(Math.random() * canvas_width, Math.random() * canvas_height);
	        context.stroke();
	    }
	    for (var i = 0; i <= 30; i++) { //验证码上显示小点
	        context.strokeStyle = randomColor();
	        context.beginPath();
	        var x = Math.random() * canvas_width;
	        var y = Math.random() * canvas_height;
	        context.moveTo(x, y);
	        context.lineTo(x + 1, y + 1);
	        context.stroke();
	    }
	}
	
	function randomColor() {//得到随机的颜色值
	    var r = Math.floor(Math.random() * 256);
	    var g = Math.floor(Math.random() * 256);
	    var b = Math.floor(Math.random() * 256);
	    return "rgb(" + r + "," + g + "," + b + ")";
	}
	
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