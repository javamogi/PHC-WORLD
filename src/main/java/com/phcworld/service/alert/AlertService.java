package com.phcworld.service.alert;

import java.util.List;

import com.phcworld.domain.alert.Alert;
import com.phcworld.user.infrastructure.UserEntity;

public interface AlertService {
	
	Alert getOneAlert(Long id);
	
	List<Alert> findListAlertByPostUser(UserEntity loginUser);
	
}
