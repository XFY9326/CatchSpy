package game.xfy9326.catchspy.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import game.xfy9326.catchspy.Methods.IOMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class RuleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllView();
    }

    private void setAllView() {
        setContentView(R.layout.activity_rule);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String data = IOMethod.getFromAssets(this, Config.DEFAULT_RULE_FILE_NAME);
        if (data != null) {
            ((TextView) findViewById(R.id.textview_rule)).setText(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
