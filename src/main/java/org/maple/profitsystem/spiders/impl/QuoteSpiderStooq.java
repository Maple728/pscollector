package org.maple.profitsystem.spiders.impl;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.StockQuoteModel;
import org.maple.profitsystem.spiders.QuoteSpider;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class QuoteSpiderStooq implements QuoteSpider{
	

	private static String BASE_URL = "https://stooq.com/q/d/l/?s={symbol}.us&i=d";
	private static String PARAM_DATE_FORMAT = "yyyyMMdd";
	@Override
	public List<StockQuoteModel> fetchQuotes(String symbol, Integer startDt) throws PSException {
		
		try {
			String url = combineTargetUrl(symbol, startDt);
			String content;
			try {
				content = Jsoup.connect(url).ignoreContentType(true).execute().body();
			} catch (Exception e1) {
				throw new HttpException(url, "Jsoup GET", 1, e1.getMessage());
			}
			

			List<StockQuoteModel> result = new ArrayList<>();
			
			// parse all csv records to model
			Integer nowDt = TradingDateUtil.convertDate2NumDate(new Date());
			String[] records = content.split(CommonConstants.CSV_NEWLINE_REG);
			for(int i = 1; i < records.length; ++i) {
				try {
					StockQuoteModel tmp = parseQuoteFromHistoricalCSV(records[i]);
					
					if(tmp.getQuoteDate() < startDt || tmp.getQuoteDate() > nowDt) {
						// skip error record
						continue;
					}
					result.add(tmp);
					
				} catch (Exception e) {
//					logger.warn(symbol + " parse record failed: " + records[i]);
				}
			}
			
			// check if updating the quote failed caused by content error
			if(result.size() == 0) {
				if(TradingDateUtil.betweenDays(TradingDateUtil.convertNumDate2Date(startDt), new Date()) > CommonConstants.MAX_QUOTES_GAP) {
					throw new PSException("no records in content");
				}
			}
			
			return result;
		} catch(Exception e) {
			throw new PSException(symbol + " - Stooq get quote failed: " + e.getMessage());
		}
	}

	private static StockQuoteModel parseQuoteFromHistoricalCSV(String csvRecord) throws PSException {
		final String DATE_FORMAT = "yyyy-MM-dd";
		String[] fields = CSVUtil.splitCSVRecord(csvRecord, ",");
		try{
			StockQuoteModel result = new StockQuoteModel();
			result.setQuoteDate(TradingDateUtil.convertStrDate2NumDate(fields[0], DATE_FORMAT));
			result.setOpen(Double.valueOf(fields[1].trim()));
			result.setHigh(Double.valueOf(fields[2].trim()));
			result.setLow(Double.valueOf(fields[3].trim()));
			result.setClose(Double.valueOf(fields[4].trim()));
			result.setVolume(Double.valueOf(fields[5].trim()).longValue());
			
			return result;
		} catch(Exception e) {
			throw new PSException(e.getMessage());
		}
	}
	
	@SuppressWarnings("deprecation")
	private String combineTargetUrl(String symbol, int startDt) {
		SimpleDateFormat sdf = new SimpleDateFormat(PARAM_DATE_FORMAT, Locale.ENGLISH);
		final int OLDEST_DATE = 20000101;
		// reset startDt
		if(startDt < OLDEST_DATE) {
			startDt = OLDEST_DATE;
		}
		// Date format
		Date startDate = TradingDateUtil.convertNumDate2Date(startDt);
		Date endDate = new Date();
		
		return BASE_URL.replaceAll("\\{symbol\\}", symbol) + "&d1=" + URLEncoder.encode(sdf.format(startDate)) + "&d2=" + URLEncoder.encode(sdf.format(endDate));
		
	}
}
