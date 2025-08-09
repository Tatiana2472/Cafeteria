package dominio;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un registro de log para persistir en la tabla logs.
 */
public class LogEntry {
    private int id;
    private LocalDateTime fechaHora;
    private String nivel;      // INFO, ERROR, WARN, DEBUG...
    private String evento;     // identificador breve del evento
    private String detalle;    // mensaje amigable o resumen
    private String stacktrace; // stacktrace completo (opcional)

    public LogEntry() {
        this.fechaHora = LocalDateTime.now();
    }

    public LogEntry(int id, String nivel, String evento, String detalle, String stacktrace) {
        this();
        this.id = id;
        this.nivel = nivel;
        this.evento = evento;
        this.detalle = detalle;
        this.stacktrace = stacktrace;
    }

    public static LogEntry of(String nivel, String evento, String detalle) {
        return new LogEntry(0, nivel, evento, detalle, null);
    }

    public static LogEntry of(String nivel, String evento, String detalle, String stacktrace) {
        return new LogEntry(0, nivel, evento, detalle, stacktrace);
    }

    // getters / setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getNivel() { return nivel; }
    public void setNivel(String nivel) { this.nivel = nivel; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getDetalle() { return detalle; }
    public void setDetalle(String detalle) { this.detalle = detalle; }

    public String getStacktrace() { return stacktrace; }
    public void setStacktrace(String stacktrace) { this.stacktrace = stacktrace; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogEntry)) return false;
        LogEntry logEntry = (LogEntry) o;
        return id != 0 && id == logEntry.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "LogEntry{id=" + id + ", fechaHora=" + fechaHora + ", nivel='" + nivel + '\'' +
                ", evento='" + evento + '\'' + ", detalle='" + detalle + '\'' + '}';
    }
}
