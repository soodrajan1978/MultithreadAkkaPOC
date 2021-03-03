package com.ibm.cai.employee.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.bind.annotation.ControllerAdvice;


import com.ibm.cai.employee.MVP9HistoricalDataExtractionApplication;

/**
 * Spring Data Rest configuration class.
 * 
 * @auhor rajasood@in.ibm.com
 */
@Configuration
@EntityScan(basePackageClasses = { MVP9HistoricalDataExtractionApplication.class, Jsr310JpaConverters.class })
@ControllerAdvice
public class SpringDataRestConfig extends RepositoryRestConfigurerAdapter {
	/**
	 * Configure RepositoryRestConfiguration
	 * 
	 * @param config
	 */
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
	}


}
