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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements StartTestingListener {

    private ArrayList<MyPoints> pointsArryList = new ArrayList<>();
    private RecyclerView recyclerView;
    private int pointCount;
    private EditText editText;
    private Button start;
    private pointsAdapter pointsAdapter;
    private TextView note;
    private LinearLayout testingLayout;
    private ArrayList<MyPoints> LocationPoints;
    private TextView LocationPointsDetails,testingTextView;
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(getApplicationContext(), GpsService.class));
        recyclerView = (RecyclerView) findViewById(R.id.recycler_places);
        editText = (EditText) findViewById(R.id.edittext_count);
        note = (TextView) findViewById(R.id.note);
        start = (Button) findViewById(R.id.start_btn);
        testingTextView =(TextView)findViewById(R.id.textview_tested_point);
        testingLayout = (LinearLayout) findViewById(R.id.testing_layout);
        LocationPointsDetails = (TextView) findViewById(R.id.textview_details);
        pointsAdapter = new pointsAdapter(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(pointsAdapter);
    }


    public void startSetPoints(View view) {
        if (editText.getText().toString().equals("")) {
            Toast.makeText(this, "please enter points #", Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(editText.getText().toString()) <= 2) {

            Toast.makeText(this, "points must be more than 2 ", Toast.LENGTH_SHORT).show();
        } else {
            pointCount = Integer.parseInt(editText.getText().toString());
            pointsAdapter.setPointsCount(pointCount);
            start.setText("Reset");
            note.setVisibility(View.VISIBLE);
            testingTextView.setText("Wait ...");
            testingLayout.setVisibility(View.GONE);
        }


    }

    public void testPoint(View view) {
        testingTextView.setVisibility(View.VISIBLE);
        double[] lats = new double[LocationPoints.size()];
        double[] lons = new double[LocationPoints.size()];
        for (int x = 0; x < LocationPoints.size(); x++) {
            lats[x]=LocationPoints.get(x).getLat();
            lons[x]=LocationPoints.get(x).getLon();
        }
        double maxLat = maxValue(lats);
        double minLat = minValue(lats);
        double maxLon = maxValue(lons);
        double minLon = minValue(lons);
        getComparedPoint(maxLat, minLat, maxLon, minLon);
    }

    private void getComparedPoint(final double maxLat, final double minLat, final double maxLon, final double minLon) {
        final double[] lat = new double[1];
        final double[] lon = new double[1];
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (lat[0] == 0.0) {
                    lat[0] = intent.getExtras().getDouble("lati");
                    lon[0] = intent.getExtras().getDouble("long");
                    if (lat[0] != 0.0) {
                        context.unregisterReceiver(receiver);
                        receiver = null;
                        if (lat[0] < minLat || lat[0] > maxLat || lon[0] < minLon || lon[0] > maxLon) {
                            testingTextView.setText("this point is not in your Location");
                        } else {
                            testingTextView.setText("this point is in your Location");

                        }
                    }
                }
            }
        };
        registerReceiver(receiver, new IntentFilter("location_update"));
    }

    @Override
    public void startTesting(ArrayList<MyPoints> myPoints) {
        LocationPoints = myPoints;
        testingLayout.setVisibility(View.VISIBLE);
        note.setVisibility(View.GONE);
        String s = "";
        for (int x = 0; x < LocationPoints.size(); x++) {
            s += "P--> " + (x + 1) + "\n" + "lat = " + LocationPoints.get(x).getLat() + "\n" +
                    "lon = " + LocationPoints.get(x).getLon() + "\n" +
                    "alti = " + LocationPoints.get(x).getLat() + "\n\n";
        }
        LocationPointsDetails.setText(s);
    }

    private double maxValue(double[] arr) {
        double max = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] > max) {
                max = arr[x];
            }
        }
        return max;
    }

    private double minValue(double[] arr) {
        double min = arr[0];
        for (int x = 1; x < arr.length; x++) {
            if (arr[x] < min) {
                min = arr[x];
            }
        }
        return min;
    }


}
