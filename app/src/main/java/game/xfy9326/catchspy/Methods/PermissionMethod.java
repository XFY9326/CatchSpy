package game.xfy9326.catchspy.Methods;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class PermissionMethod {

    public static final String[] StoragePermission = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void askExStoragePermission(Activity activity) {
        if (checkExStoragePermission(activity)) {
            ActivityCompat.requestPermissions(activity, StoragePermission, Config.REQUEST_EXTERNAL_STRANGE);
        }
    }

    public static boolean checkExStoragePermission(Context mContext) {
        boolean result = false;
        for (String permission : StoragePermission) {
            if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
                result = true;
                break;
            }
        }
        return !result;
    }

    public static void explainExStoragePermission(final Activity mActivity) {
        AlertDialog.Builder permissionExplanation = new AlertDialog.Builder(mActivity);
        permissionExplanation.setTitle(R.string.permission_warn);
        permissionExplanation.setMessage(R.string.permission_warn_explanation);
        permissionExplanation.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                askExStoragePermission(mActivity);
            }
        });
        permissionExplanation.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mActivity.finish();
            }
        });
        permissionExplanation.setCancelable(false);
        permissionExplanation.show();
    }

}
