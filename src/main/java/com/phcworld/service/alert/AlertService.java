package com.phcworld.service.alert;

import java.util.List;

import com.phcworld.domain.alert.Alert;
import com.phcworld.domain.user.User;

public interface AlertService {
	
	Alert getOneAlert(Long id);
	
	List<Alert> findListAlertByPostUser(User loginUser);
	
}
