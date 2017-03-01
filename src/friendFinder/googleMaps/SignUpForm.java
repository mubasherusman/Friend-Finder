package friendFinder.googleMaps;


import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpForm extends Activity {
	EditText t=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signupform);
		
		TextView loginScreen = (TextView) findViewById(R.id.link_to_login);
        loginScreen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View arg0) {
                                
            	SignUpForm.this.finish();
            }
        });
        
        Button mysignUp=(Button) findViewById(R.id.btnRegister);
        mysignUp.setOnClickListener(new View.OnClickListener()
        {
			
			public void onClick(View v) 
			{

				Handler h = new Handler() {

		            @Override
		            public void handleMessage(Message msg) {

		                if (msg.what != 1) { // code if not connected
		                	
		                	Toast tt=Toast.makeText(SignUpForm.this, "Sorry! Network is not Responding", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();
		                } else { // code if connected
		                	errorCheck E=new errorCheck();
		    				
		    				String Name=((EditText) findViewById(R.id.reg_fullname)).getText().toString();
		    				String uName=((EditText) findViewById(R.id.reg_userName)).getText().toString();
		    				String Password=((EditText) findViewById(R.id.reg_password)).getText().toString();
		    				String email=((EditText) findViewById(R.id.reg_email)).getText().toString();
		    				//String address=((EditText) findViewById(R.id.reg_Address)).getText().toString();
		    				String phone=((EditText) findViewById(R.id.reg_phone)).getText().toString();
		    				if(E.isSpecialChar(Name))
		    				{
		    					Toast t=Toast.makeText(SignUpForm.this, "Name is Invalid", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(E.isSpecialChar(uName)||E.isSpace(uName))
		    				{
		    					Toast t=Toast.makeText(SignUpForm.this, "User Name is Invalid", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(E.isSpecialChar(Password)||E.isSpace(Password))
		    				{
		    					Toast t=Toast.makeText(SignUpForm.this, "Password is Invalid", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(!E.emailValidator(email))
		    				{
		    					Toast t=Toast.makeText(SignUpForm.this, "Email is Invalid", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else if(E.isSpace(phone)||E.isSpecialChar(phone)||!E.isNumeric(phone))
		    				{
		    					Toast t=Toast.makeText(SignUpForm.this, "phone is Invalid", 3000);
		    					t.setGravity(Gravity.CENTER, 0, 15);
		    					t.show();
		    				}
		    				else
		    				{
		    					
		    					DbConnection db=new DbConnection();
		    					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		    				    
		    				    nameValuePairs.add(new BasicNameValuePair("1",uName));
		    					ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"signup1.php");//
		    					for(int i=0;i<result.size();i++)
		    						{
		    							String s=result.get(i).getName();
		    							if(s.equals("Error"))
		    							{
		    								nameValuePairs.add(new BasicNameValuePair("2",Password));
		    								nameValuePairs.add(new BasicNameValuePair("3",Name));
		    								nameValuePairs.add(new BasicNameValuePair("4",email));
		    								nameValuePairs.add(new BasicNameValuePair("5",phone));
		    								result=db.getServerData(nameValuePairs,"signup2.php");//
		    								for(int j=0;j<result.size();j++)
		    								{
		    									String ss=result.get(i).getName();
		    									if(ss.equals("Error"))
		    									{
		    										Toast tt=Toast.makeText(SignUpForm.this,"Successfully Saved" , 3000);
		    										tt.setGravity(Gravity.CENTER, 0, 15);
		    										tt.show();
		    										t=(EditText) findViewById(R.id.reg_fullname);
		    										t.setText("");
		    										t=((EditText) findViewById(R.id.reg_userName));
		    										t.setText("");
		    										t=((EditText) findViewById(R.id.reg_password));
		    										t.setText("");
		    										t=((EditText) findViewById(R.id.reg_email));
		    										t.setText("");
		    										t=((EditText) findViewById(R.id.reg_Address));
		    										t.setText("");
		    										t=((EditText) findViewById(R.id.reg_phone));
		    										t.setText("");
		    										
		    											
		    										
		    									}
		    									
		    								}
		    								
		    							}
		    							else if(s.equals("Login"))
		    							{
		    								
		    							
		    								Toast tt=Toast.makeText(SignUpForm.this, "User Name already Exists", 2000);
		    								tt.setGravity(Gravity.CENTER, 0, 15);
		    								tt.show();
		    								//startActivity(new Intent(Login.this, MapViewActivity.class));
		    							}
		    						}
		    				}
		    				
		                }

		            }
		        };
				isNetworkAvailable(h,5000);
				
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
