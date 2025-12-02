package junior.rafael.financeiro.service;

import junior.rafael.financeiro.domain.Lancamento;
import junior.rafael.financeiro.repository.LancamentoRepository;
import junior.rafael.financeiro.response.BalanceteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalanceteService {

    private final LancamentoRepository lancamentoRepository;


    public List<BalanceteResponse> gerarBalancete(LocalDate startDate, LocalDate endDate) {

        List<Lancamento> lancamentosAtivosDebito = lancamentoRepository.findAllAtivosDebito(endDate);
        List<Lancamento> lancamentosAtivosCredito = lancamentoRepository.findAllAtivosCredito(endDate);
        BigDecimal ativoInicialD = getSaldoInicial(lancamentosAtivosDebito, startDate);
        BigDecimal ativoInicialC =getSaldoInicial(lancamentosAtivosCredito, startDate);
        BigDecimal ativoMovD = getSaldoMovimentacao(lancamentosAtivosDebito);
        BigDecimal ativoMovC = getSaldoMovimentacao(lancamentosAtivosCredito);

        List<Lancamento> lancamentosPassivosDebito = lancamentoRepository.findAllPassivosDebito(endDate);
        List<Lancamento> lancamentosPassivosCredito = lancamentoRepository.findAllPassivosCredito(endDate);
        BigDecimal passivoInicialD = getSaldoInicial(lancamentosPassivosDebito, startDate);
        BigDecimal passivoInicialC =getSaldoInicial(lancamentosPassivosCredito, startDate);
        BigDecimal passivoMovD = getSaldoMovimentacao(lancamentosPassivosDebito);
        BigDecimal passivoMovC = getSaldoMovimentacao(lancamentosPassivosCredito);

        List<Lancamento> lancamentosReceitasDebito = lancamentoRepository.findAllReceitasDebito(endDate);
        List<Lancamento> lancamentosReceitasCredito = lancamentoRepository.findAllReceitasCredito(endDate);
        BigDecimal receitasInicialD = getSaldoInicial(lancamentosReceitasDebito, startDate);
        BigDecimal receitasInicialC =getSaldoInicial(lancamentosReceitasCredito, startDate);
        BigDecimal receitasMovD = getSaldoMovimentacao(lancamentosReceitasDebito);
        BigDecimal receitasMovC = getSaldoMovimentacao(lancamentosReceitasCredito);

        List<Lancamento> lancamentosDespesasDebito = lancamentoRepository.findAllDespesasDebito(endDate);
        List<Lancamento> lancamentosDespesasCredito = lancamentoRepository.findAllDespesasCredito(endDate);
        BigDecimal despesasInicialD = getSaldoInicial(lancamentosDespesasDebito, startDate);
        BigDecimal despesasInicialC = getSaldoInicial(lancamentosDespesasCredito, startDate);
        BigDecimal despesasMovD = getSaldoMovimentacao(lancamentosDespesasDebito);
        BigDecimal despesasMovC = getSaldoMovimentacao(lancamentosDespesasCredito);

        List<Lancamento> lancamentosPLDebito = lancamentoRepository.findAllPLDebito(endDate);
        List<Lancamento> lancamentosPLCredito = lancamentoRepository.findAllPLCredito(endDate);
        BigDecimal PLInicialD = getSaldoInicial(lancamentosPLDebito, startDate);
        BigDecimal PLInicialC =getSaldoInicial(lancamentosPLCredito, startDate);
        BigDecimal PLMovD = getSaldoMovimentacao(lancamentosPLDebito);
        BigDecimal PLMovC = getSaldoMovimentacao(lancamentosPLCredito);

        BalanceteResponse ativos = new BalanceteResponse("Ativo",
                ativoInicialD.subtract(ativoInicialC), BigDecimal.ZERO,
                ativoMovD, ativoMovC,
                ativoMovD.add(ativoInicialD).subtract(ativoMovC),
                BigDecimal.ZERO
                );

        BalanceteResponse passivos = new BalanceteResponse("Passivo",
                BigDecimal.ZERO, passivoInicialC.subtract(passivoInicialD),
                passivoMovD, passivoMovC,
                BigDecimal.ZERO,
                passivoMovC.add(passivoInicialC).subtract(passivoMovD)
        );

        BalanceteResponse receitas = new BalanceteResponse("Receita",
                BigDecimal.ZERO, receitasInicialC.subtract(receitasInicialD),
                receitasMovD, receitasMovC,
                BigDecimal.ZERO,
                receitasMovC.add(receitasInicialC).subtract(receitasMovD)
        );

        BalanceteResponse despesas = new BalanceteResponse("Despesa",
                despesasInicialD.subtract(receitasInicialC), BigDecimal.ZERO,
                despesasMovD, despesasMovC,
                despesasMovD.add(despesasInicialD).subtract(despesasMovC),
                BigDecimal.ZERO
        );

        BalanceteResponse pl = new BalanceteResponse("Patrimônio Líquido",
                BigDecimal.ZERO,
                PLInicialC.subtract(PLInicialD),
                PLMovD, PLMovC,
                BigDecimal.ZERO,
                PLMovC.add(PLInicialC).subtract(PLMovD)
        );

        return List.of(ativos, passivos, pl, receitas, despesas);
    }

    private BigDecimal getSaldoInicial(List<Lancamento> lancamentos, LocalDate startDate) {

        return lancamentos.stream()
                .filter(l -> l.getData().isBefore(startDate))
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getSaldoMovimentacao(List<Lancamento> lancamentos) {
        return lancamentos.stream()
                .map(Lancamento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
