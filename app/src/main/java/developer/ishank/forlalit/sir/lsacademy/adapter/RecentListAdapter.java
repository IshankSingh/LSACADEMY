package developer.ishank.forlalit.sir.lsacademy.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentListAdapter  {


    class RecentHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView thumbnail;
        public RecentHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
