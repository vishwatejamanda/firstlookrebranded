package com.firstlook.controller;

import com.firstlook.service.VisitorService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    
    private final VisitorService visitorService;
    
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        visitorService.trackVisitor(request, "/");
        return "index";
    }
}
