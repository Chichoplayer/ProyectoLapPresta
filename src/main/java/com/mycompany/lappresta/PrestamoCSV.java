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

public class PrestamoCSV {

    private static final String CARPETA_RESPALDO =
            System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Datos";
    private static final String NOMBRE_ARCHIVO = "prestamos.csv";
    private static final String CABECERA = "id,codigoEquipo,cedulaUsuario,fechaPrestamo,fechaLimite,fechaDevolucion";

    private static File obtenerArchivo() {
        File local = new File(NOMBRE_ARCHIVO);
        if (local.exists()) {
            return local;
        }
        File dir = new File(CARPETA_RESPALDO);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return new File(dir, NOMBRE_ARCHIVO);
    }

    private static void asegurarArchivo() {
        try {
            File f = obtenerArchivo();
            File parent = f.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!f.exists()) {
                try (BufferedWriter bw = new BufferedWriter(
                        new FileWriter(f, StandardCharsets.UTF_8))) {
                    bw.write(CABECERA);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error creando prestamos.csv: " + e.getMessage());
        }
    }

    public static boolean guardarPrestamo(Prestamo p) {
        asegurarArchivo();
        File f = obtenerArchivo();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(f, StandardCharsets.UTF_8, true))) {
            bw.write(formatearLinea(p));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando préstamo: " + e.getMessage());
            return false;
        }
    }

    public static List<Prestamo> listarPrestamos() {
        asegurarArchivo();
        File f = obtenerArchivo();
        List<Prestamo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new FileReader(f, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) {
                    primera = false;
                    continue;
                }
                if (linea.trim().isEmpty()) {
                    continue;
                }
                String[] p = linea.split(",", -1);
                if (p.length < 6) {
                    continue;
                }
                lista.add(new Prestamo(
                        desescapar(p[0]),
                        desescapar(p[1]),
                        desescapar(p[2]),
                        desescapar(p[3]),
                        desescapar(p[4]),
                        desescapar(p[5])
                ));
            }
        } catch (IOException e) {
            System.err.println("Error leyendo prestamos.csv: " + e.getMessage());
        }
        return lista;
    }

    public static boolean actualizarLista(List<Prestamo> lista) {
        asegurarArchivo();
        File f = obtenerArchivo();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(f, StandardCharsets.UTF_8, false))) {
            bw.write(CABECERA);
            bw.newLine();
            for (Prestamo p : lista) {
                bw.write(formatearLinea(p));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error actualizando préstamos: " + e.getMessage());
            return false;
        }
    }

    private static String formatearLinea(Prestamo p) {
        return escapar(p.getId()) + ","
                + escapar(p.getCodigoEquipo()) + ","
                + escapar(p.getCedulaUsuario()) + ","
                + escapar(p.getFechaPrestamo()) + ","
                + escapar(p.getFechaLimite()) + ","
                + escapar(p.getFechaDevolucion());
    }

    private static String escapar(String s) {
        if (s == null) return "";
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