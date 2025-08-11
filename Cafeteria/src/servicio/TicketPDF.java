/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import dominio.DetalleVenta;
import dominio.Venta;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author tatia
 */
public class TicketPDF {
     public static void generarTicketPDF(Venta venta, String rutaArchivo) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        document.open();

        // Título
        Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulo = new Paragraph("Ticket de Venta", fontTitulo);
        titulo.setAlignment(Element.ALIGN_CENTER);
        document.add(titulo);

        document.add(new Paragraph(" "));
        
        // Datos generales
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        document.add(new Paragraph("ID Venta: " + venta.getId(), fontNormal));
        document.add(new Paragraph("Fecha: " + venta.getFechaHora().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), fontNormal));
        document.add(new Paragraph("Usuario: " + venta.getUsuario().getUsername(), fontNormal));

        document.add(new Paragraph(" "));

        // Tabla con detalle
        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new int[]{4, 1, 2, 2});
        
        // Cabecera
        tabla.addCell("Producto");
        tabla.addCell("Cant.");
        tabla.addCell("Precio Unit.");
        tabla.addCell("Total Línea");

        // Datos de los detalles
        for (DetalleVenta det : venta.getDetalles()) {
            tabla.addCell(det.getProducto().getNombreCompleto());
            tabla.addCell(String.valueOf(det.getCantidad()));
            tabla.addCell(String.format("%.2f", det.getPrecioUnit()));
            tabla.addCell(String.format("%.2f", det.getTotalLinea()));
        }

        document.add(tabla);

        document.add(new Paragraph(" "));

        // Totales
        document.add(new Paragraph(String.format("Subtotal: %.2f", venta.getSubtotal()), fontNormal));
        document.add(new Paragraph(String.format("IVA (7%%): %.2f", venta.getImpuestoIVA()), fontNormal));
        document.add(new Paragraph(String.format("IVI (13%%): %.2f", venta.getImpuestoIVI()), fontNormal));
        document.add(new Paragraph(String.format("Descuento: %.2f", venta.getDescuento()), fontNormal));
        document.add(new Paragraph(String.format("Total: %.2f", venta.getTotal()), fontNormal));

        document.close();
    }
}
