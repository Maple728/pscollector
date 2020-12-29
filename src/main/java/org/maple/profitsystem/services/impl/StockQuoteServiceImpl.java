package org.maple.profitsystem.services.impl;

import java.util.List;

import org.maple.profitsystem.mappers.StockQuoteModelMapper;
import org.maple.profitsystem.models.StockQuoteModel;
import org.maple.profitsystem.services.StockQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(isolation = Isolation.READ_COMMITTED)
public class StockQuoteServiceImpl implements StockQuoteService {

	@Autowired
	private StockQuoteModelMapper stockQuoteModelMapper;
	
	@Override
	public List<StockQuoteModel> getAllStockQuotesByCompanyId(long companyId) {
		return stockQuoteModelMapper.selectListByCompanyId(companyId);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public int addStockQuote(StockQuoteModel record) {
		if(null == record) {
			return 0;
		} else {
			return stockQuoteModelMapper.insert(record);
		}
	}

	@Override
	public int addStockQuoteList(List<StockQuoteModel> records) {
		if(records == null || records.isEmpty()) {
			return 0;
		} else {
			int count = 0;
			for(StockQuoteModel record : records) {
				count += addStockQuote(record);
			}
			return count;
		}
	}

}
