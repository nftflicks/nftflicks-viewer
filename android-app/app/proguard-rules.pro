-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class com.nftflicks.app.** {
    *;
}
-keep class com.nftflicks.app.CastOptionsProvider { *; }
-keep class com.nftflicks.app.CastJsBridge { *; }
-dontwarn com.google.android.gms.cast.**
