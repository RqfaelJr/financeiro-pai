package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.domain.PatrimonioLiquido;
import junior.rafael.financeiro.domain.passivo.Passivo;
import junior.rafael.financeiro.repository.PatrimonioLiquidoRepository;
import junior.rafael.financeiro.response.PassivoResponse;
import junior.rafael.financeiro.response.PatrimonioLiquidoResponse;
import junior.rafael.financeiro.response.RelatorioResponse;
import junior.rafael.financeiro.service.BalancoPatrimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patrimonios-liquidos")
@RequiredArgsConstructor
public class PatrimonioLiquidoController {

    private final BalancoPatrimonialService balancoPatrimonialService;
    private final PatrimonioLiquidoRepository patrimonioLiquidoRepository;

    @GetMapping("/buscar")
    public ResponseEntity<List<PatrimonioLiquidoResponse>> buscar() {
        List<PatrimonioLiquido> pl = patrimonioLiquidoRepository.findAll();
        return ResponseEntity.ok(pl.stream().map(PatrimonioLiquidoResponse::new).toList());
    }

    @GetMapping("/buscar/balanco-patrimonial/{startDate}/{endDate}")
    public ResponseEntity<List<RelatorioResponse>> balancoPatrimonial(
            @PathVariable LocalDate startDate, @PathVariable LocalDate endDate
    ) {

        var response = balancoPatrimonialService.gerarRelatorioPatrimonioLiquido(startDate, endDate);

        return ResponseEntity.ok(response);
    }
}
