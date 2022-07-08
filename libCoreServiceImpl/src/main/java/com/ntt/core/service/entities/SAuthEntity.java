package com.ntt.core.service.entities;

import android.os.Parcel;
import android.os.Parcelable;


public final class SAuthEntity implements Parcelable {
    private String token_type;
    private String access_token;
    private long expires_in;
    private String refreshToken;
    private long uid;
    private long ts;
    private String uptoken;
    private String key;

    public SAuthEntity(){}

    protected SAuthEntity(Parcel in) {
        token_type = in.readString();
        access_token = in.readString();
        expires_in = in.readLong();
        refreshToken = in.readString();
        uid = in.readLong();
        ts = in.readLong();
        uptoken = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(token_type);
        dest.writeString(access_token);
        dest.writeLong(expires_in);
        dest.writeString(refreshToken);
        dest.writeLong(uid);
        dest.writeLong(ts);
        dest.writeString(uptoken);
        dest.writeString(key);
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccess_token(String accessToken) {
        this.access_token = accessToken;
    }

    public long getExpiresIn() {
        return expires_in;
    }

    public void setExpires_in(long expiresIn) {
        this.expires_in = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public String getUptoken() {
        return uptoken;
    }

    public void setUptoken(String uptoken) {
        this.uptoken = uptoken;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public static final Creator<SAuthEntity> CREATOR = new Creator<SAuthEntity>() {
        @Override
        public SAuthEntity createFromParcel(Parcel in) {
            return new SAuthEntity(in);
        }

        @Override
        public SAuthEntity[] newArray(int size) {
            return new SAuthEntity[size];
        }
    };

}
