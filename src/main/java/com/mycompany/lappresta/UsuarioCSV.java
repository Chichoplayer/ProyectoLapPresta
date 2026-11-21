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

public class UsuarioCSV {

    private static final String CARPETA =
            System.getProperty("user.home") + File.separator + "Documents" + File.separator + "Datos";
    private static final String ARCHIVO = CARPETA + File.separator + "usuarios.csv";
    private static final String CABECERA = "cedula,nombre,tipo,telefono";

    private static void asegurarArchivo() {
        try {
            File dir = new File(CARPETA);
            if (!dir.exists()) dir.mkdirs();
            File f = new File(ARCHIVO);
            if (!f.exists()) {
                try (BufferedWriter bw = new BufferedWriter(
                        new FileWriter(f, StandardCharsets.UTF_8))) {
                    bw.write(CABECERA);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error creando usuarios.csv: " + e.getMessage());
        }
    }

    /** Devuelve true si ya existe un usuario con esa cédula. */
    public static boolean existeCedula(String cedula) {
        asegurarArchivo();
        for (Usuario u : listarUsuarios()) {
            if (u.getCedula().equals(cedula)) {
                return true;
            }
        }
        return false;
    }

    /** Agrega un usuario al final del CSV. */
    public static boolean guardarUsuario(Usuario usuario) {
        asegurarArchivo();
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(ARCHIVO, StandardCharsets.UTF_8, true))) {
            bw.write(escapar(usuario.getCedula()) + "," +
                     escapar(usuario.getNombre()) + "," +
                     escapar(usuario.getTipo()) + "," +
                     escapar(usuario.getTelefono()));
            bw.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando usuario: " + e.getMessage());
            return false;
        }
    }

    public static List<Usuario> listarUsuarios() {
        asegurarArchivo();
        List<Usuario> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new FileReader(ARCHIVO, StandardCharsets.UTF_8))) {
            String linea;
            boolean primera = true;
            while ((linea = br.readLine()) != null) {
                if (primera) { primera = false; continue; }
                if (linea.trim().isEmpty()) continue;
                String[] p = linea.split(",", -1);
                if (p.length < 4) continue;
                lista.add(new Usuario(
                        desescapar(p[0]),
                        desescapar(p[1]),
                        desescapar(p[2]),
                        desescapar(p[3])));
            }
        } catch (IOException e) {
            System.err.println("Error leyendo usuarios.csv: " + e.getMessage());
        }
        return lista;
    }

    public static int contarTotal() {
        return listarUsuarios().size();
    }

    public static int contarDocentes() {
        int n = 0;
        for (Usuario u : listarUsuarios()) {
            if ("Docente".equalsIgnoreCase(u.getTipo())) n++;
        }
        return n;
    }

    public static int contarEstudiantes() {
        int n = 0;
        for (Usuario u : listarUsuarios()) {
            if ("Estudiante".equalsIgnoreCase(u.getTipo())) n++;
        }
        return n;
    }

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
    /** Busca usuarios cuya cédula o nombre coincida parcialmente con el texto ingresado. */
    public static List<Usuario> buscarUsuarios(String criterio) {
        List<Usuario> todos = listarUsuarios();
        if (criterio == null || criterio.trim().isEmpty()) {
            return todos;
        }

        String texto = criterio.trim().toLowerCase();
        List<Usuario> filtrados = new ArrayList<>();

        for (Usuario u : todos) {
            boolean coincideCedula = u.getCedula() != null && u.getCedula().toLowerCase().contains(texto);
            boolean coincideNombre = u.getNombre() != null && u.getNombre().toLowerCase().contains(texto);

            if (coincideCedula || coincideNombre) {
                filtrados.add(u);
            }
        }
        return filtrados;
    }
}