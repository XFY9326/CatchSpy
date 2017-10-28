package game.xfy9326.catchspy.Views;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import game.xfy9326.catchspy.R;

class GuessListViewHolder extends RecyclerView.ViewHolder {
    final CardView cardview_list;
    final TextView textview_player_name;
    final TextView textview_player_identify;
    final TextView textview_player_word;

    GuessListViewHolder(View view) {
        super(view);
        cardview_list = view.findViewById(R.id.cardview_list_guess);
        textview_player_name = view.findViewById(R.id.textview_guess_player_name);
        textview_player_identify = view.findViewById(R.id.textview_guess_player_identify);
        textview_player_word = view.findViewById(R.id.textview_guess_player_word);
    }
}
