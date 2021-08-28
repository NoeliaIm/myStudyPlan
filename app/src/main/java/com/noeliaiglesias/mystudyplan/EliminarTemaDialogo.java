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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class EliminarTemaDialogo extends DialogFragment {

    AlertDialog.Builder builder;
    private StudyPlanLab studyPlanlab;
    private String asignatura;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        builder = new AlertDialog.Builder(Objects.requireNonNull(requireActivity()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialogo_eliminar_tema, null);
        studyPlanlab= new StudyPlanLab(Objects.requireNonNull(requireContext()));
        Spinner spinnerAsignatura = view.findViewById(R.id.asignaturParaBuscarTemaEliminar);
        Spinner spinnerTema = view.findViewById(R.id.temaParaEliminar);
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
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //TODO guardar log o implementar borrado lógico
                String asignaturaElegida = (String)spinnerAsignatura.getSelectedItem();
                String temaElegido= (String) spinnerTema.getSelectedItem();
                if(asignaturaElegida != null && temaElegido != null && !asignaturaElegida.isEmpty() && !temaElegido.isEmpty()){
                    Study study =studyPlanlab.getStudyByAsignaturaAndTema(asignaturaElegida, temaElegido);
                    studyPlanlab.deleteStudy(study);
                    ((MainActivity)getActivity()).refrescarContenido();
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setTitle("Elimina el tema");
        builder.setView(view);

        return builder.create();
    }
}
