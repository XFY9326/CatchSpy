package game.xfy9326.catchspy.Views;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences sharedPreferences;

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
}
