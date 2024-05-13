package com.phcworld.alert.service.port;

import java.util.List;

import com.phcworld.alert.infrasturcture.AlertEntity;
import com.phcworld.user.infrastructure.UserEntity;

public interface AlertService {
	
	AlertEntity getOneAlert(Long id);
	
	List<AlertEntity> findListAlertByPostUser(UserEntity loginUser);
	
}
