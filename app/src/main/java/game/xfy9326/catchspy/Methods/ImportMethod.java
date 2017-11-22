package game.xfy9326.catchspy.Methods;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import game.xfy9326.catchspy.Utils.Config;

public class ImportMethod {

    public static void selectFile(Activity mActivity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("file/*");
        mActivity.startActivityForResult(intent, requestCode);
    }

    public static int extraFileImporter(String path, String name, String groupDivider, String wordDivider, boolean ignoreChangeLine) {
        String data = IOMethod.readFile(path);
        if (data != null) {
            if (ignoreChangeLine) {
                data = data.replaceAll("\n", "");
            }
            if (data.contains(groupDivider)) {
                String[] group = data.split(groupDivider);
                JSONArray list = new JSONArray();
                for (String str : group) {
                    if (str.contains(wordDivider)) {
                        String[] words = str.split(wordDivider);
                        JSONArray list_data = new JSONArray();
                        int i = 0;
                        for (String str_data : words) {
                            list_data.put(str_data.trim());
                            if (i > 1) {
                                break;
                            }
                            i++;
                        }
                        list.put(list_data);
                    } else {
                        return Config.IMPORT_WORDS_ERROR;
                    }
                }
                try {
                    JSONObject object = new JSONObject().put(Config.DEFAULT_EXTRA_WORDS_DATA_NAME, list);
                    if (saveImportFile(object.toString(), name)) {
                        return Config.IMPORT_OK;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Config.IMPORT_OUTPUT_ERROR;
            } else {
                return Config.IMPORT_GROUP_ERROR;
            }
        } else {
            return Config.IMPORT_FILE_ERROR;
        }
    }

    public static int extraFileRegImporter(String path, String name, String groupReg, String wordReg, boolean ignoreChangeLine) {
        String data = IOMethod.readFile(path);
        if (data != null) {
            if (ignoreChangeLine) {
                data = data.replaceAll("\n", "");
            }
            JSONArray jsonArray = new JSONArray();
            ArrayList<String> groups = new ArrayList<>();
            Pattern group_pattern = Pattern.compile(groupReg);
            Matcher group_matcher = group_pattern.matcher(data);
            while (group_matcher.find()) {
                groups.add(group_matcher.group());
            }
            if (groups.size() != 0) {
                Pattern word_pattern = Pattern.compile(wordReg);
                for (String words : groups) {
                    Matcher word_matcher = word_pattern.matcher(words);
                    String[] word = new String[2];
                    int i = 0;
                    while (word_matcher.find() && i <= 1) {
                        word[i] = word_matcher.group();
                        i++;
                    }
                    if (word[0] != null && word[1] != null) {
                        if (!word[0].isEmpty() && !word[1].isEmpty()) {
                            jsonArray.put(new JSONArray().put(word[0]).put(word[1]));
                        } else {
                            return Config.IMPORT_WORDS_ERROR;
                        }
                    } else {
                        return Config.IMPORT_WORDS_ERROR;
                    }
                }
                try {
                    JSONObject object = new JSONObject().put(Config.DEFAULT_EXTRA_WORDS_DATA_NAME, jsonArray);
                    if (saveImportFile(object.toString(), name)) {
                        return Config.IMPORT_OK;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return Config.IMPORT_OUTPUT_ERROR;
            } else {
                return Config.IMPORT_GROUP_ERROR;
            }
        }
        return Config.IMPORT_FILE_ERROR;
    }

    private static boolean saveImportFile(String content, String name) {
        String file_path = Config.DEFAULT_APPLICATION_DATA_DIR + name;
        return EditWordMethod.saveExtraWords(null, content, file_path, name, false);
    }

}
