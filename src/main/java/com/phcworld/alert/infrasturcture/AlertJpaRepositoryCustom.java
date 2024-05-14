package com.phcworld.alert.infrasturcture;

import com.phcworld.alert.infrasturcture.port.AlertSelectDto;
import com.phcworld.domain.common.SaveType;
import com.phcworld.user.infrastructure.UserEntity;

import java.util.List;

public interface AlertJpaRepositoryCustom {
    List<AlertSelectDto> findAlertListByPostWriter(UserEntity user);

    void deleteAlert(SaveType saveType, Long id);
}
