package io.pivotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DemoFileUploaderController {
    private final VirusScannerClient virusScannerClient;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @Autowired
    public DemoFileUploaderController(final VirusScannerClient virusScannerClient) {
        this.virusScannerClient = virusScannerClient;
    }

    @PostMapping("/")
    public String result(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        boolean hasVirus = virusScannerClient.hasVirus(file);
        redirectAttributes.addFlashAttribute("message",
                "Your file " + file.getOriginalFilename() + (hasVirus?" contains":" doesn't contain") + " a virus");
        return "redirect:/";
    }
}
