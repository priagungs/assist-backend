package com.future.assist;

import com.future.assist.exception.UnauthorizedException;
import com.future.assist.mapper.UserMapper;
import com.future.assist.model.response_model.UserResponse;
import com.future.assist.printer.PrinterService;
import com.future.assist.service.service_impl.BackupRestoreService;
import com.future.assist.service.service_impl.FileStorageService;
import com.future.assist.service.service_impl.LoggedinUserInfo;
import com.future.assist.service.service_interface.ItemService;
import com.future.assist.service.service_interface.TransactionService;
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
    PrinterService printerService;

    @Autowired
    ItemService itemService;

    @Autowired
    UserMapper mapper;

    @Autowired
    BackupRestoreService backupRestoreService;

    @GetMapping("/login-detail")
    public UserResponse getLoginDetail() {
        return mapper.entityToResponse(loggedinUserInfo.getUser());
    }

    @GetMapping("/item-detail/{id}")
    public ResponseEntity generateItemDetail(@PathVariable Long id, HttpServletRequest request) {
        printerService.printItem(itemService.readItemByIdItem(id));
        String filename = "item_" + id.toString() + ".pdf";
        Resource resource = storageService.loadFileAsResource(filename);

        String contentType = "application/octet-stream";

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/invoice/{id}")
    public ResponseEntity generateInvoice(@PathVariable Long id, HttpServletRequest request) {

        printerService.printInvoice(transactionService.readTransactionByIdTransaction(id));
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
