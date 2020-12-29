/**
 * 
 */
/**
 * @author SEELE
 *
 */
package org.maple.profitsystem.spiders.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;
import org.maple.profitsystem.spiders.CompanySpider;
import org.maple.profitsystem.utils.CSVUtil;

public class CompanySpiderNasdaq implements CompanySpider {
	
	public final static String URL_GET_COMPANY_LIST_NASDAQ = "https://www.nasdaq.com/screening/companies-by-industry.aspx?render=download";

	private static Logger logger = Logger.getLogger(CompanySpiderNasdaq.class);
	
	private static Map<String, String> httpHeaders = null;
	
	private final static String STOCK_SYMBOL_REG = "[^$.^]*";
	
	static {
		// set headers of http for nasdaq
		httpHeaders = new HashMap<>();
		
		httpHeaders.put("Host", "www.nasdaq.com");
		httpHeaders.put("Origin", "http://www.nasdaq.com");
	}
	
	@Override
	public List<CompanyModel> fetchCompanyList() throws HttpException {
		List<CompanyModel> result = new ArrayList<>();
		
		String response = null;
		try {
			response = Jsoup.connect(URL_GET_COMPANY_LIST_NASDAQ).ignoreContentType(true).execute().body();
		} catch (Exception e1) {
			throw new HttpException(URL_GET_COMPANY_LIST_NASDAQ, "Jsoup GET", 1, e1.getMessage());
		}
		
		String[] lines = response.split(CommonConstants.CSV_NEWLINE_REG);
		for(int i = 1; i < lines.length; ++i) {
			try{
				result.add(parseCompanyModel(lines[i]));
			} catch(Exception e) {
				// This company is which for nasdaq test or had been bankrupted.
				logger.info("Invalid company: " + lines[i]);
			}
		}
		// filter invalid company
		return result.stream()
				.filter(company -> company.getSymbol().matches(STOCK_SYMBOL_REG) )
				.collect(Collectors.toList());
	}
	
	private CompanyModel parseCompanyModel(String csv) throws PSException {
		String[] fields = CSVUtil.splitCSVRecord(csv, "\",\"");

		try {
			CompanyModel result = new CompanyModel();
			
			result.setSymbol(fields[0]);
			result.setName(fields[1]);
			try{
				result.setIpoYear(Integer.valueOf(fields[5]));
			} catch(NumberFormatException e){
				result.setIpoYear(0);
			}
			
			result.setSector(fields[6]);
			result.setIndustry(fields[7]);
			
			return result;
		} catch(Exception e) {
			throw new PSException(e.getMessage());
		}
	}
	
}