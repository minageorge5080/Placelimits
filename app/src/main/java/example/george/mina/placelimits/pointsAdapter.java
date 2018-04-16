package example.george.mina.placelimits;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import junit.framework.TestListener;

import java.util.ArrayList;

/**
 * Created by minageorge on 4/15/18.
 */

public class pointsAdapter extends RecyclerView.Adapter<pointsAdapter.ViewHolder> implements View.OnClickListener {
    private ArrayList<Integer> Items = new ArrayList<>();
    private Context context;
    private ArrayList<MyPoints> myPoints = new ArrayList<>();
    private BroadcastReceiver receiver;
    private StartTestingListener testingListener;

    public pointsAdapter(Context context) {
        this.context = context;
        this.testingListener = (StartTestingListener) context;

    }

    @Override
    public pointsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new pointsAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_point, null));
    }

    @Override
    public void onBindViewHolder(final pointsAdapter.ViewHolder holder, int position) {
        holder.placeCount.setText("Point : " + (Items.get(position) + 1));
        holder.view.setTag(position);
        holder.view.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public void setPointsCount(int count) {
        Items.clear();
        myPoints.clear();
        for (int x = 0; x < count; x++) {
            Items.add(x);
        }
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView placeCount;
        View view;


        public ViewHolder(final View itemView) {
            super(itemView);
            this.placeCount = itemView.findViewById(R.id.place_count);
            this.view = itemView;
        }
    }

    @Override
    public void onClick(View v) {
        final int pos = (int) v.getTag();
        final double[] lat = new double[1];
        final double[] lon = new double[1];
        final double[] alti = new double[1];
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (lat[0] == 0.0) {
                    lat[0] = intent.getExtras().getDouble("lati");
                    lon[0] = intent.getExtras().getDouble("long");
                    alti[0] = intent.getExtras().getDouble("alti");
                    if (lat[0] != 0.0) {
                        myPoints.add(new MyPoints(lat[0], lon[0], alti[0]));
                        context.unregisterReceiver(receiver);
                        receiver = null;
                        removeAt(pos);
                        if (Items.size() == 0) {
                            Toast.makeText(context, "location Points successfully inserted", Toast.LENGTH_SHORT).show();
                            testingListener.startTesting(myPoints);
                        }
                    }
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter("location_update"));
    }

    public void removeAt(int position) {
        Items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, Items.size());
    }
}
