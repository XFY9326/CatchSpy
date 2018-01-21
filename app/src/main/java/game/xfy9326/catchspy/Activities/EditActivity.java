package game.xfy9326.catchspy.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import game.xfy9326.catchspy.Methods.EditWordMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;
import game.xfy9326.catchspy.Views.EditListAdapter;

public class EditActivity extends AppCompatActivity {
    private String ExtraWordsPath;
    private String ExtraWordsFileName;
    private EditListAdapter editListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPath();
        setAllView();
        showAttention();
    }

    private void showAttention() {
        Snackbar.make(findViewById(R.id.edit_layout_content), R.string.edit_extra_words_attention, Snackbar.LENGTH_SHORT).show();
    }

    private void getPath() {
        Intent intent = getIntent();
        if (intent != null) {
            ExtraWordsPath = intent.getStringExtra(Config.INTENT_EXTRA_PATH);
            ExtraWordsFileName = intent.getStringExtra(Config.INTENT_EXTRA_FILE_NAME);
            if (ExtraWordsPath == null || ExtraWordsFileName == null) {
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setAllView() {
        setContentView(R.layout.activity_edit);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        editListAdapter = new EditListAdapter(this, EditWordMethod.viewExtraWordList(ExtraWordsPath));
        RecyclerView recyclerView = findViewById(R.id.list_edit);
        recyclerView.setAdapter(editListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        if (!editListAdapter.hasEdited) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.attention);
            builder.setMessage(R.string.edit_exit_warn);
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (EditWordMethod.saveArrayToFile(EditActivity.this, editListAdapter.getEditedData(), ExtraWordsPath, ExtraWordsFileName)) {
                        Toast.makeText(EditActivity.this, R.string.edit_save_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditActivity.this, R.string.edit_save_failed, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            builder.setNegativeButton(android.R.string.no, null);
            builder.setNeutralButton(R.string.exit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit_menu_add:
                editListAdapter.addWords();
                break;
            case R.id.edit_menu_save:
                if (EditWordMethod.saveArrayToFile(EditActivity.this, editListAdapter.getEditedData(), ExtraWordsPath, ExtraWordsFileName)) {
                    Toast.makeText(EditActivity.this, R.string.edit_save_success, Toast.LENGTH_SHORT).show();
                    editListAdapter.hasEdited = false;
                } else {
                    Toast.makeText(EditActivity.this, R.string.edit_save_failed, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
