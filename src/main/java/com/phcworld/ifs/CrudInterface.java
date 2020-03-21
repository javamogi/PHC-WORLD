package com.phcworld.ifs;

import com.phcworld.domain.user.User;

public interface CrudInterface<Response, Success> {

	Response create(User loginUser, Long id, String contents);
	
	Response read(Long id, User loginUser);
	
	Response update(Long id, String contents, User loginUser);
	
	Success delete(Long id, User loginUser);
	
	
}
