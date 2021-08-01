package com.noeliaiglesias.mystudyplan;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Map;


public class EditarTemaDialogo extends DialogFragment {
    AlertDialog.Builder builder;
    private StudyPlanLab studyPlanlab;
    private String asignatura;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialogo_editar_tema, null);
        studyPlanlab = new StudyPlanLab(requireContext());
        Spinner spinnerAsignatura = view.findViewById(R.id.asignaturParaBuscar);
        Spinner spinnerTema = view.findViewById(R.id.temaParaEditar);
        EditText editTextNuevoTema = view.findViewById(R.id.editTextNuevoTema);
        SharedPreferences preferences = requireContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerAsignatura.setAdapter(adaptador);
        spinnerAsignatura.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> itemsTemas = new ArrayList<>();
                asignatura= parent.getItemAtPosition(position).toString();
                ArrayList<Study> studiesByAsignatura = new ArrayList<>(studyPlanlab.getStudiesByAsignatura(asignatura));
                for (Study study:
                    studiesByAsignatura ) {
                    itemsTemas.add(study.getTema());
                }
                ArrayAdapter<String> adaptadorTemas = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, itemsTemas);
                adaptadorTemas.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerTema.setAdapter(adaptadorTemas);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setPositiveButton(R.string.accept, (dialog, which) -> {
            String asignaturaElegida = (String) spinnerAsignatura.getSelectedItem();
            String temaElegido= (String) spinnerTema.getSelectedItem();
            String nuevoTema= String.valueOf(editTextNuevoTema.getEditableText());
            if(asignaturaElegida!= null && temaElegido!= null && !nuevoTema.isEmpty()){
                Study study = studyPlanlab.getStudyByAsignaturaAndTema(asignaturaElegida, temaElegido);
                study.setTema(nuevoTema);
                studyPlanlab.updateStudy(study);
            }

        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });

        builder.setTitle("Edita el tema");
        builder.setView(view);

        return builder.create();
    }
}
