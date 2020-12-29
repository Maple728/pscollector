package org.maple.profitsystem.mappers;

import org.maple.profitsystem.models.OIModel;

public interface OIModelMapper {

	int upsert(OIModel record);
}
