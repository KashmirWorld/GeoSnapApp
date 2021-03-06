package org.kashmirworldfoundation.WildlifeGeoSnap.study.station.takedown;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.kashmirworldfoundation.WildlifeGeoSnap.R;
import org.kashmirworldfoundation.WildlifeGeoSnap.study.station.station.Takedown;

import java.util.ArrayList;

public class TakedownListActivity extends AppCompatActivity {


    public String AuthorS;
    private RecyclerView recyclerView;
    public String pathRecord;
    private ArrayList<Takedown> CTakedownArrayList= new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takedown__list);
        recyclerView=findViewById(R.id.RecyclerTakedown);
        AuthorS=getIntent().getStringExtra("stationN");
        pathRecord=getIntent().getStringExtra("path");
        //new RebaitAsyncTaskA(this).execute();

    }
    public void updateStationList(ArrayList<Takedown> s){
        CTakedownArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        TakedownListAdapter listAdapter=new TakedownListAdapter(CTakedownArrayList,this);
        recyclerView.setAdapter(listAdapter);

    }
}