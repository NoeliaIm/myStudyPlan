package com.noeliaiglesias.mystudyplan.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.noeliaiglesias.mystudyplan.Study;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class StudyCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public StudyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Study  getStudy(){
        String uuidString = getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.UUID));
        String asignatura = getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.ASIGNATURA));
        String tema = getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.TEMA));
        String fecha = getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.FECHA_INICIO));
        String fechaFin= getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.FECHA_FIN));
        String fechaExam = getString(getColumnIndex(MyStudyPlanDbSchema.MyStudyPlanTable.Cols.PROX_EXAM));
        Study study = new Study(UUID.fromString(uuidString));
        study.setAsignatura(asignatura);
        study.setTema(tema);
        study.setFechaInicio(LocalDate.parse(fecha, DateTimeFormatter.ofPattern("dd-MM-yyyy") ));
        study.setFechaFin(LocalDate.parse(fechaFin, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        study.setProxExam(LocalDate.parse(fechaExam, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        return study;
    }

}
