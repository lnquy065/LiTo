package com.bitstudio.lito;

import android.content.Intent;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.bitstudio.lito.adapter.Player;
import com.bitstudio.lito.adapter.SongsAdapter;
import com.bitstudio.lito.models.FirebaseSong;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Button btnAdd;
    private TextView lbConnectionID;
    private EditText txtURL;

    private ListView lvSongList;
    public static String mID;
    public static boolean playing = false;
    public static FirebaseSong Song = null;
    public static boolean Slave = false;

    public TextView lbPlayingSongTitle;
    public TextView lbPlayingSongArtist;

    private ImageButton btnConnect;
    private EditText txtPartnerID;

    Timer timer = new Timer();

    //firebase
    DatabaseReference dbUsers;
    DatabaseReference dbFbSongs;
    DatabaseReference dbUserMaster;

    //media
    private MediaPlayer mediaPlayer;
    public TextView lbPlayingTime;

    private ArrayList<FirebaseSong> alSong;
    private SongsAdapter songsAdapter;

    //playing
    private ImageButton btnPlayingPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mID = getDeviceName();

        dbUsers = FirebaseDatabase.getInstance().getReference("Users");
        dbFbSongs = FirebaseDatabase.getInstance().getReference("Songs");

        loadControls();
        loadEvents();
    }

    private void loadControls() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        btnAdd = findViewById(R.id.btnAdd);
        lvSongList = findViewById(R.id.lvSongList);
        lbPlayingTime = findViewById(R.id.lbPlayingTime);

        lbPlayingSongTitle = findViewById(R.id.lbPlayingSongTitle);
        lbPlayingSongArtist = findViewById(R.id.lbPlayingSongArtist);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lbPlayingTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
//                            dbUsers.child(mID).child("Playing").child("State").setValue(true);
//                            dbUsers.child(mID).child("Playing").child("SongID").setValue(MainActivity.Song.getId());
//                            dbUsers.child(mID).child("Playing").child("Pos").setValue(mediaPlayer.getCurrentPosition());

                        }
                    });
                }
            }
        }, 0, 1000);




        alSong = new ArrayList<>();
        songsAdapter = new SongsAdapter(this, R.layout.item_song, alSong, mediaPlayer, this);

        lvSongList.setAdapter(songsAdapter);

    }

    private void loadEvents() {
        btnAdd.setOnClickListener( v -> {
            Intent intent = new Intent(getApplicationContext(), FireBaseSongListActivity.class);
            startActivity(intent);
        });

        dbUsers.child(mID).child("Songs").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String songID = dataSnapshot.getKey();
                dbFbSongs.child(songID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap hSong = (HashMap) dataSnapshot.getValue();
                        alSong.add(new FirebaseSong(songID, hSong.get("Title").toString(),
                                hSong.get("Artist").toString(),
                                hSong.get("URL").toString(),
                                hSong.get("Img").toString()));
                        songsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

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


    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public  String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";

        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        return finalTimerString;
    }
}
