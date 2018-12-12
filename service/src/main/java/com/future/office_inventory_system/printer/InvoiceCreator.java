package com.future.office_inventory_system.printer;

import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

/**
 
 * This is to create a PDF file.
 
 */

public class InvoiceCreator {
    
    private final static String[] HEADER_ARRAY = {"ID Item", "Item Name", "Price/item", "Bought Qty", "Subtotal"};
    
    public final static Font SMALL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.BOLD);
    
    public final static Font NORMAL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
    
    public static void addMetaData(Document document, String title) {
        
        document.addTitle(title);
        
        document.addAuthor("Admin");
        
    }
    
    public static void addContent(Document document, Transaction trx) throws DocumentException {
        
        Paragraph paragraph = new Paragraph();
        
        paragraph.setFont(NORMAL_FONT);
        
        createReportTable(paragraph, trx);
        
        document.add(paragraph);
        
    }
    
    private static void createReportTable(Paragraph paragraph, Transaction trx) throws BadElementException {
        
        PdfPTable table = new PdfPTable(5);
        
        table.setWidthPercentage(100);
        
        paragraph.add(new Chunk("Invoice For Transaction ID = " + trx.getIdTransaction().toString(), SMALL_BOLD));
        addEmptyLine(paragraph,1);
        paragraph.add(new Chunk("Transaction Date = " + trx.getTransactionDate().toString(), NORMAL_FONT));
        addEmptyLine(paragraph,1);
        paragraph.add(new Chunk("Supplier = " + trx.getSupplier().toString(), NORMAL_FONT));
        addEmptyLine(paragraph,1);
        paragraph.add(new Chunk("Admin in charge = " + trx.getAdmin().getName() + " (" + trx.getAdmin().getIdUser().toString() + ")", NORMAL_FONT));
        
        addEmptyLine(paragraph, 2);
        
        addHeaderInTable(HEADER_ARRAY, table);

        for(ItemTransaction itemtrx : trx.getItemTransactions()){

            addToTable(table, itemtrx.getItem().getIdItem().toString());

            addToTable(table, itemtrx.getItem().getItemName());

            addToTable(table, itemtrx.getBoughtQty().toString());

            addToTable(table, itemtrx.getPrice().toString());

            Long subtotal = itemtrx.getPrice() * itemtrx.getBoughtQty();

            addToTable(table, subtotal.toString());

        }

        paragraph.add(table);
        
    }
    
    /** Helper methods start here **/
    
    
    public static void addEmptyLine(Paragraph paragraph, int number) {
        
        for (int i = 0; i < number; i++) {
            
            paragraph.add(new Paragraph(" "));
            
        }
        
    }
    
    public static void addHeaderInTable(String[] headerArray, PdfPTable table){
        
        PdfPCell c1;
        
        for(String header : headerArray) {
            
            c1 = new PdfPCell(new Phrase(header, InvoiceCreator.SMALL_BOLD));
            
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            table.addCell(c1);
            
        }
        
        table.setHeaderRows(1);
        
    }
    
    public static void addToTable(PdfPTable table, String data){
        
        table.addCell(new Phrase(data, InvoiceCreator.NORMAL_FONT));
        
    }
    
    public static Paragraph getParagraph(){
        
        Paragraph paragraph = new Paragraph();
        
        paragraph.setFont(InvoiceCreator.NORMAL_FONT);
        
        addEmptyLine(paragraph, 1);
        
        return paragraph;
        
    }
    
}


