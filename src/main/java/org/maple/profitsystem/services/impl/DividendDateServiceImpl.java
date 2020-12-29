package org.maple.profitsystem.services.impl;

import org.maple.profitsystem.mappers.DividendDateModelMapper;
import org.maple.profitsystem.models.DividendDateModel;
import org.maple.profitsystem.services.DividendDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "transactionManager", rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class DividendDateServiceImpl implements DividendDateService {

	@Autowired
	private DividendDateModelMapper dividendDateModelMapper;
	
	@Override
	public int addEarningDateNoConflict(DividendDateModel record) {
		if(record == null) {
			return 0;
		} else {
			return dividendDateModelMapper.insertNoConflict(record);
		}
	}

}
