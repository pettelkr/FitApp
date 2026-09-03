package com.fitapp.model;

/**
 * Merkt sich, welcher Benutzer gerade angemeldet ist.
 *
 * "static" heisst: diese Angabe gibt es EINMAL im ganzen Programm.
 * So kann jede Seite fragen "wer ist eingeloggt?", ohne den Namen
 * herumreichen zu muessen.
 */
public class Session {

    // Standardwert, falls sich noch niemand angemeldet hat.
    private static String currentUser = "Gast";

    // Beim Login setzen.
    public static void setUser(String name) {
        currentUser = name;
    }

    // Ueberall abfragbar.
    public static String getUser() {
        return currentUser;
    }
}
