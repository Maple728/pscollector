package org.maple.profitsystem.spiders.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyStatisticsModel;
import org.maple.profitsystem.spiders.StatisticsSpider;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;

public class StatisticsSpiderMarketWatch implements StatisticsSpider{

	private final static String BASE_URL_PATTERN = "https://www.marketwatch.com/investing/stock/{symbol}";
	
	@Override
	public CompanyStatisticsModel fetchStatistics(String symbol) throws PSException {
		final String SHS_OUTSTANDING_REG = "<small.*?kv__label.*?>Shares Outstanding</small>[\\s\\S]*?kv__primary.*?>(.*?)</span>";
		final String SHS_FLOAT_REG = "<small.*?kv__label.*?>Public Float</small>[\\s\\S]*?kv__primary.*?>(.*?)</span>";
		
		CompanyStatisticsModel result = new CompanyStatisticsModel();
		
		try {
			String url = combineUrl(symbol);
			String content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
			Pattern shsOutstandPat = Pattern.compile(SHS_OUTSTANDING_REG);
			Pattern shsFloatPat = Pattern.compile(SHS_FLOAT_REG);
			
			int count = 0;
			// match shares outstanding
			Matcher tmpMatcher = shsOutstandPat.matcher(content);
			if(tmpMatcher.find()) {
				try {
					result.setShsOutstand(CSVUtil.converDisplayNum2Long(tmpMatcher.group(1)));
					++count;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			// match shares float
			tmpMatcher = shsFloatPat.matcher(content);
			if(tmpMatcher.find()) {
				try {
					result.setShsFloat(CSVUtil.converDisplayNum2Long(tmpMatcher.group(1)));
					++count;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			if(count == 0) {
				throw new PSException("MarketWatch no statistics info: " + symbol);
			}
			return result;
			
		} catch (HttpException e) {
			throw new PSException(symbol + " fetch statistics failed: " + e.getErrorMsg());
		}
	}
	
	private String combineUrl(String symbol) {
		return BASE_URL_PATTERN.replaceAll("\\{symbol\\}", symbol);
	}

}
