# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class com.tuya.**{*;}
-dontwarn com.tuya.**


 -keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**

  # react-native
   -keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
   -keep,allowobfuscation @interface com.facebook.proguard.annotations.DoNotStrip
   -keep,allowobfuscation @interface com.facebook.proguard.annotations.KeepGettersAndSetters
   # Do not strip any method/class that is annotated with @DoNotStrip
   -keep @com.facebook.proguard.annotations.DoNotStrip class *
   -keep @com.facebook.common.internal.DoNotStrip class *
   -keepclassmembers class * {
       @com.facebook.proguard.annotations.DoNotStrip *;
       @com.facebook.common.internal.DoNotStrip *;
   }
   -keepclassmembers @com.facebook.proguard.annotations.KeepGettersAndSetters class * {
     void set*(***);
     *** get*();
   }
   -keep class * extends com.facebook.react.bridge.JavaScriptModule { *; }
   -keep class * extends com.facebook.react.bridge.NativeModule { *; }
   -keepclassmembers,includedescriptorclasses class * { native <methods>; }
   -keepclassmembers class *  { @com.facebook.react.uimanager.UIProp <fields>; }
   -keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactProp <methods>; }
   -keepclassmembers class *  { @com.facebook.react.uimanager.annotations.ReactPropGroup <methods>; }
   -dontwarn com.facebook.react.**
   -keep,includedescriptorclasses class com.facebook.react.bridge.** { *; }