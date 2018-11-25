package com.future.office_inventory_system.printer;

import com.future.office_inventory_system.model.Transaction;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PrinterService {
    
    private static final String PATH = "../asset/";
    
    public static final String PDF_EXTENSION = ".pdf";
    
    public static void printInvoice(Transaction trx) {
        
        Document document = null;
        
        try {
            
            //Document is not auto-closable hence need to close it separately
            
            document = new Document(PageSize.A4);
    
            String title = "Invoice_" + trx.getIdTransaction().toString();
            
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(
                
                new File(title + PDF_EXTENSION)));
            
            HeaderFooter event = new HeaderFooter();
            
            event.setHeader("Transaction Invoice");
            
            writer.setPageEvent(event);
            
            document.open();
    
            document.add(new Chunk(""));
            
            PDFCreator.addMetaData(document, title);
            
            PDFCreator.addContent(document, trx);
            
        }catch (DocumentException | FileNotFoundException e) {
            
            e.printStackTrace();
            
            System.out.println("FileNotFoundException occurs.." + e.getMessage());
            
        }finally{
            
            if(null != document){
                
                document.close();
                
            }
            
        }
        
    }
    
}