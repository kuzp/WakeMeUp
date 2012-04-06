package ru.trunk;

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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	final String[] days = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}

	public void showMondaySchedule(View v) throws URISyntaxException, JSONException {
		String [] sch = {"09:30 Иностранный язык","11:00 Иностранный язык"};
		setContentView(R.layout.shedule);
		//for (int i = 0; i < sch.length; i++ ){
			TextView tv1 = (TextView)findViewById(R.id.tw1);
			tv1.setText(sch[0]);
		//} 
			List <Item> iii = getScedule("Среда", "неч");
	}


	public void showMainActivity(View v) {
		
		setContentView(R.layout.main);
	}

   /* private static String convertStreamToString(InputStream is) {

        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
 
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    } */
	
	
	private JSONObject getJson(String method, String action, String day, String parity, String group) throws URISyntaxException, JSONException{
		final String serverURL = "http://178.130.32.166:8075/";
    	final HttpClient httpclient = new DefaultHttpClient();
    	final HttpGet httpget = new HttpGet();
    	JSONObject jsonObj = null;
    	String jsonString = "Oups!";
    	final String uriS = serverURL+ method + "?action=" + action + "&day=" + day + "&parity=" + parity +"&group=" + group;
    	httpget.setURI(new URI(uriS));
		System.out.println(uriS);
    	try {
    		//final ResponseHandler<String> rh = new BasicResponseHandler();
    		//result = httpclient.execute(httpget, rh);
    		HttpResponse response = httpclient.execute(httpget);
    		HttpEntity entity = response.getEntity();
    		//InputStream is = entity.getContent();
    		jsonString = EntityUtils.toString(entity, "UTF-8");//convertStreamToString(is);
    		Log.i("json:",jsonString);    		
    		jsonObj = new JSONObject(jsonString);
    		//Log.e("ErrorsInJson:", jsonObj.getString("errors"));

    		jsonObj.remove("errors"); ///    		
    		
    	} catch (ClientProtocolException e) {
    		Log.e("ClientPortExc", e.getMessage());
    	} catch (IOException e) {
    		Log.e("IOExc:",e.getMessage());
    	}

    	return jsonObj;
    }
	private JSONObject getJson(String action, String day, String parity)throws URISyntaxException, JSONException{
		return this.getJson("wakeBolet", action, day, parity, "2742");
	}
	
	public String[] getRings(String day, String parity) throws JSONException, URISyntaxException{
		JSONObject json = getJson("rings", day, parity);
		JSONArray jarray = json.getJSONArray("rings");
		int len = jarray.length();
		String[] result= new String[len];
		for (int i = 0; i < len; i++){
			result[i] = jarray.getString(i);
		}
		return result;
	}
	
	public List<Item> getScedule(String day, String parity) throws JSONException, URISyntaxException{
		List<Item> items = new LinkedList<Item>();
		JSONObject json = getJson("schedule", day, parity);
		int len = json.length();
		for (int i = 0; i < len; i++){
			JSONArray jarray = json.getJSONArray(Integer.toString(i));
			items.add(new Item(jarray.getString(1),jarray.getString(2),jarray.getString(3),jarray.getString(4),jarray.getString(5)));
		}
		return items;
	}
}