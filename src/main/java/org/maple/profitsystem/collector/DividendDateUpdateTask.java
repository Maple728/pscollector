package org.maple.profitsystem.collector;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.maple.profitsystem.Application;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.DividendDateModel;
import org.maple.profitsystem.services.DividendDateService;
import org.maple.profitsystem.spiders.DividendDateSpider;
import org.maple.profitsystem.spiders.impl.DividendDateSpiderStreet;

public class DividendDateUpdateTask implements Runnable {
	
	private static Logger logger = Logger.getLogger(DividendDateUpdateTask.class);
	
	private static List<DividendDateSpider> spiders = new ArrayList<>();
	
	private static DividendDateService DividendDateService;
	
	static {
		spiders.add(new DividendDateSpiderStreet());
		
		// get service bean from spring context
		DividendDateService = Application.springContext.getBean(DividendDateService.class);
	}
	
	@Override
	public void run() {
		try {
			List<DividendDateModel> dividendDateList = fetchDividendDate(new Date(), CommonConstants.DIVIDEND_DATE_EXTEND_DAYS);
			
			for(DividendDateModel dd : dividendDateList) {
				DividendDateService.addEarningDateNoConflict(dd);
			}
			
		} catch (PSException e) {
			logger.error(e.getMessage());
		}

	}

	private List<DividendDateModel> fetchDividendDate(Date startDate, int extendDays) throws PSException {
		for(DividendDateSpider spider : spiders) {
			try {
				return spider.fetchEarningDate(startDate, extendDays);
			} catch (PSException e) {
				logger.error("Fail fetching Dividend date: " + e.getMessage());
			}
		}
		throw new PSException("Fail to fetch Dividend date");
	}
}
