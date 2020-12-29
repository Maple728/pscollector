package org.maple.profitsystem.utils;

//import org.maple.profitsystem.exceptions.HttpException;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//
//public class SeleniumUtil {
//
//	public static void main(String[] args) throws HttpException {
//		
//		String url = "https://screener.fidelity.com/ftgw/etf/downloadCSV.jhtml?symbol=AAPL";
////		String url = "https://www.nasdaq.com/api/v1/historical/SNAP/stocks/2019-10-01/2019-12-26";
//		String content = HttpRequestUtil.getMethod(url, null, 1);
//		System.out.println(content);
////		SeleniumUtil obj = new SeleniumUtil();
////		System.out.println(obj.getPageContent(url));
//	}
//	
//	public String getPageContent(String url) {
//		System.setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("headless");
//        chromeOptions.addArguments("disable-extensions");
//        chromeOptions.addArguments("Accept:application/text");
//		WebDriver driver = new ChromeDriver(chromeOptions);
//		driver.get(url);
//		String content = driver.getPageSource();
//		return content;
//	}
//}
