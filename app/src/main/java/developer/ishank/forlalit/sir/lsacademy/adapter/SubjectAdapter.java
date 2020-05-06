package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.datamodel.SubjectDataModel;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {

    private Context mContext;
    private ArrayList<SubjectDataModel> subjectList;
    private OnSubjectListener onSubjectListener;

    public SubjectAdapter(Context mContext, ArrayList<SubjectDataModel> subjectList, OnSubjectListener onSubjectListener) {
        this.mContext = mContext;
        this.subjectList = subjectList;
        this.onSubjectListener = onSubjectListener;
    }

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_recycler_view_item,parent,false);
        return new SubjectHolder(view, onSubjectListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        holder.title.setText(subjectList.get(position).getTitle());
        Picasso.get().load(subjectList.get(position).getThumbnail()).resize(160,120).centerCrop().into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class SubjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView thumbnail;
        OnSubjectListener onSubjectListener;


         SubjectHolder(@NonNull View itemView, OnSubjectListener onSubjectListener) {
            super(itemView);
            title = itemView.findViewById(R.id.subject_title);
            thumbnail = itemView.findViewById(R.id.subject_thumbnail);
            this.onSubjectListener = onSubjectListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onSubjectListener.onSubjectViewClick(getAdapterPosition());
        }
    }
    public interface OnSubjectListener {
        void onSubjectViewClick(int position);
    }
}
