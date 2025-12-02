package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.domain.despesa.Despesa;
import junior.rafael.financeiro.repository.DespesaRepository;
import junior.rafael.financeiro.request.DespesaRequest;
import junior.rafael.financeiro.response.DespesaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaRepository despesaRepository;

    @GetMapping("/buscar")
    public ResponseEntity<List<DespesaResponse>> buscar() {
        List<Despesa> despesas = despesaRepository.findAll();
        return ResponseEntity.ok(despesas.stream().map(DespesaResponse::new).toList());
    }

    @PostMapping("/criar")
    public ResponseEntity<DespesaResponse> criar(@RequestBody DespesaRequest request) {
        Despesa despesa = new Despesa(request);
        despesa = despesaRepository.save(despesa);
        return ResponseEntity.ok(new DespesaResponse(despesa));
    }
}
