package com.firstlook.controller;

import com.firstlook.model.ContactMessage;
import com.firstlook.repository.ContactMessageRepository;
import com.firstlook.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/contacts")
@RequiredArgsConstructor
public class ContactManagementController {
    
    private final ContactMessageRepository contactMessageRepository;
    private final ExcelExportService excelExportService;
    
    @GetMapping
    public String contactsPage(Model model) {
        List<ContactMessage> contacts = contactMessageRepository.findAllByOrderBySubmittedAtDesc();
        
        long onHoldCount = contacts.stream().filter(c -> "ON_HOLD".equals(c.getStatus())).count();
        long contactedCount = contacts.stream().filter(c -> "CONTACTED".equals(c.getStatus())).count();
        
        model.addAttribute("contacts", contacts);
        model.addAttribute("onHoldCount", onHoldCount);
        model.addAttribute("contactedCount", contactedCount);
        model.addAttribute("totalCount", contacts.size());
        
        return "admin/contacts";
    }
    
    @PostMapping("/update-status")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateStatus(
            @RequestParam String id,
            @RequestParam String status) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            ContactMessage contact = contactMessageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
            
            contact.setStatus(status);
            contactMessageRepository.save(contact);
            
            response.put("success", "true");
            response.put("message", "Status updated successfully");
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Error updating status: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportToExcel() throws IOException {
        List<ContactMessage> contacts = contactMessageRepository.findAllByOrderBySubmittedAtDesc();
        byte[] excelData = excelExportService.exportContactsToExcel(contacts);
        
        String filename = "contacts_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", filename);
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteContact(@PathVariable String id) {
        Map<String, String> response = new HashMap<>();
        
        try {
            contactMessageRepository.deleteById(id);
            response.put("success", "true");
            response.put("message", "Contact deleted successfully");
        } catch (Exception e) {
            response.put("success", "false");
            response.put("message", "Error deleting contact: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
