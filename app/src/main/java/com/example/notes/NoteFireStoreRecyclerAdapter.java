package com.example.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;

public class NoteFireStoreRecyclerAdapter extends FirestoreRecyclerAdapter<Note, NoteFireStoreRecyclerAdapter.NoteViewHolder> {

   NoteActionHandler noteActionHandler;
    public NoteFireStoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Note> options, NoteActionHandler noteActionHandler) {
        super(options);
        this.noteActionHandler=noteActionHandler;

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {

        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.description.setText(note.getDescription());
        noteViewHolder.checkBox.setChecked(note.getComplete());

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.note_item,parent,false);

        return new NoteViewHolder(view) ;
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView title;
        MaterialTextView description;
        MaterialCheckBox checkBox;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.note_item_title);
            description=itemView.findViewById(R.id.note_item_description);
            checkBox=itemView.findViewById(R.id.note_item_checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

                    DocumentSnapshot snapshot=getSnapshots().getSnapshot(getAdapterPosition());
                    //since oncheckedchangelistener called twice because the intial state of checkbox is false by default and assign note getcomplete
                    // vaiable from firestore (true/false) , that's why it is called two times.
                    Note note=getItem(getAdapterPosition());
                    if(note.getComplete()!=ischecked){
                        noteActionHandler.onCheckboxClick(ischecked,snapshot);
                    }
                }
            });

        }

    }
}

