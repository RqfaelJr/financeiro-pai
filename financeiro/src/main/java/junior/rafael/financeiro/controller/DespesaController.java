package junior.rafael.financeiro.controller;

import jakarta.transaction.Transactional;
import junior.rafael.financeiro.component.VerificaDisponibilidadeEmDeletar;
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
    private final VerificaDisponibilidadeEmDeletar verificaDisponibilidadeEmDeletar;

    @GetMapping("/buscar")
    public ResponseEntity<List<DespesaResponse>> buscar() {
        List<Despesa> despesas = despesaRepository.findAll();
        return ResponseEntity.ok(despesas.stream().map(DespesaResponse::new).toList());
    }

    @Transactional
    @PostMapping("/criar")
    public ResponseEntity<DespesaResponse> criar(@RequestBody DespesaRequest request) {
        Despesa despesa = new Despesa(request);
        despesa = despesaRepository.save(despesa);
        return ResponseEntity.ok(new DespesaResponse(despesa));
    }

    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!verificaDisponibilidadeEmDeletar.execute(id)) {
            despesaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new RuntimeException("Não é possível deletar a despesa pois ela está associado a outros registros.");
    }
}
