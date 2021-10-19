package com.rita.playground.springquartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@DisallowConcurrentExecution
public class RestTemplateJob implements Job {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());


	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			logger.info("hello RestTemplate job ");
			JobDataMap data = context.getMergedJobDataMap();
			System.out.println("restUrl = " + data.getString("restUrl"));
			
			
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}
	

	
	
}
