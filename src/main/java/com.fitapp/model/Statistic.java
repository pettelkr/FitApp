package com.fitapp.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Wertet die Schritt-Daten eines Benutzers fuer einen Zeitraum aus.
 *
 * Die Daten kommen aus der Datei "steps.csv" (Klasse StepCsv).
 * Fuer jede Kennzahl wird die Liste einmal durchgegangen und
 * zusammengezaehlt.
 */
public class Statistic {

    // ===== BESTEHEND – nicht aendern (StatisticTest baut darauf) =====
    private String period;
    private int numOfExercises;

    public Statistic(String period, int numOfExercises) {
        this.period = period;
        this.numOfExercises = numOfExercises;
    }

    public String getPeriod() { return period; }
    public int getNumOfExercises() { return numOfExercises; }

    public void calcStatistic() {
        // to be implemented
    }

    public double getAverage() {
        // to be implemented
        return 0;
    }

    public String export() {
        // to be implemented
        return "Implement this method.";
    }

    // ===== NEU – Auswertung der Schritte aus steps.csv =====

    private String username;
    private LocalDate von;
    private LocalDate bis;

    // Die Datei wird EINMAL gelesen und hier gemerkt, damit die
    // einzelnen Methoden nicht jedes Mal neu auf die Platte zugreifen.
    private List<StepEntry> alleEintraege;

    /**
     * Konstruktor fuer die Statistik-Seite:
     * fuer welchen Benutzer und welchen Zeitraum ausgewertet wird.
     */
    public Statistic(String username, LocalDate von, LocalDate bis) {
        this.username = username;
        this.von = von;
        this.bis = bis;
        this.alleEintraege = StepCsv.readAll();
    }

    // true, wenn der Eintrag zu diesem Benutzer gehoert und im Zeitraum liegt
    private boolean passt(StepEntry e) {
        return e.username.equals(username)
                && !e.datum.isBefore(von)
                && !e.datum.isAfter(bis);
    }

    // Anzahl Tage im gewaehlten Zeitraum
    private long tageImZeitraum() {
        return ChronoUnit.DAYS.between(von, bis) + 1; // +1: Start- und Endtag zaehlen mit
    }

    // --- die Kennzahlen ---

    // Erreichte Schritte des Benutzers an EINEM bestimmten Tag.
    // Wird vom Balkendiagramm der StepCounter-Statistik genutzt.
    public int schritteAmTag(LocalDate tag) {
        int summe = 0;
        for (StepEntry e : alleEintraege) {
            if (e.username.equals(username) && e.datum.equals(tag)) {
                summe += e.schritte;
            }
        }
        return summe;
    }

    // Summe aller Schritte des Benutzers im Zeitraum.
    public int schritteImZeitraum() {
        int summe = 0;
        for (StepEntry e : alleEintraege) {
            if (passt(e)) {
                summe += e.schritte;
            }
        }
        return summe;
    }

    // Geplante Schritte = Tagesziel * Anzahl Tage im Zeitraum.
    public int geplanteSchritte() {
        return letztesZiel() * (int) tageImZeitraum();
    }

    // Ist minus Soll (negativ = weniger als geplant).
    public int schritteDifferenz() {
        return schritteImZeitraum() - geplanteSchritte();
    }

    // Grober Kalorienverbrauch: ca. 0,04 kcal pro Schritt (Durchschnittswert).
    public double verbrannteKalorien() {
        return schritteImZeitraum() * 0.04;
    }

    // Durchschnittliche Schritte pro Tag im Zeitraum.
    public double schritteProTag() {
        long tage = tageImZeitraum();
        if (tage <= 0) {
            return 0;
        }
        return schritteImZeitraum() / (double) tage;
    }

    // An wie vielen Tagen im Zeitraum wurde das Tagesziel erreicht?
    public int tageZielErreicht() {
        int erreicht = 0;

        // jeden Tag im Zeitraum durchgehen
        for (LocalDate tag = von; !tag.isAfter(bis); tag = tag.plusDays(1)) {

            int summeAmTag = 0;
            int zielAmTag = 0;

            for (StepEntry e : alleEintraege) {
                if (e.username.equals(username) && e.datum.equals(tag)) {
                    summeAmTag += e.schritte;
                    zielAmTag = e.ziel;
                }
            }

            if (zielAmTag > 0 && summeAmTag >= zielAmTag) {
                erreicht++;
            }
        }

        return erreicht;
    }

    // Das zuletzt gespeicherte Tagesziel des Benutzers.
    private int letztesZiel() {
        int ziel = 0;
        for (StepEntry e : alleEintraege) {
            if (e.username.equals(username)) {
                ziel = e.ziel;
            }
        }
        return ziel;
    }
}
