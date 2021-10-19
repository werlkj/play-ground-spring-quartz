package com.rita.playground.springquartz.controller;

import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rita.playground.springquartz.job.RestTemplateJob;


@RestController
public class JobController {
	private static Logger logger = LoggerFactory.getLogger(JobController.class);

	
	@Autowired
	private Scheduler scheduler;
	
	@GetMapping(value = "schedulerInfo")
	public void listScheduler() throws Exception {
		System.out.println(scheduler.getSchedulerName());
		
		System.out.println();
		for(String t: scheduler.getJobGroupNames()) {
			System.out.println("job group name = "+t);
		}
		System.out.println();
		for(String group: scheduler.getJobGroupNames()) {
		    // enumerate each job in group
		    for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.groupEquals(group))) {
		        System.out.println("Found JobKey identified by: " + jobKey);
		    }
		}
		
		System.out.println();
		for(String t: scheduler.getTriggerGroupNames()) {
			System.out.println("Trigger group name = "+t);
		}
		
		System.out.println();
		for(String group: scheduler.getTriggerGroupNames()) {
		    // enumerate each job in group
		    for(TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.groupEquals(group))) {
		        System.out.println("Found TriggerKey identified by: " + triggerKey + ", " + triggerKey.getName());
		        
		        TriggerState triggerState = scheduler.getTriggerState(triggerKey);
		        System.out.println("TriggerState: "+ triggerState);
		        
		        Trigger trigger = scheduler.getTrigger(triggerKey);
		        if (trigger instanceof CronTrigger) {
		            CronTrigger cronTrigger = (CronTrigger) trigger;
		            String cronExpr = cronTrigger.getCronExpression();
		            System.out.println("cronExpr: "+ cronExpr);
		        }
		        System.out.println();
		    }
		}
		
		System.out.println();
	}
	
	@GetMapping(value = "fireTemplateJob")
	public void fireTemplateJob() throws Exception {
		defineJobInstance();
	}
	
	

	/** 單次發動 **/
	public void defineJobInstance() throws Exception {
		// Define job instance
		JobDetail job = JobBuilder.newJob(RestTemplateJob.class)
			    .withIdentity("job1", "group1")
			    .usingJobData("restUrl", "/querySku")
			    .build();
		// Define a Trigger that will fire "now", and not repeat
		Trigger trigger = TriggerBuilder.newTrigger()
		    .withIdentity("trigger1", "group1")
		    .startNow()
		    .build();
		

		// Schedule the job with the trigger
		scheduler.scheduleJob(job, trigger);
		
	}
	

	public void unscheduler() throws Exception {
		// Unschedule a particular trigger from the job (a job may have more than one trigger)
		scheduler.unscheduleJob(new TriggerKey("trigger1", "group1"));
		
		// Schedule the job with the trigger
		scheduler.deleteJob(new JobKey("job1", "group1"));
		
	}
	
	
	@GetMapping(value = "storeJob")
	public void storeJobEntry() throws Exception {
		storeJob();
		storeJob();
	}
	
	@GetMapping(value = "settingTrigger")
	public void settingTrigger() throws Exception {
		storeJob();
		trigger();
	}
	
	
	
	
	/** Storing a Job for Later Use **/
	public void storeJob() throws Exception {
		// Define a durable job instance (durable jobs can exist without triggers)
		JobDetail job = JobBuilder.newJob(RestTemplateJob.class)
		    .withIdentity("job2", "group1")
		    .usingJobData("restUrl", "/createReport")
		    .storeDurably()
		    .build();

		// Add the the job to the scheduler's store
		// 重複設定會報錯
//		scheduler.addJob(job, false);
		// 重複設定會自動覆蓋
		scheduler.addJob(job, true);
	}
	
	/** Scheduling an already stored job **/
	public void trigger() throws Exception {
		// Define a Trigger that will fire "now" and associate it with the existing job
		Trigger trigger = TriggerBuilder.newTrigger()
		    .withIdentity("trigger2", "group1")
		    .startNow()
		    .forJob(new JobKey("job2", "group1"))
		    .build();

		// Schedule the trigger
		scheduler.scheduleJob(trigger);
	}
	
	
}
