# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keep class com.consica.code.data.local.entity.** { *; }
-keep class com.consica.code.data.model.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class kotlinx.serialization.** { *; }
-keep class org.python.** { *; }
