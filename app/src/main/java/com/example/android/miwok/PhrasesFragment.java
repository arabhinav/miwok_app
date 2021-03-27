package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhrasesFragment# new Instance} factory method to
 * create an instance of this fragment.
 */

public class PhrasesFragment extends Fragment {

    /** Handles playback of all the sound files */
    private MediaPlayer mMediaPlayer;

    /** Handles audio focus when playing a sound file */
    private AudioManager mAudioManager;

    /**
     * This listener gets triggered whenever the audio focus changes
     * (i.e., we gain or lose audio focus because of another app or device).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                // The AUDIOFOCUS_LOSS_TRANSIENT case means that we've lost audio focus for a
                // short amount of time. The AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that
                // our app is allowed to continue playing sound but at a lower volume. We'll treat
                // both cases the same way because our app is playing short sound files.

                // Pause playback and reset player to the start of the file. That way, we can
                // play the word from the beginning when we resume playback.
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                // The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback.
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                // The AUDIOFOCUS_LOSS case means we've lost audio focus and
                // Stop playback and clean up resources
                releaseMediaPlayer();
            }
        }
    };

    /**
     * This listener gets triggered when the {@link MediaPlayer} has completed
     * playing the audio file.
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("Where are you going?","minto wuksus", R.raw.phrase_where_are_you_going));
        words.add(new Word("What is your name?","tinnә oyaase'nә", R.raw.phrase_what_is_your_name));
        words.add(new Word("My name is...","oyaaset...", R.raw.phrase_my_name_is));
        words.add(new Word("How are you feeling? ","michәksәs?", R.raw.phrase_how_are_you_feeling));
        words.add(new Word("I'm feeling good.","taachi", R.raw.phrase_im_feeling_good));
        words.add(new Word("Are you coming?","әәnәs'aa?", R.raw.phrase_are_you_coming));
        words.add(new Word("Yes, I'm comimg.","hәә’ әәnәm", R.raw.phrase_yes_im_coming));
        words.add(new Word("I'm coming.","әәnәm", R.raw.phrase_im_coming));
        words.add(new Word("Let's go.","yoowutis", R.raw.phrase_lets_go));
        words.add(new Word("Come here.","әnni'nem", R.raw.phrase_come_here));

        WordAdapter itemAdapter = new WordAdapter(getActivity(), words, R.color.category_phrases);

        ListView listView = (ListView) rootView.findViewById(R.id.list);

        assert listView != null;
        listView.setAdapter(itemAdapter);

        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // i is the position(which starts from 0) of clicked item of list
                Word word = words.get(i);

                //Release media player if it currently exists as we are about to play  different sound file
                releaseMediaPlayer();

                //Request audio focus from playback
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
                        //Use the music stream
                        AudioManager.STREAM_MUSIC,
                        //Request short focus
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //Got audio focus
                    //Now passing the audio resource id to media player
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getmAudioResourceId());
                    mMediaPlayer.start();// no need to call prepare(); create does that for you
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

        return rootView;
    }



    //Clean up the media player by releasing its resources.
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            //Regardless of whether or not we were granted audio focus abandon it.
            // This also unregisters the AudioFocusChangeListner so we don't get anymore callbacks
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //When the app stops release media player
        releaseMediaPlayer();
    }

}