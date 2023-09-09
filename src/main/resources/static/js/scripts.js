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
			error : function(jqXHR, txtStatus, errorThrown){
				console.log(jqXHR);
				console.log(jqXHR.responseText.error)
//				alert("댓글통신실패");
				alert(jqXHR.responseJSON.error);
			},
			success : function(data){
//				console.log(data);
				var deleteUrl = url + "/" + data.id;
				var getUrl = url + "/" + data.id;
				var profileUrl = "/users/"+data.writer.id+"/profile";
//				console.log(deleteUrl);
				var answerTemplate = "<article class='answer-article' id='answer-article'>" + "<div class='be-comment'><div class='be-img-comment'><a href='" + profileUrl +
				"'><img src='/images/profile/" + data.writer.profileImage + "' class='be-ava-comment'></a></div>" + 
				"<div class='be-comment-content'><span class='be-comment-name'><a href='" + profileUrl + 
				"'>" + data.writer.name + "</a></span><span class='be-comment-time'><a class='answer-get' href='"+ getUrl + "'>수정 </a>"+
						"<a class='answer-delete' href='"+deleteUrl+"'>삭제 </a><i class='fa fa-clock-o'></i> " + 
				data.updateDate + "</span><p class='be-comment-text'>" + data.contents + "</p></div></div></article>";
				$(".answer-template").append(answerTemplate);
				$("textarea[name=contents]").val('');
				if(url.indexOf("freeboard") > 0){
					$("#countOfAnswer").text(data.countOfAnswers);
				} else {
					$("#countOfDiaryAnswer").text(data.countOfAnswers);
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
			error : function(jqXHR, txtStatus, errorThrown){
				console.log(jqXHR.responseJSON);
				alert(jqXHR.responseJSON.error);
			},
			success : function(data){
//				console.log(data);
				deleteBtn.closest("article").remove();
				var temp = $("#countOfDiaryAnswer").length;
				if(temp === 0){
					var txt = $("#countOfAnswer").text();
					$("#countOfAnswer").text(countAnswer(txt));
				} else {
					var txt = $("#countOfDiaryAnswer").text();
					$("#countOfDiaryAnswer").text(countAnswer(txt));
				}
			}
		});
	});

	function countAnswer(txt){
		var val = txt.replace(/\[|\]/g, "");
		val -= 1;
		var count;
		if(val === 0){
			count = "";
		} else {
			count = "[" + val + "]";
		}
		return count;
	}
	
	$(document).on("click", "a.answer-get", function(e){
		e.preventDefault();
		var getBtn = $(this);
		var url = getBtn.attr("href");
		var article = getBtn.closest("article");
		console.log(article.children());
//		var txt = getBtn.closest('div').children('.be-comment-text');
//		console.log(txt);
		console.log("url : " + url);
		
		$.ajax({
			type : 'get',
			url : url,
			dataType : 'json',
			error : function(jqXHR, txtStatus, errorThrown){
				console.log(jqXHR.responseJSON);
				alert(jqXHR.responseJSON.error);
			},
			success : function(data){
				var subUrl = url.substring(0, url.lastIndexOf("/"));
				console.log(subUrl);
				console.log(data);
				article.children().remove();
				var updateTemplate = "<div class='col-lg-12 col-md-12'>" 
					+ "<form class='write-update' id='form-group' action='" + subUrl + "' method='put'>"
					+ "<div class='form-group'>"
					+ "<input type='hidden' name='id' value='"+data.id+"'>"
					+ "<textarea class='col-lg-10 col-md-10' id='contents' name='contents'>"+data.contents+"</textarea>"
					+ "<div class='col-lg-2 col-md-2'>"
					+ "<button type='submit' id='update_button' class='btn btn-default'>수정</button>"
					+ "</div></div></form></div>";
//				+ "</div></div></form></div>";
				article.append(updateTemplate);
				
			}
		});
	});
	
	$(document).on("click", "#update_button", function(e){
		e.preventDefault();
		var getBtn = $(this);
		var article = getBtn.closest("article");
		var queryString = $(".write-update").serialize();
		var url = $(".write-update").attr("action");
		console.log("qs : " + queryString);
		console.log("url : " + url);
		
		$.ajax({
			type : 'patch',
			url : url,
			data : queryString,
			dataType : 'json',
			error : function(jqXHR, txtStatus, errorThrown){
				console.log(jqXHR);
				alert(jqXHR.responseJSON.error);
			},
			success : function(data){
				console.log(data);
				var deleteUrl = url + "/" + data.id;
				var getUrl = url + "/" + data.id;
				var profileUrl = "/users/"+data.writer.id+"/profile";
				article.children().remove();
				var answerTemplate = "<div class='be-comment'><div class='be-img-comment'><a href='" + profileUrl +
				"'><img src='/images/profile/" + data.writer.profileImage + "' class='be-ava-comment'></a></div>" + 
				"<div class='be-comment-content'><span class='be-comment-name'><a href='" + profileUrl + 
				"'>" + data.writer.name + "</a></span><span class='be-comment-time'><a class='answer-get' href='"+ getUrl + "'>수정 </a>"+
						"<a class='answer-delete' href='"+deleteUrl+"'>삭제 </a><i class='fa fa-clock-o'></i> " + 
				data.updateDate + "</span><p class='be-comment-text'>" + data.contents + "</p></div></div>";
				article.append(answerTemplate);
			}
		});
	});
	
	$('#upGood').click(function(e){
		e.preventDefault();
		var url = $('#upGood').attr("href");
		console.log("url : " + url);
		
		$.ajax({
			type : 'put',
			url : url,
			dataType : 'json',
			error : function(xhr, status, error){
				alert("좋아요실패");
				var errorMessage = jQuery.parseJSON(xhr.responseText);
				console.log(errorMessage);
			},
			success : function(data, status){
				$("#countOfGood").text(" " + data.success);
//				if(data.success == "로그인을 해야합니다."){
//					alert(data.success);
//				} else {
//					$("#countOfGood").text(" " + data.success);
//				}
			}
		})
	});
	
})