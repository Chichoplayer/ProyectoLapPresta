package com.mycompany.lappresta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EquipoCSV {

    // Carpeta: ~/Documents/Datos/equipos.csv
    private static final String CARPETA =
            System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Datos";
    private static final String ARCHIVO = CARPETA + File.separator + "equipos.csv";
    private static final String CABECERA = "codigo,tipo,descripcion,estado";

    /** Crea la carpeta y el archivo con cabecera si no existen. */
    private static void asegurarArchivo() {
        try {
            File dir = new File(CARPETA);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File f = new File(ARCHIVO);
            if (!f.exists()) {
                try (BufferedWriter bw = new BufferedWriter(
                        new FileWriter(f, StandardCharsets.UTF_8))) {
                    bw.write(CABECERA);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error creando archivo CSV: " + e.getMessage());
        }
    }

    /** Devuelve true si ya existe un equipo con ese código. */
    public static boolean existeCodigo(String codigo) {
        asegurarArchivo();
        for (Equipo e : listarEquipos()) {
            if (e.getCodigo().equalsIgnoreCase(codigo)) {
                return true;
            }
        }
        return false;
    }

    /** Agrega un equipo al final del CSV. */
    public static boolean guardarEquipo(Equipo equipo) {
        asegurarArchivo();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ARCHIVO, StandardCharsets.UTF_8, true))) {
            bw.write(escapar(equipo.getCodigo()) + "," +
                     escapar(equipo.getTipo()) + "," +
                     escapar(equipo.getDescripcion()) + "," +
                     escapar(equipo.getEstado()));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando equipo: " + e.getMessage());
            return false;
        }
    }

    /** Lee todos los equipos del CSV (sin la cabecera). */
    public static List<Equipo> listarEquipos() {
        asegurarArchivo();
        List<Equipo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new FileReader(ARCHIVO, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; } // saltar cabecera
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 4) continue;
                lista.add(new Equipo(
                        desescapar(p[0]),
                        desescapar(p[1]),
                        desescapar(p[2]),
                        desescapar(p[3])));
            }
        } catch (IOException e) {
            System.err.println("Error leyendo CSV: " + e.getMessage());
        }
        return lista;
    }

    /** Total de equipos registrados. */
    public static int contarTotal() {
        return listarEquipos().size();
    }

    /** Total de equipos con estado "Disponible". */
    public static int contarDisponibles() {
        int n = 0;
        for (Equipo e : listarEquipos()) {
            if ("Disponible".equalsIgnoreCase(e.getEstado())) {
                n++;
            }
        }
        return n;
    }

    // --- Utilidades CSV: escapar/desescapar comas y comillas ---
    private static String escapar(String s) {
        if (s == null) s = "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }

    private static String desescapar(String s) {
        if (s == null) return "";
        if (s.startsWith("\"") && s.endsWith("\"") && s.length() >= 2) {
            return s.substring(1, s.length() - 1).replace("\"\"", "\"");
        }
        return s;
    }
}