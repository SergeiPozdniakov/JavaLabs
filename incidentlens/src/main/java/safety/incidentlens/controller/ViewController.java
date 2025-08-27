package safety.incidentlens.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "index";  // → теперь доступен только после входа
    }

    @GetMapping("/editor")
    public String createEditor() {
        return "editor";
    }

    @GetMapping("/editor/{id}")
    public String editEditor() {
        return "editor";
    }
}
