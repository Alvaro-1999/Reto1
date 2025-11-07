package com.gymapp.util;

import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectionGestor {

    // Método que verifica si hay conexión a Internet
    public static boolean hayConexion() {
        try {
            URL url = new URL("https://www.google.com");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            conn.connect();
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

    // Alias para mayor claridad
    public static boolean isOnline() {
        return hayConexion();
    }
}
