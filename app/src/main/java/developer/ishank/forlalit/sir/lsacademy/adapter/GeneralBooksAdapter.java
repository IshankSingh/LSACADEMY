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

public class GeneralBooksAdapter extends RecyclerView.Adapter<GeneralBooksAdapter.GeneralBooksViewHolder>{

    private Context mContext;
    private ArrayList<BooksDataModel> generalList;
    private GeneralBookListener generalBookListener;

    public GeneralBooksAdapter(Context mContext, ArrayList<BooksDataModel> generalList, GeneralBookListener generalBookListener) {
        this.mContext = mContext;
        this.generalList = generalList;
        this.generalBookListener = generalBookListener;
    }

    @NonNull
    @Override
    public GeneralBooksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.technical_books_item, parent, false);
        return new GeneralBooksViewHolder(view, generalBookListener);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralBooksViewHolder holder, int position) {
        holder.title.setText(SentenceCase.getTitleForBooks(generalList.get(position).getTitle()));
        Picasso.get()
                .load(generalList.get(position).getThumbnail())
                .centerCrop()
                .resize(120,150)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return generalList.size();
    }

    class GeneralBooksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView thumbnail;
        GeneralBookListener generalBookListener;

        GeneralBooksViewHolder(@NonNull View itemView, GeneralBookListener generalBookListener) {
            super(itemView);
            title = itemView.findViewById(R.id.technical_book_title);
            thumbnail = itemView.findViewById(R.id.technical_book_thumbnail);
            this.generalBookListener = generalBookListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            generalBookListener.generalBookItemClickListener(getAdapterPosition());
        }
    }

    public interface GeneralBookListener {
        void generalBookItemClickListener(int position);
    }
}

