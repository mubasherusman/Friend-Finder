package friendFinder.googleMaps;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DbConnection {

	DbConnection()
	{
	}
	
	public ArrayList<NameValuePair> getServerData(ArrayList<NameValuePair> nameValuePair,String fileName)
	{
	    
	   InputStream is = null;
	    
	   String result = "";
	   ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    try{
	            HttpClient httpclient = new DefaultHttpClient();
	            HttpPost httppost=null;
	            
	            httppost = new HttpPost("http://www.ffinder.site90.com/"+fileName);
	            httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
	            HttpResponse response = httpclient.execute(httppost);
	            HttpEntity entity = response.getEntity();
	            is = entity.getContent();

	    }
	    catch(Exception e)
	    {
	            Log.e("log_tag", "Error in http connection "+e.toString());
	    }

	    //convert response to string
	    try
	    {
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while ((line = reader.readLine()) != null) 
	                    sb.append(line + "\n");
	       
	            is.close();
	            result=sb.toString();
	    }catch(Exception e)
	    {
	            Log.e("log_tag", "Error converting result "+e.toString());
	    }
	    //parse json data
	    try{
	            JSONArray jArray = new JSONArray(result);
	            JSONArray colName=jArray.getJSONObject(0).names();

	    	    
	            if(colName!=null)
	            {
	            	
	            for(int i=0;i<jArray.length();i++)
	            {
	                    JSONObject json_data = jArray.getJSONObject(i);
	                    
	                    Log.i("log_tag",colName.getString(0)+": "+json_data.getString(colName.getString(0)));
	                    //Get an output to the screen
	                    
	                    for(int j=0;j<colName.length();j++)
	                    {
	                    	nameValuePairs.add(new BasicNameValuePair(colName.getString(j),json_data.getString(colName.getString(j))));
	                    	
	           
	                    }
	                  
	            }            
	            }
	            else
	            {
	            	nameValuePairs.add(new BasicNameValuePair("Error","colName error"));
	            }
	    }catch(JSONException e)
	    {
	            Log.e("log_tag", "Error parsing data "+e.toString());
	            nameValuePairs.add(new BasicNameValuePair("Error","Error! parsing data"));
	    }
	    return nameValuePairs; 
	}    
	    


}
