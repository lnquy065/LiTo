package com.bitstudio.lito.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitstudio.lito.MainActivity;
import com.bitstudio.lito.R;
import com.bitstudio.lito.models.FirebaseSong;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by LN Quy on 14/04/2018.
 */

public class FirebaseSongsAdapter extends ArrayAdapter<FirebaseSong> {
    private Activity context;
    private int resource;
    private ArrayList<FirebaseSong> objects;
    private DatabaseReference dbUsers;

    public FirebaseSongsAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<FirebaseSong> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;

        dbUsers = FirebaseDatabase.getInstance().getReference("Users");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =  this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        FirebaseSong song = objects.get(position);

        TextView lbFBSongTitle = row.findViewById(R.id.lbFBSongTitle);
        TextView lbFBSongArtist = row.findViewById(R.id.lbFBSongArtist);
        ImageView imFBSong = row.findViewById(R.id.imFBSong);
        ImageButton btnFBSongAdd = row.findViewById(R.id.btnFBSongAdd);

        lbFBSongTitle.setText(song.getName());
        lbFBSongArtist.setText(song.getArtist());

        btnFBSongAdd.setOnClickListener( v -> {
                dbUsers.child(MainActivity.mID).child("Songs").child(song.getId()).setValue(true);
            Toast.makeText(getContext(), "Song added", Toast.LENGTH_SHORT).show();
        });


        return row;
    }
}
