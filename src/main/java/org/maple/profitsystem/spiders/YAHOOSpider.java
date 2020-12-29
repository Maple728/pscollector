package org.maple.profitsystem.spiders;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyStatisticsModel;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;

public class YAHOOSpider {
	
	private final static int MAX_RETRY_TIMES = 2;
	
	// -------------- Regular Expression of fields in Statistics ------------------------------
	private final static String SHS_OUTSTAND_REG = ">Shares Outstanding</span>.*?<td.*?>(.*?)</td>";
	
	private final static String SHS_FLOAT_REG = ">Float</span>.*?<td.*?>(.*?)</td>";
	
	private final static String INSIDER_OWN_REG = ">% Held by Insiders</span>.*?<td.*?>(.*?)</td>";
	
	private final static String INST_OWN_REG = ">% Held by Institutions</span>.*?<td.*?>(.*?)</td>";
	
	public static CompanyStatisticsModel fetchCompanyStatistics(String symbol) throws HttpException, PSException {
		CompanyStatisticsModel result = new CompanyStatisticsModel();
		
		String response = HttpRequestUtil.getMethod(getURLOfCompanyStatistics(symbol), null, MAX_RETRY_TIMES);
		
		Pattern insiderOwnPat = Pattern.compile(INSIDER_OWN_REG);
		Pattern instOwnPat = Pattern.compile(INST_OWN_REG);
		Pattern shsFloatPat = Pattern.compile(SHS_FLOAT_REG);
		Pattern shsOutstandPat = Pattern.compile(SHS_OUTSTAND_REG);
		
		int count = 0;
		// match insider ownership
		Matcher tmpMatcher = insiderOwnPat.matcher(response);
		if(tmpMatcher.find()) {
			result.setInsiderOwnPerc(CSVUtil.convertStringPerc2DoublePerc(tmpMatcher.group(1)));
			++count;
		}
		
		// match institutional ownership
		tmpMatcher = instOwnPat.matcher(response);
		if(tmpMatcher.find()) {
			result.setInstOwnPerc(CSVUtil.convertStringPerc2DoublePerc(tmpMatcher.group(1)));
			++count;
		}
		
		// match shares outstanding
		tmpMatcher = shsOutstandPat.matcher(response);
		if(tmpMatcher.find()) {
			result.setShsOutstand(CSVUtil.converDisplayNum2Long(tmpMatcher.group(1)));
			++count;
		}
		
		// match float shares 
		tmpMatcher = shsFloatPat.matcher(response);
		if(tmpMatcher.find()) {
			result.setShsFloat(CSVUtil.converDisplayNum2Long(tmpMatcher.group(1)));
			++count;
		}
		
		if(count == 0) {
			throw new PSException("No statistics info: " + symbol);
		}
		return result;
	}

	
	private static String getURLOfCompanyStatistics(String symbol) {
		return String.format(CommonConstants.URL_GET_COMPANY_STATISTICS_YAHOO, symbol, symbol);
	}
}
