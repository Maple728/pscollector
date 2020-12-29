package org.maple.profitsystem.spiders.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.DividendDateModel;
import org.maple.profitsystem.spiders.DividendDateSpider;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class DividendDateSpiderStreet implements DividendDateSpider {

	private final static String BASE_URL_PATTERN = "https://www.thestreet.com/util/divs.jsp?date={date}";
	
	private final static String URL_DATE_FORMAT = "MM_dd_yyyy";
	
	// ------------- Regular Expression --------------------
	private final static String SYMBOL_REG = "symbol:\"(.*?)\"";
	
	@Override
	public List<DividendDateModel> fetchEarningDate(Date startDate, int extendDays) throws PSException {
		Pattern symbolPat = Pattern.compile(SYMBOL_REG);
		
		List<DividendDateModel> result = new ArrayList<>();
		
		for(int i = 0; i < extendDays; ++i) {
			// date process
			Calendar cal = Calendar.getInstance();
			cal.setTime(startDate);
			cal.add(Calendar.DATE, i);
			
			String url = combineUrl(cal.getTime());
			try {
				String content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
				Matcher mat = symbolPat.matcher(content);
				while(mat.find()) {
					// construct dividend model
					DividendDateModel tmpModel = new DividendDateModel();
					tmpModel.setSymbol(mat.group(1));
					tmpModel.setReportDate(TradingDateUtil.convertDate2NumDate(cal.getTime()));
					
					result.add(tmpModel);
				}
			} catch (HttpException e) {
				if(e.getErrorMsg() != null) {
					throw new PSException(cal.getTime() + "TheStreet fetch earning date failed: " + e.getErrorMsg());
				}
			}
		}
		return result;
	}

	private String combineUrl(Date targetDate) {
		DateFormat df = new SimpleDateFormat(URL_DATE_FORMAT, Locale.ENGLISH);
		return BASE_URL_PATTERN.replaceAll("\\{date\\}", df.format(targetDate));
	}
}
