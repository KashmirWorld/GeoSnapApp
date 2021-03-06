package org.kashmirworldfoundation.WildlifeGeoSnap.study.station;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.kashmirworldfoundation.WildlifeGeoSnap.GlideApp;
import org.kashmirworldfoundation.WildlifeGeoSnap.R;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.CameraStation;

import java.util.ArrayList;

public class StationListAdapter extends RecyclerView.Adapter<StationListViewHolder> {
    StationListActivity activity;
    //final public ListItemClickListener mOnClickListener;

    ArrayList<CameraStation> CstationList;
    Context mContext;
    CameraStation Cstation;
    StationListAdapter(ArrayList<CameraStation> stationList, StationListActivity activity){
        this.activity=activity;
        this.CstationList=stationList;

    }

    @NonNull
    @Override
    public StationListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(parent.getContext())
                //pass the list_row_item back to itemView
                .inflate(R.layout.row_station,parent,false);
        Log.e("layout", "inflated");
        //wait to open browser
        itemView.setOnClickListener(activity);
        //and pass the entry
        return new StationListViewHolder(itemView);

    }


    public void onBindViewHolder(@NonNull StationListViewHolder holder, int position) {
        Cstation=CstationList.get(position);

        holder.stationId.setText(Cstation.getStationId());
        holder.dateId.setText(Cstation.getPosted().toDate().toString());
        fetchData(Cstation.getPic(),holder.imgId);

    }
    private void fetchData(String location, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference(location);
        GlideApp.with(activity)
                .load(ref)
                .into(image);
    }







    public int getItemCount() {
        return CstationList.size();
    }

}
