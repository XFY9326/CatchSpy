package game.xfy9326.catchspy.Methods;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import game.xfy9326.catchspy.Tools.Code;
import game.xfy9326.catchspy.Utils.Config;

public class EditWordMethod {

    static boolean saveExtraWords(Context context, String data, String path, String name, boolean checkUsing) {
        if (data == null || IOMethod.writeFile(data, path)) {
            String newName = Code.unicodeEncode(name) + "-" + Code.getFileMD5String(path);
            if (IOMethod.renameFile(path, newName)) {
                if (checkUsing && context != null) {
                    if (ExtraWordMethod.isUsingDictionary(context, path)) {
                        ExtraWordMethod.changeUsingDictionaryPath(context, new File(path).getParent() + File.separator + newName);
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    public static ArrayList<String[]> viewExtraWordList(String path) {
        String data = IOMethod.readFile(path);
        if (data != null) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray jsonArray = jsonObject.getJSONArray(Config.DEFAULT_EXTRA_WORDS_DATA_NAME);
                ArrayList<String[]> result = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    result.add(new String[]{jsonArray.getJSONArray(i).getString(0), jsonArray.getJSONArray(i).getString(1)});
                }
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean saveArrayToFile(Context context, ArrayList<String[]> data, String path, String name) {
        if (data != null && path != null) {
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            try {
                for (int i = 0; i < data.size(); i++) {
                    String[] words = data.get(i);
                    jsonArray.put(i, new JSONArray().put(0, words[0]).put(1, words[1]));
                }
                jsonObject.put(Config.DEFAULT_EXTRA_WORDS_DATA_NAME, jsonArray);
                return saveExtraWords(context, jsonObject.toString(), path, name, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
