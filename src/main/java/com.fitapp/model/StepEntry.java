package com.fitapp.model;

import java.time.LocalDate;

/**
 * Ein einzelner Schritt-Eintrag aus dem Step Counter.
 *
 * Steht fuer eine Zeile in der Datei "steps.csv":
 * wer, an welchem Tag, wie viele Schritte, welches Tagesziel.
 */
public class StepEntry {

    public String username;
    public LocalDate datum;
    public int schritte;
    public int ziel;

    public StepEntry(String username, LocalDate datum, int schritte, int ziel) {
        this.username = username;
        this.datum = datum;
        this.schritte = schritte;
        this.ziel = ziel;
    }
}
