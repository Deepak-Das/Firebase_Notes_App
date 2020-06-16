package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private static final String TAG = "MainActivity";
    
    private FloatingActionButton mfab;
    private MaterialButton mButton_Delete;
   
//    private TextInputLayout mTitle;
//    private TextInputLayout mDescription;

    private ProgressBar progressBar;
    private MaterialToolbar mtoolbar;

    private MaterialAlertDialogBuilder materialAlertDialogBuilder;
    
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore=FirebaseFirestore.getInstance();


        FirebaseFirestore.setLoggingEnabled(true);
        user=FirebaseAuth.getInstance().getCurrentUser();

        mtoolbar=findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mfab=findViewById(R.id.floating_action_button);
        mfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert_dialoge_show();
            }
        });

        mButton_Delete=findViewById(R.id.button_delete);
        mButton_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_note(v);
            }
        });


        
        
    }

    private void alert_dialoge_show() {

        //It's important to get Layout Inflater if u r finding the layout out of different activity or xml file
        // which is not associated with your current activity
        final View mView =getLayoutInflater().inflate(R.layout.alert_dialoge,null);
        materialAlertDialogBuilder=new MaterialAlertDialogBuilder(this);
        materialAlertDialogBuilder.setTitle("Add new notes");
        materialAlertDialogBuilder.setView(mView);
        materialAlertDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                add_note(mView);
            }
        });
        materialAlertDialogBuilder.show();
    }

    public void startLoginActivity(){
        Intent intent=new Intent(this,LoginAndRegister.class);
        startActivity(intent);
        finish();

    }

    private void logOutHandler() {
        AuthUI.getInstance().signOut(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_items,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case (R.id.profile):
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                return true;

            case (R.id.setting):
                Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
                return true;

            case (R.id.logOut):{
                logOutHandler();
                Toast.makeText(this, "log out successfully", Toast.LENGTH_SHORT).show();
                return true;
                }


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void add_note(View view) {

        //same view must be pass so that it can read input that form alert dialog which is created by Material Ui.
        //if directly fvbId() it will find the view in the same activity or if u create new View it will find value to the instance that
        //u have create, so its important to pass the same view which Material Alert builder is using.

        final TextInputEditText mTitle=view.findViewById(R.id.edite_text_title);
        final TextInputEditText mDescription=view.findViewById(R.id.edite_text_description);
        
        String title=mTitle.getText().toString();
        String description=mDescription.getText().toString();
        Log.d(TAG, "add_note: "+title);
        Log.d(TAG, "add_note: "+description);
            if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(description)){
                progressBar.setVisibility(View.VISIBLE);
    
    
                Note note=new Note(title,description,user.getUid(),false, new Timestamp(new Date()));
    
                firebaseFirestore.collection("Notes").add(note)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "onSuccess: "+documentReference.getId());
                                progressBar.setVisibility(View.GONE);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: "+e.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }else{
                Toast.makeText(this, "invalid! give input in both field", Toast.LENGTH_SHORT).show();
            }


    }

    private void delete_note(View v) {

        progressBar.setVisibility(View.VISIBLE);

        firebaseFirestore.collection("Notes")
                .whereEqualTo("user_id",user.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        WriteBatch batch=FirebaseFirestore.getInstance().batch();

                        List<DocumentSnapshot> snapshots=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot snapshot : snapshots){
                            batch.delete(snapshot.getReference());
                        }

                        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: doc deleted with brand Apple ");
                                progressBar.setVisibility(View.GONE);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: doc not deleted yet");
                                progressBar.setVisibility(View.GONE);

                            }
                        });

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startLoginActivity();
            return;
        }

    }
}
