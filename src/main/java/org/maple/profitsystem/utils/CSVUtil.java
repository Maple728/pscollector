package org.maple.profitsystem.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.maple.profitsystem.constants.CommonConstants;

public class CSVUtil {
	
	public static String stripCSVField(String rawField) {
		if(rawField == null)
			return null;
		
		String trimStr = rawField.trim();
		if(trimStr.equals(""))
			return null;
		
		int lIndex = 0;
		int rIndex = trimStr.length() - 1;
		if(trimStr.charAt(lIndex) == CommonConstants.CSV_SURROUNDER_OF_FIELD.charAt(0)) {
			++lIndex;
		}
		if(trimStr.charAt(rIndex) != CommonConstants.CSV_SURROUNDER_OF_FIELD.charAt(0)) {
			++rIndex;
		}
		if(lIndex >= rIndex)
			return null;
		else
			return trimStr.substring(lIndex, rIndex);
	}
	
	public static String[] splitCSVListRecord(String records) {
		return records.split(CommonConstants.CSV_NEWLINE_REG);
	}
	
	public static String[] splitCSVRecord(String csvRecord, String separator) {
		String[] tmpfields = csvRecord.split(separator);
		String[] results = new String[tmpfields.length];
		for(int i = 0; i < tmpfields.length; ++i) {
			results[i] = stripCSVField(tmpfields[i]);
		}
		return results;
	}
	
	public static String readFileContent(File file) {
		BufferedReader bf = null;
		StringBuffer result = new StringBuffer();
		try {
			bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			char[] buffer = new char[CommonConstants.BUFFER_SIZE_OF_READER];
			int countOnce = 0;
			while((countOnce = bf.read(buffer)) != -1) {
				result.append(buffer, 0, countOnce);
			}
			return result.toString();
		} catch (Exception e) {
			return result.toString();
		} finally {
			if(bf != null) {
				try {
					bf.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Convert string percent like 1.8%, -98,4% to Double percent like 1.8, -98.4.
	 *  
	 * @param perc
	 * @return
	 */
	public static Double convertStringPerc2DoublePerc(String perc) {
		try {
			return Double.valueOf(perc.substring(0, perc.length() - 1));
		} catch(Exception e) {
			return null;
		}
	}

	/**
	 * Convert display number(for example: 1.8B, 85M) to Integer number.
	 * 
	 * @param dispNum
	 * @return
	 */
	public static Long converDisplayNum2Long(String dispNum) {
		try{
			dispNum = dispNum.replaceAll(",", "");
			double number = Double.valueOf(dispNum.substring(0, dispNum.length() - 1));
			long multiplier = 0;
			switch(Character.toUpperCase(dispNum.charAt(dispNum.length() - 1))) {
			case 'K':
				multiplier = 1000;
				break;
			case 'M':
				multiplier = 1000000;
				break;
			case 'B':
				multiplier = 1000000000;
				break;
			}
			return (long) (number * multiplier);
		} catch(Exception e) {
			return null;
		}
	}
}
