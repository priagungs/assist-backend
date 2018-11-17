package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.model.ItemTransaction;
import com.future.office_inventory_system.model.Transaction;
import com.future.office_inventory_system.service.TransactionService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicReference;

import static com.sun.tools.internal.xjc.reader.Ring.add;


@RestController
@RequestMapping("/download")
public class Printer {
    @Autowired
    TransactionService transactionService;
    
    @RequestMapping("/pdf/{idTransaction}")
    public void printInvoice(HttpServletResponse response,
                             @PathVariable("idTransaction") Long idTransaction) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
        
        Transaction transactions = transactionService.readTransactionByIdTransaction(idTransaction);
        
        Document document = new Document(PageSize.A4.rotate(), 36, 36, 54, 36);
        AtomicReference<PdfWriter> pdfWriter = new AtomicReference<>(PdfWriter.getInstance(document, new FileOutputStream("my-pdf-file.pdf")));
        document.open();
        document.add(new Paragraph("Invoice For Transaction " + idTransaction));
        
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setSpacingBefore(10);
        
        // define font for table header row
        Font font = FontFactory.getFont(FontFactory.TIMES);
        font.setColor(BaseColor.WHITE);
        
        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setPadding(6);
        
        // write table header
        cell.setPhrase(new Phrase("No", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("ID Item", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Item Name", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Bought Qty", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Price/Item", font));
        table.addCell(cell);
        
        cell.setPhrase(new Phrase("Total Price", font));
        table.addCell(cell);
        
        Integer number = 1;
        for (ItemTransaction itemTransaction : transactions.getItemTransactions()) {
            table.addCell(number.toString());
            number++;
            table.addCell(itemTransaction.getItem().getIdItem().toString());
            table.addCell(itemTransaction.getItem().getItemName());
            table.addCell(itemTransaction.getBoughtQty().toString());
            table.addCell(itemTransaction.getPrice().toString());
            Long sum = itemTransaction.getBoughtQty() * itemTransaction.getPrice();
            table.addCell(sum.toString());
        }
        
        document.add(table);
        document.close();
        
    }
}
