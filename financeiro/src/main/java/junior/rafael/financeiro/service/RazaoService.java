package junior.rafael.financeiro.service;

import junior.rafael.financeiro.domain.Lancamento;
import junior.rafael.financeiro.repository.AtivoRepository;
import junior.rafael.financeiro.repository.DespesaRepository;
import junior.rafael.financeiro.repository.LancamentoRepository;
import junior.rafael.financeiro.response.RazaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RazaoService {

    private final LancamentoRepository lancamentoRepository;
    private final AtivoRepository ativoRepository;
    private final DespesaRepository despesaRepository;

    public List<RazaoResponse> gerarRelatorioRazao(Long id, LocalDate startDate, LocalDate endDate) {

        BigDecimal saldoDebito = BigDecimal.ZERO;
        BigDecimal saldoCredito = BigDecimal.ZERO;

        List<Lancamento> lancamentos = lancamentoRepository.findByIdAndDataBetween(id, startDate, endDate);

        List<RazaoResponse> listRazao = new ArrayList<>();
        for (Lancamento l : lancamentos) {
            if (l.getDebito().getId().equals(id)) {
                saldoDebito = saldoDebito.add(l.getValor());
                listRazao.add(new RazaoResponse(l.getData(), l.getDebito().getNome(), l.getValor(), "D"));
            } else {
                saldoCredito = saldoCredito.add(l.getValor());
                listRazao.add(new RazaoResponse(l.getData(), l.getCredito().getNome(), l.getValor(), "C"));
            }
        }

        Boolean ehAtivoOuDespesa = ativoRepository.existsById(id) || despesaRepository.existsById(id);

        var saldoInicial = new RazaoResponse(null, "Saldo Inicial", calcularSaldoInicial(id, startDate, ehAtivoOuDespesa), "I");

        var saldoFinal = new RazaoResponse(null, "Saldo Final", calcularSaldoFinal(saldoInicial.valor(), saldoDebito, saldoCredito, ehAtivoOuDespesa), "F");

        return Stream.concat(
                Stream.concat(
                        Stream.of(saldoInicial),
                        listRazao.stream()
                ),
                Stream.of(saldoFinal)
        ).toList();
    }

    private BigDecimal calcularSaldoInicial(Long id, LocalDate startDate, Boolean ehAtivoOuDespesa) {

        BigDecimal totalDebito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(id, LocalDate.of(2025, 10, 1), startDate.minusDays(1));
        BigDecimal totalCredito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(id, LocalDate.of(2025, 10, 1), startDate.minusDays(1));

        if (ehAtivoOuDespesa) {
            return totalDebito.subtract(totalCredito);
        }

        return totalCredito.subtract(totalDebito);
    }

    private BigDecimal calcularSaldoFinal(BigDecimal saldoInicial, BigDecimal saldoDebito, BigDecimal saldoCredito, Boolean ehAtivoOuDespesa) {

        if (ehAtivoOuDespesa) {
            return saldoInicial.add(saldoDebito).subtract(saldoCredito);
        }

        return saldoInicial.add(saldoCredito).subtract(saldoDebito);

    }
}
