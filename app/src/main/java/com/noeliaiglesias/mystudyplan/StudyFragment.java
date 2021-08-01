package com.noeliaiglesias.mystudyplan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudyFragment extends Fragment {

    private Study mStudy;
    private String asignatura;
    private EditText mTemaField;
    private StudyPlanLab studyPlanlab;
    private TextView planning;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudy = new Study();
        studyPlanlab = new StudyPlanLab(requireContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_study, container, false);
        Spinner spinner = v.findViewById(R.id.asignaturaSpinner);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, asignaturas());
        adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                asignatura= parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTemaField = (EditText) v.findViewById(R.id.tema);
        mTemaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStudy.setTema(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
       planning = v.findViewById(R.id.planning);
       planning.setOnClickListener(v1 -> {
           if(!mTemaField.getEditableText().toString().isEmpty()) {
               addStudy(v1);
           }

       });
        return v;
    }


    private ArrayList<String> asignaturas(){
        SharedPreferences preferences = requireContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
           if(!entry.getKey().equals("numAsig")){
               items.add(entry.getValue().toString());
           }
        }

        return  items;
    }


    public void addStudy(View v){
        Study s = new Study();
        s.setAsignatura(asignatura);
        s.setTema(mTemaField.getEditableText().toString());
        s.setFechaInicio(LocalDate.now());
        s.setFechaFin(s.getFechaInicio().plusMonths(4));
        s.setProxExam(s.getFechaInicio().plusMonths(10));
        studyPlanlab.addStudy(s);
        mTemaField.setText("");
    }
    private static class ListStudyHolder extends RecyclerView.ViewHolder {
        LinearLayout llStudyContainer;
        TextView tvItemAsignatura, tvItemTema, tvItemFechaInicio;
        public ListStudyHolder(@NonNull View itemView) {
            super(itemView);
            llStudyContainer = itemView.findViewById(R.id.llStudyContainer);
            tvItemAsignatura = itemView.findViewById(R.id.tvItemAsignatura);
            tvItemTema = itemView.findViewById(R.id.tvItemTema);
            tvItemFechaInicio = itemView.findViewById(R.id.tvItemFechaInicio);
        }
    }

    public static class StudyList extends Fragment {
        private RecyclerView mStudyRecyclerView;
        private ListAdapter listAdapter;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.recycler, container, false);
            mStudyRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewList);
            RecyclerView.LayoutManager manager= new LinearLayoutManager(getActivity());
            mStudyRecyclerView.setLayoutManager(manager);
            updateUI(mStudyRecyclerView);
            return  view;
        }

        private void updateUI(RecyclerView mStudyRecyclerView) {
           StudyPlanLab studyPlanLab = StudyPlanLab.get(getActivity());
            List<Study> mStudies = studyPlanLab.getStudies();

            listAdapter = new ListAdapter(mStudies);
            mStudyRecyclerView.setAdapter(listAdapter);
        }

        private class ListAdapter extends RecyclerView.Adapter<ListStudyHolder>{
            private List<Study> mStudies;

            public ListAdapter(List<Study> mStudies) {
                this.mStudies = mStudies;
            }

            @NonNull
            @Override
            public ListStudyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View view = layoutInflater.inflate(R.layout.study_list, parent, false);
                return new ListStudyHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull  StudyFragment.ListStudyHolder holder, int position) {
               holder.tvItemAsignatura.setText(mStudies.get(position).getAsignatura());
               holder.tvItemTema.setText(mStudies.get(position).getTema());
               holder.tvItemFechaInicio.setText(mStudies.get(position).getFechaInicio().format(DateTimeFormatter.ofPattern("EEE , dd-MM-yyyy")));
            }

            @Override
            public int getItemCount() {
                return mStudies.size();
            }
        }
    }
}

