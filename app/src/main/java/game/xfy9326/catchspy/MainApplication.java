package game.xfy9326.catchspy;

import android.app.Application;

import game.xfy9326.catchspy.Tools.CrashHandler;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (!BuildConfig.DEBUG) {
            CrashHandler.get().Catch(this);
        }
    }
}
