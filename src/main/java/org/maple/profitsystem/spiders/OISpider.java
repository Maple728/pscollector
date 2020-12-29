package org.maple.profitsystem.spiders;

import java.util.List;

import org.maple.profitsystem.exceptions.PSException;
import org.maple.profitsystem.models.CompanyModel;
import org.maple.profitsystem.models.OIModel;

public interface OISpider {

	List<OIModel> fetchOpenInterest(CompanyModel company) throws PSException;
}
