-keepattributes Signature
-keepattributes *Annotation*

# Retrofit
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# Gson
-keep class com.checkitout.weather.data.api.** { *; }
-keepclassmembers class com.checkitout.weather.data.api.** { *; }

# EdDSA
-keep class net.i2p.crypto.eddsa.** { *; }
-dontwarn net.i2p.crypto.eddsa.**
-dontwarn sun.security.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
