package com.fitapp.controller;

import com.fitapp.model.Session;
import com.fitapp.model.Statistic;
import com.fitapp.navigation.Navigator;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.YearMonth;

/**
 * StepCounter-Statistik.
 *
 * Man waehlt einen Monat. Das Balkendiagramm zeigt fuer jeden Tag
 * dieses Monats die erreichten Schritte des angemeldeten Benutzers.
 * Die Zahlen kommen aus steps.csv (ueber die Klasse Statistic).
 */
public class StatisticsController implements Controller {

    private Navigator navigator;

    @Override
    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public void changeView(String fxmlFile) {
        navigator.changeView(fxmlFile);
    }

    // --- Elemente aus dem FXML ---
    @FXML private Label userNameLabel;
    @FXML private ComboBox<String> monatBox;
    @FXML private BarChart<String, Number> chart;

    // Wird nach dem Laden des FXML automatisch aufgerufen.
    @FXML
    public void initialize() {

        // angemeldeten Benutzer anzeigen
        userNameLabel.setText("Angemeldet: " + Session.getUser());

        // Monatsauswahl: die letzten 12 Monate, aktueller Monat vorausgewaehlt
        YearMonth jetzt = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            monatBox.getItems().add(jetzt.minusMonths(i).toString()); // z. B. "2026-09"
        }
        monatBox.setValue(jetzt.toString());

        zeichneDiagramm();
    }

    // anderer Monat gewaehlt -> Diagramm neu zeichnen
    @FXML
    public void handleMonatWechsel() {
        zeichneDiagramm();
    }

    // Balkendiagramm fuellen: ein Balken pro Tag des gewaehlten Monats.
    private void zeichneDiagramm() {

        String monatText = monatBox.getValue();
        if (monatText == null) {
            return;
        }
        YearMonth monat = YearMonth.parse(monatText);

        // Statistic wertet steps.csv fuer diesen Benutzer und Monat aus
        Statistic s = new Statistic(
                Session.getUser(),
                monat.atDay(1),
                monat.atDay(monat.lengthOfMonth()));

        // eine Datenreihe: Tag -> erreichte Schritte
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Erreichte Schritte");

        for (int tagNr = 1; tagNr <= monat.lengthOfMonth(); tagNr++) {
            LocalDate tag = monat.atDay(tagNr);
            int schritte = s.schritteAmTag(tag);
            serie.getData().add(new XYChart.Data<>(String.valueOf(tagNr), schritte));
        }

        // altes Diagramm ersetzen
        chart.getData().clear();
        chart.getData().add(serie);
    }

    @FXML
    public void handleBackToMenu() {
        changeView("mainMenu.fxml");
    }
}
