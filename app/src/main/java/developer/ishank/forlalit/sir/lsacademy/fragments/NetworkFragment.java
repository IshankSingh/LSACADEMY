package developer.ishank.forlalit.sir.lsacademy.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import developer.ishank.forlalit.sir.lsacademy.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NetworkFragment extends Fragment{
    private OnNetworkCallback mCallBack;

    public NetworkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_network, container, false);
        Button mNetworkBtn = view.findViewById(R.id.network_btn);
        mNetworkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onNetworkCallback();
            }
        });
        return view;
    }


    public interface OnNetworkCallback {
        void onNetworkCallback();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;

        try{
            mCallBack = (OnNetworkCallback) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()+ "must override onNetworkCallback");
        }
    }
}
