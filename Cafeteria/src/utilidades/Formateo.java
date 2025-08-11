/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utilidades;

import java.text.NumberFormat;
import java.util.Locale;
/**
 *
 * @author tatia
 */
public class Formateo {
    // Formatea un double a formato moneda local, ejemplo: ₡1,234.56 o $1,234.56
    public static String formatearMoneda(double monto) {
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(Locale.getDefault());
        return formatoMoneda.format(monto);
    }

    public static void main(String[] args) {
        double valor = 1234.56;
        System.out.println(formatearMoneda(valor));  // Salida: $1,234.56 o ₡1,234.56 según locale
    }
}
