package com.fitapp.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Speichert Schritt-Eintraege in einer einfachen Textdatei "steps.csv"
 * und liest sie wieder ein.
 *
 * Aufbau einer Zeile (durch Kommas getrennt):
 *   username,datum,schritte,ziel
 * Beispiel:
 *   Rene,2026-09-01,3500,10000
 *
 * Die Datei liegt bei "users.csv" unter src/main/resources/data/.
 * Hinweis: Das funktioniert, solange die App aus dem Projektordner
 * gestartet wird (mvn javafx:run / IntelliJ). Aus einer fertigen .jar
 * heraus waere der Ordner schreibgeschuetzt.
 */
public class StepCsv {

    // Datei im Projekt, neben users.csv.
    private static final File DATEI =
            new File("src/main/resources/data/steps.csv");

    // Laeuft EINMAL, wenn die Klasse zuerst benutzt wird:
    // legt Beispieldaten an, falls es die Datei noch nicht gibt.
    static {
        if (!DATEI.exists()) {
            seedDemo();
        }
    }

    // --- Schreiben ---

    // Haengt einen Eintrag ans Ende der Datei an.
    public static void append(StepEntry e) {
        // Das zweite Argument "true" heisst: anhaengen statt ueberschreiben.
        try (BufferedWriter w = new BufferedWriter(new FileWriter(DATEI, true))) {
            w.write(e.username + "," + e.datum + "," + e.schritte + "," + e.ziel);
            w.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // Speichert den Eintrag fuer Benutzer + Datum und UEBERSCHREIBT dabei
    // einen vorhandenen Eintrag desselben Tages.
    public static void saveOrReplace(StepEntry neu) {
        List<StepEntry> alle = readAll();
        // alten Eintrag fuer denselben Benutzer am selben Tag entfernen
        alle.removeIf(e -> e.username.equals(neu.username)
                && e.datum.equals(neu.datum));
        alle.add(neu);
        schreibeAlle(alle);
    }

    // Loescht den Eintrag fuer Benutzer + Datum (Reset fuer einen Tag).
    public static void delete(String username, LocalDate datum) {
        List<StepEntry> alle = readAll();
        alle.removeIf(e -> e.username.equals(username) && e.datum.equals(datum));
        schreibeAlle(alle);
    }

    // Erreichte Schritte fuer Benutzer + Datum (0, wenn nichts gespeichert ist).
    public static int schritteFuer(String username, LocalDate datum) {
        int summe = 0;
        for (StepEntry e : readAll()) {
            if (e.username.equals(username) && e.datum.equals(datum)) {
                summe += e.schritte;
            }
        }
        return summe;
    }

    // Schreibt die komplette Liste neu in die Datei (ueberschreibt alles).
    // Nach Datum sortiert, damit die Datei ordentlich bleibt.
    private static void schreibeAlle(List<StepEntry> liste) {
        liste.sort((a, b) -> a.datum.compareTo(b.datum));
        try (BufferedWriter w = new BufferedWriter(new FileWriter(DATEI, false))) {
            for (StepEntry e : liste) {
                w.write(e.username + "," + e.datum + "," + e.schritte + "," + e.ziel);
                w.newLine();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // --- Lesen ---

    // Liest alle Eintraege aus der Datei in eine Liste.
    public static List<StepEntry> readAll() {
        List<StepEntry> liste = new ArrayList<>();

        if (!DATEI.exists()) {
            return liste; // noch keine Datei -> leere Liste
        }

        try (BufferedReader r = new BufferedReader(new FileReader(DATEI))) {
            String zeile;
            while ((zeile = r.readLine()) != null) {

                if (zeile.isBlank()) {
                    continue; // Leerzeile ueberspringen
                }

                // Zeile an den Kommas zerlegen: [username, datum, schritte, ziel]
                String[] teile = zeile.split(",");
                if (teile.length < 4) {
                    continue; // kaputte Zeile ueberspringen
                }

                liste.add(new StepEntry(
                        teile[0].trim(),
                        LocalDate.parse(teile[1].trim()),
                        Integer.parseInt(teile[2].trim()),
                        Integer.parseInt(teile[3].trim())));
            }
        } catch (IOException | RuntimeException ex) {
            // RuntimeException faengt z. B. eine falsch geschriebene Zahl ab
            ex.printStackTrace();
        }

        return liste;
    }

    // Alle verschiedenen Benutzernamen, die in der Datei vorkommen.
    public static List<String> alleBenutzer() {
        Set<String> namen = new LinkedHashSet<>(); // ohne Doppelte, in Reihenfolge
        for (StepEntry e : readAll()) {
            namen.add(e.username);
        }
        return new ArrayList<>(namen);
    }

    // --- Beispieldaten (nur beim ersten Start) ---
    // 14 Tage fuer drei Benutzer, damit man sofort etwas sieht.
    private static void seedDemo() {
        String[] benutzer = {"Rene", "Hasan", "John"};
        int[] ziele = {10000, 12000, 8000};

        for (int b = 0; b < benutzer.length; b++) {
            for (int i = 0; i < 14; i++) {
                LocalDate tag = LocalDate.now().minusDays(i);
                int schritte = 5000 + (i % 6) * 1200 + b * 500;
                append(new StepEntry(benutzer[b], tag, schritte, ziele[b]));
            }
        }
    }
}
