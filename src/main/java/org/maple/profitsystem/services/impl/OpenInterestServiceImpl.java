package org.maple.profitsystem.services.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.maple.profitsystem.mappers.OIModelMapper;
import org.maple.profitsystem.models.OIModel;
import org.maple.profitsystem.services.OpenInterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OpenInterestServiceImpl implements OpenInterestService {
	
	@Autowired
	private OIModelMapper mapper;

	@Override
	public int upsertListOIModel(List<OIModel> oiModelList) {
		int count = 0;
		if (oiModelList != null) {
			// filter oi model
			oiModelList = filterOIModel(oiModelList);
			// upsert oi
			for(OIModel oiModel : oiModelList) {
				count += upsertOIModel(oiModel);
			}
		}
		return count;

	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@Override
	public int upsertOIModel(OIModel oiModel) {
		if(check(oiModel)) {
			try {
				return mapper.upsert(oiModel);
			} catch(Exception e) {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	/**
	 * Check if the oi model is fulled.
	 * @param oiModel
	 * @return
	 */
	private boolean check(OIModel oiModel) {
		if(oiModel != null && oiModel.getCompanyId() != null && oiModel.getCallPut() != null && oiModel.getStrike() != null && oiModel.getExpiration() != null
				&& oiModel.getOiDate() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Keep the most open interest date records, and discard the other.
	 * @param oiModel
	 * @return
	 */
	private List<OIModel> filterOIModel(List<OIModel> oiModelList) {
		// filter oi records
		final int targetEventDay = getMostOIDate(oiModelList);
		return oiModelList.stream().filter( oiModel -> oiModel.getOiDate() >= targetEventDay).collect(Collectors.toList());
	}

	/**
	 * 
	 * @param collection
	 * @return
	 */
	private int getMostOIDate(Collection<OIModel> collection) {
		int maxEventDay = 0;
		Map<Integer, Long> collect = collection.stream().collect(Collectors.groupingBy(OIModel::getOiDate, Collectors.counting()));
		Long maxCount = 0L;
		for(Integer eventDay : collect.keySet()) {
			if(collect.get(eventDay) > maxCount) {
				maxEventDay = eventDay;
				maxCount = collect.get(eventDay);
			}
		}
		return maxEventDay;
	}
}
