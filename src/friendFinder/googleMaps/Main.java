package friendFinder.googleMaps;

//import android.content.Intent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
//import android.widget.Toast;
//import android.widget.AdapterView.OnItemClickListener;
import android.provider.Settings;

public class Main extends Activity implements android.content.DialogInterface.OnClickListener {
	ConnectivityManager connManager=null;
	NetworkInfo mWifi=null, mMobile=null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	//WifiManager wifiManager;
 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
        mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mWifi.isConnected())
        {
        	 mainActivityLogic();
        }
        else if (mMobile.isConnected()) 
        {
        	 mainActivityLogic();
        }
        else{
        	alert();
        }
    }
    @SuppressWarnings("static-access")
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
		if(dialog.BUTTON_NEUTRAL==which)
		  {

			startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		
			Main.this.finish();
		  }
		
	}
    private void alert()
    {
    	
    	AlertDialog.Builder alertbuilder = new AlertDialog.Builder(Main.this);

		alertbuilder.setTitle("Internet");
		alertbuilder.setMessage("Internet is not available. Turn it on");
		alertbuilder.setIcon(android.R.drawable.ic_dialog_alert);
		alertbuilder.setNeutralButton("Ok",this);

		alertbuilder.show();
  
    }
	
	private void mainActivityLogic()
	{
		
		new Handler().postDelayed(new Runnable() {
            public void run() 
            {
                final Intent mainIntent = new Intent(Main.this, Login.class);
                Main.this.startActivity(mainIntent);
                Main.this.finish();
            }
		}, 4000);
        
        	
	}
	 // wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	//  if(!wifiManager.isWifiEnabled())
	//  {
	//	  wifiManager.setWifiEnabled(true);
		  
		 
	//  }
	  


    
    /*public final boolean isInternetOn() 
    {
    	ConnectivityManager connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	
    	if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED ||
    			connec.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTING ||
    			connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING ||
    			connec.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED ) 
    	{
    		Toast.makeText(this, "Network connected", Toast.LENGTH_SHORT).show();
    		return true;
    	} 
    	else if ( connec.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED ||  connec.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED  )
    	{
    
    		return false;
    	}
    return false;
    }*/

}