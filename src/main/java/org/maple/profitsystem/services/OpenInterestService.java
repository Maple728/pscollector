package org.maple.profitsystem.services;

import java.util.List;

import org.maple.profitsystem.models.OIModel;

public interface OpenInterestService {
	
	int upsertOIModel(OIModel oiModel);
	
	int upsertListOIModel(List<OIModel> oiModelList);
}
