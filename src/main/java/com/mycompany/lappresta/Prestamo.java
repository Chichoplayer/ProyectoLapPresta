package com.mycompany.lappresta;

public class Prestamo {
    private String id;
    private String codigoEquipo;
    private String cedulaUsuario;
    private String fechaPrestamo;
    private String fechaLimite;
    private String fechaDevolucion; // Vacía si aún no se devuelve

    public Prestamo(String id, String codigoEquipo, String cedulaUsuario, String fechaPrestamo, String fechaLimite, String fechaDevolucion) {
        this.id = id;
        this.codigoEquipo = codigoEquipo;
        this.cedulaUsuario = cedulaUsuario;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaLimite = fechaLimite;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getId() { return id; }
    public String getCodigoEquipo() { return codigoEquipo; }
    public String getCedulaUsuario() { return cedulaUsuario; }
    public String getFechaPrestamo() { return fechaPrestamo; }
    public String getFechaLimite() { return fechaLimite; }
    public String getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
}
