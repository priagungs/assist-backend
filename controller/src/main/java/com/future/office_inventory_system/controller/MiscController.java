package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.model.User;
import com.future.office_inventory_system.printer.PrinterService;
import com.future.office_inventory_system.service.FileStorageService;
import com.future.office_inventory_system.service.ItemService;
import com.future.office_inventory_system.service.TransactionService;
import com.future.office_inventory_system.value_object.LoggedinUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;

@RestController
@RequestMapping("/api")
public class MiscController {
    
    @Autowired
    FileStorageService storageService;

    @Autowired
    LoggedinUserInfo loggedinUserInfo;
    
    @Autowired
    TransactionService transactionService;
    
    @Autowired
    PrinterService p;
    
    @Autowired
    ItemService itemService;

    @GetMapping("/login-detail")
    public User getLoginDetail() {
        return loggedinUserInfo.getUser();
    }
    
    @GetMapping("/item-detail/{id}")
    public ResponseEntity generateItemDetail(@PathVariable Long id, HttpServletRequest request) {
    
        p.printItem(itemService.readItemByIdItem(id));
        String filename = "item_" + id.toString() + ".pdf";
        Resource resource = storageService.loadFileAsResource(filename);
    
        String contentType = "application/octet-stream";
    
    
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    
    @GetMapping("/invoice/{id}")
    public ResponseEntity generateInvoice(@PathVariable Long id, HttpServletRequest request) {
        
        p.printInvoice(transactionService.readTransactionByIdTransaction(id));
        String filename = "Invoice_" + id.toString() + ".pdf";
        Resource resource = storageService.loadFileAsResource(filename);
        
        String contentType = "application/octet-stream";
        
        
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
}
