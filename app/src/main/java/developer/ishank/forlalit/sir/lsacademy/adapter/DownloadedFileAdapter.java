package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import developer.ishank.forlalit.sir.lsacademy.R;
import developer.ishank.forlalit.sir.lsacademy.datamodel.DownloadedFileDataModel;

public class DownloadedFileAdapter extends RecyclerView.Adapter<DownloadedFileAdapter.DownloadedFileHolder> {
    private Context context;
    private ArrayList<DownloadedFileDataModel> DFD_ArrayList;
    private DownloadFileListener downloadFileListener;

    public DownloadedFileAdapter(Context context, ArrayList<DownloadedFileDataModel> DFD_ArrayList, DownloadFileListener downloadFileListener) {
        this.context = context;
        this.DFD_ArrayList = DFD_ArrayList;
        this.downloadFileListener = downloadFileListener;
    }

    @NonNull
    @Override
    public DownloadedFileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.downloaded_file_view_model, parent, false);
        return new DownloadedFileHolder(view, downloadFileListener);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadedFileHolder holder, int position) {
        holder.Title_TV.setText(DFD_ArrayList.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return DFD_ArrayList.size();
    }

    class DownloadedFileHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView Title_TV;
        DownloadFileListener downloadFileListener;

        DownloadedFileHolder(@NonNull View itemView, DownloadFileListener downloadFileListener) {
            super(itemView);
            Title_TV = itemView.findViewById(R.id.downloaded_file_title);

            this.downloadFileListener = downloadFileListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            downloadFileListener.downloadFileItemClickListener(getAdapterPosition());
        }
    }

    public interface DownloadFileListener {
        void downloadFileItemClickListener(int position);
    }
}
