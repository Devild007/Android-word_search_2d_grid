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

-keep class javax.annotation.** { *; }
-dontnote android.support.**
-dontnote androidx.**
-dontwarn android.support.**
-dontwarn androidx.**
-dontwarn android.util.FloatMath
-keep class androidx.annotation.Keep
-keep @androidx.annotation.Keep class * {*;}
-allowaccessmodification
-keepattributes *Annotation*
-keep class com.example.wordsearch.**
-keep class jxl.** { *; }

-keep class com.example.wordsearch.device.utility.**{*;}
-dontwarn javax.annotation.Nullable
