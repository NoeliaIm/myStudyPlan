package com.noeliaiglesias.mystudyplan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.noeliaiglesias.mystudyplan.database.MyStudyPlanDbSchema.MyStudyPlanTable;
import com.noeliaiglesias.mystudyplan.database.StudyCursorWrapper;
import com.noeliaiglesias.mystudyplan.database.StudyPlanBaseHelper;
import com.noeliaiglesias.mystudyplan.placeholder.Repaso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class StudyPlanLab {
    private static StudyPlanLab sStudyPlanLab; //se utiliza la convención Android s para indicar que es una variable estática
    private final SQLiteDatabase mDatabase;

    public static StudyPlanLab get(Context context) {
        if (sStudyPlanLab == null) {
            sStudyPlanLab = new StudyPlanLab(context);
        }
        return sStudyPlanLab;
    }

    StudyPlanLab(Context context) {
        Context mContext = context.getApplicationContext();
        mDatabase = new StudyPlanBaseHelper(mContext).getWritableDatabase();
    }

    public void addStudy(Study s) {
        ContentValues values = getContentValues(s);
        mDatabase.insert(MyStudyPlanTable.NAME_STUDY, null, values);
        addRepasos(s);
    }

    public void updateStudy(Study s) {
        String uuidString = s.getId().toString();
        ContentValues values = getContentValues(s);

        mDatabase.update(MyStudyPlanTable.NAME_STUDY, values, MyStudyPlanTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public void deleteStudy(Study s){
        String table= MyStudyPlanTable.NAME_STUDY;
        String whereClause = MyStudyPlanTable.Cols.UUID+"=?";
        String [] whereArgs= {s.getId().toString()};
        int idStudy= getIdStudy(s);
        mDatabase.delete(table, whereClause, whereArgs);
        deleteRepasos(idStudy);
    }

    public void deleteStudiesByAsignatura(String asignatura){
        String table = MyStudyPlanTable.NAME_STUDY;
        String whereClause = MyStudyPlanTable.Cols.ASIGNATURA +"=?";
        String [] whereArgs =  {asignatura};
        ArrayList<Integer> idStudies =getIdStudiesByAsignatura(asignatura);
        mDatabase.delete(table, whereClause,whereArgs);
        if(!idStudies.isEmpty()){
            deleteRepasosByAsignatura(idStudies);
        }
    }

    public Study getStudyByAsignaturaAndTema(String asignatura, String tema){
        Study study = new Study();
        String whereClause = "asignatura=? and tema=?";
        String [] whereArgs ={asignatura, tema};
        StudyCursorWrapper cursorWrapper = queryStudies(whereClause, whereArgs);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                study = cursorWrapper.getStudy();
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return  study;
    }

    public List<Study> getStudies() {
        ArrayList<Study> studies = new ArrayList<>();
        StudyCursorWrapper cursorWrapper = queryStudies(null, null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                if(cursorWrapper.getStudy().getFechaFin().isAfter(LocalDate.now())) {
                    studies.add(cursorWrapper.getStudy());
                }
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return studies;
    }
    public List<Study> getStudiesByAsignatura(String asignatura) {
        ArrayList<Study> studies = new ArrayList<>();
        String  whereClause = "asignatura=?";
        String [] whereArgs = {asignatura};
        StudyCursorWrapper cursorWrapper = queryStudies(whereClause , whereArgs);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                if(cursorWrapper.getStudy().getFechaFin().isAfter(LocalDate.now())) {
                    studies.add(cursorWrapper.getStudy());
                }
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }

        return studies;
    }


    public List<LocalDate> getRepasos(Study s) {
        String sql = "select * from " + MyStudyPlanTable.NAME_REPASOS + " where id_study =" + getIdStudy(s);
        Cursor cursor = mDatabase.rawQuery(sql, null);
        List<LocalDate> repasos = new ArrayList<>();
        if (cursor.moveToFirst()) {
            for (int i = 3; i <= 17; i++) {
                LocalDate date = LocalDate.parse(cursor.getString(i), DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                if(s.getFechaFin().isAfter(LocalDate.now()) || s.getFechaFin().isEqual(LocalDate.now())){
                    repasos.add(date);
                }
            }
        }
        cursor.close();
        return repasos;
    }

    public Map<String, LocalDate> getRepasosAndColumns(Study s){
        String sql = "select * from " + MyStudyPlanTable.NAME_REPASOS + " where id_study =" + getIdStudy(s);
        Cursor cursor = mDatabase.rawQuery(sql, null);
       Map<String, LocalDate> repasos = new HashMap<>();
        if (cursor.moveToFirst()) {
            for (int i = 3; i <= 22; i++) {
                repasos.put(cursor.getColumnName(i),LocalDate.parse(cursor.getString(i), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
        }
        cursor.close();
        return  repasos;
    }

    private LocalDate fecha_sig_repaso(List<LocalDate> repasos) {
        LocalDate fecha;
        for (int i = 0; i < repasos.size(); i++) {
            if (repasos.get(i).isAfter(LocalDate.now())|| repasos.get(i).isEqual(LocalDate.now())) {
                fecha = repasos.get(i);
                return fecha;
            }
        }
        return null;
    }


    public Study getStudy(UUID id) {
        Study study = new Study();
        String whereClause = MyStudyPlanTable.Cols.UUID + " = ?";
        String[] selectionArgs = {id.toString()};
        StudyCursorWrapper cursorWrapper = queryStudies(whereClause, selectionArgs);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                study = cursorWrapper.getStudy();
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return study;
    }

    private static ContentValues getContentValues(Study study) {
        ContentValues values = new ContentValues();
        values.put(MyStudyPlanTable.Cols.UUID, study.getId().toString());
        values.put(MyStudyPlanTable.Cols.ASIGNATURA, study.getAsignatura());
        values.put(MyStudyPlanTable.Cols.TEMA, study.getTema());
        values.put(MyStudyPlanTable.Cols.FECHA_INICIO, study.getFechaInicio().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        values.put(MyStudyPlanTable.Cols.FECHA_FIN, study.getFechaFin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        values.put(MyStudyPlanTable.Cols.PROX_EXAM, study.getProxExam().format(DateTimeFormatter.ofPattern("dd-MM-yyyy") ));
        return values;
    }

    private void addRepasos(Study s) {
        String sql = "select _id from " + MyStudyPlanTable.NAME_STUDY + " where uuid =?";
        String[] selectionArgs = {s.getId().toString()};
        Cursor cursor = mDatabase.rawQuery(sql, selectionArgs);
        Integer id = null;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        LocalDate fecha_inicio = s.getFechaInicio();
        LocalDate r1 = fecha_inicio.plusDays(1);
        LocalDate r2 = r1.plusDays(3);
        List<LocalDate> sucesivas = new ArrayList<>();
        for (int i = 0; i <= 17; i++) {
            if (i == 0) {
                sucesivas.add(sigFecha(r2));
            } else {
                sucesivas.add(sigFecha(sucesivas.get(i - 1)));
            }
        }
        String uuid = UUID.randomUUID().toString();
        String sqlInsert = String.format("insert into %s ( uuid,id_study,r1, r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20) " +
                "values (%s,%s,%s, %s,%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s, %s, %s, %s )", MyStudyPlanTable.NAME_REPASOS, "\"" + uuid + "\"", id, "\"" + r1.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + r2.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(0).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(2).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(3).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(4).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(5).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(6).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(7).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(8).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(9).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(10).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(11).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(12).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(13).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(14).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(15).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(16).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"", "\"" + sucesivas.get(17).format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + "\"");
        mDatabase.execSQL(sqlInsert);
    }

    public void updateRepasos(Study s){
      Map<String, LocalDate> repasos = getRepasosAndColumns(s);
        for (Map.Entry <String,LocalDate> repaso:
             repasos.entrySet()) {
            if(repaso.getValue().isAfter(s.getFechaFin()) || repaso.getValue().isEqual(s.getFechaFin())){
                updateRepaso(getIdStudy(s), repaso.getKey(), s.getFechaFin().minusDays(1));
            }
        }
    }

    private void deleteRepasos(int idStudy){
        String table =MyStudyPlanTable.NAME_REPASOS;
        String whereClause = MyStudyPlanTable.Cols.ID_STUDY +" = ";
        String sql = String.format("delete from %s where %s %d", table, whereClause, idStudy);
        mDatabase.execSQL(sql);
    }

    private void deleteRepasosByAsignatura(ArrayList<Integer> idStudies){
        String table = MyStudyPlanTable.NAME_REPASOS;
        String whereClause = MyStudyPlanTable.Cols.ID_STUDY + " in (";
        StringBuilder sql = new StringBuilder();
        sql.append("delete from ");
        sql.append(table+ " ");
        sql.append("where "+whereClause);
        for (Integer id:
             idStudies) {
            sql.append(id + ",");
        }
        int count = sql.length();
        sql.replace(count-1, count, ")" );
        String sqlString = sql.toString();
        mDatabase.execSQL(sqlString);
    }

    private void updateRepaso(int idStudy,String columnName, LocalDate fechaNueva){
        String sql = "update repasos set "+ columnName+ " = '" +fechaNueva.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))+ "' where id_study =" +idStudy;
        mDatabase.execSQL(sql);

    }
    private LocalDate sigFecha(LocalDate date) {
        return date.plusDays(7);
    }

    private StudyCursorWrapper queryStudies(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(MyStudyPlanTable.NAME_STUDY, null, whereClause, whereArgs, null, null, MyStudyPlanTable.Cols.FECHA_INICIO);
        return new StudyCursorWrapper(cursor);
    }

    public List<Repaso> getAllFirstRepasos() {
        List<Repaso> firstRepasos = new ArrayList<>();
        List<Study> studies = getStudies();
        for (Study study :
                studies) {
            List<LocalDate> repasos = getRepasos(study);
            LocalDate firstRepaso = fecha_sig_repaso(repasos);
            Repaso repaso = new Repaso(study.getAsignatura(), study.getTema(), firstRepaso);
            firstRepasos.add(repaso);
        }

        return firstRepasos;
    }

    private Integer getIdStudy(Study s) {
        String sql = "select _id from " + MyStudyPlanTable.NAME_STUDY + " where uuid =?";
        String[] selectionArgs = {s.getId().toString()};
        Cursor cursor = mDatabase.rawQuery(sql, selectionArgs);
        Integer id = null;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    private ArrayList<Integer> getIdStudiesByAsignatura(String asignatura){
        ArrayList<Integer> id_studies= new ArrayList<>();
        String [] columnas = {"_id"};
        String whereClause= "asignatura = ?";
        String [] whereArgs= {asignatura};
        Cursor cursor = mDatabase.query(MyStudyPlanTable.NAME_STUDY, columnas, whereClause, whereArgs, null, null, null );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            id_studies.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return id_studies;
    }

}
