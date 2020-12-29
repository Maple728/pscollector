package org.maple.profitsystem.spiders.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyStatisticsModel;
import org.maple.profitsystem.spiders.StatisticsSpider;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;

public class StatisticsSpiderAdvfn implements StatisticsSpider {
	
	private static Logger logger = Logger.getLogger(StatisticsSpiderAdvfn.class);
	
	private final static String BASE_URL_PATTERN = "https://www.advfn.com/stock-market/NYSE/{symbol}/financials";
	
	@Override
	public CompanyStatisticsModel fetchStatistics(String symbol) throws PSException {
		final String SHS_OUTSTANDING_REG = ">Latest Shares Outstanding</td>[\\s\\S]*?>(.*?)</td>[\\s\\S]*?>(.*?)</td>\\s*?</tr>";
		final String SHS_FLOAT_REG = ">Float</td>[\\s\\S]*?>(.*?)</td>[\\s\\S]*?>(.*?)</td>\\s*?</tr>";
		

		CompanyStatisticsModel result = new CompanyStatisticsModel();
		try {
			String url = combineUrl(symbol);
			String content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
			
			Pattern shsOutstandPtn = Pattern.compile(SHS_OUTSTANDING_REG);
			Pattern shsFloatPtn = Pattern.compile(SHS_FLOAT_REG);
			
			int count = 0;
			// match shares outstanding
			Matcher tmpMatcher = shsOutstandPtn.matcher(content);
			if(tmpMatcher.find()) {
				try {
					result.setShsOutstand(CSVUtil.converDisplayNum2Long(tmpMatcher.group(1).trim() + tmpMatcher.group(2).substring(0, 1)));
					++count;
				} catch(Exception e) {
					logger.warn(e.getMessage());
				}
			}
			
			// match float shares
			tmpMatcher = shsFloatPtn.matcher(content);
			if(tmpMatcher.find()) {
				try {
					result.setShsFloat((CSVUtil.converDisplayNum2Long(tmpMatcher.group(1).trim()  + tmpMatcher.group(2).substring(0, 1))));
					++count;
				} catch(Exception e) {
					logger.warn(e.getMessage());
				}
			}
			
			if(count == 0) {
				throw new PSException("Advfn no statistics info: " + symbol);
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
