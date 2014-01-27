package com.johnjones.harexperiments.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Har {
	private List<Resource> entries;
	
	public Har() {
		this.entries = new LinkedList<Resource>();
	}
	
	public List<Resource> getEntries() {
		return this.entries;
	}
	
	public Resource computeRequestTree() {
		Resource root = this.entries.get(0);
		this.buildTree(root);
		return root;
	}
	
	private void buildTree(Resource resource) {
		resource.setChildren(this.getObjectsLoadedForURL(resource.getUrl()));
		for(Resource childResource : resource.getChildren()) {
			if (childResource.getParent() == null) {
				childResource.setParent(resource);
				this.buildTree(childResource);
			}
		}
	}
	
	public List<Resource> getObjectsLoadedForURL(URL url) {
		List<Resource> resources = new LinkedList<Resource>();
		for(Resource resource : this.entries) {
			String refererURL = resource.getRequestHeaders().get("Referer");
			if (refererURL != null && url.toString().equals(refererURL)) {
				resources.add(resource);
			}
		}
		return resources;
	}
	
	public static Har parseFile(File file) throws IOException, JSONException, ParseException {
		Har har = new Har();
	    FileReader fileReader = new FileReader(file);
	    BufferedReader bufferedReader = new BufferedReader(fileReader);
	    char[] buffer = new char[256];
	    int read = 0;
	    StringBuilder inString = new StringBuilder();
	    while((read = bufferedReader.read(buffer)) > 0) {
	    	inString.append(buffer, 0, read);
	    }
	    bufferedReader.close();
	    JSONObject object = new JSONObject(inString.toString());
	    JSONArray entries = object.getJSONObject("log").getJSONArray("entries");
	    for(int i=0;i<entries.length();i++) {
	    	JSONObject entry = entries.getJSONObject(i);
	    	har.entries.add(new Resource(entry));
	    }
		return har;
	}
}
