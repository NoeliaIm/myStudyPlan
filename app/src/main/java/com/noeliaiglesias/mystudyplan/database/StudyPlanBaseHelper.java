package com.noeliaiglesias.mystudyplan.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.noeliaiglesias.mystudyplan.database.MyStudyPlanDbSchema.MyStudyPlanTable;

public class StudyPlanBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String DATABASE_NAME ="studyPlan.db";

    //TODO refactorizar querys
    public StudyPlanBaseHelper(@Nullable Context context) {
        super(context,DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ MyStudyPlanTable.NAME_STUDY + "("+
                "_id integer primary key  autoincrement , "+
                MyStudyPlanTable.Cols.UUID+","+
                MyStudyPlanTable.Cols.ASIGNATURA +", "
                + MyStudyPlanTable.Cols.TEMA+", "+ MyStudyPlanTable.Cols.FECHA_INICIO+" Date,"+
                MyStudyPlanTable.Cols.FECHA_FIN+ " Date, "+ MyStudyPlanTable.Cols.PROX_EXAM+" Date)");

        db.execSQL("create table "+ MyStudyPlanTable.NAME_REPASOS+ "("+  "_id integer primary key  autoincrement , "+
                MyStudyPlanTable.Cols.UUID+","+
                MyStudyPlanTable.Cols.ID_STUDY+" references "+ MyStudyPlanTable.NAME_STUDY+ "(_id),"+
                MyStudyPlanTable.Cols.R_1+" Date,"+
                MyStudyPlanTable.Cols.R_2+" Date,"+
                MyStudyPlanTable.Cols.R_3+" Date,"+
                MyStudyPlanTable.Cols.R_4+" Date,"+
                MyStudyPlanTable.Cols.R_5+" Date,"+
                MyStudyPlanTable.Cols.R_6+" Date,"+
                MyStudyPlanTable.Cols.R_7+" Date,"+
                MyStudyPlanTable.Cols.R_8+" Date,"+
                MyStudyPlanTable.Cols.R_9+" Date,"+
                MyStudyPlanTable.Cols.R_10+" Date,"+
                MyStudyPlanTable.Cols.R_11+" Date,"+
                MyStudyPlanTable.Cols.R_12+" Date,"+
                MyStudyPlanTable.Cols.R_13+" Date,"+
                MyStudyPlanTable.Cols.R_14+" Date,"+
                MyStudyPlanTable.Cols.R_15+" Date,"+
                MyStudyPlanTable.Cols.R_16+" Date,"+
                MyStudyPlanTable.Cols.R_17+" Date,"+
                MyStudyPlanTable.Cols.R_18+" Date,"+
                MyStudyPlanTable.Cols.R_19+" Date,"+
                MyStudyPlanTable.Cols.R_20+" Date)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
