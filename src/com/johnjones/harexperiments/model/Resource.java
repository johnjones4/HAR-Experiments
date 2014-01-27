package com.johnjones.harexperiments.model;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Resource {
	
	private URL url;
	private String method;
	private String dateTimeRequested;
	private Map<String,String> requestHeaders;
	
	private int size;
	private int responseCode;
	private String responseMessage;
	private double loadTime;
	private Map<String,String> responseHeaders;
	
	private List<Resource> children;
	private Resource parent;
	
	public Resource(JSONObject obj) throws MalformedURLException, JSONException, ParseException {
		assert(obj.has("request") && obj.has("response"));
		
		JSONObject request = obj.getJSONObject("request");
		JSONObject response = obj.getJSONObject("response");
		
		this.url = new URL(request.getString("url"));
		this.method = request.getString("method");
		//SimpleDateFormat format = new SimpleDateFormat();
		//format.applyPattern("YYYY-MM-DDThh:mm:ss.sTZD");
		//this.dateTimeRequested = format.parse(obj.getString("startedDateTime"));
		this.dateTimeRequested = obj.getString("startedDateTime");
		this.requestHeaders = parseHeaders(request);
		
		this.size = response.getInt("headersSize") + response.getInt("bodySize");
		this.responseCode = response.getInt("status");
		this.responseMessage = response.getString("statusText");
		this.loadTime = obj.getDouble("time");
		this.responseHeaders = parseHeaders(response);
	}
	
	private static Map<String,String> parseHeaders(JSONObject obj) {
		assert(obj.has("headers"));
		Map<String,String> headers = new HashMap<String,String>();
		JSONArray headerJson = obj.getJSONArray("headers");
		for(int i=0;i<headerJson.length();i++) {
			JSONObject header = headerJson.getJSONObject(i);
			assert(header.has("name") && header.has("value"));
			headers.put(header.getString("name"), header.getString("value"));
		}
		return headers;
	}

	public URL getUrl() {
		return url;
	}

	public String getMethod() {
		return method;
	}

	public String getDateTimeRequested() {
		return dateTimeRequested;
	}

	public Map<String, String> getRequestHeaders() {
		return requestHeaders;
	}

	public int getSize() {
		return size;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public double getLoadTime() {
		return loadTime;
	}

	public Map<String, String> getResponseHeaders() {
		return responseHeaders;
	}

	public List<Resource> getChildren() {
		return children;
	}
	
	public void setChildren(List<Resource> r) {
		this.children = r;
	}

	public Resource getParent() {
		return parent;
	}

	public void setParent(Resource parent) {
		this.parent = parent;
	}
	
	public String getResourceID() {
		return this.getUrl().toString() + " " + this.getDateTimeRequested().toString();
	}
}
