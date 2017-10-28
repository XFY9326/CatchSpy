package game.xfy9326.catchspy.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class ShowActivity extends AppCompatActivity {
    //Player identify
    private static int[] Player;
    //Player's Name
    private static String[] PlayerName;
    //Play Words
    private static String[] PlayerWords;
    private boolean wordsPlayed = false;
    private int playingPlayerNumber = 0;
    private int playerNumber = 0;
    private TextView textView_attentions;
    private TextView textView_words;
    private TextInputEditText editText_player_name;
    private FloatingActionButton fab_play;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPlayerData();
        setAllView();
    }

    private void getPlayerData() {
        Intent intent = getIntent();
        if (intent != null) {
            Player = intent.getIntArrayExtra(Config.INTENT_EXTRA_PLAYER);
            PlayerWords = intent.getStringArrayExtra(Config.INTENT_EXTRA_PLAYER_WORDS);
            if (Player == null || PlayerWords == null) {
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                playerNumber = Player.length;
                PlayerName = new String[playerNumber];
            }
        } else {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setAllView() {
        setContentView(R.layout.activity_show);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        textView_attentions = findViewById(R.id.textview_show_attentions);
        textView_words = findViewById(R.id.textView_show_words);
        editText_player_name = findViewById(R.id.edittext_player_name);
        fab_play = findViewById(R.id.fab_show_play);
        setPlayerName();
        fab_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wordsPlayed) {
                    //Word Showed
                    String name = editText_player_name.getText().toString();
                    if (name.equals("")) {
                        name = getString(R.string.player) + (playingPlayerNumber + 1);
                    }
                    PlayerName[playingPlayerNumber] = name;
                    if (playingPlayerNumber >= playerNumber - 1) {
                        //Show word finish
                        Intent intent = new Intent(ShowActivity.this, GuessActivity.class);
                        intent.putExtra(Config.INTENT_EXTRA_PLAYER, Player);
                        intent.putExtra(Config.INTENT_EXTRA_PLAYER_NAME, PlayerName);
                        intent.putExtra(Config.INTENT_EXTRA_PLAYER_WORDS, PlayerWords);
                        startActivity(intent);
                        finish();
                    } else {
                        //Next Player
                        textView_words.setText("");
                        playingPlayerNumber++;
                        setFabImage(true);
                        setAttention(true);
                        setPlayerName();
                        wordsPlayed = false;
                    }

                } else {
                    //Word not show
                    wordsPlayed = true;
                    if (Player[playingPlayerNumber] == Config.PLAYER_IDENTIFY_NORMAL) {
                        textView_words.setText(PlayerWords[Config.PLAYER_WORD_NORMAL]);
                    } else if (Player[playingPlayerNumber] == Config.PLAYER_IDENTIFY_SPY) {
                        textView_words.setText(PlayerWords[Config.PLAYER_WORD_SPY]);
                    } else if (Player[playingPlayerNumber] == Config.PLAYER_IDENTIFY_WHITE_BOARD) {
                        textView_words.setText(R.string.white_board);
                    } else {
                        finish();
                    }
                    setFabImage(false);
                    setAttention(false);
                }
            }
        });
    }

    private void setFabImage(boolean noPlay) {
        if (noPlay) {
            fab_play.setImageResource(R.drawable.ic_play);
        } else {
            fab_play.setImageResource(R.drawable.ic_next);
        }
    }

    private void setPlayerName() {
        String name = getString(R.string.player) + (playingPlayerNumber + 1);
        editText_player_name.setText(name);
    }

    private void setAttention(boolean noPlay) {
        if (noPlay) {
            textView_attentions.setText(R.string.show_press_button_to_show);
        } else {
            if (playingPlayerNumber == playerNumber - 1) {
                textView_attentions.setText(R.string.show_press_button_to_guess);
            } else {
                textView_attentions.setText(R.string.show_press_button_next);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.show_layout_content), R.string.show_no_click_back, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction(R.string.replay, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Instance.finish();
                finish();
            }
        });
        snackbar.show();
    }
}
