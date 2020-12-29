package org.maple.profitsystem.spiders.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.StockQuoteModel;
import org.maple.profitsystem.spiders.QuoteSpider;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class QuoteSpiderFidelity implements QuoteSpider {
	
//	private static Logger logger = Logger.getLogger(QuoteSpiderFidelity.class);

	private final static String URL_PATTERN = "https://screener.fidelity.com/ftgw/etf/downloadCSV.jhtml?symbol={symbol}";
	
	private final static String QUOTE_DATE_FORMAT = "MM/dd/yyyy";
	
//	private final static int SKIP_LINES_TAIL = 20;
	
	@Override
	public List<StockQuoteModel> fetchQuotes(String symbol, Integer startDt) throws PSException {
		try {
			List<StockQuoteModel> result = new ArrayList<StockQuoteModel>();
			
			String url = combineUrl(symbol);
			String content;
			try {
				content = Jsoup.connect(url).ignoreContentType(true).execute().body();
//				content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
			} catch (Exception e1) {
				throw new HttpException(url, "Jsoup GET", 1, e1.getMessage());
			}
			
			String[] records = content.split(CommonConstants.CSV_NEWLINE_REG);
			Integer nowDt = TradingDateUtil.convertDate2NumDate(new Date());
			StockQuoteModel tmp = null;
			for(int i = records.length; i > 0; --i) {
				try {
					tmp = parseQuoteFromCSV(records[i]);
					if(tmp.getQuoteDate() <= startDt || tmp.getQuoteDate() > nowDt) {
						// filter some error quote
						continue;
					} else {
						result.add(tmp);
					}
					
				} catch (Exception e) {
				}
			}
			
			// check if updating the quote failed caused by content error
			if(result.size() == 0) {
				if(TradingDateUtil.betweenDays(TradingDateUtil.convertNumDate2Date(startDt), new Date()) > CommonConstants.MAX_QUOTES_GAP) {
					throw new PSException("no records in content");
				}
			}
			
			return result;
		} catch (Exception e) {
			throw new PSException(symbol + " - Fidelity get quote failed: " + e.getMessage());
		}
	}
	
	private StockQuoteModel parseQuoteFromCSV(String record) throws Exception {
		try {
			StockQuoteModel result = new StockQuoteModel();
			String[] fields = record.split(",");
			SimpleDateFormat sdf = new SimpleDateFormat(QUOTE_DATE_FORMAT);
			
			result.setQuoteDate(TradingDateUtil.convertDate2NumDate(sdf.parse(fields[0].trim())));
			result.setOpen(Double.valueOf(fields[1].trim()));
			result.setHigh(Double.valueOf(fields[2].trim()));
			result.setLow(Double.valueOf(fields[3].trim()));
			result.setClose(Double.valueOf(fields[4].trim()));
			result.setVolume(Long.valueOf(fields[5].trim()));
			
			return result;
		} catch (Exception e) {
			throw new PSException("Parse record failed: " + e.getMessage());
		}
	}
	
	private String combineUrl(String symbol) {
		return URL_PATTERN.replaceAll("\\{symbol\\}", symbol);
	}

}
