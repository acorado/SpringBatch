package com.example.batchprocessing;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component

public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

  private static final Logger LOGGER = LogManager.getLogger(JobCompletionNotificationListener.class.getName());

  public int contador;

  @Value("${spring.url.elastic.user}")
  public String user;

  @Value("${spring.url.elastic.psw}")
  public String pass;

  @Value("${spring.url.validator.day}")
  public int validacion;

  @Value("${spring.url.elastic}")
  public String url;


  @Value("${spring.name.index_new}")
  public String nameindex;





/*
  private final JdbcTemplate jdbcTemplate;





  @Autowired
  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }
  */



@Override

public void afterJob(JobExecution jobExecution) {
  // TODO Auto-generated method stub
  super.beforeJob(jobExecution);

  if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
    System.out.println("!!! JOB FINISHED! Time to verify the results");

    LOGGER.info("!!! JOB FINISHED! Time to verify the results");
    

  }

  try {
      
  

  if(day==validacion)
  {

  


      int exchange=0;
      String numbermonth="";
      if(month==1)
      {
        exchange=12;
        year=year-1;
      
      }
      
      if(month>1)
      {
        exchange=month-1;
      }
      
      if(exchange>1 && exchange<10)
      {
        numbermonth="0"+exchange;
      
      }
      
      if(exchange>10)
      {
        numbermonth=String.valueOf(exchange);
      }
      
      
      System.out.println(numbermonth);
      






    RestTemplate restTemplate = new RestTemplate();
    System.out.println("enviar peticion");
    LOGGER.info("*************************************************************************Enviando Petición**************************************************************************");
    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    LOGGER.info("*************************************************************************Obteniendo Hits**************************************************************************");
    String plainCreds = user+":"+pass;
    byte[] plainCredsBytes = plainCreds.getBytes();
    String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);
    int retro=month-1;
    String uri = url+"/"+nameindex+"-"+year+"."+numbermonth+".*/_search";
    
    // set headers
    HttpHeaders headers2 = new HttpHeaders();
    headers2.setContentType(MediaType.APPLICATION_JSON);
    headers2.set("Authorization", "Basic " + base64Creds);

    HttpEntity<String> entity = new HttpEntity<String>(headers2);
    
    // send request and parse result
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
    
    String data =response.getBody();
    
    JSONObject json = new JSONObject(data);
    JSONObject j=json.getJSONObject("hits");
    JSONObject k=j.getJSONObject("total");
    int value=k.getInt("value");
    //System.out.println(value);
    
  
    LOGGER.info("HITS :"+value);


    ScheduledTasks sch= new ScheduledTasks();
    sch.reportCurrentTime(year,numbermonth,value,user,pass,url,nameindex);


  }
else{
  LOGGER.info("Hoy no es el dia programado");
  LOGGER.info("*********************************************************************************************************************************************************************");
}
} catch (Exception e) {
LOGGER.warn(e);
}


}



Date date = new Date();
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
public int month = localDate.getMonthValue();
public int day=localDate.getDayOfMonth();
public int year=localDate.getYear();







  @Override
  public void beforeJob(JobExecution jobExecution) {

  
try {
  

    if(day==validacion)
    {
 
    System.out.println(jobExecution.getJobId());

    LOGGER.info("****************************************INICIO***********************************");
    RestTemplate restTemplate = new RestTemplate();
    LOGGER.info("****************************************Creando indice***********************************");

    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
int exchange=0;
String numbermonth="";
if(month==1)
{
  exchange=12;
  year=year-1;

}

if(month>1)
{
  exchange=month-1;
}

if(exchange>1 && exchange<10)
{
  numbermonth="0"+exchange;

}

if(exchange>10)
{
  numbermonth=String.valueOf(exchange);
}


System.out.println(numbermonth);



    System.out.println("el mes es:"+month);
    LOGGER.info("****************************************El indice a crear: "+nameindex+"-"+year+"."+numbermonth+"***********************************");
    LOGGER.info("****************************************Creando: "+nameindex+"-"+year+"."+numbermonth+"***********************************");

    
    String plainCreds = user+":"+pass;
    byte[] plainCredsBytes = plainCreds.getBytes();
    String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);
    String uri = url+"/"+nameindex+"-"+year+"."+numbermonth;
    
    // set headers
    HttpHeaders headers2 = new HttpHeaders();
    headers2.setContentType(MediaType.APPLICATION_JSON);
    headers2.set("Authorization", "Basic " + base64Creds);
    String input = "{ \"settings\" : { \"index\" : { \"number_of_shards\" : 1  } } }";
    LOGGER.info(input);
    HttpEntity<String> entity = new HttpEntity<String>(input,headers2);
    
    // send request and parse result
    ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
    LOGGER.info(response);
    String data =response.getBody();
    int status=response.getStatusCodeValue();
  
 LOGGER.info("codigo: "+status+" Response: "+data);
    }
else
  {
    System.out.println("Hoy no se creo indice");
    LOGGER.info("Hoy no es la fecha programada");
  }

} catch (Exception e) {
LOGGER.warn(e);
}

  }
}