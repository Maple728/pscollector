package org.maple.profitsystem.spiders.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.maple.profitsystem.constants.CommonConstants;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;
import org.maple.profitsystem.models.OIModel;
import org.maple.profitsystem.spiders.OISpider;
import org.maple.profitsystem.utils.HttpRequestUtil;
import org.maple.profitsystem.utils.TradingDateUtil;

public class OISpiderInvestopedia implements OISpider{
	
	private static Logger logger = Logger.getLogger(OISpiderInvestopedia.class);

	private final static String URL_GET_TOKEN = "https://www.investopedia.com/markets/api/token/xignite/encrypted/";
	
	private final static String URL_GET_OPTION_OI = "https://globaloptions.xignite.com/xglobaloptions.csv/GetAllEquityOptionChain?IdentifierType=Symbol&SymbologyType=DTNSymbol&OptionExchange=&Identifier={symbol}";
	
	private final static String PARAM_TOEKN_USERID = "&_token={1}&_token_userid={2}";
	
	private final static String CP_CALL = "Calls";
	
	private final static String CP_PUT = "Puts";
	
	private final static String DATE_FORMAT = "MM/dd/yyyy";
	
	// use by all instances
	private static String token = null; 
	
	@Override
	public List<OIModel> fetchOpenInterest(CompanyModel company) throws PSException {
		int count = 3;
		Set<OIModel> result = new HashSet<>();
		if(token == null) {
			updateTokenAndUserId();
		}
		while(true) {
			try {
				// get calls
				String url = combineUrl(company.getSymbol(), CP_CALL);
				String content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
				result.addAll(parseOIList(content, CP_CALL.charAt(0), company.getId()));
				
				// get puts
				url = combineUrl(company.getSymbol(), CP_PUT);
				content = HttpRequestUtil.getMethod(url, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
				
				result.addAll(parseOIList(content, CP_PUT.charAt(0), company.getId()));
				
				return new ArrayList<>(result);
			} catch (Exception e) {
				// token expired
				logger.info(e.getMessage());
				updateTokenAndUserId();
				--count;
				if(count <= 0) {
					throw new PSException("Fail to get token!");
				}
			}
		}
	}

	private List<OIModel> parseOIList(String content, char callPut, long companyId) throws PSException {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		List<OIModel> result = new ArrayList<>();
		String[] records = content.split(CommonConstants.CSV_NEWLINE_REG);
		for(int i = 1; i < records.length; ++i) {
			String[] fields = records[i].split(",");
			if(fields.length == 9) {
				try {
					OIModel oiModel = new OIModel();
					oiModel.setExpiration(TradingDateUtil.convertDate2NumDate(sdf.parse(fields[0].trim())));
					oiModel.setStrike(Double.valueOf(fields[1].trim()));
					oiModel.setOpen(Double.valueOf(fields[2].trim()));
					oiModel.setClose(Double.valueOf(fields[3].trim()));
					oiModel.setHigh(Double.valueOf(fields[4].trim()));
					oiModel.setLow(Double.valueOf(fields[5].trim()));
					oiModel.setVolume(Integer.valueOf(fields[6].trim()));
					oiModel.setOi(Integer.valueOf(fields[7].trim()));
					oiModel.setOiDate(TradingDateUtil.convertDate2NumDate(sdf.parse(fields[8].trim())));
					oiModel.setCallPut(callPut);
					oiModel.setCompanyId(companyId);
	
					result.add(oiModel);
				} catch(Exception e) {
					logger.warn(e.getMessage() + "|" + records[i]);
				}
			}
		}
		return result;
	}
	
	private String combineUrl(String symbol, String callPut) {
		String fields = "&_fields=Expirations.{cp},Expirations.{cp}.ExpirationDate,Expirations.{cp}.StrikePrice,Expirations.{cp}.Open,Expirations.{cp}.Close,Expirations.{cp}.High,Expirations.{cp}.Low,Expirations.{cp}.Volume,Expirations.{cp}.OpenInterest,Expirations.{cp}.OpenInterestDate";
		return URL_GET_OPTION_OI.replaceAll("\\{symbol\\}", symbol) + token + fields.replaceAll("\\{cp\\}", callPut);
	}
	
	private void updateTokenAndUserId() throws PSException {
		try {
			String content = HttpRequestUtil.getMethod(URL_GET_TOKEN, null, CommonConstants.REQUEST_MAX_RETRY_TIMES);
			JSONObject jsonObj = new JSONObject(content);
			token = PARAM_TOEKN_USERID.replaceAll("\\{1\\}", jsonObj.getString("token")).replaceAll("\\{2\\}", jsonObj.get("userId").toString());
		} catch (Exception e) {
			throw new PSException("Fail to get token: " + e.getMessage());
		}
	}
}
