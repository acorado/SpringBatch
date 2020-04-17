package com.example.batchprocessing;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import org.springframework.web.client.RestTemplate;

import org.json.JSONObject;

//@Component
public class ScheduleDelete {
	private static final Logger LOGGER = LogManager.getLogger(ScheduleDelete .class.getName());
	Date date = new Date();
LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
public int month = localDate.getMonthValue();
public int day=localDate.getDayOfMonth();
public int year=localDate.getYear();

 



	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


	public void delleteAll(String name, int year, String mes,String user,String pass, String url) {
		System.out.println("Borrado time is now {}"+ dateFormat.format(new Date()));




	try {
		
	RestTemplate restTemplate = new RestTemplate();
		System.out.println("Eliminar indices");
		LOGGER.info("*******************************************************Proceso de eliminación iniciado***********************************************************");
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		
		String plainCreds = user+":"+pass;
		byte[] plainCredsBytes = plainCreds.getBytes();
		String base64Creds = Base64.getEncoder().encodeToString(plainCredsBytes);

		String uri = url+"/"+name+"-"+year+"."+mes+".*";
		
		// set headers
		HttpHeaders headers2 = new HttpHeaders();
		headers2.setContentType(MediaType.APPLICATION_JSON);
		headers2.set("Authorization", "Basic " + base64Creds);
		LOGGER.info("/"+name+"-"+year+"."+mes+".*");
		//Object input = "{\"source\": {\"index\": \"dell.0"+retro+".*\"},\"dest\": {\"index\": \"dell.0"+month+"."+year+"\", \"op_type\": \"create\"}}";

		//System.out.println(input);
	//	HttpEntity<String> entity = new HttpEntity<String>(headers2);
		// send request and parse resultcs
		//ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.DELETE, entity, String.class);
		
	//String data =response.getBody();
		
		//JSONObject json = new JSONObject(data);
	
//System.out.println(json);
		
LOGGER.info("*******************************************************Eliminando indices correspondientes al mes "+mes+"***********************************************************");
LOGGER.info("Indices correspondientes al mes "+mes+" borrados");


	} catch (Exception e) {
		LOGGER.warn("el error es"+e);
		LOGGER.warn("Error en proceso de eliminación "+e);
	}
		

	}


	
}