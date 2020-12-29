package org.maple.profitsystem.spiders;

import java.util.Date;
import java.util.List;

import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.DividendDateModel;

public interface DividendDateSpider {
	
	List<DividendDateModel> fetchEarningDate(Date startDate, int extendDays) throws PSException;
}
