package com.pcr.controller;


import com.pcr.domain.Pcr;
import com.pcr.model.PcrDto;
import com.pcr.service.PcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/pcr")
public class PcrController {

    @Autowired
    private PcrService service;


    @GetMapping("/add")
    public String add(@ModelAttribute("tPcrMaster") final Pcr pcr, Model model) {
        int page = 0; // First page
        int size = 6; // Maximum of 6 items per page
        Page<Pcr> pcrPage = service.getPcrs(page, size); // Use pagination to fetch 6 items
        List<Pcr> pcrList = new ArrayList<>();
        for (Pcr p : pcrPage.getContent()) {
            if ((p.getClientName() != null && !p.getClientName().isEmpty()) &&
                    (p.getNotes() != null && !(p.getNotes().equals(""))) &&
                    p.getFileName() != null &&
                    p.getDateCompleted() != null) {
                p.setClassName("bg-green");
            } else {
                p.setClassName("bg-yellow");
            }
            pcrList.add(p);
        }
        model.addAttribute("pcrs", pcrList);
        return "tPcrMaster/add";
    }

    @PostMapping("/add")
    public String addPcr(@ModelAttribute Pcr pcr, @RequestParam("file") MultipartFile file) throws IOException {

        if (!file.isEmpty()) {
            pcr.setPcrFile(file.getBytes());
            pcr.setFileName(file.getOriginalFilename());
        }
        service.savePcr(pcr);
        return "redirect:/pcr/add";
    }

    @PostMapping("/delete/{id}")
    public String deletePcr(@PathVariable Long id) {
        service.deletePcr(id);
        return "redirect:/pcr/add";
    }

    @GetMapping("/load/{offset}")
    public List<Pcr> loadTiles(@PathVariable int offset) {
        return service.getAllPcrs().stream().skip(offset).limit(6).toList();
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        Pcr pcr = service.getPcrById(id);
        if (pcr.getPcrFile() == null || pcr.getFileName() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + pcr.getFileName() + "\"")
                .body(pcr.getPcrFile());
    }

    @PostMapping("/persist")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> persistTPcrMaster(@ModelAttribute PcrDto pcrMaster, @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (file != null && !file.isEmpty()) {
                pcrMaster.setPcrFile(file.getBytes());
                pcrMaster.setFileName(file.getOriginalFilename());
            }
            Pcr savedMaster = service.saveOrUpdate(pcrMaster);
            response.put("success", true);
            response.put("pcrId", savedMaster.getPcrId());
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "Error persisting data");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/loadMore")
    public ResponseEntity<List<Pcr>> getPcrs(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<Pcr> pcrPage = service.getPcrs(page, size);
        List<Pcr> pcrList = new ArrayList<>();
        for (Pcr p : pcrPage.getContent()) {
            if ((p.getClientName() != null && !p.getClientName().isEmpty()) &&
                    (p.getNotes() != null && !(p.getNotes().equals(""))) &&
                    p.getFileName() != null &&
                    p.getDateCompleted() != null) {
                p.setClassName("bg-green");
            } else {
                p.setClassName("bg-yellow");
            }
            pcrList.add(p);
        }
        return ResponseEntity.ok(pcrList);
    }


}