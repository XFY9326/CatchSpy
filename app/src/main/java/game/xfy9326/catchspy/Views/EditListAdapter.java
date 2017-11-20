package game.xfy9326.catchspy.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import game.xfy9326.catchspy.R;

public class EditListAdapter extends RecyclerView.Adapter<EditListViewHolder> {
    private final ArrayList<String[]> ExtraWord;
    private final Activity activity;
    public boolean hasEdited = false;

    public EditListAdapter(Activity activity, ArrayList<String[]> words) {
        this.activity = activity;
        this.ExtraWord = words;
    }

    @Override
    public void onBindViewHolder(final EditListViewHolder holder, int position) {
        holder.edittext_word_0.setText(ExtraWord.get(holder.getAdapterPosition())[0]);
        holder.edittext_word_1.setText(ExtraWord.get(holder.getAdapterPosition())[1]);
        holder.cardview_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editWords(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public EditListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.list_edit_extra_words, parent, false);
        return new EditListViewHolder(mView);
    }

    @Override
    public int getItemCount() {
        return ExtraWord.size();
    }

    public ArrayList<String[]> getEditedData() {
        return ExtraWord;
    }

    public void addWords() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_edit_extra_words, (ViewGroup) activity.findViewById(R.id.dialog_layout_edit_words));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.add);
        final TextInputEditText word_1 = mView.findViewById(R.id.edittext_edit_word_1);
        final TextInputEditText word_2 = mView.findViewById(R.id.edittext_edit_word_2);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String edit_1 = word_1.getText().toString();
                String edit_2 = word_2.getText().toString();
                if (!edit_1.isEmpty() && !edit_2.isEmpty()) {
                    ExtraWord.add(new String[]{edit_1, edit_2});
                    hasEdited = true;
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(activity, R.string.edit_extra_words_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setView(mView);
        builder.show();
    }

    private void editWords(final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View mView = inflater.inflate(R.layout.dialog_edit_extra_words, (ViewGroup) activity.findViewById(R.id.dialog_layout_edit_words));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.edit_extra_words);
        final TextInputEditText word_1 = mView.findViewById(R.id.edittext_edit_word_1);
        final TextInputEditText word_2 = mView.findViewById(R.id.edittext_edit_word_2);
        word_1.setText(ExtraWord.get(position)[0]);
        word_2.setText(ExtraWord.get(position)[1]);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String edit_1 = word_1.getText().toString();
                String edit_2 = word_2.getText().toString();
                if (!edit_1.isEmpty() && !edit_2.isEmpty()) {
                    ExtraWord.set(position, new String[]{edit_1, edit_2});
                    hasEdited = true;
                    notifyItemChanged(position);
                } else {
                    Toast.makeText(activity, R.string.edit_extra_words_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.setNeutralButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExtraWord.remove(position);
                hasEdited = true;
                notifyItemRemoved(position);
            }
        });
        builder.setView(mView);
        builder.show();
    }
}
