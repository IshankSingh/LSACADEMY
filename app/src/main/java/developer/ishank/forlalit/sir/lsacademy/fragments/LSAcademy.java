package developer.ishank.forlalit.sir.lsacademy.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import developer.ishank.forlalit.sir.lsacademy.R;

public class LSAcademy extends Fragment {

    public LSAcademy() {

    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_lsacademy, container, false);
    }
}
