package org.maple.profitsystem.spiders.impl;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.EarningDateModel;
import org.maple.profitsystem.spiders.EarningDateSpider;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class EarningDateSpiderYahoo implements EarningDateSpider {

	private final static String BASE_URL_PATTERN = "https://finance.yahoo.com/calendar/earnings/?symbol={symbol}";
	
	// Regular expression
	private final static String DATA_TABLE_REG = "<table.*?data-table.*?>([\\s\\S]*?)</table>";
	private final static String DATA_ROW_REG = "<tr.*?data-row.*?>([\\s\\S]*?)</tr>";
	private final static String EARNING_ROW_REG = "<td.*?data-col2.*?>[\\s\\S]*?<span.*?>([\\s\\S]*?)</span>";
	
	@Override
	public List<EarningDateModel> fetchEarningDate(String symbol) throws PSException {
		try {
			String url = combineUrl(symbol);
			String content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
			Pattern dataTablePat = Pattern.compile(DATA_TABLE_REG);
			Pattern dataRowPat = Pattern.compile(DATA_ROW_REG);
			
			List<EarningDateModel> result = new ArrayList<>();
			
			// find data table
			Matcher tmpMatcher = dataTablePat.matcher(content);
			if(tmpMatcher.find()) {
				String dataTable = tmpMatcher.group(1);
				// find data row
				tmpMatcher = dataRowPat.matcher(dataTable);
				while(tmpMatcher.find()) {
					EarningDateModel tmp = parseEarningDate(symbol, tmpMatcher.group(1));
					if(tmp != null) {
						result.add(tmp);
					}
				}
			}
			// reverse list to fit asc order
			Collections.reverse(result);
			return result;
			
		} catch (HttpException e) {
			throw new PSException(symbol + " Yahoo fetch earning date failed: " + e.getErrorMsg());
		}
	}
	
	/**
	 * Parse a text record of earning date to java model.
	 * @param symbol
	 * @param record
	 * @return null if fail.
	 */
	private EarningDateModel parseEarningDate(String symbol, String record) {
		Pattern earningRowPat = Pattern.compile(EARNING_ROW_REG);
		Matcher matcher = earningRowPat.matcher(record);
		if(matcher.find()) {
			String earningDate = matcher.group(1);
			DateFormat df = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
			int splitIndex = earningDate.lastIndexOf(",");
			try {
				EarningDateModel result = new EarningDateModel();
				result.setSymbol(symbol);
				
				result.setReportDate(TradingDateUtil.convertDate2NumDate(df.parse(earningDate.substring(0, splitIndex))));
				
				if(earningDate.substring(splitIndex).contains("PM")) {
					result.setTime(CommonConstants.EARNING_TIME_AMC);
				} else {
					result.setTime(CommonConstants.EARNING_TIME_BMO);
				}
				
				return result;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private String combineUrl(String symbol) {
		return BASE_URL_PATTERN.replaceAll("\\{symbol\\}", symbol);
	}

}
