package game.xfy9326.catchspy.Methods;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedHashMap;

import game.xfy9326.catchspy.Tools.Code;
import game.xfy9326.catchspy.Utils.Config;

public class ExtraWordMethod {

    static boolean isUsingDictionary(Context context, String path) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String data = sharedPreferences.getString(Config.PREFERENCE_SELECT_EXTRA_WORDS, null);
        if (data != null) {
            if (data.equalsIgnoreCase(path)) {
                return true;
            }
        }
        return false;
    }

    static void changeUsingDictionaryPath(Context context, String path) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(Config.PREFERENCE_SELECT_EXTRA_WORDS, path).apply();
    }

    public static String getExtraWordsDataByPath(String path) {
        return IOMethod.readFile(path);
    }

    public static String getSelectedExtraWordsPath(SharedPreferences sharedPreferences) {
        String result = sharedPreferences.getString(Config.PREFERENCE_SELECT_EXTRA_WORDS, null);
        if (result != null && !result.equals(Config.EMPTY)) {
            if (CheckExtraWords(result)) {
                return result;
            }
        }
        return null;
    }

    private static boolean CheckExtraWords(String file_name) {
        File file = new File(file_name);
        return file.isFile() && file.exists();
    }

    //Dictionary Name - Dictionary Path
    public static LinkedHashMap<String, String> getAllExtraWordsList() {
        LinkedHashMap<String, String> list = new LinkedHashMap<>();
        File data = new File(Config.DEFAULT_APPLICATION_DATA_DIR);
        if (data.isDirectory() && data.exists()) {
            String[] path = data.list();
            for (String str : path) {
                String md5 = str.substring(str.indexOf("-") + 1);
                String file_md5 = Code.getFileMD5String(Config.DEFAULT_APPLICATION_DATA_DIR + str);
                if (file_md5 != null) {
                    if (file_md5.equalsIgnoreCase(md5)) {
                        String name = str.substring(0, str.indexOf("-"));
                        name = Code.unicodeDecode(name);
                        list.put(name, Config.DEFAULT_APPLICATION_DATA_DIR + str);
                    }
                }
            }
        }
        return list;
    }

    public static String[] getAllExtraWordsNameList(LinkedHashMap<String, String> data, boolean defaultOpinion, String defaultString) {
        int length;
        if (defaultOpinion) {
            length = data.size() + 1;
        } else {
            length = data.size();
        }
        String[] result = new String[length];
        int start = 0;
        if (defaultOpinion && defaultString != null) {
            result[0] = defaultString;
            start++;
        }
        for (Object o : data.entrySet()) {
            LinkedHashMap.Entry entry = (LinkedHashMap.Entry) o;
            result[start] = entry.getKey().toString();
            start++;
        }
        return result;
    }

    public static boolean renameDictionary(Context context, String path, String newName) {
        return EditWordMethod.saveExtraWords(context, null, path, newName, true);
    }

    public static void checkExistWords(Context context, String[] path, String[] file_name) {
        for (int i = 0; i < path.length; i++) {
            String data = IOMethod.readFile(path[i]);
            if (data != null) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = delExistWords(jsonObject.getJSONArray(Config.DEFAULT_EXTRA_WORDS_DATA_NAME));
                    EditWordMethod.saveExtraWords(context, new JSONObject().put(Config.DEFAULT_EXTRA_WORDS_DATA_NAME, jsonArray).toString(), path[i], file_name[i], true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static JSONArray delExistWords(JSONArray group_words) {
        try {
            int length = group_words.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    if (existJSONArray(group_words, group_words.getJSONArray(i), true)) {
                        group_words.remove(i);
                        length--;
                        i--;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return group_words;
    }

    @SuppressWarnings("SameParameterValue")
    public static JSONObject combineDictionary(String[] data, String data_group_name_0, String data_group_name_1, String output_group_name) {
        try {
            JSONObject result = new JSONObject();
            JSONArray array = new JSONArray();
            for (int i = 0; i < data.length; i++) {
                if (i == 0) {
                    result = new JSONObject(data[i]);
                } else if (i == 1) {
                    array = combineJSONArray(result.getJSONArray(data_group_name_0), (new JSONObject(data[i])).getJSONArray(data_group_name_1));
                } else if (i == 2) {
                    array = combineJSONArray(array, (new JSONObject(data[i])).getJSONArray(data_group_name_1));
                }
            }
            result = new JSONObject().put(output_group_name, array);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONArray combineJSONArray(JSONArray origin, JSONArray extra) {
        try {
            for (int i = 0; i < extra.length(); i++) {
                JSONArray jsonArray = extra.getJSONArray(i);
                if (!existJSONArray(origin, jsonArray, false)) {
                    origin.put(jsonArray);
                }
            }
            return origin;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static boolean existJSONArray(JSONArray origin, JSONArray data, boolean twice) {
        try {
            JSONArray ex_arr = new JSONArray();
            ex_arr.put(0, data.getString(1));
            ex_arr.put(1, data.getString(0));
            if (twice) {
                return wordExistCount(origin.toString(), data.toString()) > 1 || wordExistCount(origin.toString(), ex_arr.toString()) > 1;
            } else {
                return origin.toString().contains(data.toString()) || origin.toString().contains(ex_arr.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

    private static int wordExistCount(String text, String sub) {
        int count = 0, start = 0;
        while ((start = text.indexOf(sub, start)) >= 0) {
            start += sub.length();
            count++;
        }
        return count;
    }
}
