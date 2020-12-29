package org.maple.profitsystem.services;

import org.maple.profitsystem.models.CompanyStatisticsModel;

public interface CompanyStatisticsService {

	CompanyStatisticsModel getCompanyStatisticsByCompanyId(long companyId);
	
	int addCompanyStatistics(CompanyStatisticsModel record);
	
	int updateCompanyStatistics(CompanyStatisticsModel record);
}
