package com.noeliaiglesias.mystudyplan;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;

public class FechaExamenDialogo extends DialogFragment {
    AlertDialog.Builder builder;
    private StudyPlanLab studyPlanlab;
    private String asignatura;


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle mArgs = getArguments();
        studyPlanlab = new StudyPlanLab(requireContext());
        SharedPreferences preferences = requireContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }

        builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogo_fecha_examen, null);
        Spinner spinner = view.findViewById(R.id.asignatura_examen);
        MultiSelectionSpinner selectionSpinner = view.findViewById(R.id.spn_items);
        selectionSpinner.setItems(new ArrayList<>());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                asignatura= parent.getItemAtPosition(position).toString();
                ArrayList<Study> itemsMult = new ArrayList<>(studyPlanlab.getStudiesByAsignatura(asignatura));
                selectionSpinner.setItems(itemsMult);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adaptador);
        builder.setView(view);
        builder.setTitle("Fecha de examen");
        builder.setPositiveButton(R.string.accept, (dialog, which) -> {
            ArrayList<Study> studiesModificadas =  selectionSpinner.getSelectedItems();
            for (Study study:
                studiesModificadas ) {
                assert mArgs != null;
                study.setFechaFin(LocalDate.parse(mArgs.getString("selecction_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                study.setProxExam(LocalDate.parse(mArgs.getString("selecction_date"), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                studyPlanlab.updateStudy(study);
                studyPlanlab.updateRepasos(study);
                ((MainActivity)getActivity()).refrescarContenido();
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });

        return builder.create();
    }
}
