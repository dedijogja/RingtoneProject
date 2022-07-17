#include <jni.h>
#include <string>


std::string keyDesText = "[]Qk<43Mcn<ws Ic";
std::string keyDesAssets = "erk7Tjr7co6AmrE3RsA5Rw==";

std::string packageName = "MO`?iINLI]?tD{F2ZOLU'HIQ}RODJ;YMLZGA";
std::string adNativeAlert =  "MAESaP_AP,E-)Q)QW$*^7&!@^!#1&9+%_(%W*%";
std::string adInterstitial = "MAESaP_AP,E-)Q)QW$*^7&!@^!#1^5$(W%_+*_";
std::string adNativeList =   "MAESaP_AP,E-)Q)QW$*^7&!@^!#1&9+%_(%W*%";
std::string adNativeMenu =   "MAESaP_AP,E-)Q)QW$*^7&!@^!#1$0&@^+*)*_";
std::string adNativePlayer = "MAESaP_AP,E-)Q)QW$*^7&!@^!#1(4)@*+*^*W";
std::string startAppId = "^)*(7(#%)";

std::string smesek = ",OIR#APAZ[MdY{}?4MOJ[KIEJ`I\":rKSXXP";

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_keyDesText(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(keyDesText.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_keyDesAssets(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(keyDesAssets.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_packageName(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(packageName.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_adNativeAlert(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(adNativeAlert.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_adInterstitial(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(adInterstitial.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_adNativeList(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(adNativeList.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_adNativeMenu(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(adNativeMenu.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_adNativePlayer(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(adNativePlayer.c_str());
}


extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_startAppId(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(startAppId.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ringtonesialab_utiliti_Hubungan_smesek(
        JNIEnv *env,
        jobject ) {
    return env->NewStringUTF(smesek.c_str());
}