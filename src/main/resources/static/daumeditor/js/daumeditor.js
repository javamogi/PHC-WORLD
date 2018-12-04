
	var config = {
		txHost: '', /* 런타임 시 리소스들을 로딩할 때 필요한 부분으로, 경로가 변경되면 이 부분 수정이 필요. ex) http://xxx.xxx.com */
		txPath: '', /* 런타임 시 리소스들을 로딩할 때 필요한 부분으로, 경로가 변경되면 이 부분 수정이 필요. ex) /xxx/xxx/ */
		txService: 'sample', /* 수정필요없음. */
		txProject: 'sample', /* 수정필요없음. 프로젝트가 여러개일 경우만 수정한다. */
		initializedId: "", /* 대부분의 경우에 빈문자열 */
		wrapper: "tx_trex_container", /* 에디터를 둘러싸고 있는 레이어 이름(에디터 컨테이너) */
		form: 'journal'+"", /* 등록하기 위한 Form 이름 */
		txIconPath: "/daumeditor/images/icon/editor/", /*에디터에 사용되는 이미지 디렉터리, 필요에 따라 수정한다. */
		txDecoPath: "/daumeditor/images/deco/contents/", /*본문에 사용되는 이미지 디렉터리, 서비스에서 사용할 때는 완성된 컨텐츠로 배포되기 위해 절대경로로 수정한다. */
		canvas: {
            exitEditor:{
                /*
                desc:'빠져 나오시려면 shift+b를 누르세요.',
                hotKey: {
                    shiftKey:true,
                    keyCode:66
                },
                nextElement: document.getElementsByTagName('button')[0]
                */
            },
			styles: {
				color: "#123456", /* 기본 글자색 */
				fontFamily: "굴림", /* 기본 글자체 */
				fontSize: "10pt", /* 기본 글자크기 */
				backgroundColor: "#fff", /*기본 배경색 */
				lineHeight: "1.5", /*기본 줄간격 */
				padding: "8px" /* 위지윅 영역의 여백 */
			},
			showGuideArea: false
		},
		events: {
			preventUnload: false
		},
		sidebar: {
			attachbox: {
				show: true,
				confirmForDeleteAll: true
			}
		},
		size: {
			//contentWidth: 700 /* 지정된 본문영역의 넓이가 있을 경우에 설정 */
		}
	};
	
	EditorJSLoader.ready(function(Editor) {
		var editor = new Editor(config);
	});

	
	/* 예제용 함수 */
	function saveContent() {
		Editor.save(); // 이 함수를 호출하여 글을 등록하면 된다.
	};
	
	/**
	 * Editor.save()를 호출한 경우 데이터가 유효한지 검사하기 위해 부르는 콜백함수로
	 * 상황에 맞게 수정하여 사용한다.
	 * 모든 데이터가 유효할 경우에 true를 리턴한다.
	 * @function
	 * @param {Object} editor - 에디터에서 넘겨주는 editor 객체
	 * @returns {Boolean} 모든 데이터가 유효할 경우에 true
	 */
	function validForm(editor) {
		// Place your validation logic here

		// sample : validate that content exists
		var validator = new Trex.Validator();
		var title = $("#title").val();
		var content = editor.getContent();
		
		if (!validator.exists(title)) {
			alert('제목을 입력하세요');
			return false;
		}
		if (!validator.exists(content)) {
			alert('내용을 입력하세요');
			return false;
		}

		return true;
	};

	/**
	 * Editor.save()를 호출한 경우 validForm callback 이 수행된 이후
	 * 실제 form submit을 위해 form 필드를 생성, 변경하기 위해 부르는 콜백함수로
	 * 각자 상황에 맞게 적절히 응용하여 사용한다.
	 * @function
	 * @param {Object} editor - 에디터에서 넘겨주는 editor 객체
	 * @returns {Boolean} 정상적인 경우에 true
	 */
	function setForm(editor) {
        var i, input;
        var form = editor.getForm();
        var content = editor.getContent();

        // 본문 내용을 필드를 생성하여 값을 할당하는 부분
        var textarea = document.createElement('textarea');
        textarea.name = 'contents';
        textarea.value = content;
        form.createField(textarea);
        

        /* 아래의 코드는 첨부된 데이터를 필드를 생성하여 값을 할당하는 부분으로 상황에 맞게 수정하여 사용한다.
         첨부된 데이터 중에 주어진 종류(image,file..)에 해당하는 것만 배열로 넘겨준다. */
        var images = editor.getAttachments('image');
        for (i = 0; i < images.length; i++) {
            // existStage는 현재 본문에 존재하는지 여부
            if (images[i].existStage) {
                // data는 팝업에서 execAttach 등을 통해 넘긴 데이터
                //alert('attachment information - image[' + i + '] \r\n' + JSON.stringify(images[i].data));
//                input = document.createElement('input');
//                input.type = 'hidden';
//                input.name = 'attach_image';
//                input.value = images[i].data.imageurl;  // 예에서는 이미지경로만 받아서 사용
//                form.createField(input);
            }
        }

//        var files = editor.getAttachments('file');
//        for (i = 0; i < files.length; i++) {
//            input = document.createElement('input');
//            input.type = 'hidden';
//            input.name = 'attach_file';
//            input.value = files[i].data.attachurl;
//            form.createField(input);
//        }
        return true;
	};
	
	function loadContent() {
		
		var attachments = {};
		attachments['image'] = [];
		
		$('#sample_contents_source').find('img').each(function(){
			var imgsrc = $(this).attr('src');
//			var fileName = imgsrc.split("/").slice(-1).join().split(".").shift();
			var fileName = imgsrc.split("/").slice(-1).join();
			$.ajax({
				type : "get",
				url : "/upload/findImage",
				data : {randFileName : fileName},
				dataType : "json",
				async: false,
				success : function(data){
					attachments['image'].push({
						'attacher': 'image',
						'data': {
							'randFileName' : data.randFileName,
							'imageurl': '/images/'+data.randFileName,
							'filename': data.originalFileName,
							'filesize': data.size
						}
					});
				},
				error : function(data){
					alert("에러");
				}
			});
		});
		
//		attachments['file'] = [];
//		attachments['file'].push({
//			'attacher': 'file',
//			'data': {
//				'attachurl': 'http://cfile297.uf.daum.net/attach/207C8C1B4AA4F5DC01A644',
//				'filemime': 'image/gif',
//				'filename': 'editor_bi.gif',
//				'filesize': 640
//			}
//		});
		
		/* 저장된 컨텐츠를 불러오기 위한 함수 호출 */
		Editor.modify({
			"attachments": function () { /* 저장된 첨부가 있을 경우 배열로 넘김, 위의 부분을 수정하고 아래 부분은 수정없이 사용 */
				var allattachments = [];
				for (var i in attachments) {
					allattachments = allattachments.concat(attachments[i]);
				}
				return allattachments;
			}(),
//			"content": document.getElementById("sample_contents_source") /* 내용 문자열, 주어진 필드(textarea) 엘리먼트 */
			"content": $('#sample_contents_source').html() /* 내용 문자열, 주어진 필드(textarea) 엘리먼트 */
		});
	};
