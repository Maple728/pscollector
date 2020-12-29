package org.maple.profitsystem.mappers;

import java.util.List;

import org.maple.profitsystem.models.CompanyModel;

public interface CompanyModelMapper {
    int insert(CompanyModel record);

    int insertSelective(CompanyModel record);

    int updateByPrimaryKeySelective(CompanyModel record);

    int updateByPrimaryKey(CompanyModel record);
    
    /**
     * Select all company model with base info
     * @return
     */
    List<CompanyModel> selectAll();
    
    List<CompanyModel> selectAllWithStatistics();
    
    List<CompanyModel> selectAllFull();
    
    CompanyModel selectFullById(long id);
    
    // deprecated
    //int insertList(List<CompanyModel> records);
}