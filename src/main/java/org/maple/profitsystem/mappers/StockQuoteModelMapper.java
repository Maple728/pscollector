package org.maple.profitsystem.mappers;

import java.util.List;

import org.maple.profitsystem.models.StockQuoteModel;

public interface StockQuoteModelMapper {

    int insert(StockQuoteModel record);

    int insertSelective(StockQuoteModel record);
    
    List<StockQuoteModel> selectListByCompanyId(long companyId);
    
    // deprecated causing by insert sql is too large
    // int insertList(List<StockQuoteModel> records);
}