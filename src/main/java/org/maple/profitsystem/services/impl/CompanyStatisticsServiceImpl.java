package org.maple.profitsystem.services.impl;

import java.util.Date;

import org.maple.profitsystem.mappers.CompanyStatisticsModelMapper;
import org.maple.profitsystem.models.CompanyStatisticsModel;
import org.maple.profitsystem.services.CompanyStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(value = "transactionManager", rollbackFor = Exception.class, isolation = Isolation.READ_COMMITTED)
public class CompanyStatisticsServiceImpl implements CompanyStatisticsService {

	@Autowired
	private CompanyStatisticsModelMapper companyStatisticsModelMapper;
	
	@Override
	public CompanyStatisticsModel getCompanyStatisticsByCompanyId(long companyId) {
		return companyStatisticsModelMapper.selectByCompanyId(companyId);
	}

	@Override
	public int addCompanyStatistics(CompanyStatisticsModel record) {
		if(null == record) {
			return 0;
		}
		record.setCreateDt(new Date());
		record.setLastUpdateDt(new Date());
		return companyStatisticsModelMapper.insert(record);
	}

	@Override
	public int updateCompanyStatistics(CompanyStatisticsModel record) {
		if(null == record) {
			return 0;
		} 
		record.setLastUpdateDt(new Date());
		return companyStatisticsModelMapper.updateByCompanyIdSelective(record);
	}
}
