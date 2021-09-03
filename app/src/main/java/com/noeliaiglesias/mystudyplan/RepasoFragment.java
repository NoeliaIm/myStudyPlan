package com.noeliaiglesias.mystudyplan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.noeliaiglesias.mystudyplan.placeholder.Repaso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class RepasoFragment extends Fragment {

    private StudyPlanLab studyPlanlab;
    private Repaso repaso;
    private RecyclerView recyclerView;


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RepasoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static RepasoFragment newInstance(int columnCount) {
        RepasoFragment fragment = new RepasoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        repaso = new Repaso();
        studyPlanlab = new StudyPlanLab(requireContext());


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repaso_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            List<Repaso> repasos = studyPlanlab.getAllFirstRepasos();
            repasos.sort(new Comparator<Repaso>() {
                @Override
                public int compare(Repaso o1, Repaso o2) {
                    return o1.getFechaSiguienteRepaso().compareTo(o2.getFechaSiguienteRepaso());
                }
            });
            recyclerView.setAdapter(new MyRepasoRecyclerViewAdapter(repasos));
        }
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void refrescarContenido() {
        List<Repaso> repasos = studyPlanlab.getAllFirstRepasos();
        repasos.sort(new Comparator<Repaso>() {
            @Override
            public int compare(Repaso o1, Repaso o2) {
                return o1.getFechaSiguienteRepaso().compareTo(o2.getFechaSiguienteRepaso());
            }
        });
        recyclerView.setAdapter(new MyRepasoRecyclerViewAdapter(repasos));
    }
}
