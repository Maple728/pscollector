package org.maple.profitsystem.spiders;

import java.util.List;

import org.maple.profitsystem.exceptions.HttpException;
import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;

public interface CompanySpider {
	List<CompanyModel> fetchCompanyList() throws HttpException, PSException;
}
