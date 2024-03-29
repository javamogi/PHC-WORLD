package com.phcworld.ifs;

import com.phcworld.user.infrastructure.UserEntity;

public interface CrudInterface<Request, Response, Success> {

	Response create(UserEntity loginUser, Long id, Request request);
	
	Response read(Long id, UserEntity loginUser);
	
	Response update(Request request, UserEntity loginUser);
	
	Success delete(Long id, UserEntity loginUser);
	
	
}
