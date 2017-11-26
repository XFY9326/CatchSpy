package game.xfy9326.catchspy.Utils;

import android.os.Environment;

import java.io.File;

public class Config {
    public static final String EMPTY = " ";

    public static final int REQUEST_EXTERNAL_STRANGE = 0;
    public static final int REQUEST_ACTIVITY_SETTINGS = 1;
    public static final int REQUEST_GET_EXTRA_FILE = 2;
    public static final int REQUEST_GET_LOCAL_DICTIONARY = 3;

    public static final int MSG_EXTRA_WORDS_CHECK_FINISH = 0;
    public static final int MSG_EXTRA_WORDS_COMBINE_SUCCESS = 1;
    public static final int MSG_EXTRA_WORDS_COMBINE_FAILED = 2;

    public static final int IMPORT_OK = 0;
    public static final int IMPORT_FILE_ERROR = 1;
    public static final int IMPORT_GROUP_ERROR = 2;
    public static final int IMPORT_WORDS_ERROR = 3;
    public static final int IMPORT_OUTPUT_ERROR = 4;

    public static final String DEFAULT_WORDS_FILE_NAME = "default_local_words.json";
    public static final String DEFAULT_RULE_FILE_NAME = "rule.txt";
    public static final String DEFAULT_WORDS_DATA_NAME = "local_words";
    public static final String DEFAULT_EXTRA_WORDS_DATA_NAME = "addon_words";

    public static final String INTENT_EXTRA_PLAYER_NAME = "PLAYER_NAME";
    public static final String INTENT_EXTRA_PLAYER_WORDS = "PLAYER_WORDS";
    public static final String INTENT_EXTRA_PLAYER = "PLAYER";
    public static final String INTENT_EXTRA_PATH = "PATH";
    public static final String INTENT_EXTRA_FILE_NAME = "FILE_NAME";

    public static final int DEFAULT_MAX_NUMBER = 100;

    public static final int DEFAULT_MAX_PLAYER_NUMBER = 20;
    public static final int DEFAULT_MAX_PLAYER_BOARD_NUMBER = 2;
    public static final boolean DEFAULT_AUTO_SHOW_WINNER = true;
    public static final boolean DEFAULT__WHITE_BOARD_NOT_FIRST = true;
    public static final boolean DEFAULT_USE_DIY_WORDS = false;
    public static final String DEFAULT_DIY_WORDS = "[]";
    public static final boolean DEFAULT_USE_EXTRA_WORDS = false;
    public static final boolean DEFAULT_ONLY_USE_EXTRA_WORDS = false;

    public static final String PREFERENCE_MAX_PLAYER_NUMBER = "MAX_PLAYER_NUMBER";
    public static final String PREFERENCE_MAX_PLAYER_BOARD_NUMBER = "MAX_PLAYER_BOARD_NUMBER";
    public static final String PREFERENCE_WHITE_BOARD_NOT_FIRST = "WHITE_BOARD_NOT_FIRST";
    public static final String PREFERENCE_AUTO_SHOW_WINNER = "AUTO_SHOW_WINNER";
    public static final String PREFERENCE_USE_DIY_WORDS = "USE_DIY_WORDS";
    public static final String PREFERENCE_DIY_WORDS = "DIY_WORDS";
    public static final String PREFERENCE_USE_EXTRA_WORDS = "USE_EXTRA_WORDS";
    public static final String PREFERENCE_ONLY_USE_EXTRA_WORDS = "ONLY_USE_EXTRA_WORDS";
    public static final String PREFERENCE_IMPORT_EXTRA_WORDS = "IMPORT_EXTRA_WORDS";
    public static final String PREFERENCE_LOAD_LOCAL_DICTIONARY = "LOAD_LOCAL_DICTIONARY";
    public static final String PREFERENCE_ADD_NEW_EXTRA_WORDS = "ADD_NEW_EXTRA_WORDS";
    public static final String PREFERENCE_EDIT_EXTRA_WORDS = "EDIT_EXTRA_WORDS";
    public static final String PREFERENCE_COMBINE_EXTRA_WORDS = "COMBINE_EXTRA_WORDS";
    public static final String PREFERENCE_SELECT_EXTRA_WORDS = "SELECT_EXTRA_WORDS";
    public static final String PREFERENCE_DELETE_EXTRA_WORDS = "DELETE_EXTRA_WORDS";
    public static final String PREFERENCE_CHECK_EXTRA_WORDS = "CHECK_EXTRA_WORDS";
    public static final String PREFERENCE_RENAME_EXTRA_WORDS = "RENAME_EXTRA_WORDS";
    public static final String PREFERENCE_MANAGE_EXTRA_DICTIONARY = "MANAGE_EXTRA_DICTIONARY";
    public static final String PREFERENCE_GAME_RULE = "GAME_RULE";

    public static final int PLAYER_IDENTIFY_NORMAL = 0;
    public static final int PLAYER_IDENTIFY_SPY = 1;
    public static final int PLAYER_IDENTIFY_WHITE_BOARD = 2;

    public static final int PLAYER_WORD_NORMAL = 0;
    public static final int PLAYER_WORD_SPY = 1;

    public static final int DEFAULT_PLAYER_NUM = 5;
    public static final int DEFAULT_PLAYER_BOARD_NUM = 0;

    public static final int DEFAULT_MIN_PLAYER_NUM = 4;
    public static final int DEFAULT_MIN_PLAYER_SPY_NUM = 1;
    public static final int DEFAULT_MIN_PLAYER_BOARD_NUM = 0;

    private static final String DEFAULT_SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
    private static final String DEFAULT_APPLICATION_ROOT_DIR = DEFAULT_SDCARD_DIR + "CatchSpy" + File.separator;
    public static final String DEFAULT_APPLICATION_DATA_DIR = DEFAULT_APPLICATION_ROOT_DIR + "Extra Words" + File.separator;
}
