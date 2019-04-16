package blind.al.contactsguardplugin;

import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.os.IPluginCalendarInterposer;
import android.os.IPluginCameraInterposer;
import android.os.IPluginContactsInterposer;
import android.os.IPluginLocationInterposer;
import android.os.IPluginStorageInterposer;
import android.os.PluginService;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

public class PluginMain extends Service {

    public static String TAG = "Dalf";
    public static boolean DEBUG = false;

    public PluginContactsInterposer mContactsInterposer;

    @Override
    public void onCreate() {
        super.onCreate();

        mContactsInterposer = new PluginContactsInterposer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final PluginService mBinder = new PluginService() {
        @Override
        public IPluginLocationInterposer getLocationInterposer() throws RemoteException {
            return null;
        }

        @Override
        public IPluginContactsInterposer getContactsInterposer() throws RemoteException {
            return mContactsInterposer;
        }

        @Override
        public IPluginCalendarInterposer getCalendarInterposer() throws RemoteException {
            return null;
        }

        @Override
        public IPluginCameraInterposer getCameraInterposer() throws RemoteException {
            return null;
        }

        @Override
        public IPluginStorageInterposer getStorageInterposer() throws RemoteException {
            return null;
        }
    };
}
