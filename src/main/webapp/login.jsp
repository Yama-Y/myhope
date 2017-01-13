<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
    <head>
        <title>MyHope</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="<%=contextPath%>/style/loginCss.css"/>
    </head>
    <body>
		<div class="wrapper">
			<h1>MyHope</h1>
			<h2><div id="subtitle_str"></div></h2>
			<div class="content">
				<div id="form_wrapper" class="form_wrapper">
					<form id="reg_form" class="register">
						<h3>注册</h3>
						<div class="column">
							<div>
								<label>登录名：</label>
								<input id="reg_loginname" name="data.loginname" type="text"/>
								<span class="error">This is an error</span>
							</div>
							<div>
								<label>密码：</label>
								<input id="reg_pwd" name="data.pwd" type="password" />
								<span class="error">This is an error</span>
							</div>
							<div>
								<label>确认密码：</label>
								<input id="reg_repwd" type="password" />
								<span class="error">This is an error</span>
							</div>
						</div>
						<div class="column">
							<div>
								<label>姓名：</label>
								<input id="reg_name" name="data.name"  type="text" />
								<span class="error">This is an error</span>
							</div>
							<div>
								<label>QQ：</label>
								<input id="reg_qq" name="data.qq"  type="text" />
								<span class="error">This is an error</span>
							</div>
							<div>
								<label>邮箱：</label>
								<input id="reg_mail" name="data.mail"  type="text" />
								<span class="error">This is an error</span>
							</div>
						</div>
						<div class="bottom">
							<span id="reg_error" class="error">This is an error</span>
							<input id="regBtn" onclick="regFun();" type="submit" value="注册" />
							<a href="#" rel="login" class="linkform">已有账号？点此登录</a>
							<div class="clear"></div>
						</div>
					</form>
					<form id="login_form" class="login active">
						<h3>登录</h3>
						<div>
							<label>登录名：</label>
							<input type="text" name="data.loginname" id="login_loginname"/>
							<span class="error">登录名不能为空</span>
						</div>
						<div>
							<label>密码：</label>
							<input type="password" name="data.pwd" id="login_pwd"/>
							<span class="error">This is an error</span>
						</div>
						<div class="bottom">
							<div class="remember"><input type="checkbox" id="login_remember" checked="checked"/><span>记住我</span></div>
							<input id="loginBtn" onclick="loginFun();" type="submit" value="登录"></input>
							<a href="#" rel="register" class="linkform">还没账号？点此注册</a>
							<div class="clear"></div>
						</div>
					</form>
				</div>
				<div class="clear"></div>
			</div>
			<a href="mailto:Yang<395217750@qq.com>" class="back" title="Yang<395217750@qq.com>">版权所有：@Yang</a>
		</div>
		<script type="text/javascript" src="<%=contextPath%>/jslib/jquery-1.9.1.js" charset="utf-8"></script>
		<script type="text/javascript" src="<%=contextPath%>/jslib/jquery.cookie.js"></script>
		<script type="text/javascript">
			$(function() {
				//$(document).bind('contextmenu',function() {
				//	return false;
				//});
				var subtitle_strs = new Array();
				//------------------------------------------------------------------
				//1
				subtitle_strs.push('我们一路奋战，不是为了改变世界，而是为了不让世界，改变我们。');
				subtitle_strs.push('千夫所指，我亦坚持。');
				subtitle_strs.push('你可以说我在天堂，其实我一直都在路上。');
				subtitle_strs.push('梦想就像沙漠里的绿洲，坚持走了许久，也只是海市蜃楼。但梦想也像是沙漠里的沙子，只要走过之后，就有脚印停留。');
				subtitle_strs.push('普通人之所以普通，是因为他们普遍有一个通病，那就是认为自己永远普通。');
				subtitle_strs.push('刀只是锋利，有光才有锋芒。');
				subtitle_strs.push('最可怕的剑，永远都藏在剑鞘里面。');
				subtitle_strs.push('抬头仰望，不一定有星光，胜利者，不一定为王。');
				subtitle_strs.push('当你觉得自己走不到终点的时候，请不要放弃，或许你的对手也是这种感觉。');
				//10
				subtitle_strs.push('想要成为一个强者，就必须要有一样想要保护的东西，这可以是一样物品，一个人，也可以是一个世界。');
				subtitle_strs.push('危险是确实存在的，但恐惧只是一种选择。');
				subtitle_strs.push('永远不要被表象所迷惑。有些敌人只是看起来强大，当你勇敢地去挑战时，你会发现，真正强大的其实是你自己。而有些敌人看起来很弱，但却拥有强大的战斗力，轻易就能得来的胜利，往往是一种陷阱。');
				subtitle_strs.push('人就是这样，很喜欢用一些表面的行为，去揣测别人真实的意图，当真相来临之前，才恍然大悟。但他们所能做的，也只是一句道歉，或者干脆选择性遗忘，但他们之前的行为带给当事人的，却是足以影响一生。');
				subtitle_strs.push('如果你看到面前的阴影，别怕，那是因为你的背后有阳光。');
				subtitle_strs.push('做自己生命的主角，而不是别人生命中的看客。');
				subtitle_strs.push('不忘初心，方得始终。');
				//17
				subtitle_strs.push('奔跑不单是一种能力，更是一种态度，决定你人生高度的态度。');
				//------------------------------------------------------------------
				var _subtitle_strs_index = (Math.random() * subtitle_strs.length);
				_subtitle_strs_index = Math.floor(_subtitle_strs_index);
				$('#subtitle_str').html(subtitle_strs[_subtitle_strs_index]);
	
				var $form_wrapper = $('#form_wrapper'),
				$currentForm = $form_wrapper.children('form.active'),
				$linkform = $form_wrapper.find('.linkform');
				$form_wrapper.children('form').each(function(i) {
					var $theForm = $(this);
					if (!$theForm.hasClass('active')) $theForm.hide();
					$theForm.data({
						width: $theForm.width(),
						height: $theForm.height()
					});
				});
				setWrapperWidth();
				$linkform.bind('click',
				function(e) {
					var $link = $(this);
					var target = $link.attr('rel');
					$currentForm.fadeOut(400,
					function() {
						$currentForm.removeClass('active');
						$currentForm = $form_wrapper.children('form.' + target);
						$form_wrapper.stop().animate({
							width: $currentForm.data('width') + 'px',
							height: $currentForm.data('height') + 'px'
						},
						500,
						function() {
							$currentForm.addClass('active');
							$currentForm.fadeIn(400);
						});
					});
					e.preventDefault();
				});
				function setWrapperWidth() {
					$form_wrapper.css({
						width: $currentForm.data('width') + 'px',
						height: $currentForm.data('height') + 'px'
					});
				}
				$form_wrapper.find('input[type="submit"]').click(function(e) {
					e.preventDefault();
				});
				var loginData = $.parseJSON($.cookie('login-data'));
				if (loginData) {
					$('#login_loginname').val(loginData.login_loginname);
					$('#login_pwd').val(loginData.login_pwd);
				}
			});
	
			function loginFun() {
				cleanError();
				var $login_loginname = $('#login_loginname');
				var $login_pwd = $('#login_pwd');
	
				if (inputNotNull($login_loginname, '登录名') | inputNotNull($login_pwd, '密码')) return;
				var $loginForm = $('#login_form');
				$('#loginBtn').attr('disabled', true);
				$.post('<%=contextPath%>/base/user!doNotNeedSessionAndSecurity_login.myhope', $loginForm.serialize(),
				function(result) {
					if (result.success) {
						if ($('#login_remember').is(':checked')) {
							$.cookie('login-data', '{"login_loginname":"' + $login_loginname.val() + '","login_pwd":"' + $login_pwd.val() + '"}', {
								expires: 7
							});
						} else {
							$.cookie('login-data', null);
						}
						location.replace('<%=contextPath%>/index.jsp');
					} else {
						$login_pwd.next().text(result.msg);
						$login_pwd.next().css('visibility', 'visible');
						$('#loginBtn').attr('disabled', false);
					}
				},
				'json');
			};
	
			function regFun() {
				cleanError();
				var $reg_loginname = $('#reg_loginname');
				var $reg_pwd = $('#reg_pwd');
				var $reg_repwd = $('#reg_repwd');
				var $reg_name = $('#reg_name');
				var $reg_qq = $('#reg_qq');
				var $reg_mail = $('#reg_mail');
	
				if (inputNotNull($reg_loginname, '登录名') | inputNotNull($reg_pwd, '密码') | inputNotNull($reg_repwd, '重复密码') | inputNotNull($reg_name, '姓名')) return;
	
				if ($reg_pwd.val() != $reg_repwd.val()) {
					$reg_pwd.next().text('密码输入不一致');
					$reg_pwd.next().css('visibility', 'visible');
					$reg_repwd.next().text('密码输入不一致');
					$reg_repwd.next().css('visibility', 'visible');
					return;
				}
				var $regForm = $('#reg_form');
				$('#regBtn').attr('disabled', true);
	
				$.post('<%=contextPath%>/base/user!doNotNeedSessionAndSecurity_reg.myhope', $regForm.serialize(),
				function(result) {
					if (result.success) {
						location.replace('<%=contextPath%>/index.jsp');
					} else {
						$('#reg_error').text(result.msg);
						$('#reg_error').css('visibility', 'visible');
						$('#regBtn').attr('disabled', false);
					}
				},
				'json');
			};
	
			function inputNotNull($pom, text) {
				if ($.trim($pom.val()) == '') {
					var $nextDiv = $pom.next();
					$nextDiv.text(text + '不能为空');
					$nextDiv.css('visibility', 'visible');
					return true;
				}
			};
	
			function cleanError() {
				$('.error').text('haha');
				$('.error').css('visibility', 'hidden');
			};
        </script>
    </body>
</html>