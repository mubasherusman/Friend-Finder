package friendFinder.googleMaps;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView; 
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapViewActivity extends MapActivity implements   android.content.DialogInterface.OnClickListener{
	//Data Section
	private  MapView mapView=null;
	private MapController mapController=null;
	private LocationManager locationManager=null;
	private MyLocationOverlay myLocationOverlay;
	public static final int INSERT_ID = Menu.FIRST;
	public static final int CENTER_ID = Menu.FIRST + 1;
	public static final int SATELLITE_ID = Menu.FIRST + 2;
	public static final int MAP_ID = Menu.FIRST + 3;
	public static final int Notifications = Menu.FIRST + 4;
	public static final int Refresh = Menu.FIRST + 5;
	GeoPoint point=null;
	String longitudes,latitudes;
	String LoginId="";String s="";
	ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	ArrayList<String> coOrd=new ArrayList<String>();
	ArrayList<String> friends=new ArrayList<String>();
	DbConnection db=new DbConnection();
	
	
	
	//Logic Section
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_view);
		
		//get Id of Currently Loged In User
		LoginId=getIntent().getExtras().getString("Login");
		// Get Google map
		mapView = (MapView) findViewById(R.id.mapView);
		//Get Zooms Over Map
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(13);
		
		//Get Location Service from device i.e GPS
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!enabled) 
		{
			AlertDialog.Builder b = new AlertDialog.Builder(this);
			  b.setIcon(android.R.drawable.ic_dialog_alert);
			  b.setTitle("Gps is Disabled");
			  b.setMessage("Go and Enabled It?");
			  b.setPositiveButton("Go", this);
			  b.setNegativeButton("Cancel", this);
			  b.show();
			
		}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,new GeoUpdateHandler() );
			
			myLocationOverlay = new MyLocationOverlay(this, mapView);
			mapView.getOverlays().add(myLocationOverlay);
			
			myLocationOverlay.runOnFirstFix(new Runnable() 
			{
				public void run() 
				{
					point=myLocationOverlay.getMyLocation();
				
					mapView.getController().animateTo(point);
					
					
		            
				}
			});
			
            
			mapView.postInvalidate();
			OnClickOverlayListner ocl=new OnClickOverlayListner();
	        List<Overlay> listOfOverlays = mapView.getOverlays();
	        listOfOverlays.add(ocl);

	    	Handler h = new Handler() {

	                @Override
	                public void handleMessage(Message msg) {

	                    if (msg.what != 1) { // code if not connected
	                    	
	                    	Toast tt=Toast.makeText(MapViewActivity.this, "Sorry! Network is not Responding", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();
	                    } else { // code if connected
	                    	 friendAdd();
	                    }

	                }
	            };
	    		isNetworkAvailable(h,5000);
	       
	        mapView.postInvalidate();
	       
	}
	@SuppressWarnings("static-access")
	public void onClick(DialogInterface dialog, int which)
	{
		  // TODO Auto-generated method stub
		
		  if(dialog.BUTTON_POSITIVE==which)
		  {
			  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			  startActivity(intent);
		  }
		else if(dialog.BUTTON_NEGATIVE==which)
		  {
			dialog.dismiss();
		  }
	}
	private void friendAdd()
	{
		String name="";
		nameValuePairs.add(new BasicNameValuePair("id",LoginId));
		ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"getfriends1.php");
		for(int i=0;i<result.size();i++)
		{
			s=result.get(i).getName();
		
			if(s.equals("Error"))
			{
			}
			else if(s.equals("latd"))
			{
				coOrd.add(result.get(i).getValue());
				friends.add(result.get(i).getValue());
			}
			else if(s.equals("Name"))
			{
				name=result.get(i).getValue();
			}
			else if(s.equals("lotd"))
			{
				coOrd.add(result.get(i).getValue());
				friends.add(result.get(i).getValue());
				if(coOrd.get(0)!=null)
				{
					MapOverlay mapOverlay = new MapOverlay(getGeoPoint( coOrd),MapViewActivity.this,name );
			        List<Overlay> listOfOverlays = mapView.getOverlays();
			        listOfOverlays.add(mapOverlay);
				}
		        coOrd.clear();
			}
			
		}
		result.clear();
		result=db.getServerData(nameValuePairs,"getfriends2.php");
		for(int i=0;i<result.size();i++)
		{
			s=result.get(i).getName();

			
			if(s.equals("Error"))
			{
			}
			else if(s.equals("latd"))
			{
				coOrd.add(result.get(i).getValue());
				friends.add(result.get(i).getValue());
			}

			else if(s.equals("Name"))
			{
				name=result.get(i).getValue();
			}
			else if(s.equals("lotd"))
			{
				coOrd.add(result.get(i).getValue());
				friends.add(result.get(i).getValue());
				if(coOrd.get(0)!=null)
				{
					MapOverlay mapOverlay = new MapOverlay(getGeoPoint( coOrd),MapViewActivity.this,name);
			        List<Overlay> listOfOverlays = mapView.getOverlays();
			        listOfOverlays.add(mapOverlay);
		        }
		        coOrd.clear();
			}
			
		}result.clear();nameValuePairs.clear();
		
	}
	//--------------------------------------------------------------------------------//
	public class OnClickOverlayListner extends Overlay {
		@Override
		public boolean onTap(GeoPoint p, MapView mapView) 
		{
			
			//GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),(int) event.getY());
			if(p!=null){
	        longitudes = ""+p.getLongitudeE6()/1E6;
            latitudes =  ""+p.getLatitudeE6()/1E6;
            //MapOverlay mapOverlay = new MapOverlay(p,MapViewActivity.this);
	        //List<Overlay> listOfOverlays = mapView.getOverlays();
	        //listOfOverlays.add(mapOverlay);
	       // mapView.postInvalidate();
            
            
            Handler h = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                    if (msg.what != 1) { // code if not connected
                    	
                    	Toast tt=Toast.makeText(MapViewActivity.this, "Sorry! Network is not Responding", 3000);
						tt.setGravity(Gravity.CENTER, 0, 15);
						tt.show();
                    } else
                    { // code if connected
                    	nameValuePairs.add(new BasicNameValuePair("latd",latitudes));
            			nameValuePairs.add(new BasicNameValuePair("lotd",longitudes));
            			nameValuePairs.add(new BasicNameValuePair("id",LoginId));
            			ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"updatecoordinates.php");
            			nameValuePairs.clear();
            			for(int i=0;i<result.size();i++)
            			{
            				s=result.get(i).getName();
            				if(s.equals("Error"))
            				{
            					Toast tt=Toast.makeText(MapViewActivity.this, "Adress Updated", 3000);
            					tt.setGravity(Gravity.CENTER, 0, 15);
            					tt.show();
            				}
            				else
            				{
            					Toast tt=Toast.makeText(MapViewActivity.this, "Adress Updated Error", 3000);
            					tt.setGravity(Gravity.CENTER, 0, 15);
            					tt.show();break;
            				}
            			}
            			result.clear();
                    }

                }
            };
    		isNetworkAvailable(h,5000);
	       
			
	        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());
            try
            {
                    List<Address> addresses = geoCoder.getFromLocation(p.getLatitudeE6()  / 1E6, p.getLongitudeE6() / 1E6, 1);
 
                    String add = "";
                    if (addresses.size() > 0) 
                    {
                        for (int i=0; i<addresses.get(0).getMaxAddressLineIndex();i++)
                           add += addresses.get(0).getAddressLine(i) + "\n";
                    }
 
                    Toast.makeText(getBaseContext(), add, Toast.LENGTH_SHORT).show();
             }
             catch (IOException e)
             {                
                    e.printStackTrace();
             } 
           
             return true;
			}
          else                
                return false  ; 
			
		}

	}

	//---------------------------------------------------------------------------------//
		public GeoPoint getGeoPoint(Location location) {
			int lat = (int) (location.getLatitude() * 1E6);
			int lng = (int) (location.getLongitude() * 1E6);
			GeoPoint point = new GeoPoint(lat, lng);
			return point;

		}
		public GeoPoint getGeoPoint( ArrayList<String>  coordinates) {
			double lat = Double.parseDouble(coordinates.get(0));
	        double lng = Double.parseDouble(coordinates.get(1));
	         return new GeoPoint(
	                (int) (lat * 1E6), 
	                (int) (lng * 1E6));
			

		}
	//---------------------------------------------------------------------------------//
		private class GeoUpdateHandler implements LocationListener
		{

			public void onLocationChanged(Location newlocation) {
				
				// TODO Auto-generated method stub
				
				if (newlocation != null) 
				{
					locationManager.removeUpdates(this);
					point=getGeoPoint(newlocation);
					//String altitiude = "Altitiude: " + newlocation.getAltitude();
					//String accuracy = "Accuracy: " + newlocation.getAccuracy();
					//String time = "Time: " + newlocation.getTime();
					mapController.animateTo(point);
					
					latitudes=  String.valueOf(newlocation.getLatitude() * 1E6);
					longitudes=  String.valueOf (newlocation.getLongitude() * 1E6);
					nameValuePairs.add(new BasicNameValuePair("latd",latitudes));
					nameValuePairs.add(new BasicNameValuePair("lotd",longitudes));
					nameValuePairs.add(new BasicNameValuePair("id",LoginId));
					ArrayList<NameValuePair> result=db.getServerData(nameValuePairs,"updatecoordinates.php");
					nameValuePairs.clear();
					for(int i=0;i<result.size();i++)
					{
						s=result.get(i).getName();
						if(s.equals("Error"))
						{
							Toast tt=Toast.makeText(MapViewActivity.this, "Co-Ords Updated", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();
						}
						else
						{
							Toast tt=Toast.makeText(MapViewActivity.this, "Co-Ords Updated Error", 3000);
							tt.setGravity(Gravity.CENTER, 0, 15);
							tt.show();break;
						}
					}
					result.clear();
					
					mapView.invalidate();
				}
				
				
			}

			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
				
			}

			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				Toast.makeText( getApplicationContext(),"Gps Enable",Toast.LENGTH_SHORT ).show();
				
			}

			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
			
		}
	//--------------------------------------------------------------------------------//
		
		
	//--------------------------------------------------------------------------------//
	@Override
	protected boolean isRouteDisplayed() {
	
		return false;
	}
	//--------------------------------------------------------------------------------//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		menu.add(0, INSERT_ID, 0, R.string.menu_add);
		menu.add(0, CENTER_ID, 0, R.string.menu_location);
		menu.add(0, Refresh, 0, "Refresh");
		menu.add(0,Notifications,0,R.string.notifications);
		menu.add(0, SATELLITE_ID, 0, R.string.menu_satellite);
		menu.add(0, MAP_ID, 0, R.string.menu_map);
		
		return result;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			AddFriend();
			return true;
		case Notifications:
			notifications();
			return true;
		case Refresh:
			refresh();
			return true;
		case CENTER_ID:
			delFriend();
			return true;
		case SATELLITE_ID:
			satelliteMap(true);
			return true;
		case MAP_ID:
			satelliteMap(false);
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}
	private void refresh()
	{
		Intent i=new Intent(MapViewActivity.this,MapViewActivity.class);
		i.putExtra("Login", LoginId);
		MapViewActivity.this.startActivity(i);
		MapViewActivity.this.finish();
		
	}
	private void delFriend()
	{
		Intent i = new Intent( MapViewActivity.this, DeleteFriend.class);
		i.putExtra("id", LoginId);
		MapViewActivity.this.startActivity(i);
	}
	private void notifications()
	{
		Intent i = new Intent( MapViewActivity.this, notifications.class);
		i.putExtra("current user id", LoginId);
		MapViewActivity.this.startActivity(i);
	}
	private void satelliteMap(boolean satellite) {
		if (satellite) {
			mapView.setSatellite(true);
		} else {
			mapView.setSatellite(false);
		}
	}
	private void AddFriend()
	{
		Intent i = new Intent( MapViewActivity.this, AddFriend.class);
		i.putExtra("current user id", LoginId);
		MapViewActivity.this.startActivity(i);
	}

	//------------------------------------------------------------------------//
	@Override
	protected void onResume() {
		super.onResume();
		
		myLocationOverlay.enableMyLocation();
		myLocationOverlay.enableCompass();
	}

	@Override
	protected void onPause() {
		super.onResume();
		myLocationOverlay.disableMyLocation();
		myLocationOverlay.disableCompass();
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
