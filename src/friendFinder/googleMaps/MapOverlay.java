package friendFinder.googleMaps;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay 
		{
			GeoPoint point=null;
			 
			 MapViewActivity mva=null;
			 String UserNameTag="";
			 public MapOverlay(GeoPoint p,MapViewActivity mva1)
			 {
				 mva=mva1;
					point=p;
			}
			 public MapOverlay(GeoPoint p,MapViewActivity mva1,String Name)
			 {
				 this.mva=mva1;
					this.point=p;
					this.UserNameTag=Name;
			}
			@Override
			public void draw(Canvas canvas, MapView mapView, boolean shadow) 
			{
				// TODO Auto-generated method stub
				super.draw(canvas, mapView, shadow);
				//---translate the GeoPoint to screen pixels---
		        Point screenPts = new Point();
		        mapView.getProjection().toPixels(point, screenPts);
		        Paint paint =new Paint();
		        paint.setStrokeWidth(1);
		        paint.setAntiAlias(true);
		        paint.setARGB(150, 000, 000, 000);
		        paint.setStyle(Paint.Style.STROKE);

		        //---add the marker---
		        Bitmap bmp = BitmapFactory.decodeResource(mva.getResources(), R.drawable.gps_marker);            
		        canvas.drawBitmap(bmp, screenPts.x-13, screenPts.y-32, null);  
		        canvas.drawText(UserNameTag, screenPts.x-10,screenPts.y-38 , paint);
				//return true;
			}//-----------------------------//
			
			

		}