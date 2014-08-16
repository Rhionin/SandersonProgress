package com.rawonion.www.sandersonprogress;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CJ on 7/19/2014.
 */
public class WorkInProgress implements Parcelable{

    String title;
    int progress;

    public WorkInProgress(String title, int progress) {
        this.setTitle(title);
        this.setProgress(progress);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return String.format("%s: %d %%", title, progress);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.getTitle());
        parcel.writeInt(this.getProgress());
    }

    public void readFromParcel(Parcel in) {
        String title = in.readString();
        int progress = in.readInt();
        this.setTitle(title);
        this.setProgress(progress);
    }

    public static final Parcelable.Creator<WorkInProgress> CREATOR = new Parcelable.Creator<WorkInProgress>() {
        public WorkInProgress createFromParcel(Parcel in) {
            return new WorkInProgress(in);
        }

        public WorkInProgress[] newArray(int size) {
            System.out.println(String.format("Creating array of size %d", size));
            return new WorkInProgress[size];
        }
    };

    private WorkInProgress(Parcel in) {
        readFromParcel(in);
    }
}
