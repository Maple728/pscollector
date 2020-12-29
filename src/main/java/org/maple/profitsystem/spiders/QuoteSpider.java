package org.maple.profitsystem.spiders;

import java.util.List;

import org.maple.profitsystem.models.StockQuoteModel;

public interface QuoteSpider {
	/**
	 * 
	 * @param symbol: the symbol of company. 
	 * @param startDt: the date of last existed quote.
	 * @return
	 * @throws Exception
	 */
	List<StockQuoteModel> fetchQuotes(String symbol, Integer startDt) throws Exception;
}
