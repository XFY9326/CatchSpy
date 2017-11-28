package game.xfy9326.catchspy.Views;

import android.app.Activity;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import game.xfy9326.catchspy.Methods.WordMethod;
import game.xfy9326.catchspy.R;
import game.xfy9326.catchspy.Utils.Config;

public class GuessListAdapter extends RecyclerView.Adapter<GuessListViewHolder> {
    private final Activity activity;
    private final boolean[] PlayerShowed;
    private final String[] PlayerName;
    private final int[] PlayerId;
    private final String[] PlayerWord;
    private boolean showAll;

    public GuessListAdapter(Activity activity, String[] name, int[] id, String[] word) {
        this.activity = activity;
        this.PlayerName = name;
        this.PlayerId = id;
        this.PlayerWord = word;
        this.showAll = false;
        this.PlayerShowed = getPlayerShowed(name.length);
    }

    public void showAllMessage() {
        this.showAll = true;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final GuessListViewHolder holder, int position) {
        holder.textview_player_name.setText(PlayerName[holder.getAdapterPosition()]);
        if (showAll) {
            showAnswer(holder);
        } else {
            holder.cardview_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerShowed[holder.getAdapterPosition()] = true;
                    showAnswer(holder);
                    if (!showAll) {
                        if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean(Config.PREFERENCE_AUTO_SHOW_WINNER, Config.DEFAULT_AUTO_SHOW_WINNER)) {
                            int winner = getWinner();
                            if (winner >= 0) {
                                showWinner(winner);
                                showAll = true;
                                notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public GuessListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View mView = inflater.inflate(R.layout.list_guess_player, parent, false);
        return new GuessListViewHolder(mView);
    }

    @Override
    public int getItemCount() {
        return PlayerName.length;
    }

    private void showAnswer(GuessListViewHolder holder) {
        int id = PlayerId[holder.getAdapterPosition()];
        if (id == Config.PLAYER_IDENTIFY_WHITE_BOARD) {
            holder.textview_player_word.setText(activity.getString(R.string.white_board));
        } else {
            holder.textview_player_word.setText(PlayerWord[id]);
        }
        holder.textview_player_identify.setText(WordMethod.getPlayerIdentifyString(activity, id));
    }

    private boolean[] getPlayerShowed(int length) {
        boolean[] bol = new boolean[length];
        for (int i = 0; i < length; i++) {
            bol[i] = false;
        }
        return bol;
    }

    private int getWinner() {
        int count_normal_not_showed = 0;
        int count_spy_not_showed = 0;
        for (int i = 0; i < PlayerName.length; i++) {
            if (!PlayerShowed[i]) {
                if (PlayerId[i] == Config.PLAYER_WORD_NORMAL) {
                    count_normal_not_showed++;
                } else {
                    count_spy_not_showed++;
                }
            }
        }
        if (count_spy_not_showed == 0) {
            return Config.PLAYER_IDENTIFY_NORMAL;
        } else if ((count_normal_not_showed == 1 && count_spy_not_showed == 1) || (count_normal_not_showed == 0)) {
            return Config.PLAYER_IDENTIFY_SPY;
        }
        return -1;
    }

    private void showWinner(int type) {
        int text;
        if (type == Config.PLAYER_IDENTIFY_NORMAL) {
            text = R.string.win_normal;
        } else {
            text = R.string.win_spy;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(text);
        builder.setMessage(R.string.win_msg);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        builder.setNegativeButton(R.string.later, null);
        builder.show();
    }
}
