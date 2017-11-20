package game.xfy9326.catchspy.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import game.xfy9326.catchspy.BuildConfig;
import game.xfy9326.catchspy.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllView();
    }

    private void setAllView() {
        setContentView(R.layout.activity_about);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        String version = BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")";
        ((TextView) findViewById(R.id.textview_about_version)).setText(version);
        findViewById(R.id.textview_about_open_source).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/XFY9326/CatchSpy/blob/master/LICENSE")));
            }
        });
    }
}
