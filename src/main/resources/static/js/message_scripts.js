/**
 * 
 */
$(document).ready(function(){

	$("li.active").each(function(){

		let userId = $("#userId").val();

		$.ajax({
			type : 'get',
			url : "/api/freeboards/users/" + userId,
			data: {"freeBoardId": null},
			dataType : 'json',
			error : function(xhr, status, error){
				alert("통신 실패");
			},
			success : function(data){
				for(let i = 0; i < data.length; i++){
					addFreeboardTemplate(data[i]);
				}

				if(data.length !== 0 && !(data.length < 10)){
					let showBtn = "<a href='/api/freeboards/users/" + userId + "?freeBoardId=" +
						data[data.length -1].id + "' title='show more' id='showmore' class='btn btn-primary showMore'><i class='fa fa-refresh'></i> Show more</a>";
					$("#tab-item-2").append(showBtn);
				}
			}
		})
	});
	
	// if (location.hash == "#tab-item-3"){
	// 	 window.sessionStorage.removeItem('activeTab');
	// 	 window.sessionStorage.setItem('activeTab', "#tab-item-3");
	// }
	 	
// 	$('a[data-toggle="tab"]').on('click', function(e) {
// //        window.localStorage.setItem('activeTab', $(e.target).attr('href'));
//         window.sessionStorage.setItem('activeTab', $(e.target).attr('href'));
//     });
// //    var activeTab = window.localStorage.getItem('activeTab');
//     var activeTab = window.sessionStorage.getItem('activeTab');
//     if (activeTab) {
//         $('#profileTabs a[href="' + activeTab + '"]').tab('show');
// //        window.localStorage.removeItem("activeTab");
//     }
    
	$('#btn-sendMessage').click(function(e){
		e.preventDefault();
//		console.log("btn-sendMessage click!!");
		var queryString = $("#sendMessage").serialize();
		var url = $("#sendMessage").attr("action");
//		console.log("query : " + queryString);
//		console.log("url : " + url);
		
		$.ajax({
			type : 'post',
			url : url,
			data : queryString,
			dataType : 'json',
			error : function(xhr, status, error){
//				console.log(xhr);
				var errorMessage = jQuery.parseJSON(xhr.responseText);
//				console.log(errorMessage);
				alert(errorMessage.message);
				if(errorMessage.message === "로그인을 해야합니다."){
					window.location.href="/users/loginForm";
				}
//				alert("메세지 통신 실패");
			},
			success : function(data){
//				console.log(data);
				alert("메세지를 보냈습니다.");
				$("#modalCompose").modal("hide");
			    window.localStorage.setItem('activeTab', "#tab-item-4");
				location.reload();
			}
		})
	});
	
	$(document).on("click", "a#messageConfirm", function(e){
		e.preventDefault();
		
		var messagea = $(this);
		var url = messagea.attr("href");
//		console.log("url : " + url);
		
		var read = messagea.find(".important");
		
		$.ajax({
			type : 'get',
			url : url,
			dataType : 'json',
			error : function(){
				alert("메세지 읽기 실패");
			},
			success : function(data){
//				console.log(data);
				read.text(data.className);
				read.removeClass('important').addClass('read');
				$("#countOfMessageHeader").text(data.countOfMessage);
				$("#countOfMessagePage").text(data.countOfMessage);
			}
		})
	});
	
	$(document).on("click", "a#getUserInfo", function(e){
		e.preventDefault();
		
		var userInfo = $(this);
		var url = userInfo.attr("href");
//		console.log("url : " + url);
		
		var read = userInfo.next().find(".important");
		
		$.ajax({
			type : 'post',
			url : url,
			dataType : 'json',
			error : function(){
				alert("유저 정보 가져오기 실패");
			},
			success : function(data){
//				console.log(data);
				$("#modalCompose").modal("show");
				$("#inputTo").val(data.fromUser);
				read.text(data.className);
				read.removeClass('important').addClass('read');
				$("#countOfMessageHeader").text(data.countOfMessage);
				$("#countOfMessagePage").text(data.countOfMessage);
			}
		})
	});
	
	$(document).on("click", "a#showmore", function(e){
		e.preventDefault();
		
		var showmore = $(this);
		var url = showmore.attr("href");

		$.ajax({
			type : 'get',
			url : url,
			dataType : 'json',
			error : function(){
				alert("리스트 불러오기 실패");
			},
			success : function(data){
				console.log(data);
				showmore.remove();
				var list = data;
//				console.log("list size : " + list.length);

				for(var i = 0; i < list.length; i++){
					addFreeboardTemplate(list[i]);
				}
				if(data.length !== 0 && !(data.length < 10)){
					let showBtn = "<a href='/api/freeboards/users/" + userId + "?freeBoardId=" +
						data[data.length -1].id + "' title='show more' id='showmore' class='btn btn-primary showMore'><i class='fa fa-refresh'></i> Show more</a>";
					$("#tab-item-2").append(showBtn);
				}
			}
		})
	});

	// $("li a[href='#tab-item-5']").click(function(e){
	$(document).on("click", "li a[href='#tab-item-5']", function(e){
		e.preventDefault();

		let userId = $("#userId").val();
		$.ajax({
			type : 'get',
			url : "/answers/users/" + userId,
			dataType : 'json',
			error : function(){
				alert("리스트 불러오기 실패");
			},
			success : function(data){
				$("#tab-item-5").empty();
				for(let i = 0; i < data.length; i++){
					addFreeboardAnswerTemplate(data[i]);
				}

				if(data.length !== 0 && !(data.length < 10)){
					let showBtn = "<a href='/answers/users/" + userId + "?answerId=" +
						data[data.length -1].id + "' title='show more' id='showmoreAnswer' class='btn btn-primary showMore'><i class='fa fa-refresh'></i> Show more</a>";
					$("#tab-item-5").append(showBtn);
				}
			}
		})
	});

	$(document).on("click", "a#showmoreAnswer", function(e){
		e.preventDefault();

		var showmore = $(this);
		var url = showmore.attr("href");
		let userId = $("#userId").val();

		$.ajax({
			type : 'get',
			url : url,
			dataType : 'json',
			error : function(){
				alert("리스트 불러오기 실패");
			},
			success : function(data){
				showmore.remove();
				var list = data;
//				console.log("list size : " + list.length);

				for(var i = 0; i < list.length; i++){
					addFreeboardAnswerTemplate(list[i]);
				}
				if(data.length !== 0 && !(data.length < 10)){
					let showBtn = "<a href='/answers/users/" + userId + "?answerId=" +
						data[data.length -1].id + "' title='show more' id='showmoreAnswer' class='btn btn-primary showMore'><i class='fa fa-refresh'></i> Show more</a>";
					$("#tab-item-5").append(showBtn);
				}
			}
		})
	});
	
})

function addDiaryTemplate(data){
	var template = "<div class='userActivities'><div class='i'><a href='#' title='#' class='image'>"
		+ "<img src='/images/profile/"+data.diary.writer.profileImage+"' alt='#' width='44' height='44'>"
		+ "</a><div class='activityContent'><ul class='simpleListings status'>"
		+ "<li><div class='title'>"
		+ "<a href='/diaries/"+ data.diary.id +"'>"+ data.diary.title +"</a>"
		+ "</div><div class='info'>"+ data.formattedSaveDate +"</div>"
		+ "	<p>"+ data.type +"</p><div class='share'>"
		+ "<a href='/diaries/"+ data.diary.id +"' title='#'>"
		+ "<i class='fa fa-comments'></i> "+ data.diary.countOfAnswer +"</a></div></li>"
		+ "</ul></div></div></div>";
//	$("#timelineList").append(template);
	$("#tab-item-2").append(template);
}
function addFreeboardTemplate(data){
	var template = "<div class='userActivities'><div class='i'><div class='activityContent'><ul class='simpleListings status'>"
		+ "<li><div class='title'>"
		+ "<a href='/freeboards/"+ data.id +"'>"+ data.title +"</a>"
		+ "</div><div class='info'>"+ data.createDate +"</div>"
		+ "<div class='share'>"
		+ "<a href='/freeboards/"+ data.id +"' title='#'>"
		+ "<i class='fa fa-comments'></i> "+ data.countOfAnswer +"</a></div></li>"
		+ "</ul></div></div></div>";
	$("#tab-item-2").append(template);
}

function addFreeboardAnswerTemplate(data){
	var template = "<div class='userActivities'><div class='i'><div class='activityContent'><ul class='simpleListings status'>"
		+ "<li><div class='title'>"
		+ "<a href='/freeboards/"+ data.boardId +"'>"+ data.titleOfBoard +"</a>"
		+ "</div><div class='info'>"+ data.updateDate +"</div>"
		+ "<div class='share'>"
		+ data.contents +"</div></li>"
		+ "</ul></div></div></div>";
	$("#tab-item-5").append(template);
}