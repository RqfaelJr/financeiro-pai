package junior.rafael.financeiro.controller;

import jakarta.transaction.Transactional;
import junior.rafael.financeiro.component.VerificaDisponibilidadeEmDeletar;
import junior.rafael.financeiro.domain.passivo.Passivo;
import junior.rafael.financeiro.repository.PassivoRepository;
import junior.rafael.financeiro.request.PassivoRequest;
import junior.rafael.financeiro.response.RelatorioResponse;
import junior.rafael.financeiro.response.PassivoResponse;
import junior.rafael.financeiro.service.BalancoPatrimonialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/passivos")
@RequiredArgsConstructor
public class PassivoController {

    private final PassivoRepository passivoRepository;

    private final BalancoPatrimonialService balancoPatrimonialService;
    private final VerificaDisponibilidadeEmDeletar verificaDisponibilidadeEmDeletar;

    @GetMapping("/buscar")
    public ResponseEntity<List<PassivoResponse>> buscar() {
        List<Passivo> passivos = passivoRepository.findAll();
        return ResponseEntity.ok(passivos.stream().map(PassivoResponse::new).toList());
    }

    @Transactional
    @PostMapping("/criar")
    public ResponseEntity<PassivoResponse> criar(@RequestBody PassivoRequest request) {
        Passivo passivo = new Passivo(request);
        passivo = passivoRepository.save(passivo);
        return ResponseEntity.ok(new PassivoResponse(passivo));
    }

    @GetMapping("/buscar/balanco-patrimonial/{date}")
    public ResponseEntity<List<RelatorioResponse>> balancoPatrimonial(
            @PathVariable LocalDate date
    ) {

        var response = balancoPatrimonialService.gerarRelatorioPassivos(date);
        return ResponseEntity.ok(response);

    }

    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!verificaDisponibilidadeEmDeletar.execute(id)) {
            passivoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new RuntimeException("Não é possível deletar o passivo pois ele está associado a outros registros.");
    }
}
