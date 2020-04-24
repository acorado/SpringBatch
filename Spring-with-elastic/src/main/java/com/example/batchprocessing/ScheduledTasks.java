package com.example.batchprocessing;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;



import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import org.json.JSONObject;

@Component
public class ScheduledTasks {

	private static final Logger LOGGER = LogManager.getLogger(ScheduledTasks.class.getName());


	Date date = new Date();
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
public int month = localDate.getMonthValue();
public int day=localDate.getDayOfMonth();
public int year=localDate.getYear();


	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	public void reportCurrentTime(int y ,String m, int val,String user, String pass, String url, String nameindex, boolean param) {
		LOGGER.info("The time is now {}"+ dateFormat.format(new Date()));

	try {
		
	RestTemplate restTemplate = new RestTemplate();
		System.out.println("Reindexar");
		LOGGER.info("el nombre del indice es "+nameindex);
		LOGGER.info("*******************************************************Reindexando ***********************************************************");
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		
		String plainCreds = user+":"+pass;
		byte[] plainCredsBytes = plainCreds.getBytes();
		String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);
		String uri = url+"/_reindex";

		LOGGER.info(uri);
		
		// set headers
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.set("Authorization", "Basic " + base64Creds);
	
		Object input = "{\"source\": {\"index\": \""+nameindex+"-"+y+"."+m+".*\"},\"dest\": {\"index\": \""+nameindex+"-"+y+"."+m+"\", \"op_type\": \"create\"}}";

		LOGGER.info(input);
		HttpEntity<Object> entity = new HttpEntity<Object>(input,headers2);


		LOGGER.info("*******************************************************Enviando***********************************************************");
		// send request and parse resultcs
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
		
	String data =response.getBody();
	LOGGER.info("*******************************************************Respuesta***********************************************************");
	LOGGER.info(data);
		JSONObject json = new JSONObject(data);
		int created=json.getInt("created");
	
		LOGGER.info("Hits Created: "+created);

int descuadre=val-created;
		LOGGER.info("Descuadre de: "+descuadre);

		if(descuadre==0)
		{
ScheduleDelete sched= new ScheduleDelete();
	sched.delleteAll(nameindex, y, m,user,pass,url,param);
	LOGGER.info("Reindexado exitoso**********************");
		}
		
int code=response.getStatusCodeValue();
if(code==200)
{
	LOGGER.info("Reindexado realizado*******************************");
}
else{
	LOGGER.info("Error en proceso de Reindexado");
}

	} catch (Exception e) {
		LOGGER.warn("el error es"+e);
		LOGGER.warn("Error en proceso de Reindexado "+e);
	}
		

	}


	
}