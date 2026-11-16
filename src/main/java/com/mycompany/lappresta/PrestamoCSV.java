package com.mycompany.lappresta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PrestamoCSV {
    private static final String CARPETA = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Datos";
    private static final String ARCHIVO = CARPETA + File.separator + "prestamos.csv";
    private static final String CABECERA = "id,codigoEquipo,cedulaUsuario,fechaPrestamo,fechaLimite,fechaDevolucion";

    private static void asegurarArchivo() {
        try {
            File dir = new File(CARPETA);
            if (!dir.exists()) dir.mkdirs();
            File f = new File(ARCHIVO);
            if (!f.exists()) {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8))) {
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
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, StandardCharsets.UTF_8, true))) {
            bw.write(p.getId() + "," + p.getCodigoEquipo() + "," + p.getCedulaUsuario() + "," +
                     p.getFechaPrestamo() + "," + p.getFechaLimite() + "," + p.getFechaDevolucion());
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando préstamo: " + e.getMessage());
            return false;
        }
    }

    public static List<Prestamo> listarPrestamos() {
        asegurarArchivo();
        List<Prestamo> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 6) continue;
                lista.add(new Prestamo(p[0], p[1], p[2], p[3], p[4], p[5]));
            }
        } catch (IOException e) {
            System.err.println("Error leyendo prestamos.csv: " + e.getMessage());
        }
        return lista;
    }

    public static boolean actualizarLista(List<Prestamo> lista) {
        asegurarArchivo();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, StandardCharsets.UTF_8, false))) {
            bw.write(CABECERA);
            bw.newLine();
            for (Prestamo p : lista) {
                bw.write(p.getId() + "," + p.getCodigoEquipo() + "," + p.getCedulaUsuario() + "," +
                         p.getFechaPrestamo() + "," + p.getFechaLimite() + "," + p.getFechaDevolucion());
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error actualizando préstamos: " + e.getMessage());
            return false;
        }
    }
}
