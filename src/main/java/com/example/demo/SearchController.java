package com.example.demo;

import com.example.demo.service.TavilySearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class SearchController {

    private final TavilySearchService tavilySearchService;

    public SearchController(TavilySearchService tavilySearchService) {
        this.tavilySearchService = tavilySearchService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/search")
    public String search(
            @RequestParam(required = false) String query,
            Model model) {

        model.addAttribute("query", query);

        if (query != null && !query.trim().isEmpty()) {
            List<Map<String, String>> results =
                    tavilySearchService.search(query.trim());

            model.addAttribute("results", results);
            model.addAttribute("searched", true);
        }

        return "index";
    }
}


