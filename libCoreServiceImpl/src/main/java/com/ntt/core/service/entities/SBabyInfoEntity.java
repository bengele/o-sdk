package com.ntt.core.service.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class SBabyInfoEntity implements Parcelable {
    private long uid;
    private String name;
    private long familyId;
    private String avatar;
    private String sex;
    private String birthday;
    private String updated;
    private String created;

    public SBabyInfoEntity(){}

    protected SBabyInfoEntity(Parcel in) {
        uid = in.readLong();
        name = in.readString();
        familyId = in.readLong();
        avatar = in.readString();
        sex = in.readString();
        birthday = in.readString();
        updated = in.readString();
        created = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uid);
        dest.writeString(name);
        dest.writeLong(familyId);
        dest.writeString(avatar);
        dest.writeString(sex);
        dest.writeString(birthday);
        dest.writeString(updated);
        dest.writeString(created);
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(long familyId) {
        this.familyId = familyId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SBabyInfoEntity> CREATOR = new Creator<SBabyInfoEntity>() {
        @Override
        public SBabyInfoEntity createFromParcel(Parcel in) {
            return new SBabyInfoEntity(in);
        }

        @Override
        public SBabyInfoEntity[] newArray(int size) {
            return new SBabyInfoEntity[size];
        }
    };
}
