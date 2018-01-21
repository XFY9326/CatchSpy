package game.xfy9326.catchspy.Methods;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.widget.ArrayAdapter;

import game.xfy9326.catchspy.R;

public class ApplicationMethod {
    private static boolean waitDoubleClick;

    public static void DoubleClickCloseSnackBar(final Activity mActivity, boolean isDoubleClick) {
        if (isDoubleClick && waitDoubleClick) {
            mActivity.finish();
        } else {
            Snackbar snackbar = Snackbar.make(mActivity.findViewById(R.id.main_layout_content), R.string.action_warn_double_click_close_application, Snackbar.LENGTH_SHORT);
            snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    waitDoubleClick = false;
                    super.onDismissed(transientBottomBar, event);
                }
            });
            waitDoubleClick = true;
            snackbar.show();
        }
    }

    public static String[] getNumArray(int max, int min) {
        int length = max + 1 - min;
        String[] arr = new String[length];
        for (int i = 0; i < length; i++) {
            arr[i] = String.valueOf(i + min);
        }
        return arr;
    }

    public static ArrayAdapter<String> getSpinnerAdapter(Context context, String[] data) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
}
