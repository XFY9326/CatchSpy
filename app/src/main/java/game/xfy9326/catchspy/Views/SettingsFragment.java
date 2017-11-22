package game.xfy9326.catchspy.Views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import game.xfy9326.catchspy.Activities.EditActivity;
import game.xfy9326.catchspy.Activities.ImportActivity;
import game.xfy9326.catchspy.Methods.ExtraWordMethod;
import game.xfy9326.catchspy.Methods.IOMethod;
import game.xfy9326.catchspy.Methods.ImportMethod;
import game.xfy9326.catchspy.Methods.PermissionMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Tools.Code;
import game.xfy9326.catchspy.Utils.Config;
import game.xfy9326.catchspy.Utils.MessageHandler;

public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences sharedPreferences;
    private AlertDialog loading;
    private boolean[] checkedItem;
    private int singleChoiceResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_settings);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        PreferenceSet();

    }

    private void PreferenceSet() {
        findPreference(Config.PREFERENCE_MAX_PLAYER_NUMBER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                playerNumberAlert(getString(R.string.settings_general_max_player), Config.PREFERENCE_MAX_PLAYER_NUMBER, Config.DEFAULT_MIN_PLAYER_NUM, Config.DEFAULT_MAX_NUMBER, sharedPreferences.getInt(Config.PREFERENCE_MAX_PLAYER_NUMBER, Config.DEFAULT_MAX_PLAYER_NUMBER));
                return true;
            }
        });
        findPreference(Config.PREFERENCE_MAX_PLAYER_BOARD_NUMBER).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                playerNumberAlert(getString(R.string.settings_general_max_white_board), Config.PREFERENCE_MAX_PLAYER_BOARD_NUMBER, Config.DEFAULT_MIN_PLAYER_BOARD_NUM, Config.DEFAULT_MAX_NUMBER, sharedPreferences.getInt(Config.PREFERENCE_MAX_PLAYER_BOARD_NUMBER, Config.DEFAULT_MAX_PLAYER_BOARD_NUMBER));
                return true;
            }
        });
        findPreference(Config.PREFERENCE_USE_EXTRA_WORDS).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return extraWordsUse(preference, (boolean) newValue);
            }
        });
        findPreference(Config.PREFERENCE_ONLY_USE_EXTRA_WORDS).setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                return extraWordsOnlyUse(preference, (boolean) newValue);
            }
        });
        findPreference(Config.PREFERENCE_IMPORT_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), ImportActivity.class));
                return true;
            }
        });
        findPreference(Config.PREFERENCE_LOAD_LOCAL_DICTIONARY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                loadLocalDictionary();
                return false;
            }
        });
        findPreference(Config.PREFERENCE_ADD_NEW_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                addNewDictionary();
                return false;
            }
        });
        findPreference(Config.PREFERENCE_EDIT_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SingleChooseDictionary(R.string.settings_extra_words_edit, 2, false);
                return true;
            }
        });
        findPreference(Config.PREFERENCE_SELECT_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SingleChooseDictionary(R.string.settings_extra_words_use, 0, true);
                return true;
            }
        });
        findPreference(Config.PREFERENCE_RENAME_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SingleChooseDictionary(R.string.settings_extra_words_rename_dictionary, 1, false);
                return true;
            }
        });
        findPreference(Config.PREFERENCE_DELETE_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MultiChooseDictionary(R.string.settings_extra_words_delete_dictionary, 0);
                return true;
            }
        });
        findPreference(Config.PREFERENCE_CHECK_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MultiChooseDictionary(R.string.settings_extra_words_check_exist, 1);
                return true;
            }
        });
        findPreference(Config.PREFERENCE_COMBINE_EXTRA_WORDS).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                MultiChooseDictionary(R.string.settings_extra_words_combine, 2);
                return true;
            }
        });
    }

    @SuppressWarnings("SameParameterValue")
    private void playerNumberAlert(String title, final String preferenceName, int min, int max, int default_num) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View mView = inflater.inflate(R.layout.dialog_number_picker, (ViewGroup) getActivity().findViewById(R.id.dialog_layout_number_picker));
        final NumberPicker numberPicker = mView.findViewById(R.id.dialog_number_picker);
        numberPicker.setMinValue(min);
        numberPicker.setMaxValue(max);
        numberPicker.setValue(default_num);
        builder.setTitle(title);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences.edit().putInt(preferenceName, numberPicker.getValue()).apply();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private boolean extraWordsUse(Preference preference, boolean value) {
        if (value) {
            if (PermissionMethod.checkExStoragePermission(getActivity())) {
                return true;
            } else {
                ((CheckBoxPreference) preference).setChecked(false);
                Toast.makeText(getActivity(), R.string.permission_request_error, Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean extraWordsOnlyUse(Preference preference, boolean value) {
        if (value) {
            if (ExtraWordMethod.getSelectedExtraWordsPath(sharedPreferences) == null) {
                ((CheckBoxPreference) preference).setChecked(false);
                Toast.makeText(getActivity(), R.string.settings_extra_words_not_set, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                return true;
            }
        }
        return true;
    }

    private void loadLocalDictionary() {
        ImportMethod.selectFile(getActivity(), Config.REQUEST_GET_LOCAL_DICTIONARY);
    }

    private void addNewDictionary() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_edittext, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_edit_text));
        final TextInputEditText editText = mView.findViewById(R.id.edittext_dialog);
        editText.setHint(R.string.default_new_dictionary_name);
        String name = getString(R.string.default_new_dictionary_name) + "_" + System.currentTimeMillis();
        editText.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings_extra_words_add);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = editText.getText().toString();
                if (result.replace(" ", "").equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), R.string.settings_extra_words_add_error, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        File file = new File(Config.DEFAULT_APPLICATION_DATA_DIR + result);
                        if (!file.exists()) {
                            if (file.createNewFile()) {
                                if (IOMethod.writeFile(new JSONObject().put(Config.DEFAULT_EXTRA_WORDS_DATA_NAME, new JSONArray()).toString(), file.getAbsolutePath())) {
                                    String newName = Code.unicodeEncode(result) + "-" + Code.getFileMD5String(file.getAbsolutePath());
                                    if (IOMethod.renameFile(file.getAbsolutePath(), newName)) {
                                        editWords(file.getParent() + File.separator + newName, result);
                                        return;
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), R.string.settings_extra_words_add_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private void SingleChooseDictionary(int titleId, final int type, final boolean defaultOpinion) {
        final LinkedHashMap<String, String> data = ExtraWordMethod.getAllExtraWordsList();
        if (!data.isEmpty()) {
            int chooseItem = 0;
            final String[] names = ExtraWordMethod.getAllExtraWordsNameList(data, defaultOpinion, getString(R.string.default_dictionary));
            String path = ExtraWordMethod.getSelectedExtraWordsPath(sharedPreferences);
            if (path != null) {
                Object[] objects = data.entrySet().toArray();
                for (int i = 0; i < data.size(); i++) {
                    Map.Entry entry = (Map.Entry) objects[i];
                    if (entry.getValue().toString().equalsIgnoreCase(path)) {
                        chooseItem = i;
                        if (defaultOpinion) {
                            chooseItem++;
                        }
                        break;
                    }
                }
            }
            if (chooseItem == 0 && defaultOpinion) {
                singleChoiceResult = 0;
            } else {
                singleChoiceResult = chooseItem;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(titleId);
            builder.setSingleChoiceItems(names, chooseItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    singleChoiceResult = which;
                }
            });
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (singleChoiceResult == 0 && defaultOpinion) {
                        dialogSingleManage(type, Config.EMPTY, names[singleChoiceResult]);
                    } else {
                        dialogSingleManage(type, data.get(names[singleChoiceResult]), names[singleChoiceResult]);
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        } else {
            Toast.makeText(getActivity(), R.string.settings_extra_words_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogSingleManage(int type, String path, String name) {
        switch (type) {
            case 0:
                selectDictionary(path);
                break;
            case 1:
                renameDictionary(path, name);
                break;
            case 2:
                editWords(path, name);
                break;
        }
    }

    private void editWords(String path, String name) {
        startActivity(new Intent(getActivity(), EditActivity.class).putExtra(Config.INTENT_EXTRA_PATH, path).putExtra(Config.INTENT_EXTRA_FILE_NAME, name));
    }

    private void selectDictionary(String path) {
        sharedPreferences.edit().putString(Config.PREFERENCE_SELECT_EXTRA_WORDS, path).apply();
    }

    private void renameDictionary(final String path, String name) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_edittext, (ViewGroup) getActivity().findViewById(R.id.layout_dialog_edit_text));
        final TextInputEditText editText = mView.findViewById(R.id.edittext_dialog);
        editText.setHint(R.string.settings_extra_words_rename_dictionary);
        editText.setText(name);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.settings_extra_words_rename_dictionary);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String result = editText.getText().toString();
                if (result.replace(" ", "").equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), R.string.settings_extra_words_rename_dictionary_failed, Toast.LENGTH_SHORT).show();
                } else {
                    if (ExtraWordMethod.renameDictionary(getActivity(), path, result)) {
                        Toast.makeText(getActivity(), R.string.settings_extra_words_rename_dictionary_success, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.settings_extra_words_rename_dictionary_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private void MultiChooseDictionary(int titleId, final int type) {
        final LinkedHashMap<String, String> data = ExtraWordMethod.getAllExtraWordsList();
        if (!data.isEmpty()) {
            checkedItem = getDefaultChoiceValue(data.size());
            final String[] names = ExtraWordMethod.getAllExtraWordsNameList(data, false, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(titleId);
            builder.setMultiChoiceItems(names, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                    checkedItem[which] = isChecked;
                }
            });
            builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final ArrayList<String> content = new ArrayList<>();
                    final ArrayList<String> content_names = new ArrayList<>();
                    for (int i = 0; i < checkedItem.length; i++) {
                        if (checkedItem[i]) {
                            content.add(data.get(names[i]));
                            content_names.add(names[i]);
                        }
                    }
                    dialogMultiManage(type, content, content_names);
                }
            });
            builder.setNegativeButton(R.string.cancel, null);
            builder.show();
        } else {
            Toast.makeText(getActivity(), R.string.settings_extra_words_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogMultiManage(int type, ArrayList<String> content, ArrayList<String> content_names) {
        String[] path = content.toArray(new String[content.size()]);
        String[] names = content_names.toArray(new String[content_names.size()]);
        switch (type) {
            case 0:
                deleteDictionary(path);
                break;
            case 1:
                checkDictionary(path, names);
                break;
            case 2:
                saveCombinedDictionary(path);
                break;
        }
    }

    private void deleteDictionary(final String[] content) {
        boolean result = true;
        String data = ExtraWordMethod.getSelectedExtraWordsPath(sharedPreferences);
        for (String path : content) {
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                result = file.delete();
                if (data != null) {
                    if (data.equalsIgnoreCase(path)) {
                        sharedPreferences.edit().putString(Config.PREFERENCE_SELECT_EXTRA_WORDS, Config.EMPTY).apply();
                    }
                }
            }
        }
        if (result) {
            Toast.makeText(getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), R.string.delete_failed, Toast.LENGTH_SHORT).show();
        }
    }

    private void checkDictionary(final String[] content, final String[] names) {
        loading();
        final MessageHandler messageHandler = new MessageHandler(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                ExtraWordMethod.checkExistWords(getActivity(), content, names);
                if (loading != null) {
                    loading.cancel();
                }
                messageHandler.sendEmptyMessage(Config.MSG_EXTRA_WORDS_CHECK_FINISH);
            }
        }).start();
    }

    private void saveCombinedDictionary(final String[] content) {
        loading();
        final MessageHandler messageHandler = new MessageHandler(getActivity());
        if (content.length > 1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < content.length; i++) {
                        content[i] = ExtraWordMethod.getExtraWordsDataByPath(content[i]);
                    }
                    String file_name = getString(R.string.default_new_dictionary_name) + "_" + System.currentTimeMillis();
                    String path = Config.DEFAULT_APPLICATION_DATA_DIR + file_name;
                    JSONObject object = ExtraWordMethod.combineDictionary(content, Config.DEFAULT_EXTRA_WORDS_DATA_NAME, Config.DEFAULT_EXTRA_WORDS_DATA_NAME, Config.DEFAULT_EXTRA_WORDS_DATA_NAME);
                    if (object != null) {
                        if (IOMethod.writeFile(object.toString(), path)) {
                            if (IOMethod.renameFile(path, Code.unicodeEncode(file_name) + "-" + Code.getFileMD5String(path))) {
                                messageHandler.sendEmptyMessage(Config.MSG_EXTRA_WORDS_COMBINE_SUCCESS);
                            } else {
                                messageHandler.sendEmptyMessage(Config.MSG_EXTRA_WORDS_COMBINE_FAILED);
                            }
                        } else {
                            messageHandler.sendEmptyMessage(Config.MSG_EXTRA_WORDS_COMBINE_FAILED);
                        }
                    } else {
                        messageHandler.sendEmptyMessage(Config.MSG_EXTRA_WORDS_COMBINE_FAILED);
                    }
                    if (loading != null) {
                        loading.cancel();
                    }
                }
            }).start();
        } else {
            Toast.makeText(getActivity(), R.string.settings_extra_words_combine_error, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean[] getDefaultChoiceValue(int length) {
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            result[i] = false;
        }
        return result;
    }

    private void loading() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setMessage(getString(R.string.loading));
        dialog.setCancelable(false);
        loading = dialog.show();
    }
}
