package game.xfy9326.catchspy.Activities;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import game.xfy9326.catchspy.Methods.IOMethod;
import game.xfy9326.catchspy.Methods.ImportMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Tools.URI;
import game.xfy9326.catchspy.Utils.Config;

public class ImportActivity extends AppCompatActivity {
    private String DictionaryName;
    private String DictionaryPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAllView();
        setFile();
    }

    private void setAllView() {
        setContentView(R.layout.activity_import);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        final TextInputEditText group_divider = findViewById(R.id.edittext_import_group_divider);
        final TextInputEditText words_divider = findViewById(R.id.edittext_import_words_divider);
        final CheckBox ignoreChangeLine = findViewById(R.id.checkbox_ignore_change_line);
        Button button_import = findViewById(R.id.button_import);
        button_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String divider_group = group_divider.getText().toString();
                String divider_words = words_divider.getText().toString();
                if (!divider_group.equals(divider_words) && !divider_group.isEmpty() && !divider_words.isEmpty()) {
                    int result = ImportMethod.extraFileImporter(DictionaryPath, DictionaryName, divider_group, divider_words, ignoreChangeLine.isChecked());
                    if (result == Config.IMPORT_OK) {
                        Toast.makeText(ImportActivity.this, R.string.import_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (result == Config.IMPORT_FILE_ERROR) {
                        Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_file_error, Snackbar.LENGTH_SHORT).show();
                    } else if (result == Config.IMPORT_OUTPUT_ERROR) {
                        Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_output_error, Snackbar.LENGTH_SHORT).show();
                    } else if (result == Config.IMPORT_GROUP_ERROR) {
                        Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_group_divider_error, Snackbar.LENGTH_SHORT).show();
                    } else if (result == Config.IMPORT_WORDS_ERROR) {
                        Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_words_divider_error, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_divider_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        Button advanced_import = findViewById(R.id.button_advanced_import);
        advanced_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advancedImportDialog(ImportActivity.this);
            }
        });
    }

    private void advancedImportDialog(Context context) {
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_advanced_import, (ViewGroup) findViewById(R.id.dialog_layout_import_advanced));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.import_advanced);
        final TextInputEditText group = mView.findViewById(R.id.edittext_import_advanced_group);
        final TextInputEditText word = mView.findViewById(R.id.edittext_import_advanced_word);
        final CheckBox ignoreChangeLine = mView.findViewById(R.id.checkbox_import_advanced_ignore_change_line);
        builder.setPositiveButton(R.string.import_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String groupReg = group.getText().toString();
                String wordReg = word.getText().toString();
                if (!groupReg.isEmpty()) {
                    if (!wordReg.isEmpty()) {
                        int result = ImportMethod.extraFileRegImporter(DictionaryPath, DictionaryName, groupReg, wordReg, ignoreChangeLine.isChecked());
                        if (result == Config.IMPORT_OK) {
                            Toast.makeText(ImportActivity.this, R.string.import_success, Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (result == Config.IMPORT_FILE_ERROR) {
                            Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_file_error, Snackbar.LENGTH_SHORT).show();
                        } else if (result == Config.IMPORT_OUTPUT_ERROR) {
                            Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_output_error, Snackbar.LENGTH_SHORT).show();
                        } else if (result == Config.IMPORT_GROUP_ERROR) {
                            Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_advanced_group_match_error, Snackbar.LENGTH_SHORT).show();
                        } else if (result == Config.IMPORT_WORDS_ERROR) {
                            Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_advanced_word_match_error, Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_advanced_word_match_error, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.import_layout_content), R.string.import_advanced_group_match_error, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private void setFile() {
        LayoutInflater inflater = getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_edittext, (ViewGroup) findViewById(R.id.layout_dialog_edit_text));
        final TextInputEditText editText = mView.findViewById(R.id.edittext_dialog);
        editText.setHint(R.string.import_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_name);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.import_file, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String data = editText.getText().toString();
                if (!data.replace(" ", "").equalsIgnoreCase("")) {
                    DictionaryName = data;
                    ImportMethod.selectFile(ImportActivity.this, Config.REQUEST_GET_EXTRA_FILE);
                } else {
                    Toast.makeText(ImportActivity.this, R.string.import_name_error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setView(mView);
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.REQUEST_GET_EXTRA_FILE) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                ContentResolver resolver = getContentResolver();
                String fileType = resolver.getType(data.getData());
                if (fileType != null) {
                    if (fileType.startsWith("image")) {
                        Toast.makeText(this, R.string.import_file_select_error, Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }
                }
                DictionaryPath = URI.getAbsolutePath(this, data.getData());
                String text = IOMethod.readFile(DictionaryPath);
                if (text != null) {
                    TextView preview = findViewById(R.id.textview_import_text_preview);
                    preview.setText(text);
                }
            } else {
                Toast.makeText(this, R.string.import_file_select_error, Toast.LENGTH_SHORT).show();
                finish();
            }
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
