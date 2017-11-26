package game.xfy9326.catchspy.Views;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Toast;

import game.xfy9326.catchspy.Activities.RuleActivity;
import game.xfy9326.catchspy.Activities.SettingsActivity;
import game.xfy9326.catchspy.Methods.ExtraWordMethod;
import game.xfy9326.catchspy.Methods.PermissionMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class GlobalSettingsFragment extends PreferenceFragment {
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_global_settings);
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
        findPreference(Config.PREFERENCE_MANAGE_EXTRA_DICTIONARY).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startExtraEditFragment();
                return true;
            }
        });
        findPreference(Config.PREFERENCE_GAME_RULE).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(getActivity(), RuleActivity.class));
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

    private void startExtraEditFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.layout_settings_content, new ExtraDictionarySettingsFragment());
        fragmentTransaction.commit();
        ((SettingsActivity) getActivity()).openExtraDictionaryFragment = true;
    }
}
