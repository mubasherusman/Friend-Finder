package friendFinder.googleMaps;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {
	private TextView Register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Register=(TextView) findViewById(R.id.Register);
		Register.setOnClickListener(
		new View.OnClickListener() 
		{
 
            public void onClick(View v) 
            {
                Intent i = new Intent( Login.this, SignUpForm.class);
                Login.this.startActivity(i);
            }
        });
		
		Button myLogin=(Button) findViewById(R.id.btnLogin);
		myLogin.setOnClickListener(new View.OnClickListener()
		{
			errorCheck E=new errorCheck();
			public void onClick(View v) 
			{
				Handler h = new Handler() {

		            @Override
		            public void handleMessage(Message msg) {

		                if (msg.what != 1) 
		                { // code if not connected

							Toast tt=Toast.makeText(Login.this, "Sorry! Network is not Responding", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();
		                } 
		                else 
		                { // code if connected
		                	String u=((EditText) findViewById(R.id.signInusernameField)).getText().toString();
		    				String p=((EditText) findViewById(R.id.signInPasswordField)).getText().toString();
		    				if(E.isSpace(u)||u.isEmpty())
		    				{
		    					Toast t=Toast.makeText(Login.this, "Space in user name is not Allowed", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(E.isSpecialChar(u)||u.isEmpty())
		    				{
		    					Toast t=Toast.makeText(Login.this, "Only _ OR . Allowed as special char", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(E.isSpace(p)||u.isEmpty())
		    				{
		    					Toast t=Toast.makeText(Login.this, "Space in Password is not Allowed", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else					
		    				{
		    					DbConnection db=new DbConnection();
		    					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    				    
		    				    nameValuePairs.add(new BasicNameValuePair("u",u));
		    				    nameValuePairs.add(new BasicNameValuePair("p",p));
		    					ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"index.php");
		    					nameValuePairs.clear();
		    					for(int i=0;i<result.size();i++)
		    						{
		    							String s=result.get(i).getName();
		    							if(s.equals("Error"))
		    							{
		    								Toast tt=Toast.makeText(Login.this, "Sorry Signin Failed Please Try Again", 3000);
		    								tt.setGravity(Gravity.CENTER, 0, 15);
		    								tt.show();
		    							}
		    							else if(s.equals("Login"))
		    							{
		    							
		    								Toast tt=Toast.makeText(Login.this, "Successfully Loged in", 3000);
		    								tt.setGravity(Gravity.CENTER, 0, 15);
		    								tt.show();
		    								Intent ii=new Intent(Login.this, MapViewActivity.class);
		    								ii.putExtra("Login",result.get(i).getValue().toString() );
		    								startActivity(ii);
		    								
		    							}
		    						}
		    					 result.clear();
		    				}
		    			
		                }

		            }
		        };
				isNetworkAvailable(h,4000);
				
			}
			
		});
		
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
