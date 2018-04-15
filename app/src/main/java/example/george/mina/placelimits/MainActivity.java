package example.george.mina.placelimits;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    BroadcastReceiver receiver;
    ArrayList<MyPoints> pointsArryList = new ArrayList<>();
    ArrayList<myPointsInXY> newPoints = new ArrayList<>();
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.text);
        startService(new Intent(getApplicationContext(), GpsService.class));

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (!flag) {
                        if (pointsArryList.size() == 4) {
                            stopService(new Intent(getApplicationContext(), GpsService.class));
                            Toast.makeText(MainActivity.this, "Service Stoped", Toast.LENGTH_SHORT).show();
                            calculatePolygon();
                        } else {
                            String lat = intent.getExtras().getString("lati");
                            String lon = intent.getExtras().getString("long");
                            pointsArryList.add(new MyPoints(lat, lon));
                            textView.setText(lat + "\n" + lon);
                            Log.d("sssss", lat + "\n" + lon);
                        }
                    }
                }

            };
        }
        registerReceiver(receiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    void calculatePolygon() {
        for (MyPoints p : pointsArryList) {
            double R = 6371;
            double lat = Double.parseDouble(p.getLat());
            double lon = Double.parseDouble(p.getLon());

            double x = R * Math.cos(lat) * Math.cos(lon);
            double y = R * Math.cos(lat) * Math.sin(lon);
            newPoints.add(new myPointsInXY(x, y));
        }
        Log.d("dddf", newPoints.toString());
    }
}
