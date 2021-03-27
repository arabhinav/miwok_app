package com.example.android.miwok;

public class Word {

    private String mDefaultTranslation;

    private String mMiwokTranslation;

    //Image Resource ID for the word
    private int mImageResourceId = -1;

    //Play button image resource id
    private int mPlayButton;

    //Audio Resource ID for the word
    private int mAudioResourceId;

    public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    public Word(String defaultTranslation, String miwokTranslation, int imageResourceId, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    /*Get default translation of the word*/
    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }
    /*Get Miwok translation of the word*/
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }

    //Return image resource ID value for the word
    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != -1;
    }

    //Return the audio resource ID value for the word
    public int getmAudioResourceId() { return mAudioResourceId; }
}
