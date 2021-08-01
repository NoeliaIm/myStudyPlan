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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EliminarAsignaturaDialogo extends DialogFragment {
    AlertDialog.Builder builder;
    Map<String, ?> prefsKeysAndValues = new HashMap<>();
    String keyItem="";
    private StudyPlanLab studyPlanlab;

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(Objects.requireNonNull(requireActivity()));
        studyPlanlab= new StudyPlanLab(Objects.requireNonNull(requireContext()));
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view =inflater.inflate(R.layout.dialogo_eliminar_asignatura, null);
        Spinner spinner = view.findViewById(R.id.spinnerEliminarAsignatura);
        ArrayAdapter<String> adaptador = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, asignaturas());
        adaptador.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adaptador);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for (Map.Entry<String, ?> entry:
                        prefsKeysAndValues.entrySet()) {
                    if(entry.getValue().equals(parent.getItemAtPosition(position))){
                        keyItem= entry.getKey();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        builder.setPositiveButton(R.string.eliminar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = Objects.requireNonNull(requireContext()).getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                String asignatura = preferences.getString(keyItem, "");
                if(asignatura!= null && !asignatura.isEmpty()){
                    editor.remove(keyItem);
                    editor.apply();
                    studyPlanlab.deleteStudiesByAsignatura(asignatura);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {


        });
        builder.setView(view);
        builder.setTitle("Elimina una asignatura");
        return builder.create();
    }

    private ArrayList<String> asignaturas(){
        SharedPreferences preferences = Objects.requireNonNull(requireContext()).getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        prefsKeysAndValues=keys;
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }

        return  items;
    }


}
