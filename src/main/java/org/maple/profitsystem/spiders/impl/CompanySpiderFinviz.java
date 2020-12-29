package org.maple.profitsystem.spiders.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;
import org.maple.profitsystem.spiders.CompanySpider;
import org.maple.profitsystem.utils.CSVUtil;
import org.maple.profitsystem.utils.HttpRequestUtil;

public class CompanySpiderFinviz implements CompanySpider {
	
	private static Logger logger = Logger.getLogger(CompanySpiderFinviz.class);

	private final static String URL_PATTERN = "https://finviz.com/screener.ashx?v=110&o=-ipodate&r={startIndex}";
	
	private final static int RECORDS_OF_A_PAGE = 20;
	@Override
	public List<CompanyModel> fetchCompanyList() throws PSException{
		final String TOTAL_REG = "Total:.*?</b>(.*?)#";
		final String TABLE_REG = "<tr[^<>]*?center[^<>]*?middle[^<>]*?>[\\s\\S]*?(<tr[\\s\\S]*?)</table>";
		
		try {
			List<CompanyModel> result = new ArrayList<>();
			int startIndex = 1;
			// TODO: setting total means that just update 40 newest companies to avoid updating too slow.
			int total = 400;
			
			String url = null;
			String content = null;
			do {
				url = combineUrl(startIndex);
				content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
				if(total == 0) {
					// get total companies number
					Pattern totalPtn = Pattern.compile(TOTAL_REG);
					Matcher totalMat = totalPtn.matcher(content);
					if(!totalMat.find()) {
						throw new PSException("Finviz can not get total companies count!");
					}
					total = Integer.valueOf(totalMat.group(1).trim());
				}
				
				Pattern r = Pattern.compile(TABLE_REG);
				Matcher m = r.matcher(content);
				
				if(!m.find()) {
					throw new PSException("Finviz companies: content error!");
				}
				// parse html table to csv
				String csv = m.group(1).replaceAll("body=\\[.*?\\]", "");
				csv = csv.replaceAll("<tr.*?>\\s*<td.*?>", "");
				csv = csv.replaceAll("</td>\\s*?<td.*?>", CommonConstants.CSV_SEPRATOR_BETWEEN_FIELD);
				csv = csv.replaceAll("\r?\n", "");
				csv = csv.replaceAll("</td>\\s*</tr>", CommonConstants.CSV_NEWLINE);
				csv = csv.replaceAll("<[^<>]*>", "");
				
				String[] records = CSVUtil.splitCSVListRecord(csv);
				for(String record : records) {
					try {
						result.add(parseFromHtmlCSV(record));
					} catch (Exception e) {
						logger.warn(record + ": " + e.getMessage());
					}
				}
				
				startIndex += RECORDS_OF_A_PAGE;
			} while(startIndex <= total);
			return result;
			
		} catch(Exception e) {
			throw new PSException(e.getMessage());
		}

	}
	
	private CompanyModel parseFromHtmlCSV(String csvRecord) throws Exception {
		String[] fields = CSVUtil.splitCSVRecord(csvRecord, "\",\"");
		try{
			CompanyModel result = new CompanyModel();
			result.setSymbol(fields[1].trim());
			result.setName(fields[2].trim());
			result.setSector(fields[3].trim());
			result.setIndustry(fields[4].trim());
			
			return result;
		} catch(Exception e) {
			throw e;
		}		
	}
	
	
	private static String combineUrl(int startIndex) {
		return URL_PATTERN.replaceAll("\\{startIndex\\}", String.valueOf(startIndex));
	}

}
