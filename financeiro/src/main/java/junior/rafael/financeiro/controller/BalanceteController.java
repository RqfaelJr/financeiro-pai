package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.response.BalanceteResponse;
import junior.rafael.financeiro.service.BalanceteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/balancete")
@RequiredArgsConstructor
public class BalanceteController {

    private final BalanceteService service;


    @GetMapping("/{startDate}/{endDate}")
    public ResponseEntity<List<BalanceteResponse>> execute(
            @PathVariable LocalDate startDate,
            @PathVariable LocalDate endDate
    ) {
        List<BalanceteResponse> response = service.gerarBalancete(startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
