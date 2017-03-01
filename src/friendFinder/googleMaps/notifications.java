package friendFinder.googleMaps;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class notifications extends Activity implements OnItemClickListener , android.content.DialogInterface.OnClickListener{
	String recieverId="";
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayAdapter<String> adapter;ListView v=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		recieverId=getIntent().getExtras().getString("current user id");
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
		v=(ListView) findViewById(android.R.id.list);
		v.setAdapter(adapter);
		v.setOnItemClickListener(this);
		Handler h = new Handler() {

            @Override
            public void handleMessage(Message msg) {

                if (msg.what != 1) { // code if not connected
                	Toast tt=Toast.makeText(notifications.this, "Sorry! Network is not Responding", 3000);
					tt.setGravity(Gravity.CENTER, 0, 15);
					tt.show();
                	
                } else { // code if connected
                	String s="",l="";
					DbConnection db=new DbConnection();
					
					nameValuePairs.add(new BasicNameValuePair("id",recieverId));
					ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"notifications.php");
					nameValuePairs.clear();
					for(int i=0;i<result.size();i++)
					{
						s=result.get(i).getName();
						if(s.equals("Error"))
						{
							
						}
						else if(s.equals("Name"))
						{
							l=result.get(i).getValue();
						}
						else if(s.equals("reqId"))	
						{
							adapter.add(l);
							adapter.notifyDataSetChanged();
							nameValuePairs.add(new BasicNameValuePair(l,result.get(i).getValue()));
							
							l="";
							
						}
						
					}

					Toast tt=Toast.makeText(notifications.this, nameValuePairs.size()+" New Friend Requests", 3000);
					tt.setGravity(Gravity.CENTER, 0, 15);
					tt.show();
				
                }

            }
        };
		isNetworkAvailable(h,5000);
					

		
		
	}
	String userName="";
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		// TODO Auto-generated method stub
		 userName = ((TextView)arg1).getText().toString();
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		  b.setIcon(android.R.drawable.ic_dialog_alert);
		  b.setTitle("Accept friend request");
		  b.setMessage("Accept or Cancel ?");
		  b.setPositiveButton("Accept", this);
		  b.setNegativeButton("Cancel",this);
		  b.show();
		 
		 
		
	}
	@SuppressWarnings("static-access")
	public void onClick(DialogInterface dialog, int which)
	{
		  // TODO Auto-generated method stub
		
		  if(dialog.BUTTON_POSITIVE==which)
		  {
			  Handler h = new Handler() {

		            @Override
		            public void handleMessage(Message msg) {

		                if (msg.what != 1) { // code if not connected
		                	
		                	Toast tt=Toast.makeText(notifications.this, "Sorry! Network is not Responding", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();
		                } else { // code if connected
		                	String accepter="";
		                	for(int i=0;i<nameValuePairs.size();i++)
		      			  {
		      				  accepter=nameValuePairs.get(i).getName();
		      				  if(accepter.equals(userName))
		      				  {
		      					  accepter="";
		      					  accepter=nameValuePairs.get(i).getValue();
		      					  DbConnection db=new DbConnection();
		      					 
		      						nameValuePairs.add(new BasicNameValuePair("id",accepter));
		      						ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"acceptfriend.php");
		      						
		      						for(int j=0;j<result.size();j++)
		      						{
		      							accepter=result.get(j).getName();
		      							if(accepter.equals("Error"))
		      							{
		      								Toast tt=Toast.makeText(notifications.this,"Accepted", 5000);
		      								tt.setGravity(Gravity.CENTER, 0, 15);
		      								tt.show();
		      								adapter.remove(userName);
		      								adapter.notifyDataSetChanged();
		      								nameValuePairs.remove(i);
		      								break;
		      							}
		      							else
		      							{
		      								Toast tt=Toast.makeText(notifications.this, "Request does Not send\nServer Error", 7000);
		      								tt.setGravity(Gravity.CENTER, 0, 15);
		      								tt.show();
		      							}
		      						}
		      				  }
		      			  }
		                }

		            }
		        };
				isNetworkAvailable(h,5000);
			  
		  }
		else if(dialog.BUTTON_NEGATIVE==which)
		  {
			dialog.dismiss();
		  }
	}
	
	

public static void isNetworkAvailable(final Handler handler, final int timeout) {

        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)

        new Thread() {

            private boolean responded = false;

            @Override
            public void run() {

                // set 'responded' to TRUE if is able to connect with google mobile (responds fast)

                new Thread() {

                    @Override
                    public void run() {
                        HttpGet requestForTest = new HttpGet("http://m.google.com");
                        try {
                            new DefaultHttpClient().execute(requestForTest); // can last...
                            responded = true;
                        } catch (Exception e) {}
                    }

                }.start();

                try {
                    int waited = 0;
                    while(!responded && (waited < timeout)) {
                        sleep(100);
                        if(!responded ) { 
                            waited += 100;
                        }
                    }
                } 
                catch(InterruptedException e) {} // do nothing 
                finally { 
                    if (!responded) { handler.sendEmptyMessage(0); } 
                    else { handler.sendEmptyMessage(1); }
                }

            }

        }.start();

}
	

}
