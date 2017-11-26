package game.xfy9326.catchspy.Activities;

import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import game.xfy9326.catchspy.Methods.IOMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Tools.Code;
import game.xfy9326.catchspy.Tools.URI;
import game.xfy9326.catchspy.Utils.Config;
import game.xfy9326.catchspy.Views.GlobalSettingsFragment;

public class SettingsActivity extends AppCompatActivity {
    public boolean openExtraDictionaryFragment;

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
            GlobalSettingsFragment globalSettingsFragment = new GlobalSettingsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.layout_settings_content, globalSettingsFragment);
            fragmentTransaction.commit();
            openExtraDictionaryFragment = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (openExtraDictionaryFragment) {
            setFragment(null);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_GET_LOCAL_DICTIONARY) {
            if (resultCode == RESULT_OK) {
                if (data != null && data.getData() != null) {
                    ContentResolver resolver = getContentResolver();
                    String fileType = resolver.getType(data.getData());
                    if (fileType != null) {
                        if (fileType.startsWith("image")) {
                            Toast.makeText(this, R.string.settings_extra_words_load_failed, Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    String file_path = URI.getAbsolutePath(this, data.getData());
                    String text = IOMethod.readFile(file_path);
                    try {
                        JSONObject jsonObject = new JSONObject(text);
                        if (jsonObject.has(Config.DEFAULT_EXTRA_WORDS_DATA_NAME)) {
                            String name = new File(file_path).getName();
                            String path = Config.DEFAULT_APPLICATION_DATA_DIR + name;
                            if (IOMethod.writeFile(jsonObject.toString(), path)) {
                                String newName = Code.unicodeEncode(name) + "-" + Code.getFileMD5String(path);
                                if (IOMethod.renameFile(path, newName)) {
                                    Toast.makeText(this, R.string.settings_extra_words_load_success, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, R.string.settings_extra_words_load_failed, Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
