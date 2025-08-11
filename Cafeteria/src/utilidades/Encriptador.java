/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 /**
 * Utilidad para encriptar contraseñas usando SHA-256.
 * No se puede desencriptar (hash unidireccional), ideal para almacenar contraseñas.
 * @author tatia
 */
public class Encriptador {
    /**
     * Convierte una cadena de texto (como una contraseña) en su hash SHA-256.
     *
     * @param input Texto plano a encriptar.
     * @return Hash SHA-256 en formato hexadecimal.
     * @throws RuntimeException si el algoritmo SHA-256 no está disponible.
     */
    public static String hashSHA256(String input) {
        if (input == null || input.isEmpty()) {
            throw new IllegalArgumentException("El texto a encriptar no puede ser nulo o vacío.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convertir byte[] a String hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Este error es raro, porque SHA-256 siempre está disponible en JVM modernas
            throw new RuntimeException("Error crítico: Algoritmo SHA-256 no encontrado.", e);
        }
    }

    // Ejemplo de uso (opcional, para pruebas)
    public static void main(String[] args) {
        System.out.println(hashSHA256("1234")); // Útil para generar contraseñas de prueba
    }
}

