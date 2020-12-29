package org.maple.profitsystem.services.impl;

import org.maple.profitsystem.mappers.EarningDateModelMapper;
import org.maple.profitsystem.models.EarningDateModel;
import org.maple.profitsystem.services.EarningDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "transactionManager", rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class EarningDateServiceImpl implements EarningDateService {

	@Autowired
	private EarningDateModelMapper earningDateModelMapper;
	
	@Override
	public int addEarningDate(EarningDateModel record) {
		if(record == null) {
			return 0;
		} else {
			return earningDateModelMapper.insert(record);
		}
	}

	@Override
	public int removeEarningDateById(Long id) {
		return earningDateModelMapper.deleteById(id);
	}

	@Override
	public EarningDateModel getLastEarningDateBySymbol(String symbol) {
		return earningDateModelMapper.selectLastBySymbol(symbol);
	}

}
