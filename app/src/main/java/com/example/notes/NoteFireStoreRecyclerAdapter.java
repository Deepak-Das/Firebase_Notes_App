package com.example.notes;

import android.text.format.DateFormat;
import android.util.Log;
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

    private static final String TAG = "NoteFireStoreRecyclerAd";
    NoteActionHandler noteActionHandler;

    public NoteFireStoreRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Note> options, NoteActionHandler noteActionHandler) {
        super(options);
        this.noteActionHandler = noteActionHandler;

    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Note note) {

        noteViewHolder.title.setText(note.getTitle());
        noteViewHolder.description.setText(note.getDescription());
        noteViewHolder.checkBox.setChecked(note.getComplete());

        CharSequence dateCharSeq= DateFormat.format("EEEE, MMM d, yyyy h:mm:ss a",note.getOncreate().toDate());
        noteViewHolder.dateTime.setText(dateCharSeq);

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.note_item, parent, false);

        return new NoteViewHolder(view);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView title;
        MaterialTextView description;
        MaterialTextView dateTime;
        MaterialCheckBox checkBox;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.note_item_title);
            description = itemView.findViewById(R.id.note_item_description);
            dateTime = itemView.findViewById(R.id.note_item_date_time);
            checkBox = itemView.findViewById(R.id.note_item_checkbox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {

                    DocumentSnapshot snapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    //since oncheckedchangelistener called twice because the intial state of checkbox is false by default and assign note getcomplete
                    // vaiable from firestore (true/false) , that's why it is called two times.
                    Note note = getItem(getAdapterPosition());
                    if (note.getComplete() != ischecked) {
                        noteActionHandler.onCheckboxClick(ischecked, snapshot);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    noteActionHandler.onRecyclerItemClick();
                }
            });

        }

        public void deleteItem() {
            Log.d(TAG, "deleteItem: " + getAdapterPosition());
            DocumentSnapshot snapshot=getSnapshots().getSnapshot(getAdapterPosition());
            noteActionHandler.onRecyclerItemDelete(snapshot);
        }

    }
}

