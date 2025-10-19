package com.firstlook.service;

import com.firstlook.model.ContactMessage;
import com.firstlook.repository.LeadScoreRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {
    
    private final LeadScoreRepository leadScoreRepository;
    
    public byte[] exportContactsToExcel(List<ContactMessage> contacts) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Contacts");
        
        // Create header style
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = {"ID", "Name", "Email", "Mobile", "Subject", "Message", 
                           "Status", "Lead Score", "Lead Quality", "Submitted At", "IP Address"};
        
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Create data rows
        int rowNum = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (ContactMessage contact : contacts) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(contact.getId());
            row.createCell(1).setCellValue(contact.getName());
            row.createCell(2).setCellValue(contact.getEmail());
            row.createCell(3).setCellValue(contact.getMobile());
            row.createCell(4).setCellValue(contact.getSubject());
            row.createCell(5).setCellValue(contact.getMessage());
            row.createCell(6).setCellValue(contact.getStatus());
            
            // Get lead score if available
            leadScoreRepository.findByContactMessageId(contact.getId()).ifPresentOrElse(
                leadScore -> {
                    row.createCell(7).setCellValue(leadScore.getScore());
                    row.createCell(8).setCellValue(leadScore.getQuality());
                },
                () -> {
                    row.createCell(7).setCellValue("N/A");
                    row.createCell(8).setCellValue("N/A");
                }
            );
            
            row.createCell(9).setCellValue(contact.getSubmittedAt().format(formatter));
            row.createCell(10).setCellValue(contact.getIpAddress());
        }
        
        // Auto-size columns
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}
