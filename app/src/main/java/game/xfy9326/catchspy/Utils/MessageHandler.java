package game.xfy9326.catchspy.Utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import game.xfy9326.catchspy.R;

public class MessageHandler extends Handler {
    private final Context context;

    public MessageHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        int textId;
        switch (msg.what) {
            case 0:
                textId = R.string.settings_extra_words_check_finish;
                break;
            case 1:
                textId = R.string.settings_extra_words_combine_success;
                break;
            case 2:
                textId = R.string.settings_extra_words_combine_failed;
                break;
            default:
                textId = -1;
                break;
        }
        if (textId > 0) {
            Toast.makeText(context, textId, Toast.LENGTH_SHORT).show();
        }
        super.handleMessage(msg);
    }
}
