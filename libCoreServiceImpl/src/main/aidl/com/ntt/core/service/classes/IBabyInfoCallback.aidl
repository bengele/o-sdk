// IBabyInfoCallback.aidl
package com.ntt.core.service.classes;
import com.ntt.core.service.entities.SBabyInfoEntity;

// Declare any non-default types here with import statements

interface IBabyInfoCallback {
    void onSuccess(boolean isSync,in SBabyInfoEntity baby);
    void onFailure(String msg);
}