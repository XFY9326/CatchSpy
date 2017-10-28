package game.xfy9326.catchspy.Activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Views.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllView();
        setFragment(savedInstanceState);
    }

    private void setAllView() {
        setContentView(R.layout.activity_settings);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_settings_content, settingsFragment);
            fragmentTransaction.commit();
        }
    }
}
