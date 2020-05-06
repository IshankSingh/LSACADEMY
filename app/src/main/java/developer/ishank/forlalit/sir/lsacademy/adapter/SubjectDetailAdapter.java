package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.classes.SentenceCase;
import developer.ishank.forlalit.sir.lsacademy.datamodel.SubjectDetailsDataModel;

public class SubjectDetailAdapter extends RecyclerView.Adapter<SubjectDetailAdapter.DetailHolder>{

    private Context mContext;
    private ArrayList<SubjectDetailsDataModel> mSubjectDetailList;
    private OnSubjectDetailListener onSubjectDetailListener;

    public SubjectDetailAdapter(Context mContext, ArrayList<SubjectDetailsDataModel> mSubjectDetailList, OnSubjectDetailListener onSubjectDetailListener) {
        this.mContext = mContext;
        this.mSubjectDetailList = mSubjectDetailList;
        this.onSubjectDetailListener = onSubjectDetailListener;
    }

    @NonNull
    @Override
    public DetailHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.subject_detail_list_item, parent, false);
        return new DetailHolder(view, onSubjectDetailListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, int position) {

        holder.title.setText(SentenceCase.getTitleForSubjectDetailActivity(mSubjectDetailList.get(position).getTitle()));
        Picasso.get()
                .load(mSubjectDetailList.get(position).getThumbnail())
                .resize(140, 90)
                .centerCrop()
                .into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return mSubjectDetailList.size();
    }

    class DetailHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView thumbnail;
        OnSubjectDetailListener onSubjectDetailListener;

         DetailHolder(@NonNull View itemView, OnSubjectDetailListener onSubjectDetailListener) {
            super(itemView);
            title = itemView.findViewById(R.id.subject_detail_text_view);
            thumbnail = itemView.findViewById(R.id.subject_detail_image_view);
            this.onSubjectDetailListener = onSubjectDetailListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onSubjectDetailListener.onClickSubjectDetailListener(getAdapterPosition());
        }
    }
    public interface OnSubjectDetailListener {
        void onClickSubjectDetailListener(int position);
    }
}
