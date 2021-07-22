package com.noeliaiglesias.mystudyplan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FechaExamenDialogo extends DialogFragment {
    AlertDialog.Builder builder;
    private StudyPlanLab studyPlanlab;
    private String asignatura;


    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle mArgs = getArguments();
        studyPlanlab = new StudyPlanLab(getContext());
        SharedPreferences preferences = getContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }
        String[] array = items.toArray(new String[items.size()]);
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_fecha_examen, null);
        Spinner spinner = view.findViewById(R.id.asignatura_examen);
        MultiSelectionSpinner selectionSpinner = view.findViewById(R.id.spn_items);
        selectionSpinner.setItems(new ArrayList<>());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Study> itemsMult = new ArrayList<>();
                asignatura= parent.getItemAtPosition(position).toString();
                if(asignatura!= null){
                    itemsMult.addAll(studyPlanlab.getStudiesByAsignatura(asignatura));
                    selectionSpinner.setItems(itemsMult);
                }else{
                    selectionSpinner.setItems(new ArrayList<>());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adaptador = new ArrayAdapter<String >(getActivity(), android.R.layout.simple_spinner_item, items);
        adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adaptador);
        builder.setView(view);
        builder.setTitle("Fecha de examen");
        builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Study> studiesModificadas =  selectionSpinner.getSelectedItems();
                for (Study study:
                    studiesModificadas ) {
                    study.setFechaFin(LocalDate.parse(mArgs.getString("selecction_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    study.setProxExam(LocalDate.parse(mArgs.getString("selecction_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    studyPlanlab.updateStudy(study);
                    studyPlanlab.updateRepasos(study);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
