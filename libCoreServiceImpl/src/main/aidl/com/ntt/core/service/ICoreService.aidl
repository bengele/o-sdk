// ICoreService.aidl
package com.ntt.core.service;

import com.ntt.core.service.entities.SAuthEntity;
import com.ntt.core.service.classes.IAuthCallback;
import com.ntt.core.service.classes.IBabyInfoCallback;
import com.ntt.core.service.entities.SBabyInfoEntity;

interface ICoreService {

    //获取版本号
    String getVersion();

    //获取是否测试模式下的对应的域名
    String getDomainHost();

    //设置测试模式
    void setDomainDebug(boolean v);

    //获取域名是否测试模式
    boolean getDomainIsDebugMode();

    //同步获取token
    String getSyncAuthToken(boolean force);

    //获取登录标识
    void getAuthToken(boolean force,in IAuthCallback callback);

    //获取宝宝信息
    void getBabyInfo(in IBabyInfoCallback callback );

    //获取设备ID
    String getDeviceId();

    //MQTT订阅
    void subscribe(String topic,int qos);

    //MQTT取消订阅
    void unsubscribe(String topic);

    //发送信息
    void sendMessage(String topic, String data, int qos, boolean retain);

    //esim开关 isOpen: 布尔型，true为开启，false为关闭 disableProfile: 布尔型，控制在关闭服务的时候是否也禁用码号，主要在eUICC下使用
    int esimSwitchService(boolean isOpen,boolean disableProfile);

    //返回当前服务是否开启 0为关闭 1为开启
    int isEsimServiceOpen();

    //切换红茶的业务号
    int switchESIMCard();
}
