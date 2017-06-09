package org.cloudfoundry.samples.music.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cloud.cloudfoundry.com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service("communicationService")
public class CommunicationService {
	
	private final Logger log = LoggerFactory.getLogger(CommunicationService.class);

	public Map<String, Object> postRequest(String requestURL, Map<String, String> headers, Map<String, String> body) {
		Map<String, Object> response = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httpPost = new HttpPost(requestURL);			

			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> item : headers.entrySet()) {
					httpPost.addHeader(item.getKey(), item.getValue());
				}
			}

			if (body != null && !body.isEmpty()) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for (Entry<String, String> item : body.entrySet()) {
					params.add(new BasicNameValuePair(item.getKey(), item.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(params));
			}
			log.info("Executing request " + httpPost.getRequestLine());

			// Create a custom response handler
			ResponseHandler<Map<String, Object>> responseHandler = new ResponseHandler<Map<String, Object>>() {

				@Override
				public Map<String, Object> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					Map<String, Object> responseMap = new HashMap<>();
					int status = response.getStatusLine().getStatusCode();
					HttpEntity entity = response.getEntity();
					responseMap.put(String.valueOf(status), (entity != null ? EntityUtils.toString(entity) : null));
					return responseMap;
				}
				

			};

			response = httpclient.execute(httpPost, responseHandler);
			log.info(mapToJson(response));
		} catch (IOException e) {
			log.debug(e.getMessage(), e.getCause());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug(e.getMessage(), e.getCause());
			}
		}
		return response;
	}
	

	public Map<String, Object> getRequest(String requestURL, Map<String, String> headers) {
		Map<String, Object> response = null;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpGet = new HttpGet(requestURL);

			if (headers != null && !headers.isEmpty()) {
				for (Entry<String, String> item : headers.entrySet()) {
					httpGet.addHeader(item.getKey(), item.getValue());
				}
			}

			log.info("Executing request " + httpGet.getRequestLine());

			// Create a custom response handler
			ResponseHandler<Map<String, Object>> responseHandler = new ResponseHandler<Map<String, Object>>() {

				@Override
				public Map<String, Object> handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					Map<String, Object> responseMap = new HashMap<>();
					int status = response.getStatusLine().getStatusCode();
					HttpEntity entity = response.getEntity();
					responseMap.put(String.valueOf(status), (entity != null ? EntityUtils.toString(entity) : null));
					return responseMap;
				}	

			};

			response = httpclient.execute(httpGet, responseHandler);
			log.info(mapToJson(response));
		} catch (IOException e) {
			log.debug(e.getMessage(), e.getCause());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				log.debug(e.getMessage(), e.getCause());
			}
		}
		return response;
	}
	

	// Map to JSON
	private String mapToJson(Map<String, Object> map){
		ObjectMapper objectMapper = new ObjectMapper();		
		String json="";					
		try {
			json = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e.getCause());
		}					
		return json;				
	}
}
