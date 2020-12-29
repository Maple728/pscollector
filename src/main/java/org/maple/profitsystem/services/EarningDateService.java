package org.maple.profitsystem.services;

import org.maple.profitsystem.models.EarningDateModel;

public interface EarningDateService {

	int addEarningDate(EarningDateModel record);
	
	int removeEarningDateById(Long id);
	
	EarningDateModel getLastEarningDateBySymbol(String symbol);
}
