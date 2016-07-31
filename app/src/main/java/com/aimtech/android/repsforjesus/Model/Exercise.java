package com.aimtech.android.repsforjesus.Model;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by Andy on 30/07/2016.
 */
public class Exercise {

    private Double mCurrentWeight;
    private Double mPreviousWeight;
    private Date mDateLastUpdated;
    private String mName;

    public Exercise(String mName, @Nullable Double mCurrentWeight, @Nullable Double mPreviousWeight, @Nullable Date mDateLastUpdated ) {
        this.mName = mName;

        if(mPreviousWeight != null){
            this.mPreviousWeight = mPreviousWeight;
        }

        if(mCurrentWeight != null){
            this.mCurrentWeight = mCurrentWeight;
        }

        if(mDateLastUpdated != null){
            this.mDateLastUpdated = mDateLastUpdated;
        }


    }

    public Double getCurrentWeight() {
        return mCurrentWeight;
    }

    public void setCurrentWeight(Double mCurrentWeight) {
        this.mCurrentWeight = mCurrentWeight;
    }

    public Double getPreviousWeight() {
        return mPreviousWeight;
    }

    public void setPreviousWeight(Double mPreviousWeight) {
        this.mPreviousWeight = mPreviousWeight;
    }

    public Date getDateLastUpdated() {
        return mDateLastUpdated;
    }

    public void setDateLastUpdated(Date mDateLastUpdated) {
        this.mDateLastUpdated = mDateLastUpdated;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }
}
