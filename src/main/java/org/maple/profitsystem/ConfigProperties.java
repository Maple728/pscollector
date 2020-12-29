package org.maple.profitsystem;

import org.maple.profitsystem.constants.CommonConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ps", ignoreInvalidFields = true)
public class ConfigProperties {
	
	private String backupPath = CommonConstants.DEFAULT_BACKUP_PATH;		// The path of backup file
	
	private Integer statisticsUpdatePeriod = CommonConstants.DEFAULT_STATISTICS_UPDATE_PERIOD;
	
	private Integer quotesUpdatePeriod = CommonConstants.DEFAULT_QUOTES_UPDATE_PERIOD;
	
	private Integer maxThreads = CommonConstants.DEFAULT_MAX_THREADS;

	public Integer getStatisticsUpdatePeriod() {
		return statisticsUpdatePeriod;
	}

	public void setStatisticsUpdatePeriod(Integer statisticsUpdatePeriod) {
		if(statisticsUpdatePeriod != null)
			this.statisticsUpdatePeriod = statisticsUpdatePeriod;
	}

	public Integer getQuotesUpdatePeriod() {
		return quotesUpdatePeriod;
	}

	public void setQuotesUpdatePeriod(Integer quotesUpdatePeriod) {
		if(quotesUpdatePeriod != null)
			this.quotesUpdatePeriod = quotesUpdatePeriod;
	}

	public Integer getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(Integer maxThreads) {
		if(maxThreads != null)		
			this.maxThreads = maxThreads;
	}

	public String getBackupPath() {
		return backupPath;
	}

	public void setBackupPath(String backupPath) {
		this.backupPath = backupPath;
	}
	
	
}
