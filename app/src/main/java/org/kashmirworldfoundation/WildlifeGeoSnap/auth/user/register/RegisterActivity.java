package org.kashmirworldfoundation.WildlifeGeoSnap.auth.user.register;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.kashmirworldfoundation.WildlifeGeoSnap.utils.email.GMailSender;
import org.kashmirworldfoundation.WildlifeGeoSnap.MainActivity;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.Member;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.Org;
import org.kashmirworldfoundation.WildlifeGeoSnap.R;
import org.kashmirworldfoundation.WildlifeGeoSnap.auth.org.RegisterOrgActivity;
import org.kashmirworldfoundation.WildlifeGeoSnap.firebase.types.Study;
import org.kashmirworldfoundation.WildlifeGeoSnap.utils.Utils;
import org.kashmirworldfoundation.WildlifeGeoSnap.auth.user.LoginActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {


    Org Forg;
    EditText mFullName, mJobTitle, mOrganization, mEmail, mPassword, mReEnter, mPhonenumber;
    Button mRegisterBtn;
    TextView mLoginBtn,mRegisterOrgB;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    Boolean mUser=false;
    StorageReference fStore;
    ArrayList<String> studies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName         = findViewById(R.id.fullName);
        mJobTitle         = findViewById(R.id.jobTitle);
        mEmail            = findViewById(R.id.email);
        mPhonenumber      = findViewById(R.id.phone);
        mPassword         = findViewById(R.id.password);
        mReEnter          = findViewById(R.id.reEnter);
        mRegisterBtn      = findViewById(R.id.registrationBtn);
        mLoginBtn         = findViewById(R.id.createText);
        mRegisterOrgB = findViewById(R.id.RegisterOrgB);
        fAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar2);
        fStore = FirebaseStorage.getInstance().getReference();






        mRegisterOrgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterOrgActivity.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=getIntent();
                db = FirebaseFirestore.getInstance();
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String reEnter = mReEnter.getText().toString().trim();
                final String fullName = mFullName.getText().toString().trim();

                final String phoneNumber = mPhonenumber.getText().toString().trim();
                final String job = mJobTitle.getText().toString().trim();
                final String organization = i.getStringExtra("OrgName");
                //final String region =i.getStringExtra("Region");
                final String country = i.getStringExtra("Country");
                if(TextUtils.isEmpty(fullName)){
                    mFullName.setError("Full name is required.");
                    return;
                }
                if(TextUtils.isEmpty(organization)){
                    mOrganization.setError("Organization is required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is required.");
                    return;
                }
                Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                Matcher matcher = pattern.matcher(email);
                if (!matcher.matches()){
                    mEmail.setError("Invalid Email.");
                    return;
                }

                if(TextUtils.isEmpty(phoneNumber)){
                    mPhonenumber.setError("Phone number is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }
                if(password.length() < 6){
                    mPassword.setError("Password must be at least 6 characters.");
                    return;
                }
                if(TextUtils.isEmpty(reEnter)){
                    mReEnter.setError("Re-enter password.");
                    return;
                }
                if(!reEnter.equals(password)){
                    mReEnter.setError("Password do not match.");
                    return;
                }


                Utils util = Utils.getInstance();


                if (util.getAgreement(RegisterActivity.this)){
                    LayoutInflater inflater= LayoutInflater.from(RegisterActivity.this);
                    View view=inflater.inflate(R.layout.disclaimer_layout, null);


                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(RegisterActivity.this);
                    alertDialog.setTitle("Terms of Service");
                    alertDialog.setView(view);
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(RegisterActivity.this,"Agreement needed to register",Toast.LENGTH_LONG).show();
                        }

                    });

                    alertDialog.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            util.setAgreement(RegisterActivity.this);
                            register(organization,country);
                        }
                    });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
                else {
                    register(organization,country);
                }




            }
        });

    }
    private void sendMessage(final String name, final String phone, final String email, final String job,final String receive) {
        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            String Body="name = " +name +" \n phone = " + phone+ "\n email =" + email + "\njob = " + job;
            String subject = name + " wants to join your organization";
            public void run() {
                try {
                    GMailSender sender = new GMailSender("adm1nkwf1675@gmail.com", "Chowder1675!");
                    sender.sendMail(subject,
                            Body,
                            "adm1nkwf1675@gmail.com",
                            receive);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    private void saveCamNum(){
        SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences("camstations",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("CamNum",0);
        editor.apply();
    }
    private void saveStudies(ArrayList<String> studies){
        SharedPreferences sharedPreferences = RegisterActivity.this.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();


        Gson gson = new Gson();
        String json =gson.toJson(studies);
        editor.putString("studies",json);
        editor.apply();
    }

    private void register(String organization, String country){
        final String email = mEmail.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String reEnter = mReEnter.getText().toString().trim();
        final String fullName = mFullName.getText().toString().trim();
        studies=new ArrayList<>();
        studies.add("Pick a Study");
        final String phoneNumber = mPhonenumber.getText().toString().trim();
        final String job = mJobTitle.getText().toString().trim();

            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        db.collection("Organization").whereEqualTo("orgName", organization).
                                whereEqualTo("orgCountry", country).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String orgPath = "";
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        orgPath = documentSnapshot.getReference().getPath();
                                         Forg = documentSnapshot.toObject(Org.class);
                                    }
                                    final Member member =new Member(email, fullName, job, phoneNumber, Boolean.FALSE, orgPath, "profile/kwflogo.jpg");
                                    Member.setInstance(member);
                                    member.save(() -> {
                                        saveCamNum();
                                        sendMessage(fullName, phoneNumber, email, job, Forg.getOrgEmail());
                                        db.collection("Study").whereEqualTo("org", member.getOrg()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                        Study study = documentSnapshot.toObject(Study.class);
                                                        studies.add(study.getTitle());
                                                    }
                                                    if (studies == null || studies.size() == 1) {
                                                        studies.set(0, "No Studies");
                                                    }
                                                    saveStudies(studies);
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                } else {
                                                    studies.set(0, "No Studies");
                                                    saveStudies(studies);
                                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                                }
                                            }
                                        });
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    }


