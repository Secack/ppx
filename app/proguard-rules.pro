-optimizationpasses 7
-dontpreverify
-keep class com.akari.ppx.xp.HookEntry
-keep class * extends com.akari.ppx.xp.hook.code.SuperbHook {*;}
-keep class com.akari.ppx.common.utils.ModuleUtils
-keepclassmembers class com.akari.ppx.common.utils.ModuleUtils {
    public static boolean isModuleEnabled_Xposed();
}
-keep class * implements com.coremedia.iso.boxes.Box {* ; }