/**
 * 
 */
$(document).ready(function(){

	$('.page-link').click(function(e) {
		e.preventDefault();
		var pageNum = $(this).text();
		var url = $(".write-answer").attr("action");
		$.ajax({
			type : 'get',
			url : url,
			data : "pageNum=" + pageNum,
			dataType : 'json',
			error : function(jqXHR, txtStatus, errorThrown){
				console.log(jqXHR);
				console.log(jqXHR.responseText.error)
				alert(jqXHR.responseJSON.error);
			},
			success : function(data){
				console.log(data);
				$("article#answer-article").remove();
				$("div.text-center").remove();
				for(let i = 0; i < data.answers.length; i++){
					var deleteUrl = url + "/" + data.answers[i].id;
					var getUrl = url + "/" + data.answers[i].id;
					var profileUrl = "/users/"+data.answers[i].writer.id+"/profile";
//				console.log(deleteUrl);
					var answerTemplate = "<article class='answer-article' id='answer-article'>" + "<div class='be-comment'><div class='be-img-comment'><a href='" + profileUrl +
						"'><img src='/images/profile/" + data.answers[i].writer.profileImage + "' class='be-ava-comment'></a></div>" +
						"<div class='be-comment-content'><span class='be-comment-name'><a href='" + profileUrl +
						"'>" + data.answers[i].writer.name + "</a></span><span class='be-comment-time'><a class='answer-get' href='"+ getUrl + "'>수정 </a>"+
						"<a class='answer-delete' href='"+deleteUrl+"'>삭제 </a><i class='fa fa-clock-o'></i> " +
						data.answers[i].updateDate + "</span><p class='be-comment-text'>" + data.answers[i].contents + "</p></div></div></article>";
					$(".answer-template").append(answerTemplate);
				}
				let pageNationTemplate = renderPageNation(data);
				$(".answer-template").after(pageNationTemplate);
			}
		})
	});

	function renderPageNation(data){
		let previous = false;
		let next = false;
		let currentNum = data.currentPageNum;
		let totalPageNum = data.totalOfPage;
		let pageSize = 10;
		let temp = (currentNum - 1) % pageSize;
		let startNum = currentNum - temp;
		let endNum = startNum + pageSize - 1;
		if(endNum > totalPageNum){
			endNum = totalPageNum;
		}
		if(startNum > 1){
			previous = true;
			// previous = startNum - pageSize;
		}
		if(endNum < totalPageNum) {
			next = true;
			// next = endNum + 1;
		}
		let pageNationTemplate = "<div class='text-center'>" +
			"<ul class='pagination justify-content-center'>";
		if(previous){
			pageNationTemplate += "<li class='page-item'>" +
				"<a id='previous' class='page-link' href='' aria-label='Previous'>" +
					"<span aria-hidden='true'>&laquo;</span>" +
					"<span class='sr-only'>Previous</span>" +
				"</a>" +
			"</li>";
		}
		for (let i = startNum; i <= endNum; i++){
			pageNationTemplate += "<li class='page-item'>" +
				"<a class='page-link' href=''>" + i + "</a>"
			"</li>";
		}
		if(next){
			pageNationTemplate += "<li class='page-item'>" +
				"<a class='page-link' href='' aria-label='Next'>" +
					"<span aria-hidden='true'>&raquo;</span>" +
					"<span class='sr-only'>Next</span>" +
				"</a>" +
			"</li>";
		}
		pageNationTemplate += "</ul></div>";
		return pageNationTemplate;
	}
	
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
				console.log(data);
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
					let countOfAnswer = $("#countOfAnswer").text();
					let num = countOfAnswer.replace(/[\[\]]/g, "");
					if(num === ""){
						num = 0;
					}
					let count = parseInt(num) + 1;
					let countString = "[" + count + "]";
					$("#countOfAnswer").text(countString);
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