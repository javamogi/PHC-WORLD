package com.phcworld.domain.good;

import com.phcworld.domain.user.User;

public interface GoodService {
	String upGood(Long diaryId, User loginUser);
}
