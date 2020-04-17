package com.example.batchprocessing;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SpringBootApplication
@EnableScheduling
public class BatchProcessingApplication {
  @Autowired
    JobLauncher jobLauncher;
     
    @Autowired
    Job job;
    private static final Logger LOGGER = LogManager.getLogger(BatchProcessingApplication.class.getName());
  public static void main(String[] args) throws Exception {

    LOGGER.info("Init Process !!!");
    SpringApplication.run(BatchProcessingApplication.class, args);




LOGGER.info("Await next Process !!!");

  
  }




  @Scheduled(cron = "${spring.task.expresion}")
  public void perform() throws Exception 
  {

 
      JobParameters params = new JobParametersBuilder()
              .addString("JobID", String.valueOf(System.currentTimeMillis()))
              .toJobParameters();
      jobLauncher.run(job, params);
  }

}