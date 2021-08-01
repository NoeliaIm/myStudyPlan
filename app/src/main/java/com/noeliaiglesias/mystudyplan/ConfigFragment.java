package com.noeliaiglesias.mystudyplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_config, container, false);
    }

    public  void onViewCreated(@NonNull View v , Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        EditText mAsignaturaField = (EditText) v.findViewById(R.id.asignatura);
        mAsignaturaField.setOnClickListener(this::mostrarDialogo);
        TextView editarTema = v.findViewById(R.id.editarTema);
        editarTema.setOnClickListener(v1 -> {
            EditarTemaDialogo editarTemaDialogo = new EditarTemaDialogo();
            editarTemaDialogo.show(requireActivity().getSupportFragmentManager(), "EditarTemaDialogo");
        });
        TextView eliminarTema= v.findViewById(R.id.eliminarTema);
        eliminarTema.setOnClickListener(v12 -> {
            EliminarTemaDialogo eliminarTemaDialogo = new EliminarTemaDialogo();
            eliminarTemaDialogo.show(requireActivity().getSupportFragmentManager(), "EliminarTemaDialogo");
        });

        TextView eliminarAsignatura = v.findViewById(R.id.eliminarAsignatura);
        eliminarAsignatura.setOnClickListener(v13 -> {
            EliminarAsignaturaDialogo eliminarAsignaturaDialogo = new EliminarAsignaturaDialogo();
            eliminarAsignaturaDialogo.show(requireActivity().getSupportFragmentManager(), "EliminarAsignaturaDialogo");
        });

    }

    private void asignaturaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        SharedPreferences preferences = requireContext().getSharedPreferences("MisAsignaturas", Context.MODE_PRIVATE);
        Map<String,?> keys = preferences.getAll();
        ArrayList<String> items=new ArrayList<>();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            if(!entry.getKey().equals("numAsig")){
                items.add(entry.getValue().toString());
            }
        }
        String[] array = items.toArray(new String[0]);
        builder.setItems(array, (dialog, which) -> {

        });

        AlertDialog dialog =builder.create();
        dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = 650;
        lp.x=-50;
        lp.y=-20;
        dialog.getWindow().setAttributes(lp);
    }
    public void mostrarDialogo(View v){
        asignaturaDialog();
    }
}