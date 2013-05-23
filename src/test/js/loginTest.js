var _ajax=$.ajax;
var logged=false;
function mockSetup(){
	/* make tests run synchronous ajax, otherwise tests will fail
	another alternative will be to use mockjax	*/
	$.ajaxSetup({ async: false });
	window.alert=function(message){
		console.log('Alert was called.');
	};
}

mockSetup();
define(["mockjax"],function(){
	mockSetup();
	return {
		login: function(callback){
			if(logged == true){
				console.log("Already logged in.");
				QUnit.start();
			}else{
				$(document).ready(function() {
					console.log("Wait to login and start tests...");
					setTimeout (function(){
						$.ajax({
							type: "POST",
							url: '/elim/j_spring_security_check?j_username=guest&j_password=frunzisului25',
							contentType: "text/html; charset=utf-8",
							dataType:'json',
							data: {"j_username":"guest","j_password":"frunzisului25"},
							async:false
						}).always(function(data){
							console.log("We have a login!");
							console.log(data);
							callback();
							logged = true;
							QUnit.start();
						});
					}, 5000);
				});
			}
		}
	}
});

module("login test");
asyncTest("login test",function(){
	$(document).ready(function() {
		console.log("Wait to login and start tests...");
		setTimeout (function(){
			$.ajax({
				type: "POST",
				url: '/elim/j_spring_security_check?j_username=guest&j_password=frunzisului25',
				contentType: "text/html; charset=utf-8",
				dataType:'json',
				data: {"j_username":"guest","j_password":"frunzisului25"}
			}).always(function(data){
				console.log("Always for login!");
				console.log(data);
				equal(data.responseText.indexOf("You have entered an invalid username or password!"), 
						-1, 
				"The login succeeded(i.e. responseText doesn't contain invalid username message)!");
				QUnit.start();
			});
		}, 5000);
	});
});
