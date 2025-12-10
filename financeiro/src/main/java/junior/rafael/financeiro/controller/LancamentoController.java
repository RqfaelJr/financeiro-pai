package junior.rafael.financeiro.controller;

import junior.rafael.financeiro.domain.Base;
import junior.rafael.financeiro.domain.Lancamento;
import junior.rafael.financeiro.repository.BaseRepository;
import junior.rafael.financeiro.repository.LancamentoRepository;
import junior.rafael.financeiro.request.LancamentoRequest;
import junior.rafael.financeiro.response.BuscaLancamentoResponse;
import junior.rafael.financeiro.response.LancamentoResponse;
import junior.rafael.financeiro.service.VerificaIncosistenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lancamentos")
@RequiredArgsConstructor
public class LancamentoController {

    private final LancamentoRepository lancamentoRepository;
    private final BaseRepository baseRepository;

    private final VerificaIncosistenciaService verificaIncosistenciaService;

    @GetMapping("/buscar")
    public ResponseEntity<BuscaLancamentoResponse> get() {

        List<Lancamento> lancamentos = lancamentoRepository.findAllOrdenado();

        Boolean passivoNegativo = verificaIncosistenciaService.verificaPassivoNegativo();
        Boolean ativoNegativo = verificaIncosistenciaService.verificaAtivoNegativo();
        Boolean receitaNegativa = verificaIncosistenciaService.verificaReceitaNegativa();
        Boolean despesaNegativa = verificaIncosistenciaService.verificaDespesaNegativa();
        Boolean comparativoErrado = verificaIncosistenciaService.verificaPassivoXPatrimonioLiquido();

        BuscaLancamentoResponse response = new BuscaLancamentoResponse(
                lancamentos.stream().map(LancamentoResponse::fromEntity).toList(),
                ativoNegativo,
                passivoNegativo,
                receitaNegativa,
                despesaNegativa,
                comparativoErrado
        );
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping("/criar")
    public ResponseEntity<LancamentoResponse> post(@RequestBody LancamentoRequest request) {

        Base debito = baseRepository.findById(request.debito())
                .orElseThrow(() -> new IllegalArgumentException("Base de débito não encontrada"));

        Base credito = baseRepository.findById(request.credito())
                .orElseThrow(() -> new IllegalArgumentException("Base de crédito não encontrada"));

        Lancamento lancamento = new Lancamento(request, debito, credito);
        lancamento = lancamentoRepository.save(lancamento);

        Boolean passivoNegativo = verificaIncosistenciaService.verificaPassivoNegativo();
        Boolean ativoNegativo = verificaIncosistenciaService.verificaAtivoNegativo();
        Boolean receitaNegativa = verificaIncosistenciaService.verificaReceitaNegativa();
        Boolean despesaNegativa = verificaIncosistenciaService.verificaDespesaNegativa();
        Boolean comparativoErrado = verificaIncosistenciaService.verificaPassivoXPatrimonioLiquido();

        return ResponseEntity.ok(new LancamentoResponse(lancamento, ativoNegativo, passivoNegativo, receitaNegativa, despesaNegativa, comparativoErrado));
    }
}
