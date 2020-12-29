package org.maple.profitsystem.collector;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.maple.profitsystem.Application;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;
import org.maple.profitsystem.models.EarningDateModel;
import org.maple.profitsystem.services.EarningDateService;
import org.maple.profitsystem.spiders.EarningDateSpider;
import org.maple.profitsystem.spiders.impl.EarningDateSpiderYahoo;
import org.maple.profitsystem.utils.TradingDateUtil;

/**
 * Get earning date of the specified company and update these into database.
 * @author Maple
 *
 */
public class EarningDateUpdateTask implements Runnable {
	
	public static int failCount;

	private static Logger logger = Logger.getLogger(EarningDateUpdateTask.class);
	
	private static List<EarningDateSpider> spiders = new ArrayList<>();
	
	private static EarningDateService earningDateService;
	
	static {
		spiders.add(new EarningDateSpiderYahoo());
		
		// get service bean from spring context
		earningDateService = Application.springContext.getBean(EarningDateService.class);
	}
	
	private CompanyModel company;
	
	public EarningDateUpdateTask(CompanyModel com) {
		this.company = com;
	}
	
	@Override
	public void run() {
		if(company != null) {
			try {
				List<EarningDateModel> earningDateList = fetchEarningDate();
				EarningDateModel lastEarningDate = earningDateService.getLastEarningDateBySymbol(company.getSymbol());
				
				// earning date asc loop
				boolean needUpdate = false;
				if(lastEarningDate == null) {
					needUpdate = true;
				}
				for(EarningDateModel ed : earningDateList) {
					// check if the last earning date be updated
					if(!needUpdate) {
						if(Math.abs(TradingDateUtil.betweenDays(ed.getReportDate(), lastEarningDate.getReportDate())) < CommonConstants.MIN_EARNING_DATE_GAP) {
							needUpdate = true;
							// remove the last earning date
							earningDateService.removeEarningDateById(lastEarningDate.getId());
						}
					}
					// add earning date
					if(needUpdate) {
						earningDateService.addEarningDate(ed);
					}
				}
			} catch (PSException e) {
				logger.error(e.getMessage());
				++failCount;
			}
		}
	}

	private List<EarningDateModel> fetchEarningDate() throws PSException {
		for(EarningDateSpider spider : spiders) {
			try {
				return spider.fetchEarningDate(company.getSymbol());
			} catch (PSException e) {
				logger.error("Fail fetching earning date - " + company.getSymbol() + " : " + e.getMessage());
			}
		}
		throw new PSException("Fail to fetch earning date");
	}
}