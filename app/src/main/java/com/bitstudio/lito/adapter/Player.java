package com.bitstudio.lito.adapter;

import android.media.MediaPlayer;
import android.os.AsyncTask;

import com.bitstudio.lito.MainActivity;

import java.util.Timer;

/**
 * Created by LN Quy on 16/04/2018.
 */

public class Player extends AsyncTask<String, Void, Boolean> {
    MediaPlayer mp;

    public Player(MediaPlayer mp) {
        this.mp = mp;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Boolean prepared = false;

        try {
            mp.setDataSource(strings[0]);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
//                        initialStage = true;
//                        playPause = false;
//                        btn.setText("Launch Streaming");
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });



            mp.prepare();
            prepared = true;

        } catch (Exception e) {
            prepared = false;
        }

        return prepared;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
//
//            if (progressDialog.isShowing()) {
//                progressDialog.cancel();
//            }

        mp.start();
        MainActivity.playing = true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//            progressDialog.setMessage("Buffering...");
//            progressDialog.show();
    }



    /**
     * Function to convert milliseconds time to Timer Format
     * Hours:Minutes:Seconds
     * */


}