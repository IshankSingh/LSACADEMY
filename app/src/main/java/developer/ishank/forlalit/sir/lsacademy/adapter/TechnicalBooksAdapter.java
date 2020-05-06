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
import developer.ishank.forlalit.sir.lsacademy.datamodel.BooksDataModel;

public class TechnicalBooksAdapter extends RecyclerView.Adapter<TechnicalBooksAdapter.TechnicalBooksViewHolder>{

    private Context mContext;
    private ArrayList<BooksDataModel> technicalList;
    private TechnicalBookListener technicalBookListener;

    public TechnicalBooksAdapter(Context mContext, ArrayList<BooksDataModel> technicalList, TechnicalBookListener technicalBookListener) {
        this.mContext = mContext;
        this.technicalList = technicalList;
        this.technicalBookListener = technicalBookListener;
    }

    @NonNull
    @Override
    public TechnicalBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.technical_books_item, parent, false);
        return new TechnicalBooksViewHolder(view, technicalBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TechnicalBooksViewHolder holder, int position) {
        holder.title.setText(SentenceCase.getTitleForBooks(technicalList.get(position).getTitle()));
        Picasso.get()
                .load(technicalList.get(position).getThumbnail())
                .centerCrop()
                .resize(120,150)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return technicalList.size();
    }

    class TechnicalBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView thumbnail;
        TechnicalBookListener technicalBookListener;

        TechnicalBooksViewHolder(@NonNull View itemView, TechnicalBookListener technicalBookListener) {
            super(itemView);
            title = itemView.findViewById(R.id.technical_book_title);
            thumbnail = itemView.findViewById(R.id.technical_book_thumbnail);
            this.technicalBookListener = technicalBookListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            technicalBookListener.technicalBookItemClickListener(getAdapterPosition());
        }
    }

    public interface TechnicalBookListener {
        void technicalBookItemClickListener(int position);
    }
}
