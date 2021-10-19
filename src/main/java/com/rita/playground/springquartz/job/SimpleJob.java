package com.rita.playground.springquartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import com.rita.playground.springquartz.config.ConfigureQuartz;

@Component
@DisallowConcurrentExecution
public class SimpleJob implements Job {
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

//	@Value("?/10 * * * * ?")
	private String frequency = "*/10 * * * * ?";

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		try {
			logger.info("hello simple job ");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
		}
	}

	@Bean(name = "SimpleJobTriggerBean")
	public JobDetailFactoryBean sampleJob() {
		return ConfigureQuartz.createJobDetail(this.getClass());
	}

	@Bean(name = "SimpleJobTrigger")
	public CronTriggerFactoryBean sampleJobTrigger(
			@Qualifier("SimpleJobTriggerBean") JobDetailFactoryBean jdfb) {
		return ConfigureQuartz.createCronTrigger(jdfb.getObject(), frequency);
	}
	
	@Bean(name = "SimpleJobTrigger2")
	public CronTriggerFactoryBean sampleJobTrigger2(
			@Qualifier("SimpleJobTriggerBean") JobDetailFactoryBean jdfb) {
		return ConfigureQuartz.createCronTrigger(jdfb.getObject(), "*/5 * * * * ?");
	}
	
}
