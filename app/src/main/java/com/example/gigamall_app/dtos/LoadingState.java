package com.example.gigamall_app.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class LoadingState implements Parcelable {
    private int currentPage;
    private boolean isLastPage, isLoading, searched;

    public LoadingState(int currentPage, boolean isLastPage, boolean isLoading, boolean searched) {
        this.currentPage = currentPage;
        this.isLastPage = isLastPage;
        this.isLoading = isLoading;
        this.searched = searched;
    }

    protected LoadingState(Parcel in) {
        currentPage = in.readInt();
        isLastPage = in.readByte() != 0;
        isLoading = in.readByte() != 0;
        searched = in.readByte() != 0;
    }

    public static final Creator<LoadingState> CREATOR = new Creator<LoadingState>() {
        @Override
        public LoadingState createFromParcel(Parcel in) {
            return new LoadingState(in);
        }

        @Override
        public LoadingState[] newArray(int size) {
            return new LoadingState[size];
        }
    };

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public boolean isSearched() {
        return searched;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(currentPage);
        parcel.writeByte((byte) (isLastPage ? 1 : 0));
        parcel.writeByte((byte) (isLoading ? 1 : 0));
        parcel.writeByte((byte) (searched ? 1 : 0));
    }
}
