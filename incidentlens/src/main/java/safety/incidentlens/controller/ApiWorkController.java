package safety.incidentlens.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import safety.incidentlens.dto.SnapChartDTO;
import safety.incidentlens.model.Work;
import safety.incidentlens.service.WorkService;
import java.util.List;

@RestController
@RequestMapping("/api/works")
public class ApiWorkController {

    private final WorkService workService;

    public ApiWorkController(WorkService workService) {
        this.workService = workService;
    }

    @GetMapping
    public ResponseEntity<List<Work>> getAllWorks() {
        List<Work> works = workService.getAllWorks();
        return ResponseEntity.ok(works);
    }

    @PostMapping
    public ResponseEntity<Work> createWork(@RequestBody SnapChartDTO dto) {
        Work savedWork = workService.saveSnapChart(dto);
        return ResponseEntity.ok(savedWork);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Work> getWorkById(@PathVariable Long id) {
        Work work = workService.getWorkWithDetails(id);
        return ResponseEntity.ok(work);
    }
}