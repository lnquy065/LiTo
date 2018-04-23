package com.bitstudio.lito;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.bitstudio.lito.adapter.FirebaseSongsAdapter;
import com.bitstudio.lito.models.FirebaseSong;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class FireBaseSongListActivity extends AppCompatActivity {
    private ListView lvFirebaseSongList;
    private ArrayList<FirebaseSong> alFirebaseSong;
    private FirebaseSongsAdapter firebaseSongsAdapter;

    private DatabaseReference songsFbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_base_song_list);

        loadControls();
        loadEvents();
    }

    private void loadEvents() {
        songsFbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HashMap h = (HashMap) dataSnapshot.getValue();
                alFirebaseSong.add(new FirebaseSong(dataSnapshot.getKey().toString(),
                        h.get("Title").toString(),
                        h.get("Artist").toString(),
                        h.get("URL").toString(),
                        h.get("Img").toString()));
                firebaseSongsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadControls() {
        lvFirebaseSongList = findViewById(R.id.lvFirebaseSongList);
        songsFbRef = FirebaseDatabase.getInstance().getReference("Songs");
        alFirebaseSong = new ArrayList<>();
        firebaseSongsAdapter = new FirebaseSongsAdapter(this, R.layout.item_firebase_song, alFirebaseSong);
        lvFirebaseSongList.setAdapter(firebaseSongsAdapter);
    }

}
