package com.noeliaiglesias.mystudyplan.placeholder;


import java.time.LocalDate;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <
 */
public class Repaso {

   private String  asignatura;
   private String tema;
   private LocalDate fechaSiguienteRepaso;

   public Repaso() {
   }

   public Repaso(String asignatura, String tema, LocalDate fechaSiguienteRepaso) {
      this.asignatura = asignatura;
      this.tema = tema;
      this.fechaSiguienteRepaso = fechaSiguienteRepaso;
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

   public LocalDate getFechaSiguienteRepaso() {
      return fechaSiguienteRepaso;
   }

   public void setFechaSiguienteRepaso(LocalDate fechaSiguienteRepaso) {
      this.fechaSiguienteRepaso = fechaSiguienteRepaso;
   }
}