package org.maple.profitsystem.services;

import java.util.List;

import org.maple.profitsystem.models.StockQuoteModel;

public interface StockQuoteService {
	
	List<StockQuoteModel> getAllStockQuotesByCompanyId(long companyId);
	
	int addStockQuote(StockQuoteModel record);
	
	int addStockQuoteList(List<StockQuoteModel> records);
	
}
