package org.maple.profitsystem.spiders;

import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyStatisticsModel;

public interface StatisticsSpider {

	CompanyStatisticsModel fetchStatistics(String symbol)  throws PSException;
}
