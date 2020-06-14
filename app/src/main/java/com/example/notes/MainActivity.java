package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.widgets.Snapshot;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Button mButton_Add;
    Button mButton_Delete;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseFirestore=FirebaseFirestore.getInstance();


        FirebaseFirestore.setLoggingEnabled(true);

        mButton_Add=findViewById(R.id.button_add);
        mButton_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_note(v);
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

    private void add_note(View v) {

        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();

//        Map<String,Object> note= new HashMap<>();
//        note.put("brand","Apple");
//        note.put("name","Iphone 11");
//        note.put("price","$299");

        Note note=new Note("Firebase","learn","123",false);
        
        firebaseFirestore.collection("Notes").add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: "+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage());
                    }
                });
    }

    private void delete_note(View v) {

        firebaseFirestore.collection("Notes")
                .whereEqualTo("user_id","123")
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
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: doc not deleted yet");
                            }
                        });

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}
