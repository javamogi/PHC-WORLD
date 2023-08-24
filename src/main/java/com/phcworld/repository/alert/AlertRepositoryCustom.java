package com.phcworld.repository.alert;

import com.phcworld.domain.alert.dto.AlertSelectDto;
import com.phcworld.domain.user.User;

import java.util.List;

public interface AlertRepositoryCustom {
    List<AlertSelectDto> findAlertListByPostWriter(User user);
}
