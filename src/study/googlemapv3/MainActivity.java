package study.googlemapv3;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends Activity implements LocationListener {

	private EditText et1;
	private ImageButton bt;
	private WebView wv;
	private SearchDialog sd;
	private Button d_bt;
	private LocationManager locationManager;

	private class SearchDialog extends Dialog {

		public SearchDialog(Context context) {
			super(context);
			setContentView(R.layout.search_dialog);
			this.setTitle("請輸入...例：台中火車站");

			et1 = (EditText)findViewById(R.id.et1);
			et1.clearFocus();

			d_bt = (Button)findViewById(R.id.d_bt);
			d_bt.setOnClickListener(new View.OnClickListener() {

				 @ Override
				public void onClick(View v) {
					wv.loadUrl("javascript:goto('" + et1.getText() + "')");
					et1.setText("");
					sd.dismiss();
					Log.e("dismiss", sd.toString());
				}
			});
		}

	}

	 @ Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		sd = new SearchDialog(this);

		bt = (ImageButton)findViewById(R.id.bt);
		bt.setOnClickListener(new View.OnClickListener() {

			 @ Override
			public void onClick(View v) {
				sd.show();
				Log.e("show", sd.toString());
			}
		});

		wv = (WebView)findViewById(R.id.wv);
		wv.getSettings().setJavaScriptEnabled(true);
		wv.loadUrl("file:///android_asset/GoogleMap.html");
		wv.addJavascriptInterface(this, "js_debug");
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, MainActivity.this);

	}

	 @ JavascriptInterface
	public void Log(String tmp1, String tmp2) {
		Log.i("debug", "tmp1 = " + tmp1 + " tmp2 = " + tmp2);
	}

	 @ Override
	public void onLocationChanged(Location location) {
		Log.e("tag", location.toString());
		Double longitude = location.getLongitude();
		Double latitude = location.getLatitude();
		locationManager.removeUpdates(MainActivity.this);
		wv.loadUrl("javascript:initmap('" + latitude + "','" + longitude + "')");
	}

	 @ Override
	public void onProviderDisabled(String provider) {}

	 @ Override
	public void onProviderEnabled(String provider) {}

	 @ Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

}

