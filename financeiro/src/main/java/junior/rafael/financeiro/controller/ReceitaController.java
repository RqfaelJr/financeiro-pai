package junior.rafael.financeiro.controller;

import jakarta.transaction.Transactional;
import junior.rafael.financeiro.component.VerificaDisponibilidadeEmDeletar;
import junior.rafael.financeiro.domain.receita.Receita;
import junior.rafael.financeiro.repository.ReceitaRepository;
import junior.rafael.financeiro.request.ReceitaRequest;
import junior.rafael.financeiro.response.ReceitaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/receitas")
@RequiredArgsConstructor
public class ReceitaController {

    private final ReceitaRepository receitaRepository;
    private final VerificaDisponibilidadeEmDeletar verificaDisponibilidadeEmDeletar;

    @GetMapping("/buscar")
    public ResponseEntity<List<ReceitaResponse>> buscar() {

        List<Receita> receitas = receitaRepository.findAll();
        return ResponseEntity.ok(receitas.stream().map(ReceitaResponse::new).toList());
    }

    @Transactional
    @PostMapping("/criar")
    public ResponseEntity<ReceitaResponse> criar(@RequestBody ReceitaRequest request) {
        Receita receita = new Receita(request);
        receita = receitaRepository.save(receita);
        return ResponseEntity.ok(new ReceitaResponse(receita));
    }

    @Transactional
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        if (!verificaDisponibilidadeEmDeletar.execute(id)) {
            receitaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        throw new RuntimeException("Não é possível deletar a receita pois ela está associado a outros registros.");
    }
}
