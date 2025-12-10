package junior.rafael.financeiro.controller;

import jakarta.transaction.Transactional;
import junior.rafael.financeiro.domain.ativo.Ativo;
import junior.rafael.financeiro.repository.AtivoRepository;
import junior.rafael.financeiro.request.AtivoRequest;
import junior.rafael.financeiro.response.RelatorioResponse;
import junior.rafael.financeiro.response.AtivoResponse;
import junior.rafael.financeiro.service.BalancoPatrimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ativos")
@RequiredArgsConstructor
public class AtivoController {

    private final AtivoRepository ativoRepository;

    private final BalancoPatrimonialService balancoPatrimonialService;

    @GetMapping("/buscar")
    public ResponseEntity<List<AtivoResponse>> buscar() {


        List<Ativo> ativos = ativoRepository.findAll();

        return ResponseEntity.ok(ativos.stream().map(AtivoResponse::new).toList());
    }

    @Transactional
    @PostMapping("/criar")
    public ResponseEntity<AtivoResponse> criar(@RequestBody AtivoRequest request) {

        Ativo ativo = new Ativo(request);
        ativo = ativoRepository.save(ativo);

        return ResponseEntity.ok(new AtivoResponse(ativo));
    }

    @GetMapping("/buscar/balanco-patrimonial/{startDate}/{endDate}")
    public ResponseEntity<List<RelatorioResponse>> balancoPatrimonial(
            @PathVariable LocalDate startDate,
            @PathVariable LocalDate endDate
    ) {

        var response = balancoPatrimonialService.gerarRelatorioAtivos(startDate, endDate);

        return ResponseEntity.ok(response);

    }
}
