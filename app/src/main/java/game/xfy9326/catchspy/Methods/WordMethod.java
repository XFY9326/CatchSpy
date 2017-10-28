package game.xfy9326.catchspy.Methods;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class WordMethod {

    @SuppressWarnings("SameParameterValue")
    public static String[] getStringArray(String data, int length) {
        String[] result;
        if (data != null && !data.equals("")) {
            if (data.equals("[]") && length > 0) {
                return new String[length];
            }
            data = data.substring(1, data.length() - 1);
            result = data.split(",");
            for (int i = 0; i < result.length; i++) {
                result[i] = result[i].trim();
            }
            return result;
        }
        return null;
    }

    public static String[] getDefaultPlayerWords(Context context) {
        return getPlayerWords(getFromAssets(context, Config.DEFAULT_WORDS_FILE_NAME), Config.DEFAULT_WORDS_DATA_NAME);
    }

    @SuppressWarnings("SameParameterValue")
    private static String[] getPlayerWords(String data, String name) {
        String[] words = new String[2];
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray(name);
                int index = getRandomNum(jsonArray.length() - 1);
                int id = getRandomNum(1);
                JSONArray jsonArray_words = jsonArray.getJSONArray(index);
                words[Config.PLAYER_WORD_SPY] = jsonArray_words.getString(id);
                if (id == 0) {
                    id = 1;
                } else {
                    id = 0;
                }
                words[Config.PLAYER_WORD_NORMAL] = jsonArray_words.getString(id);
                return words;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int[] getPlayerIdentify(int playerNum, int spyNum, int boardNum) {
        int[] player = initPlayerIdentify(playerNum);
        //Spy Set
        player = randomSet(player, spyNum, playerNum, Config.PLAYER_IDENTIFY_SPY);
        //Board Set
        player = randomSet(player, boardNum, playerNum, Config.PLAYER_IDENTIFY_WHITE_BOARD);
        return player;
    }

    public static String getPlayerIdentifyString(Context context, int type) {
        if (type == Config.PLAYER_IDENTIFY_NORMAL) {
            return context.getString(R.string.normal);
        } else if (type == Config.PLAYER_IDENTIFY_SPY) {
            return context.getString(R.string.spy);
        } else if (type == Config.PLAYER_IDENTIFY_WHITE_BOARD) {
            return context.getString(R.string.white_board);
        }
        return null;
    }

    private static int[] randomSet(int[] player, int randomNum, int range, int type) {
        Random random = new Random();
        int num;
        boolean num_reset;
        for (int i = 0; i < randomNum; i++) {
            num_reset = true;
            while (num_reset) {
                num = random.nextInt(range);
                if (player[num] == Config.PLAYER_IDENTIFY_NORMAL) {
                    player[num] = type;
                    num_reset = false;
                }
            }
        }
        return player;
    }

    private static int[] initPlayerIdentify(int num) {
        int[] player = new int[num];
        for (int i : player) {
            player[i] = Config.PLAYER_IDENTIFY_NORMAL;
        }
        return player;
    }

    @SuppressWarnings("SameParameterValue")
    private static String getFromAssets(Context context, String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = bufReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static int getRandomNum(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}
