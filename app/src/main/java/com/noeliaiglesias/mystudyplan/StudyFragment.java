package com.noeliaiglesias.mystudyplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private EditText mAsignaturaField;
    private EditText mTemaField;
    private StudyPlanLab studyPlanlab;
    private TextView planning;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStudy = new Study();
        studyPlanlab = new StudyPlanLab(getContext());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_study, container, false);
        mAsignaturaField = (EditText) v.findViewById(R.id.asignatura);
        mAsignaturaField.setOnClickListener(this::mostrarDialogo);
        mAsignaturaField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStudy.setAsignatura(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
       planning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudy(v);
            }
        });
        return v;
    }


    private void asignaturaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
           if(!entry.getKey().equals("numAsig")){
               items.add(entry.getValue().toString());
           }
        }
        String[] array = items.toArray(new String[items.size()]);
        builder.setItems(array, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAsignaturaField.setText(array[which]);
            }
        });

       AlertDialog dialog =builder.create();
       dialog.show();
    }
    public void mostrarDialogo(View v){
        asignaturaDialog();
    }


    public void addStudy(View v){
        Study s = new Study();
        s.setAsignatura(mAsignaturaField.getEditableText().toString());
        s.setTema(mTemaField.getEditableText().toString());
        s.setFechaInicio(LocalDate.now());
        s.setFechaFin(s.getFechaInicio().plusMonths(4));
        s.setProxExam(s.getFechaInicio().plusMonths(10));
        studyPlanlab.addStudy(s);
        mAsignaturaField.setText("");
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

