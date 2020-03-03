# PHC-WORLD
### 프로젝트 특징
> * spring-boot 2.0.3.RELEASE
> * JPA
> * Maven
> * h2-database
> * bootstrap
> * jQuery
> * Ajax
> * Lombok
> * mustache template
> * devtools (liveReload)
> * Junit 테스트
***
### 프로젝트 기능
* 게시판을 이용할 수 있는 사이트입니다.
* 사이트의 모든 기능은 회원만 이용 가능합니다.
* 게시판은 두 종류입니다.
  1. 회원이 이용할 수 있는 자유게시판
  2. 자신의 글만 모아놓은 일기 게시판
* 회원간에는 메세지를 주고 받을 수 있습니다. (업데이트 필요)
* 일기 게시판은 회원 email을 주소에 타이핑하여 다른 회원의 글을 볼 수 있습니다. (업데이트 필요)
* 자유게시판의 정렬은 bootstrap dataTable을 사용하였습니다.
* 일기게시판의 정렬은 Page를 사용하였습니다. 
* 로그인 후 첫페이지는 로그인 한 User의 행적(타임라인)을 최신순으로 5개를 나타냈습니다. 
* 페이지 상단에 알림 메뉴를 클릭했을 시 로그인한 User가 쓴 게시물(자유게시판, 일기게시판)에 좋아요와 댓글이 달렸을 경우 나타납니다.
* 타임라인은 자유게시판 글쓰기, 자유게시판 댓글, 일기게시판 글쓰기, 일기게시판 댓글, 일기게시판 좋아요를 보여줍니다.
***
### 프로젝트 구현 설명
* MVC패턴 - Controller로 요청을 받고 Service Interface에서 선언한 메서드를 구현한 ServiceImpl에서 Repository로 처리 후 Controller에서 View로 응답합니다. (추상화 예정)
* 모든 Service, Repository, Controller에 대한 Junit테스트를 구현했습니다.
  * Service 테스트에서는 @MockBean을 사용하여 테스트하였습니다.
  * Repository 테스트에서는 기본 CRUD테스트와 새로 선언한 메서드를 테스트하였습니다.
  * Controller에서는 @MockBean과 WebMvc를 사용하여 테스트하였습니다.
  * User만 Validation 테스트를 위해 UserTest가 있습니다.
  * (Mockito에 대해 더 공부해야한다.)
* FreeBoardAnswer와 DiaryAnswer, Message는 RestController로 구현하였습니다. 
* RestController에서 로그인 회원이 없을 경우, 찾는 회원이 없을 경우, Message에 대해 자기자신에게 보낼경우 Exception으로 에러 메세지를 보내도록 하였습니다.
* User의 경우 탈퇴(삭제)를 구현하지 않았습니다.
* User의 Controller는 profile페이지에서 ajax로 타임라인을 불러오기 위해 RestController가 하나 더 있으며 타임라인을 불러오는 기능만 있습니다.
* Diary도 좋아요를 ajax로 처리하기 위해서 RestController가 하나 더 있으며 좋아요의 개수를 가져오는 기능만 있습니다.
* 회원가입을 하면 랜덤값을 가입요청한 메일주소로 보내고 받은 메일의 링크를 클릭하면 랜덤값을 저장된 값과 비교해 회원가입 승인을 합니다.
* 타임라인과 알림은 생성과 삭제만 사용하기 때문에 수정 기능은 구현하지 않았습니다.
* 자유게시판, 일기게시판, 프로필 사진에서 이미지 업로드를 사용할 수 있으며 ajax로 서버에 요청해서 MultipartFile을 이용해 파일을 쓰고 정보를 db에 저장 후 해당 정보를 가져와 본문 또는 회원 프로필에 적용하였습니다.
***
### 업데이트 예정
* 댓글 수정기능
* 회원탈퇴 요청시 회원을 삭제하지 않고 자격정지
* 이미지가 있는 글을 삭제할 때 이미지파일도 삭제
* 등록된 글의 회원을 클릭했을 때 회원페이지로 이동
* 회원정보를 나타내는 페이지에서 다이어리 글로 이동
***