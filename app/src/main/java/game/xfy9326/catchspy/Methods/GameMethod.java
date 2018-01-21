package game.xfy9326.catchspy.Methods;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import game.xfy9326.catchspy.Activities.ShowActivity;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Tools.Code;
import game.xfy9326.catchspy.Utils.Config;

public class GameMethod {
    public static boolean checkPlayerName(SharedPreferences sharedPreferences, int player_num) {
        int count = readPlayerNameCount(sharedPreferences);
        if (count != 0) {
            if (count == player_num) {
                return true;
            } else {
                deletePlayerName(sharedPreferences);
            }
        }
        return false;
    }

    public static void savePlayerName(Context context, String[] name) {
        if (name != null && name.length > 1) {
            StringBuilder data = new StringBuilder();
            for (int i = 0; i < name.length; i++) {
                data.append(Code.unicodeEncode(name[i]));
                if (i != name.length - 1) {
                    data.append(",");
                }
            }
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            sharedPreferences.edit().putString(Config.DATA_PLAYER_NAME, data.toString()).apply();
        }
    }

    private static String[] readPlayerName(SharedPreferences sharedPreferences) {
        String data = sharedPreferences.getString(Config.DATA_PLAYER_NAME, null);
        if (data != null && data.contains(",")) {
            String[] temp = data.split(",");
            String[] result = new String[temp.length];
            for (int i = 0; i < temp.length; i++) {
                result[i] = Code.unicodeDecode(temp[i]);
            }
            return result;
        }
        return null;
    }

    private static void deletePlayerName(SharedPreferences sharedPreferences) {
        if (sharedPreferences.contains(Config.DATA_PLAYER_NAME)) {
            sharedPreferences.edit().remove(Config.DATA_PLAYER_NAME).apply();
        }
    }

    private static int readPlayerNameCount(SharedPreferences sharedPreferences) {
        String data = sharedPreferences.getString(Config.DATA_PLAYER_NAME, null);
        if (data != null && data.contains(",")) {
            String[] temp = data.split(",");
            return temp.length;
        }
        return 0;
    }

    public static void showPlayerNameDialog(final Context context, final SharedPreferences sharedPreferences, final int[] players, final String[] words) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.player_name_change_title);
        builder.setMessage(R.string.player_name_change_content);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletePlayerName(sharedPreferences);
                startGame(context, players, words, null);
            }
        });
        builder.setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startGame(context, players, words, readPlayerName(sharedPreferences));
            }
        });
        builder.show();
    }

    public static void startGame(Context context, int[] players, String[] words, String[] name) {
        Intent intent = new Intent(context, ShowActivity.class);
        intent.putExtra(Config.INTENT_EXTRA_PLAYER, players);
        intent.putExtra(Config.INTENT_EXTRA_PLAYER_WORDS, words);
        intent.putExtra(Config.INTENT_EXTRA_PLAYER_NAME, name);
        context.startActivity(intent);
    }

}
