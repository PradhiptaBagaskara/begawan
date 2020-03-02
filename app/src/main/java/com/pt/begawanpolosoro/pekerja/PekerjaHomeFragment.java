package com.pt.begawanpolosoro.pekerja;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pt.begawanpolosoro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PekerjaHomeFragment extends Fragment {


    public PekerjaHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pekerja_home, container, false);
    }


    public class GajiAdapter extends RecyclerView.Adapter<PekerjaHomeFragment.GajiAdapter.MyviewHolder>{

        @NonNull
        @Override
        public GajiAdapter.MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull GajiAdapter.MyviewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class MyviewHolder extends RecyclerView.ViewHolder {
            public MyviewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }

}
