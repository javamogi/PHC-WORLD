package com.phcworld.alert.infrasturcture;

import com.phcworld.alert.infrasturcture.dto.AlertSelectDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.user.infrastructure.UserEntity;

import java.util.List;

public interface AlertRepositoryCustom {
    List<AlertSelectDto> findAlertListByPostWriter(UserEntity user);

    void deleteAlert(SaveType saveType, Long id);
}
