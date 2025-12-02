package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.response.DREResponse;
import junior.rafael.financeiro.service.DREService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dre")
@RequiredArgsConstructor
public class DREController {

    private final DREService service;


    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<DREResponse>> execute(
            @PathVariable LocalDate startDate,
            @PathVariable LocalDate endDate
    ) {

        var response = service.gerarRelatorioDRE(startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
