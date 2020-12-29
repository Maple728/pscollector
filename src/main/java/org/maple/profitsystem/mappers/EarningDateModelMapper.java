package org.maple.profitsystem.mappers;

import org.maple.profitsystem.models.EarningDateModel;

public interface EarningDateModelMapper {

	int insert(EarningDateModel record);
	
	int deleteById(Long id);
	
	EarningDateModel selectLastBySymbol(String symbol);
}
