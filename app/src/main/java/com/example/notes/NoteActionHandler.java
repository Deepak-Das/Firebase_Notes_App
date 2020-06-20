package com.example.notes;

import com.google.firebase.firestore.DocumentSnapshot;

public interface NoteActionHandler {
    public void onCheckboxClick(boolean isChecked, DocumentSnapshot snapshot);
    public void onRecyclerItemClick();
    public void onRecyclerItemDelete(DocumentSnapshot snapshot);

}
