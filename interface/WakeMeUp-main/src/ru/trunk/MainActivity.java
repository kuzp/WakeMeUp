package ru.trunk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}

	public void showMondaySchedule(View v) throws URISyntaxException {
		String [] sch = {"09:30 Иностранный язык","11:00 Иностранный язык"};
		System.out.println(readRings().toString());
		setContentView(R.layout.monday_shedule);
		//for (int i = 0; i < sch.length; i++ ){
			TextView tv1 = (TextView)findViewById(R.id.tw1);
			tv1.setText(sch[0]);
		//}
	}
	public void showWenRings(){
		
	}

	public void showMainActivity(View v) {
		
		setContentView(R.layout.main);
	}

    private static String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
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
    } 
	
	
	public String [] readRings() throws URISyntaxException{
		
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpget = new HttpGet();
    	httpget.setURI(new URI("http://10.0.2.2:8075/wakeBolet?action=rings&day=Понедельник&parity=неч&group=2742"));
    	//httpget.setURI(new URI("http://twitter.com/statuses/user_timeline/vogella.json"));
    	JSONObject jsonObj;
    	String[] result = {"none"};
    	try {
    		//System.out.println("QQ");
    		
    		HttpResponse response = httpclient.execute(httpget);
    		
    		//System.out.println("Resp!!!!");
    		
    		HttpEntity ent = response.getEntity();
    		
    		InputStream inpst = ent.getContent(); // Внимание! начало быдлокода!
    		
    		//System.out.println("is");
    		
    		String jsonInString = convertStreamToString(inpst);
    		Log.e("json: ",jsonInString);
    		
    		
    		//System.out.println(test.toCharArray()[1]);
    		
    		jsonObj = new JSONObject(jsonInString);
    		result = (String[]) jsonObj.get("rings");
    		
    		//System.out.println("json");
    		
    		TextView tw = (TextView)findViewById(R.id.TextView01);
    		
    	} catch (ClientProtocolException e) {

    		// TODO Auto-generated catch block
    		System.out.println("ClientProt");

    	} catch (IOException e) {

    		// TODO Auto-generated catch block
    		System.out.println("io");

    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("JsonErr");
		}catch(IllegalArgumentException ex){
			//ex.printStackTrace();
			System.out.println("!!!!");
		} catch (Exception ex){
			
		}

    	return result;
    }
}