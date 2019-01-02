package com.future.assist.printer;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

class HeaderFooter extends PdfPageEventHelper {

    private String header;
    private PdfTemplate total;

    public void setHeader(String header) {

        this.header = header;

    }

    public void onOpenDocument(PdfWriter writer, Document document) {

        total = writer.getDirectContent().createTemplate(25, 16);

    }

    public void onEndPage(PdfWriter writer, Document document) {

        PdfPTable table = new PdfPTable(2);

        try {

            table.setWidths(new int[]{200, 30});

            table.setLockedWidth(true);

            table.getDefaultCell().setBorder(Rectangle.SUBJECT);

            table.addCell(header);

            table.addCell(String.format("Page %d ", writer.getPageNumber()));

            Rectangle page = document.getPageSize();

            table.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

            table.writeSelectedRows(0, -1, document.leftMargin(), page.getHeight() - document.topMargin()

                    + table.getTotalHeight() + 5, writer.getDirectContent());

        } catch (DocumentException de) {

            throw new ExceptionConverter(de);

        }

    }

    public void onCloseDocument(PdfWriter writer, Document document) {

        ColumnText.showTextAligned(total, Element.ALIGN_LEFT,

                new Phrase(String.valueOf(writer.getPageNumber() - 1)), 2, 2, 0);

    }

}