package com.example.notes;

import com.google.firebase.firestore.DocumentSnapshot;

public interface NoteActionHandler {
    public void onCheckboxClick(boolean isChecked, DocumentSnapshot snapshot);
    public void onRecyclerItemLongClick(DocumentSnapshot snapshot);
    public void onRecyclerItemDelete(DocumentSnapshot snapshot);

}
