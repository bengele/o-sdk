package com.ntt.core.service.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class SDictTransResultEntity implements Parcelable {
    private int dictType;
    private String query;
    private int errorCode;
    private int contentType;
    private String content;

    public SDictTransResultEntity(){}

    public int getDictType() {
        return dictType;
    }

    public void setDictType(int dictType) {
        this.dictType = dictType;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    protected SDictTransResultEntity(Parcel in) {
        dictType = in.readInt();
        query = in.readString();
        errorCode = in.readInt();
        contentType = in.readInt();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dictType);
        dest.writeString(query);
        dest.writeInt(errorCode);
        dest.writeInt(contentType);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SDictTransResultEntity> CREATOR = new Creator<SDictTransResultEntity>() {
        @Override
        public SDictTransResultEntity createFromParcel(Parcel in) {
            return new SDictTransResultEntity(in);
        }

        @Override
        public SDictTransResultEntity[] newArray(int size) {
            return new SDictTransResultEntity[size];
        }
    };

    public void readFromParcel(Parcel in) {
        dictType = in.readInt();
        query = in.readString();
        errorCode = in.readInt();
        contentType = in.readInt();
        content = in.readString();
    }
}