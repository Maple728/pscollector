package org.maple.profitsystem.services;

import org.maple.profitsystem.models.DividendDateModel;

public interface DividendDateService {
	int addEarningDateNoConflict(DividendDateModel record);
}
