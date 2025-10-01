package com.aurionpro.util;

import com.aurionpro.entity.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PdfGenerator {

    public static byte[] generatePassbookPdf(String accountNumber, List<Transaction> transactions) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph header = new Paragraph("Passbook for Account: " + accountNumber, headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        document.add(new Paragraph("\n"));

        PdfPTable table = new PdfPTable(4);
        table.addCell("Transaction ID");
        table.addCell("Type");
        table.addCell("Amount");
        table.addCell("Date");

        for (Transaction tx : transactions) {
            table.addCell(String.valueOf(tx.getTransId()));
            table.addCell(tx.getTransType());
            table.addCell(tx.getAmount().toString());
            table.addCell(tx.getDate().toString());
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }
}
