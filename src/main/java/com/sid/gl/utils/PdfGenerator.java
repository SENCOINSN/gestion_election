package com.sid.gl.utils;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.sid.gl.elections.scrutins.ScrutinPVResultats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Slf4j
public final class PdfGenerator {

    public static byte[] generatePvResultats(List<ScrutinPVResultats> scrutinPVResultats, String nameElection) throws IOException {
        log.info("Generating PDF pv resultats");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        Paragraph title = new Paragraph("PV résultat du vote de l'election :"+ nameElection)
                .setFont(PdfFontFactory.createFont())
                .setFontSize(15)
                .setBold()
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.CENTER);

        document.add(title);

        String summary = """
                    Voici le résumé des résultats du scrutin de l'élection : " + nameElection + " :"
                    A l'issu du scrutin, nous avons le pv de résultat généré attestant les conditions de validité 
                    du scrutin et des résultats enregistrés pour chaque bulletin de candidature.
                        
                """;

        Paragraph description = new Paragraph(summary)
                .setFontSize(14)
                .setFontColor(ColorConstants.BLACK)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(description);

        Table table = new Table(UnitValue.createPercentArray(3));
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(new Cell().add(new Paragraph("nombre de voix")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Nom du candidat")).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        table.addHeaderCell(new Cell().add(new Paragraph("Email")).setBackgroundColor(ColorConstants.LIGHT_GRAY));

        scrutinPVResultats.forEach(pv->{
            table.addCell(new Cell().add(new Paragraph(String.valueOf(pv.getVoices()))));
            table.addCell(new Cell().add(new Paragraph(pv.getFullName())));
            table.addCell(new Cell().add(new Paragraph(pv.getEmail())));
        });

        document.add(table);
        document.close();

        return baos.toByteArray();

    }
}
