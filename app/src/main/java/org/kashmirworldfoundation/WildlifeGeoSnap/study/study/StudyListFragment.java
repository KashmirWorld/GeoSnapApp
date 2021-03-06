package org.kashmirworldfoundation.WildlifeGeoSnap.study.study;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.FileOutputStream;

import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.kashmirworldfoundation.WildlifeGeoSnap.study.station.tasks.StationAsyncTask;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.CameraStation;

import org.kashmirworldfoundation.WildlifeGeoSnap.R;
import org.kashmirworldfoundation.WildlifeGeoSnap.study.station.StationListActivity;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.Study;


import java.util.ArrayList;


import android.widget.Toast;

/**
 * put in floating action button for downloading
 */

public class StudyListFragment extends Fragment implements View.OnClickListener  {

    // objects
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private View ListFragment;

    private static final String TAG = "StudyListFragment";

    private static final int PERMISSION_REQUEST_CODE = 100;
    private TextView Title;
    private RecyclerView recyclerView;
    private StudyListFragmentAdapter studyListFragmentAdapter;
    private ArrayList<Study> CStationArrayList;
    private ArrayList<CameraStation> CStationArrayList2;
    private int WRITE_FILE=1;

    private int pos;
    boolean Available= false;
    boolean Readable= false;
    private FileOutputStream fstream;

    private int option=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {;

        ListFragment=inflater.inflate(R.layout.fragment_study_list, container, false);
        recyclerView=ListFragment.findViewById(R.id.recyler);
        Title = ListFragment.findViewById(R.id.listOfStudyLabel);
        CStationArrayList= new ArrayList<>();

        studyListFragmentAdapter =new StudyListFragmentAdapter(CStationArrayList,this);
        Title.setText("List of Studies");
        recyclerView.setAdapter(studyListFragmentAdapter);
        LinearLayoutManager manager=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(manager);








        // Add data from Firebase on the the Arrays
        new StationAsyncTask(this).execute();

        return ListFragment;
    }

    @Override

    public void onClick(View v) {
        pos =recyclerView.getChildLayoutPosition(v);
        Study selectiion=CStationArrayList.get(pos);
        Intent intent = new Intent(getContext(), StationListActivity.class);
        intent.putExtra("CurrentStudy", selectiion.getTitle());
        startActivity(intent);
    }



    //SationAsyncTask would update this
    public void updateStationList(ArrayList<Study> s){
        CStationArrayList.addAll(s);
    }

    //after list was already update, it create the adapter, put the list and show
    public void updateList(){
        studyListFragmentAdapter =new StudyListFragmentAdapter(CStationArrayList,this);
        recyclerView.setAdapter(studyListFragmentAdapter);
        LinearLayoutManager a=new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(a);


    }


    @Override
    public void onResume(){
        super.onResume();
    }


    private boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Write External Storage permission allows us to create files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");

                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public static StudyListFragment newInstance() {
        return new StudyListFragment();
    }


    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void createToast(Context context, String message, int time) {
        Toast toast = Toast.makeText(context, "" + message, time);
        View toastView = toast.getView();
        toastView.setBackgroundColor(Color.parseColor("#008577"));
        TextView tv = toast.getView().findViewById(android.R.id.message);
        tv.setPadding(140, 75, 140, 75);
        tv.setTextColor(0xFFFFFFFF);
        toast.show();
    }



//    public static void studyMiss(ArrayList<String> Studies,StudyListFragment li){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
//        alertDialog.setTitle("Attention");
//        String out= "Studies ";
//        for (String stud:Studies){
//            out=out+stud+", ";
//        }
//        out+="scheduled to be deleted in a week or less111";
//        alertDialog.setMessage(out);
//
//        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//
//            }
//        });
//        AlertDialog alert = alertDialog.create();
//        alert.show();
//    }

    public void studyMissOneDay(ArrayList<String> Studies, StudyListFragment li){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
        alertDialog.setTitle("Attention");
        String out= "Studies ";
        for (String stud:Studies){
            out=out+stud+", ";
        }
        out+="scheduled to be deleted in one day.";
        alertDialog.setMessage(out);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void studyMissTwoDays(ArrayList<String> Studies, StudyListFragment li){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
        alertDialog.setTitle("Attention");
        String out= "Studies ";
        for (String stud:Studies){
            out=out+stud+", ";
        }
        out+="scheduled to be deleted in two days.";
        alertDialog.setMessage(out);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void studyMissOneMonth(ArrayList<String> Studies, StudyListFragment li){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
        alertDialog.setTitle("Attention");
        String out= "Studies ";
        for (String stud:Studies){
            out=out+stud+", ";
        }
        out+="scheduled to be deleted in one month.";
        alertDialog.setMessage(out);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void studyMissTwoMonths(ArrayList<String> Studies, StudyListFragment li){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
        alertDialog.setTitle("Attention");
        String out= "Studies ";
        for (String stud:Studies){
            out=out+stud+", ";
        }
        out+="scheduled to be deleted in two months.";
        alertDialog.setMessage(out);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    public void studyMissThreeMonths(ArrayList<String> Studies, StudyListFragment li){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(li.getContext());
        alertDialog.setTitle("Attention");
        String out= "Studies ";
        for (String stud:Studies){
            out=out+stud+", ";
        }
        out+="scheduled to be deleted in three months.";
        alertDialog.setMessage(out);

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }


}
