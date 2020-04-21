package com.phcworld.ifs;

import com.phcworld.domain.user.User;

public interface CrudInterface<Request, Response, Success> {

	Response create(User loginUser, Long id, Request request);
	
	Response read(Long id, User loginUser);
	
	Response update(Request request, User loginUser);
	
	Success delete(Long id, User loginUser);
	
	
}
