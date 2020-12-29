package org.maple.profitsystem.spiders;

import java.util.List;

import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.EarningDateModel;

public interface EarningDateSpider {

	/**
	 * Get earning date list of asc order.
	 * @param symbol
	 * @return
	 */
	List<EarningDateModel> fetchEarningDate(String symbol) throws PSException;
}
