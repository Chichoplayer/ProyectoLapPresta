package com.mycompany.lappresta;

public class Prestamo {

    private String id;
    private String codigoEquipo;
    private String cedulaUsuario;
    private String fechaPrestamo;
    private String fechaLimite;
    private String fechaDevolucion; // Vacía si aún no se devuelve

    public Prestamo(String id, String codigoEquipo, String cedulaUsuario,
                    String fechaPrestamo, String fechaLimite, String fechaDevolucion) {
        this.id = safe(id);
        this.codigoEquipo = safe(codigoEquipo);
        this.cedulaUsuario = safe(cedulaUsuario);
        this.fechaPrestamo = safe(fechaPrestamo);
        this.fechaLimite = safe(fechaLimite);
        this.fechaDevolucion = safe(fechaDevolucion);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = safe(id); }

    public String getCodigoEquipo() { return codigoEquipo; }
    public void setCodigoEquipo(String codigoEquipo) { this.codigoEquipo = safe(codigoEquipo); }

    public String getCedulaUsuario() { return cedulaUsuario; }
    public void setCedulaUsuario(String cedulaUsuario) { this.cedulaUsuario = safe(cedulaUsuario); }

    public String getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = safe(fechaPrestamo); }

    public String getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(String fechaLimite) { this.fechaLimite = safe(fechaLimite); }

    public String getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = safe(fechaDevolucion); }
}