package com.phcworld.domain.alert;

import java.util.List;

import com.phcworld.domain.user.User;

public interface AlertService {
	
	Alert createAlert(Alert alert);
	
	Alert getOneAlert(Long id);
	
	List<Alert> findPageRequestAlertByPostUser(User loginUser);
	
}
