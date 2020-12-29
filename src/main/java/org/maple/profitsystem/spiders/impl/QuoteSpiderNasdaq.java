package org.maple.profitsystem.spiders.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.StockQuoteModel;
import org.maple.profitsystem.spiders.QuoteSpider;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class QuoteSpiderNasdaq implements QuoteSpider{

	// date format: yyyy-MM-dd
	private static String URL_QUOTES_NASDAQ_PATTERN = "https://www.nasdaq.com/api/v1/historical/{SYMBOL}/stocks/{START_DATE}/{END_DATE}";
//	private static Logger logger = Logger.getLogger(QuoteSpiderNasdaq.class);
	
	private static Map<String, String> httpHeaders = null;
	
	static {
		// set headers of http for nasdaq
		httpHeaders = new HashMap<>();
		
		httpHeaders.put("Host", "www.nasdaq.com");
		httpHeaders.put("Origin", "http://www.nasdaq.com");
	}
	
	/**
	 * Fetch the stock quotes newer than all of quotes in CompanyInfoModel.quoteList.
	 * 
	 * @param company
	 * @return List of StockQuoteModel
	 * @throws PSException
	 */
	@Override
	public List<StockQuoteModel> fetchQuotes(String symbol, Integer startDt) throws PSException{
		List<StockQuoteModel> result = null;

//		try {
//			result = fetchHistoricalQuotes(symbol, startDt);
//			fetchHistoricalQuotes
//		} catch (Exception e) {
//		}
		
//		if(result != null) {
//			return result;
//		}
		
		// the other way fetching last period(3 months) quotes from nasdaq
		try {
			result = fetchLastQuotes(symbol, startDt);
		} catch (Exception e) {
			throw new PSException(symbol + " - Nasdaq get quote failed: " + e.getMessage());
		}
		
		return result;
	}
	/**
	 * Get stock last quotes from startDt to current using parsing html.   
	 * @param symbol
	 * @param startDt
	 * @return
	 * @throws HttpException 
	 * @throws PSException 
	 */
	@SuppressWarnings("deprecation")
	private static List<StockQuoteModel> fetchLastQuotes(String symbol, Integer startDt) throws Exception {
		Date today = new Date();
		Date startDate = null;
		if(startDt == 0) {
			//
			startDate = new Date();
			startDate.setYear(today.getYear() - 10);
		} else {
			startDate = TradingDateUtil.convertNumDate2Date(startDt);
		}
		
		String baseUrl = combineHistoricalQuotesUrl(symbol, startDate, today);
		String responseStr = null;
		try {
			responseStr = Jsoup.connect(baseUrl).ignoreContentType(true).execute().body();
//			responseStr = HttpRequestUtil.getMethod(baseUrl, httpHeaders, CommonConstants.REQUEST_MAX_RETRY_TIMES);
		} catch (Exception e1) {
			throw new HttpException(baseUrl, "Jsoup GET", 1, e1.getMessage());
		}
		
		String[] records = responseStr.split(CommonConstants.CSV_NEWLINE_REG);

		List<StockQuoteModel> result = new ArrayList<>();
		for(String record : records) {
			try {
				result.add(parseQuoteFromHistoricalCSV(record));
			} catch(Exception e) {
				// jump the error line
			}
		}
		// check if updating the quote failed caused by content error
		if(result.size() == 0) {
			if(TradingDateUtil.betweenDays(TradingDateUtil.convertNumDate2Date(startDt), new Date()) > CommonConstants.MAX_QUOTES_GAP) {
				throw new PSException("no records in content");
			}
		}
		return result;
	}
	
	private static StockQuoteModel parseQuoteFromHistoricalCSV(String csvRecord) throws PSException {
		final String DATE_FORMAT = "MM/dd/yyyy";
		String[] fields = CSVUtil.splitCSVRecord(csvRecord, ",");
		try{
			StockQuoteModel result = new StockQuoteModel();
			result.setQuoteDate(TradingDateUtil.convertStrDate2NumDate(fields[0], DATE_FORMAT));
			result.setClose(Double.valueOf(fields[1].replace("$", "")));
			result.setVolume(Double.valueOf(fields[2]).longValue());
			result.setOpen(Double.valueOf(fields[3].replace("$", "")));
			result.setHigh(Double.valueOf(fields[4].replace("$", "")));
			result.setLow(Double.valueOf(fields[5].replace("$", "")));
			
			return result;
		} catch(Exception e) {
			throw new PSException(e.getMessage());
		}
	}	
	
	/**
	 * Combine the url to fetch historical quotes of the symbol specified stock.
	 * 
	 * @param symbol
	 * @return
	 */
	private static String combineHistoricalQuotesUrl(String symbol, Date startDate, Date endDate) {
		final String DATE_FORMAT = "yyyy-MM-dd";
		String startDt = TradingDateUtil.convertDate2StrDate(startDate, DATE_FORMAT);
		String endDt = TradingDateUtil.convertDate2StrDate(endDate, DATE_FORMAT);
		return URL_QUOTES_NASDAQ_PATTERN.replace("{SYMBOL}", symbol.toUpperCase()).replace("{START_DATE}", startDt).replace("{END_DATE}", endDt);
	}
}
