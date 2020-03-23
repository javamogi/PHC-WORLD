$(document).ready(function(){
	
	$(".form-inline input[type=submit]").click(addSearch);
	
	$(document).on("click",".pagination li a", function(e){
		$(".panel-body").find("div").remove();
		$("#search_pagination").find("li").remove();
		
		var queryString = $(".form-inline").serialize();
		var url = $(this).attr("href");
		
		$.ajax({
			type:"get",
			url:url,
			data:queryString,
			datatype : "json",
			success:function(data){
				var skey = $('input[name="title"]').val();
				if(data.total == 0){
					var empty = "검색결과없음";
					$(".panel-body").append(empty);
				}
				var pageCount;
				if(data.display!=10){
					data.display = 10;
				}
				if(data.total%data.display == 0){
					pageCount = data.total/data.display;
				} else{
					pageCount = Math.ceil(data.total/data.display);
				}
				
				var currentPage = Math.ceil(data.start/data.display);
				var temp = (currentPage - 1)%data.display;
				var startPage = currentPage - temp;
				var endPage = (startPage + data.display)-1;
				if(endPage>pageCount){
					endPage=pageCount;
				}
				if(startPage > data.display){
					var previous = "<li><a href='/api/movies?title="+skey+"&pnum=" + (startPage-data.display) + "' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
					$("#search_pagination").append(previous);
				}
				for(var i = startPage; i < endPage+1; i++){
					console.log(currentPage);
					if(i==currentPage){
						var pageNation = "<li class='active'><a href='/api/movies?title="+skey+"&pnum="+i+"'>"+i+"</a></li>"
					} else{
						var pageNation = "<li><a href='/api/movies?title="+skey+"&pnum="+i+"'>"+i+"</a></li>"
					}
					$("#search_pagination").append(pageNation);
				}
				if(endPage < pageCount){
					var next = "<li><a href='/api/movies?title="+skey+"&pnum=" + (startPage+data.display) + "' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
					$("#search_pagination").append(next);
				}
				
				$.each(data.items, function(key, value){
					var searchList = "<div class='col-xs-18 col-md-12'><div class='media'><div class='media-left media-top'><a href='"+data.items[key].link+"' target='_blank'>" +
					"<img class='media-object' src='"+data.items[key].image+"'></a></div><div class='media-body'><p>제목 : <a href='"+ data.items[key].link +"'>"+data.items[key].title+"</a></p>" +
					"<p>제작년도 : "+ data.items[key].pubDate +"</p><p>감독 : "+data.items[key].director+"</p><p>배우 : "+data.items[key].actor+"</p><p>평점 : "+data.items[key].userRating+"</p></div></div></div>";
					$(".panel-body").append(searchList);
				});
			},
			error:function(e){
				alert("통신실패!!");
			}
		});	
		return false;
	})
});

function addSearch(e){
	$(".panel-body").empty();
	$(".panel-body").find("div").remove();
	$("#search_pagination").find("li").remove();
	
	var queryString = $(".form-inline").serialize();
	var url = $(".form-inline").attr("action");
	
	$.ajax({
		type:"get",
		url:url,
		data:queryString,
		datatype : "json",
		success:function(data){
			console.log(data);
			var skey = $('input[name="title"]').val();
			if(data.total == 0){
				var empty = "검색결과없음";
				$(".panel-body").append(empty);
			}
			var pageCount;
			if(data.total%data.display == 0){
				pageCount = data.total/data.display;
			} else{
				pageCount = Math.ceil(data.total/data.display);
			}

			var currentPage = Math.ceil(data.start/data.display);
			var temp = (currentPage - 1)%data.display;
			var startPage = currentPage - temp;
			var endPage = (startPage + data.display)-1;
			if(endPage>pageCount){
				endPage=pageCount;
			}
			if(startPage > data.display){
				var previous = "<li><a href='/api/movies?title="+skey+"&pnum=" + (startPage-data.display) + "' aria-label='Previous'><span aria-hidden='true'>&laquo;</span></a></li>";
				$("#search_pagination").append(previous);
			}
			for(var i = startPage; i < endPage+1; i++){
				console.log("currentPage : "+currentPage);
				if(i==currentPage){
					var pageNation = "<li class='active'><a href='/api/movies?title="+skey+"&pnum="+i+"'>"+i+"</a></li>"
				} else{
					var pageNation = "<li><a href='/api/movies?title="+skey+"&pnum="+i+"'>"+i+"</a></li>"
				}
				$("#search_pagination").append(pageNation);
			}
			if(endPage < pageCount){
				var next = "<li><a href='/api/movies?title="+skey+"&pnum=" + (startPage+data.display) + "' aria-label='Next'><span aria-hidden='true'>&raquo;</span></a></li>";
				$("#search_pagination").append(next);
			}
			
			$.each(data.items, function(key, value){
				console.log(data.items[key]);
				var searchList = "<div class='col-xs-18 col-md-12'>" +
						"<div class='media'>" +
						"<div class='media-left media-top'>" +
						"<a href='"+data.items[key].link+"' target='_blank'>" +
				"<img class='media-object' src='"+data.items[key].image+"'>" +
						"</a>" +
						"</div>" +
						"<div class='media-body'>" +
						"<p>제목 : <a href='"+ data.items[key].link +"'>"+data.items[key].title+"</a></p>" +
				"<p>제작년도 : "+ data.items[key].pubDate +"</p>" +
						"<p>감독 : "+data.items[key].director+"</p>" +
								"<p>배우 : "+data.items[key].actor+"</p>" +
										"<p>평점 : "+data.items[key].userRating+"</p>" +
												"</div></div></div>";
				$(".panel-body").append(searchList);
			});
		},
		error:function(e){
			alert("통신실패!!");
		}
	});	
	return false;
}