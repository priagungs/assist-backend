package com.future.office_inventory_system.controller;

import com.future.office_inventory_system.exception.UnauthorizedException;
import com.future.office_inventory_system.model.entity_model.User;
import com.future.office_inventory_system.printer.PrinterService;
import com.future.office_inventory_system.service.service_impl.BackupRestoreService;
import com.future.office_inventory_system.service.service_impl.FileStorageService;
import com.future.office_inventory_system.service.service_impl.LoggedinUserInfo;
import com.future.office_inventory_system.service.service_interface.ItemService;
import com.future.office_inventory_system.service.service_interface.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

    @Autowired
    BackupRestoreService backupRestoreService;

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

    @GetMapping("/backup")
    ResponseEntity backup() {
        if (loggedinUserInfo.getUser().getIsAdmin()) {
            return backupRestoreService.backup();
        } else {
            throw new UnauthorizedException("you are not admin");
        }
    }

    @PostMapping("/restore")
    ResponseEntity restore(@RequestParam("file") MultipartFile file, HttpSession session) {
        if (loggedinUserInfo.getUser().getIsAdmin()) {
            session.invalidate();
            return backupRestoreService.restore(file);
        } else {
            throw new UnauthorizedException("you are not admin");
        }

    }
}
