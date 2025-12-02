package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.response.RazaoResponse;
import junior.rafael.financeiro.service.RazaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/razao")
@RequiredArgsConstructor
public class RazaoController {


    private final RazaoService razaoService;

    @GetMapping("/{id}/{startDate}/{endDate}")
    public ResponseEntity<List<RazaoResponse>> execute(
            @PathVariable Long id,
            @PathVariable LocalDate startDate,
            @PathVariable LocalDate endDate
            ) {

        var response = razaoService.gerarRelatorioRazao(id, startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
