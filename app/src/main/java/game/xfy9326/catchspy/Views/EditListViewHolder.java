package game.xfy9326.catchspy.Views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import game.xfy9326.catchspy.R;

class EditListViewHolder extends RecyclerView.ViewHolder {
    final CardView cardview_edit;
    final TextView edittext_word_0;
    final TextView edittext_word_1;

    EditListViewHolder(View view) {
        super(view);
        cardview_edit = view.findViewById(R.id.cardview_list_edit);
        edittext_word_0 = view.findViewById(R.id.textview_edit_word_zero);
        edittext_word_1 = view.findViewById(R.id.textview_edit_word_one);
    }
}
