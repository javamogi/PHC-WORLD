/**
 * 
 */
$(document).ready(function(){
	
	$('#save_button').click(function(e){
		e.preventDefault();
		var queryString = $(".write-answer").serialize();
		var url = $(".write-answer").attr("action");
//		console.log("query : " + queryString);
//		console.log("url : " + url);
		
		$.ajax({
			type : 'post',
			url : url,
			data : queryString,
			dataType : 'json',
			error : function(){
				alert("댓글통신실패");
			},
			success : function(data){
//				console.log(data);
				var deleteUrl = url + "/" + data.id;
				var profileUrl = "/users/"+data.writer.id+"/profile";
//				console.log(deleteUrl);
				var answerTemplate = "<article class='answer-article' id='answer-article'>" + "<div class='be-comment'><div class='be-img-comment'><a href='" + profileUrl +
				"'><img src='/images/profile/" + data.writer.profileImage + "' class='be-ava-comment'></a></div>" + 
				"<div class='be-comment-content'><span class='be-comment-name'><a href='" + profileUrl + 
				"'>" + data.writer.name + "</a></span><span class='be-comment-time'><a class='answer-delete' href='"+deleteUrl+"'>삭제 </a><i class='fa fa-clock-o'></i> " + 
				data.formattedCreateDate + "</span><p class='be-comment-text'>" + data.contents + "</p></div></div></article>";
				$(".answer-template").append(answerTemplate);
				$("textarea[name=contents]").val('');
				if(url.indexOf("freeboard") > 0){
					$("#countOfAnswer").text(data.freeBoard.countOfAnswer);
				} else {
					$("#countOfDiaryAnswer").text(data.diary.countOfAnswer);
				}
				$('html, body').scrollTop(document.body.scrollHeight);
			}
		})
	});
	
	$(document).on("click", "a.answer-delete", function(e){
		e.preventDefault();
		var deleteBtn = $(this);
		var url = deleteBtn.attr("href");
//		console.log("deleteBtn : " + deleteBtn);
//		console.log("url : " + url);
		
		$.ajax({
			type : 'delete',
			url : url,
			dataType : 'json',
			error : function(xhr, status, error){
				alert("실패");
			},
			success : function(data, status){
//				console.log(data);
//				if(data === "success"){
				if(data.success != null){
					deleteBtn.closest("article").remove();
					var temp = $("#countOfDiaryAnswer").length;
					if(temp === 0){
						$("#countOfAnswer").text(data.success);
					} else {
						$("#countOfDiaryAnswer").text(data.success);
					}
				} else {
					alert(data.error);
				}
			}
		});
	});
	
	$('#upGood').click(function(e){
		e.preventDefault();
		var url = $('#upGood').attr("href");
//		console.log("url : " + url);
		
		$.ajax({
			type : 'get',
			url : url,
			dataType : 'json',
			error : function(){
				alert("좋아요실패");
			},
			success : function(data){
//				console.log(data);
				$("#countOfGood").text(" " + data.success);
			}
		})
	});
	
})