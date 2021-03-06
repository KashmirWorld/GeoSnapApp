package org.kashmirworldfoundation.WildlifeGeoSnap.study.station.rebait;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.kashmirworldfoundation.WildlifeGeoSnap.R;
import org.kashmirworldfoundation.WildlifeGeoSnap.study.station.rebait.tasks.RebaitAsyncTaskA;

import java.util.ArrayList;

public class RebaitListActivity extends AppCompatActivity {


    public String AuthorS;
    private RecyclerView recyclerView;
    public String pathRecord;
    private ArrayList<Rebait> CRebaitArrayList= new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebait__list);
        recyclerView=findViewById(R.id.RecyclerRebait2);
        AuthorS=getIntent().getStringExtra("stationN");
        pathRecord=getIntent().getStringExtra("path");
        new RebaitAsyncTaskA(this).execute();

    }
    public void updateStationList(ArrayList<Rebait> s){
        CRebaitArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        LinearLayoutManager manager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(manager);
        RebaitListAdapter listAdapter=new RebaitListAdapter(CRebaitArrayList,this);
        recyclerView.setAdapter(listAdapter);

    }
}