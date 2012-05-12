package org.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class RequestUtils {
	public static JSONObject getJson(String userID, String method, String action, String day, String parity, String group) throws URISyntaxException, JSONException{
		final String SERVER_URL = "http://178.130.32.166:8075/";
    	final HttpClient httpclient = new DefaultHttpClient();
    	final HttpGet httpget = new HttpGet();
    	JSONObject jsonObj = null;
    	String jsonString = "Oups!";
    	final String uriS = SERVER_URL+ method + "?action=" + action + "&day=" + day + "&parity=" + parity +"&group=" + group + "&userId=" + userID;
    	httpget.setURI(new URI(uriS));
		System.out.println(uriS);
    	try {
    		HttpResponse response = httpclient.execute(httpget);
    		HttpEntity entity = response.getEntity();
    		jsonString = EntityUtils.toString(entity, "UTF-8");
    		Log.i("json:",jsonString);
    		jsonObj = new JSONObject(jsonString);

    		jsonObj.remove("errors"); ///

    	} catch (ClientProtocolException e) {
    		Log.e("ClientPortExc", e.getMessage());
    	} catch (IOException e) {
    		Log.e("IOExc:",e.getMessage());
    	} catch(Exception exception){
    		Log.e("Other Exc",exception.getLocalizedMessage());
    	}

    	return jsonObj;
    }
	public static JSONObject getJson(String action, String day, String parity)throws URISyntaxException, JSONException{
		return getJson("1","wakeBolet", action, day, parity, "2742");
	}

	public static String[] getRings(String day, String parity) throws JSONException, URISyntaxException{
		JSONObject json = getJson("rings", day, parity);
		JSONArray jarray = json.getJSONArray("rings");
		int len = jarray.length();
		String[] result= new String[len];
		for (int i = 0; i < len; i++){
			result[i] = jarray.getString(i);
		}
		return result;
	}

	public static List<Item> getScedule(String day, String parity) throws JSONException, URISyntaxException{
		List<Item> items = new LinkedList<Item>();
		JSONObject json = getJson("schedule", day, parity);
		int len = json.length();
		for (int i = 0; i < len; i++){
			JSONArray jarray = json.getJSONArray(Integer.toString(i));
			items.add(new Item(jarray.getString(0),jarray.getString(1),jarray.getString(3),jarray.getString(4),jarray.getString(5),jarray.getString(6)));
		}
		return items;
	}

	private int getHour(String time){
		return Integer.parseInt(time.substring(0, 2));
	}
	private int getMinute(String time){
		return Integer.parseInt(time.substring(3));
	}

}
