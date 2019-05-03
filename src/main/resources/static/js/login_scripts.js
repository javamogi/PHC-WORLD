/**
 * 
 */
$(document).ready(function(){
	var loginEmail = $.cookie("loginEmail");
	if(loginEmail != undefined) {
		$("#email").val(loginEmail);
		$("#remember").prop("checked", true);
	}
	
	$("#loginButton").click(function(){
//		console.log($("#email").val());
		if($.trim($("#email").val()) == "") {
			alert("아이디를 입력하세요.");
			return;
		} else {
			if($("#remember").prop("checked")) {
				$.cookie("loginEmail", $("#email").val(), {expires:7});
			} else {
				$.removeCookie("loginEmail");
			}
		}
	})
});