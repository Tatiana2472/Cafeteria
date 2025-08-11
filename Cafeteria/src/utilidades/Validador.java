/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 /**
 * Clase de utilidad para validar entradas del usuario en formularios y diálogos.
 * Usado principalmente en la capa de UI para evitar errores antes de enviar datos al servicio.
 *
 * @author tatia
 */
public class Validador {

    private static final String EMAIL_PATTERN =
            "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    /**
     * Valida que un campo de texto no esté vacío o sea solo espacios.
     *
     * @param input Cadena a validar.
     * @param fieldName Nombre del campo (para mensajes de error).
     * @return true si es válido, false si está vacío.
     */
    public static boolean esTextoValido(String input, String fieldName) {
        if (input == null || input.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "El campo '" + fieldName + "' no puede estar vacío.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida que un número sea un double válido y mayor a cero.
     *
     * @param input Texto que debería ser un número (ej: "15.50").
     * @param fieldName Nombre del campo (ej: "Precio").
     * @return true si es un número válido y > 0.
     */
    public static boolean esNumeroValido(String input, String fieldName) {
        if (!esTextoValido(input, fieldName)) return false;

        try {
            double valor = Double.parseDouble(input.trim());
            if (valor <= 0) {
                JOptionPane.showMessageDialog(null,
                        "El campo '" + fieldName + "' debe ser mayor a 0.",
                        "Error de validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El campo '" + fieldName + "' debe ser un número válido.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Valida que un número entero sea válido y mayor a cero.
     *
     * @param input Texto que debería ser un entero.
     * @param fieldName Nombre del campo (ej: "Cantidad").
     * @return true si es un entero válido y > 0.
     */
    public static boolean esEnteroValido(String input, String fieldName) {
        if (!esTextoValido(input, fieldName)) return false;

        try {
            int valor = Integer.parseInt(input.trim());
            if (valor <= 0) {
                JOptionPane.showMessageDialog(null,
                        "El campo '" + fieldName + "' debe ser mayor a 0.",
                        "Error de validación", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "El campo '" + fieldName + "' debe ser un número entero válido.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Valida que un correo electrónico tenga formato correcto.
     *
     * @param email Email a validar.
     * @return true si tiene formato válido.
     */
    public static boolean esEmailValido(String email) {
        if (!esTextoValido(email, "Correo electrónico")) return false;

        if (!pattern.matcher(email.trim()).matches()) {
            JOptionPane.showMessageDialog(null,
                    "El formato del correo electrónico no es válido.",
                    "Error de formato", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida que una contraseña tenga al menos 4 caracteres.
     *
     * @param password Contraseña ingresada.
     * @return true si es válida.
     */
    public static boolean esPasswordValida(String password) {
        if (password == null || password.length() < 4) {
            JOptionPane.showMessageDialog(null,
                    "La contraseña debe tener al menos 4 caracteres.",
                    "Error de seguridad", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Valida que un nombre (producto, usuario, etc.) tenga entre 2 y 50 caracteres.
     *
     * @param nombre Nombre a validar.
     * @param campo Campo que se está validando.
     * @return true si es válido.
     */
    public static boolean esNombreValido(String nombre, String campo) {
        if (!esTextoValido(nombre, campo)) return false;

        nombre = nombre.trim();
        if (nombre.length() < 2) {
            JOptionPane.showMessageDialog(null,
                    "El '" + campo + "' debe tener al menos 2 caracteres.",
                    "Error de validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (nombre.length() > 50) {
            JOptionPane.showMessageDialog(null,
                    "El '" + campo + "' no puede exceder los 50 caracteres.",
                    "Error de longitud", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}
