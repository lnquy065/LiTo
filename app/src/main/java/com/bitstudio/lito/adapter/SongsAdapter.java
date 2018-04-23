package com.bitstudio.lito.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bitstudio.lito.MainActivity;
import com.bitstudio.lito.R;
import com.bitstudio.lito.models.FirebaseSong;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by LN Quy on 14/04/2018.
 */

public class SongsAdapter extends ArrayAdapter<FirebaseSong> {
    private Activity context;
    private int resource;
    private ArrayList<FirebaseSong> objects;
    private DatabaseReference dbUsers;
    private MediaPlayer mp;
    private MainActivity main;
    private Player player;

    public SongsAdapter(@NonNull Activity context, int resource, @NonNull ArrayList<FirebaseSong> objects, MediaPlayer mp, MainActivity main) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.mp = mp;
        this.main = main;
        dbUsers = FirebaseDatabase.getInstance().getReference("Users").child(MainActivity.mID);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater =  this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);

        FirebaseSong song = objects.get(position);

        TextView lbTitle = row.findViewById(R.id.lbPlayingSongListTitle);
        TextView lbArtist = row.findViewById(R.id.lbPlayingSongListArtist);

        ImageButton btnPlay = row.findViewById(R.id.btnPlayingSongListPlay);
        ImageButton btnRemove = row.findViewById(R.id.btnPlayingSongListRemove);

        lbTitle.setText(song.getName());
        lbArtist.setText(song.getArtist());
        btnRemove.setOnClickListener(v -> {
            dbUsers.child("Songs").child(song.getId()).removeValue();
            objects.remove(position);
            notifyDataSetChanged();

        });

        btnPlay.setOnClickListener(v -> {
            Log.d("Song", song.getUrl());
            if (mp.isPlaying()) {
                mp.stop();
                player.cancel(true);
            }
            player = new Player(mp);
            player.execute(song.getUrl().toString());
           main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    main.lbPlayingSongTitle.setText(song.getName());
                    main.lbPlayingSongArtist.setText(song.getArtist());
                }
            });
        });

        return row;
    }


}
