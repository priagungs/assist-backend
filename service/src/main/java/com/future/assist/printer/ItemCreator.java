package com.future.assist.printer;

import com.future.assist.model.entity_model.Item;
import com.future.assist.model.entity_model.UserHasItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public class ItemCreator {
    public final static Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    public final static Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    private final static String[] HEADER_ARRAY = {"ID Employee", "Name", "Total Qty"};

    public static void addMetaData(Document document, String title) {
        document.addTitle(title);
        document.addAuthor("Admin");
    }

    public static void addContent(Document document, Item item) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(NORMAL_FONT);
        createReportTable(paragraph, item);
        document.add(paragraph);
    }

    private static void createReportTable(Paragraph paragraph, Item item) throws BadElementException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        paragraph.add(new Paragraph("ID Item        = " + item.getIdItem().toString(), NORMAL_FONT));
        paragraph.add(new Paragraph("Item Name      = " + item.getItemName(), SMALL_BOLD));
        paragraph.add(new Paragraph("Available Qty  = " + item.getAvailableQty().toString(), NORMAL_FONT));
        paragraph.add(new Paragraph("Total Qty      = " + item.getTotalQty().toString(), NORMAL_FONT));
        paragraph.add(new Paragraph("Price          = " + item.getPrice().toString(), NORMAL_FONT));
        paragraph.add(new Paragraph("ID Item        = " + item.getDescription(), NORMAL_FONT));
        addEmptyLine(paragraph, 2);

        paragraph.add(new Paragraph("Employees who have this item = ", NORMAL_FONT));
        paragraph.add(new Paragraph(" "));
        addHeaderInTable(HEADER_ARRAY, table);

        for (UserHasItem owner : item.getOwners()) {
            addToTable(table, owner.getUser().getIdUser().toString());
            addToTable(table, owner.getUser().getName());
            addToTable(table, owner.getHasQty().toString());
        }

        paragraph.add(table);
    }

    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    public static void addHeaderInTable(String[] headerArray, PdfPTable table) {
        PdfPCell c1;

        for (String header : headerArray) {
            c1 = new PdfPCell(new Phrase(header, InvoiceCreator.SMALL_BOLD));
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(c1);
        }

        table.setHeaderRows(1);
    }

    public static void addToTable(PdfPTable table, String data) {
        table.addCell(new Phrase(data, InvoiceCreator.NORMAL_FONT));
    }

}


