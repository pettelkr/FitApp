package com.fitapp.controller;

import com.fitapp.model.Session;
import com.fitapp.model.StepCsv;
import com.fitapp.model.StepEntry;
import com.fitapp.navigation.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Step-Counter-Seite mit drei Bereichen:
 *
 *  LINKS  - Schrittzahlrechner: aus Gewicht und Zeitraum (Start-/Enddatum)
 *           das noetige taegliche Schrittziel (Soll) berechnen.
 *  MITTE  - Schritte eintragen: fuer einen Tag die erreichten
 *           Schritte (Ist) speichern.
 *  RECHTS - Uebersicht der Schritte: ein ganzer Monat, Soll gegen Ist.
 *           Der Monat ist auswaehlbar.
 */
public class StepCounterController implements Controller {

    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }

    // Zuletzt berechnetes Schrittziel (Soll). 0 = noch nicht berechnet.
    private long sollSchritte = 0;

    // -------------------------
    // FXML - Schrittzahlrechner (links)
    // -------------------------
    @FXML private Label userNameLabel;          // oben links: angemeldeter Benutzer
    @FXML private TextField currentWeightField;
    @FXML private TextField targetWeightField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Label tageLabel;              // Anzahl der Tage im Zeitraum
    @FXML private Label calcResultLabel;        // noetige Schritte pro Tag

    // -------------------------
    // FXML - Schritte eintragen (mitte)
    // -------------------------
    @FXML private DatePicker datumPicker;
    @FXML private Label sollLabel;
    @FXML private TextField erreichteSchritteField;
    @FXML private Label saveInfoLabel;

    // -------------------------
    // FXML - Uebersicht (rechts)
    // -------------------------
    @FXML private Label uebersichtBenutzerLabel;
    @FXML private ComboBox<String> monatBox;
    @FXML private VBox uebersichtBox;
    @FXML private Label uebersichtSummeLabel;

    // Wird nach dem Laden des FXML automatisch aufgerufen.
    @FXML
    public void initialize() {
        userNameLabel.setText("Angemeldet: " + Session.getUser());
        uebersichtBenutzerLabel.setText("Benutzer: " + Session.getUser());

        // Datumsfelder vorbelegen
        startDatePicker.setValue(LocalDate.now());
        datumPicker.setValue(LocalDate.now());

        sollLabel.setText("noch nicht berechnet");
        tageLabel.setText("0 Tage");

        // Monatsauswahl: die letzten 12 Monate, aktueller Monat vorausgewaehlt
        YearMonth jetzt = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            monatBox.getItems().add(jetzt.minusMonths(i).toString());
        }
        monatBox.setValue(jetzt.toString());

        zeigeTag();
        aktualisiereUebersicht();
    }

    // Datum in der Mitte gewechselt -> gespeicherte Schritte fuer den Tag ins Feld.
    @FXML
    public void handleTagWechsel() {
        zeigeTag();
    }

    // Einen Tag weiterspringen.
    @FXML
    public void handleNextDay() {
        LocalDate aktuell = datumPicker.getValue();
        if (aktuell == null) {
            aktuell = LocalDate.now();
        }
        datumPicker.setValue(aktuell.plusDays(1));
        zeigeTag();
    }

    // Holt die gespeicherten Schritte fuer das gewaehlte Datum ins Feld
    // (leer, wenn fuer den Tag noch nichts gespeichert ist).
    private void zeigeTag() {
        LocalDate datum = datumPicker.getValue();
        if (datum == null) {
            return;
        }
        int gespeichert = StepCsv.schritteFuer(Session.getUser(), datum);
        if (gespeichert > 0) {
            erreichteSchritteField.setText(String.valueOf(gespeichert));
        } else {
            erreichteSchritteField.clear();
        }
    }

    // Start- oder Enddatum geaendert -> Anzahl der Tage neu anzeigen.
    @FXML
    public void handleDatumWechsel() {
        LocalDate start = startDatePicker.getValue();
        LocalDate ende = endDatePicker.getValue();
        if (start != null && ende != null) {
            long tage = ChronoUnit.DAYS.between(start, ende);
            tageLabel.setText(tage + " Tage");
        }
    }

    // =========================================================
    // LINKS: Schrittziel berechnen
    // =========================================================
    @FXML
    public void handleCalcSteps() {
        try {
            double aktuell = Double.parseDouble(currentWeightField.getText());
            double wunsch = Double.parseDouble(targetWeightField.getText());

            LocalDate start = startDatePicker.getValue();
            LocalDate ende = endDatePicker.getValue();
            if (start == null || ende == null) {
                calcResultLabel.setText("Bitte Start- und Enddatum waehlen.");
                return;
            }

            long tage = ChronoUnit.DAYS.between(start, ende);
            tageLabel.setText(tage + " Tage");

            double abzunehmen = aktuell - wunsch; // in kg

            if (abzunehmen <= 0 || tage <= 0) {
                calcResultLabel.setText(
                        "Bitte Wunschgewicht kleiner als aktuelles Gewicht "
                        + "und ein Enddatum nach dem Startdatum waehlen.");
                return;
            }

            // 1 kg Koerperfett entspricht ungefaehr 7000 kcal.
            double gesamtDefizit = abzunehmen * 7000.0;

            // wie viel kcal muessen pro Tag zusaetzlich verbrannt werden
            double defizitProTag = gesamtDefizit / tage;

            // Orientierung: pro 10.000 Schritte verbrennt man etwa
            // (Koerpergewicht in kg) * 5,8 kcal.
            //   60 kg -> ca. 348 kcal ; 80 kg -> ca. 464 ; 100 kg -> ca. 580
            double kcalProSchritt = aktuell * 5.8 / 10000.0;

            sollSchritte = Math.round(defizitProTag / kcalProSchritt);

            calcResultLabel.setText("ca. " + sollSchritte + " Schritte pro Tag");
            sollLabel.setText(sollSchritte + " Schritte");
            aktualisiereUebersicht();

        } catch (NumberFormatException e) {
            calcResultLabel.setText("Bitte fuer Gewicht Zahlen eingeben.");
        }
    }

    // =========================================================
    // MITTE: erreichte Schritte fuer einen Tag speichern
    // =========================================================
    @FXML
    public void handleSaveSteps() {
        try {
            int erreicht = Integer.parseInt(erreichteSchritteField.getText());

            if (erreicht < 0) {
                saveInfoLabel.setText("Schritte duerfen nicht negativ sein.");
                return;
            }

            LocalDate datum = datumPicker.getValue();
            if (datum == null) {
                saveInfoLabel.setText("Bitte ein Datum waehlen.");
                return;
            }

            // In steps.csv speichern und einen vorhandenen Eintrag
            // fuer denselben Tag UEBERSCHREIBEN.
            StepCsv.saveOrReplace(new StepEntry(
                    Session.getUser(),
                    datum,
                    erreicht,
                    (int) sollSchritte));

            saveInfoLabel.setText("Gespeichert: " + erreicht
                    + " Schritte am " + datum + ".");

            aktualisiereUebersicht();

        } catch (NumberFormatException e) {
            saveInfoLabel.setText("Bitte eine Zahl eingeben.");
        }
    }

    // Reset: den gespeicherten Eintrag fuer das gewaehlte Datum loeschen.
    @FXML
    public void handleResetTag() {
        LocalDate datum = datumPicker.getValue();
        if (datum == null) {
            saveInfoLabel.setText("Bitte ein Datum waehlen.");
            return;
        }
        StepCsv.delete(Session.getUser(), datum);
        erreichteSchritteField.clear();
        saveInfoLabel.setText("Eintrag fuer " + datum + " zurueckgesetzt.");
        aktualisiereUebersicht();
    }

    // anderer Monat gewaehlt -> Uebersicht neu aufbauen
    @FXML
    public void handleMonatWechsel() {
        aktualisiereUebersicht();
    }

    // =========================================================
    // RECHTS: Uebersicht des gewaehlten Monats (Soll / Ist)
    // =========================================================
    private void aktualisiereUebersicht() {
        String user = Session.getUser();
        List<StepEntry> alle = StepCsv.readAll();

        uebersichtBox.getChildren().clear();

        String monatText = monatBox.getValue();
        if (monatText == null) {
            return;
        }
        YearMonth monat = YearMonth.parse(monatText);

        long summeSoll = 0;
        long summeIst = 0;

        // jeden Tag des Monats durchgehen
        for (int tagNr = 1; tagNr <= monat.lengthOfMonth(); tagNr++) {
            LocalDate tag = monat.atDay(tagNr);

            int ist = 0;
            int gespeichertesSoll = 0;

            for (StepEntry e : alle) {
                if (e.username.equals(user) && e.datum.equals(tag)) {
                    ist += e.schritte;
                    gespeichertesSoll = e.ziel;
                }
            }

            // Soll aus der aktuellen Berechnung; sonst das gespeicherte Soll.
            long soll = (sollSchritte > 0) ? sollSchritte : gespeichertesSoll;

            summeSoll += soll;
            summeIst += ist;

            String haken = (soll > 0 && ist >= soll) ? "   OK" : "";
            Label zeile = new Label(
                    tag + "    Soll: " + soll + "    Ist: " + ist + haken);
            uebersichtBox.getChildren().add(zeile);
        }

        uebersichtSummeLabel.setText("Monat gesamt  ->  Soll: " + summeSoll
                + "    Ist: " + summeIst);
    }

    @FXML
    public void handleBackToMenu(ActionEvent event) {
        changeView("mainMenu.fxml");
    }
}
