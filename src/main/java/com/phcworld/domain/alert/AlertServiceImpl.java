package com.phcworld.domain.alert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.phcworld.domain.user.User;

@Service
@Transactional
public class AlertServiceImpl implements AlertService {
	
	@Autowired
	private AlertRepository alertRepository;
	
	@Override
	public Alert createAlert(Alert alert) {
		return alertRepository.save(alert);
	}
	
	@Override
	public Alert getOneAlert(Long id) {
		return alertRepository.getOne(id);
	}
	
	@Override
	public List<Alert> findPageRequestAlertByUser(User loginUser) {
		PageRequest pageRequest = PageRequest.of(0, 5, new Sort(Direction.DESC, "id"));
		Page<Alert> pageAlert = alertRepository.findByWriter(loginUser, pageRequest);
		return pageAlert.getContent();
	}
	
}
