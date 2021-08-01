package com.noeliaiglesias.mystudyplan;

import java.time.LocalDate;
import java.util.UUID;

public class Study {

    private final UUID id;
    private String asignatura;
    private String tema;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private LocalDate proxExam;

    public Study() {
        this.id = UUID.randomUUID();
    }
    public Study(UUID id){
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getTema() {
        return tema;
    }

    public void setTema(String tema) {
        this.tema = tema;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public LocalDate getProxExam() {
        return proxExam;
    }

    public void setProxExam(LocalDate proxExam) {
        this.proxExam = proxExam;
    }
}
