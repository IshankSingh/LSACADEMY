package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.account.Synthesize;
import developer.ishank.forlalit.sir.lsacademy.classes.CommentSectionInitialLetterCircle;
import developer.ishank.forlalit.sir.lsacademy.classes.CommentTime;
import developer.ishank.forlalit.sir.lsacademy.datamodel.CommentSectionDataModel;

public class CommentVideoActivityAdapter extends RecyclerView.Adapter<CommentVideoActivityAdapter.CommentHolder>{

    private Context mContext;
    private ArrayList<CommentSectionDataModel> commentList;

    public CommentVideoActivityAdapter(Context mContext, ArrayList<CommentSectionDataModel> commentList) {
        this.mContext = mContext;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_video_activity_recycler_view_list_item, parent, false);
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        holder.username.setText(commentList.get(position).getUserName());
        holder.body.setText(commentList.get(position).getBody());
        holder.timestamp.setText(CommentTime.getTime(commentList.get(position).getTimestamp()));
        holder.initialLetterCommentUsernameTV.setText(Synthesize.synthesizeInitialLetter(commentList.get(position).getUserName()));

        String s = Synthesize.synthesizeInitialLetter(commentList.get(position).getUserName());
        char c = s.charAt(0);
        CommentSectionInitialLetterCircle commentSectionInitialLetterCircle = new CommentSectionInitialLetterCircle(mContext);
        holder.commentFL.setBackground(commentSectionInitialLetterCircle.getRequiredDrawable(c));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView username, body, timestamp, initialLetterCommentUsernameTV;
        FrameLayout commentFL;

         CommentHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_video_activity_rv_list_item_tv);
            body = itemView.findViewById(R.id.body_video_activity_rv_list_item_tv);
            timestamp = itemView.findViewById(R.id.timestamp_video_activity_rv_list_item_tv);
            initialLetterCommentUsernameTV = itemView.findViewById(R.id.initial_comment_username_tv);
            commentFL = itemView.findViewById(R.id.comment_video_activity_fl);
        }
    }
}
