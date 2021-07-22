package com.noeliaiglesias.mystudyplan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarioFragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment calendarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarioFragment newInstance(String param1, String param2) {
        CalendarioFragment fragment = new CalendarioFragment();
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
        return inflater.inflate(R.layout.fragment_calendario, container, false);
    }

    public  void onViewCreated(View v , Bundle savedInstanceState){
        super.onViewCreated(v, savedInstanceState);
        initCalendar(v);
    }

    private void initCalendar(View v){
        CalendarView calendarView = v.findViewById(R.id.simpleCalendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
                FechaExamenDialogo fechaExamenDialogo = new FechaExamenDialogo();
                Bundle args = new Bundle();
                LocalDate selectionDate = LocalDate.of(year, month+1, dayOfMonth);
                if(selectionDate.isAfter(LocalDate.now())){
                    args.putString("selecction_date", selectionDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    fechaExamenDialogo.setArguments(args);
                    fechaExamenDialogo.show(getActivity().getSupportFragmentManager(),"FechaExamenDialogo");
                }else {
                    Toast.makeText(getContext(),"La fecha del examen no puede ser antes de mañana", Toast.LENGTH_LONG ).show();
                }
            }
        });
    }
}