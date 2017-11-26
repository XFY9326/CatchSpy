package game.xfy9326.catchspy.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

import game.xfy9326.catchspy.Methods.ApplicationMethod;
import game.xfy9326.catchspy.Methods.PermissionMethod;
import game.xfy9326.catchspy.Methods.WordMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class MainActivity extends AppCompatActivity {
    public static MainActivity Instance;
    private long BackClickTime;
    private int playerNum = Config.DEFAULT_PLAYER_NUM;
    private int playerSpyNum = playerNum / 2;
    private int playerBoardNum = Config.DEFAULT_PLAYER_BOARD_NUM;
    private String[] list_arr_spy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        setAllView();
        PermissionMethod.askExStoragePermission(this);
    }

    private void setAllView() {
        setContentView(R.layout.activity_main);
        //Toolbar Settings
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //Spinner Settings
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Spinner spinner_player_number = findViewById(R.id.spinner_main_player_number);
        final Spinner spinner_player_number_spy = findViewById(R.id.spinner_main_player_number_spy);
        final Spinner spinner_player_number_white_board = findViewById(R.id.spinner_main_player_number_white_board);

        final String[] arr_player = ApplicationMethod.getNumArray(sharedPreferences.getInt(Config.PREFERENCE_MAX_PLAYER_NUMBER, Config.DEFAULT_MAX_PLAYER_NUMBER), Config.DEFAULT_MIN_PLAYER_NUM);
        spinner_player_number.setAdapter(ApplicationMethod.getSpinnerAdapter(this, arr_player));
        spinner_player_number.setSelection(playerNum - Config.DEFAULT_MIN_PLAYER_NUM);
        spinner_player_number.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerNum = Integer.valueOf(arr_player[position]);
                if (playerSpyNum > playerNum / 2) {
                    playerSpyNum = playerNum / 2;
                }
                list_arr_spy = ApplicationMethod.getNumArray(playerNum / 2, Config.DEFAULT_MIN_PLAYER_SPY_NUM);
                spinner_player_number_spy.setAdapter(ApplicationMethod.getSpinnerAdapter(MainActivity.this, list_arr_spy));
                spinner_player_number_spy.setSelection(playerSpyNum - Config.DEFAULT_MIN_PLAYER_SPY_NUM);
                if (playerNum <= playerNum / 2 + playerBoardNum) {
                    playerBoardNum = 0;
                    spinner_player_number_white_board.setSelection(playerBoardNum);
                    Snackbar.make(findViewById(R.id.main_layout_content), R.string.player_num_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        list_arr_spy = ApplicationMethod.getNumArray(playerNum / 2, Config.DEFAULT_MIN_PLAYER_SPY_NUM);
        spinner_player_number_spy.setAdapter(ApplicationMethod.getSpinnerAdapter(this, list_arr_spy));
        spinner_player_number_spy.setSelection(playerSpyNum - Config.DEFAULT_MIN_PLAYER_SPY_NUM);
        spinner_player_number_spy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerSpyNum = Integer.valueOf(list_arr_spy[position]);
            }
        });

        final String[] arr_board = ApplicationMethod.getNumArray(sharedPreferences.getInt(Config.PREFERENCE_MAX_PLAYER_BOARD_NUMBER, Config.DEFAULT_MAX_PLAYER_BOARD_NUMBER), Config.DEFAULT_MIN_PLAYER_BOARD_NUM);
        spinner_player_number_white_board.setAdapter(ApplicationMethod.getSpinnerAdapter(this, arr_board));
        spinner_player_number_white_board.setSelection(playerBoardNum - Config.DEFAULT_MIN_PLAYER_BOARD_NUM);
        spinner_player_number_white_board.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                playerBoardNum = Integer.valueOf(arr_board[position]);
                if (playerBoardNum >= playerNum / 2) {
                    playerBoardNum = 0;
                    spinner_player_number_white_board.setSelection(playerBoardNum);
                    Snackbar.make(findViewById(R.id.main_layout_content), R.string.player_num_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        //CardView Settings
        findViewById(R.id.cardview_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, SettingsActivity.class), Config.REQUEST_ACTIVITY_SETTINGS);
            }
        });
        findViewById(R.id.cardview_about).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
            }
        });
        findViewById(R.id.cardview_diy_words).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDIYWords(MainActivity.this, sharedPreferences);
            }
        });

        //StartButton
        findViewById(R.id.main_button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] words;
                if (sharedPreferences.getBoolean(Config.PREFERENCE_USE_DIY_WORDS, Config.DEFAULT_USE_DIY_WORDS)) {
                    words = WordMethod.getStringArray(sharedPreferences.getString(Config.PREFERENCE_DIY_WORDS, Config.DEFAULT_DIY_WORDS), 2);
                } else if (sharedPreferences.getBoolean(Config.PREFERENCE_USE_EXTRA_WORDS, Config.DEFAULT_USE_EXTRA_WORDS)) {
                    words = WordMethod.getExtraPlayerWords(MainActivity.this, sharedPreferences);
                } else {
                    words = WordMethod.getDefaultPlayerWords(MainActivity.this);
                }
                if (words == null) {
                    Snackbar.make(findViewById(R.id.main_layout_content), R.string.settings_extra_words_error, Snackbar.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, ShowActivity.class);
                    intent.putExtra(Config.INTENT_EXTRA_PLAYER, WordMethod.getPlayerIdentify(playerNum, playerSpyNum, playerBoardNum, sharedPreferences.getBoolean(Config.PREFERENCE_WHITE_BOARD_NOT_FIRST, Config.DEFAULT__WHITE_BOARD_NOT_FIRST)));
                    intent.putExtra(Config.INTENT_EXTRA_PLAYER_WORDS, words);
                    startActivity(intent);
                }
            }
        });

        //CheckBox
        CheckBox checkBox_diy = findViewById(R.id.checkbox_diy_words);
        checkBox_diy.setChecked(sharedPreferences.getBoolean(Config.PREFERENCE_USE_DIY_WORDS, Config.DEFAULT_USE_DIY_WORDS));
        checkBox_diy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setDIYWordsSwitch((CheckBox) buttonView, sharedPreferences, isChecked);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_ACTIVITY_SETTINGS) {
            setAllView();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Config.REQUEST_EXTERNAL_STRANGE) {
            if (!PermissionMethod.checkExStoragePermission(this)) {
                for (String permission : PermissionMethod.StoragePermission) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        PermissionMethod.explainExStoragePermission(this);
                        break;
                    } else {
                        Toast.makeText(this, R.string.permission_request_error, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        long BackNowClickTime = System.currentTimeMillis();
        if ((BackNowClickTime - BackClickTime) < 2200) {
            ApplicationMethod.DoubleClickCloseSnackBar(this, true);
        } else {
            ApplicationMethod.DoubleClickCloseSnackBar(this, false);
            BackClickTime = System.currentTimeMillis();
        }
    }

    private void setDIYWords(Context context, final SharedPreferences sharedPreferences) {
        String[] words = WordMethod.getStringArray(sharedPreferences.getString(Config.PREFERENCE_DIY_WORDS, Config.DEFAULT_DIY_WORDS), 2);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.diy_words);
        LayoutInflater inflater = LayoutInflater.from(context);
        View mView = inflater.inflate(R.layout.dialog_diy_words_set, (ViewGroup) findViewById(R.id.dialog_layout_diy_words));
        final TextInputEditText edit_spy = mView.findViewById(R.id.edittext_diy_spy_word);
        final TextInputEditText edit_normal = mView.findViewById(R.id.edittext_diy_normal_word);
        edit_spy.setText(words[Config.PLAYER_WORD_SPY]);
        edit_normal.setText(words[Config.PLAYER_WORD_NORMAL]);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] result = new String[2];
                result[Config.PLAYER_WORD_NORMAL] = edit_normal.getText().toString();
                result[Config.PLAYER_WORD_SPY] = edit_spy.getText().toString();
                if (result[Config.PLAYER_WORD_NORMAL].replace(" ", "").equals("") || result[Config.PLAYER_WORD_SPY].replace(" ", "").equals("")) {
                    CheckBox checkBox_diy = findViewById(R.id.checkbox_diy_words);
                    if (checkBox_diy.isChecked()) {
                        checkBox_diy.setChecked(false);
                        sharedPreferences.edit().putBoolean(Config.PREFERENCE_USE_DIY_WORDS, false).apply();
                    }
                }
                sharedPreferences.edit().putString(Config.PREFERENCE_DIY_WORDS, Arrays.toString(result)).apply();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private void setDIYWordsSwitch(CheckBox checkBox, SharedPreferences sharedPreferences, boolean isChecked) {
        if (isChecked) {
            String[] words = WordMethod.getStringArray(sharedPreferences.getString(Config.PREFERENCE_DIY_WORDS, Config.DEFAULT_DIY_WORDS), 2);
            if (words[Config.PLAYER_WORD_NORMAL] != null && words[Config.PLAYER_WORD_SPY] != null) {
                if (!words[Config.PLAYER_WORD_NORMAL].isEmpty() && !words[Config.PLAYER_WORD_SPY].isEmpty()) {
                    sharedPreferences.edit().putBoolean(Config.PREFERENCE_USE_DIY_WORDS, true).apply();
                    return;
                }
            }
            Snackbar.make(findViewById(R.id.main_layout_content), R.string.diy_words_error, Snackbar.LENGTH_SHORT).show();
            checkBox.setChecked(false);
        } else {
            sharedPreferences.edit().putBoolean(Config.PREFERENCE_USE_DIY_WORDS, false).apply();
        }
    }
}
