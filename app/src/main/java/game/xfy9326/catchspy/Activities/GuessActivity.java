package game.xfy9326.catchspy.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;
import game.xfy9326.catchspy.Views.GuessListAdapter;

public class GuessActivity extends AppCompatActivity {
    //Player identify
    private static int[] PlayerId;
    //Player's Name
    private static String[] PlayerName;
    //Play Words
    private static String[] PlayerWords;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPlayerData();
        setAllView();
    }

    private void getPlayerData() {
        Intent intent = getIntent();
        if (intent != null) {
            PlayerId = intent.getIntArrayExtra(Config.INTENT_EXTRA_PLAYER);
            PlayerWords = intent.getStringArrayExtra(Config.INTENT_EXTRA_PLAYER_WORDS);
            PlayerName = intent.getStringArrayExtra(Config.INTENT_EXTRA_PLAYER_NAME);
            if (PlayerId == null || PlayerName == null || PlayerWords == null) {
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setAllView() {
        setContentView(R.layout.activity_guess);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        //RecyclerView
        final GuessListAdapter guessListAdapter = new GuessListAdapter(this, PlayerName, PlayerId, PlayerWords);
        RecyclerView recyclerView = findViewById(R.id.list_guess);
        recyclerView.setAdapter(guessListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //CardView
        findViewById(R.id.cardview_guess_new).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.cardview_guess_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guessListAdapter.showAllMessage();
            }
        });

        //SnackBar
        Snackbar.make(findViewById(R.id.guess_layout_content), R.string.click_to_guess, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.guess_layout_content), R.string.guess_no_click_back, Snackbar.LENGTH_SHORT);
        snackbar.setActionTextColor(Color.RED);
        snackbar.setAction(R.string.exit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.Instance.finish();
                finish();
            }
        });
        snackbar.show();
    }
}
