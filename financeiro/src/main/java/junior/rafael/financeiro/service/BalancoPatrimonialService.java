package junior.rafael.financeiro.service;

import junior.rafael.financeiro.domain.PatrimonioLiquido;
import junior.rafael.financeiro.domain.ativo.Ativo;
import junior.rafael.financeiro.domain.despesa.Despesa;
import junior.rafael.financeiro.domain.passivo.Passivo;
import junior.rafael.financeiro.domain.receita.Receita;
import junior.rafael.financeiro.repository.*;
import junior.rafael.financeiro.response.RelatorioResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BalancoPatrimonialService {


    private final AtivoRepository ativoRepository;
    private final PassivoRepository passivoRepository;
    private final ReceitaRepository receitaRepository;
    private final DespesaRepository despesaRepository;
    private final PatrimonioLiquidoRepository patrimonioLiquidoRepository;

    private final LancamentoRepository lancamentoRepository;

    public List<RelatorioResponse> gerarRelatorioAtivos(
            LocalDate startDate, LocalDate endDate
    ) {

        List<Ativo> ativos = ativoRepository.findAll();

        return ativos.stream().map(ativo -> {
            BigDecimal debito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(ativo.getId(), startDate, endDate);
            BigDecimal credito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(ativo.getId(), startDate, endDate);

            return new RelatorioResponse(
                    ativo.getNome(),
                    debito.subtract(credito)
            );
        }).toList();
    }

    public List<RelatorioResponse> gerarRelatorioPassivos(
            LocalDate startDate, LocalDate endDate
    ) {

        List<Passivo> passivos = passivoRepository.findAll();

        return passivos.stream().map(passivo -> {
            BigDecimal debito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(passivo.getId(), startDate, endDate);
            BigDecimal credito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(passivo.getId(), startDate, endDate);

            return new RelatorioResponse(
                    passivo.getNome(),
                    credito.subtract(debito)
            );
        }).toList();
    }

    public List<RelatorioResponse> gerarRelatorioPatrimonioLiquido(LocalDate startDate, LocalDate endDate) {

        List<PatrimonioLiquido> pl = patrimonioLiquidoRepository.findAll();
        List<RelatorioResponse> relatorio = new ArrayList<>(pl.stream().map(p -> {
            BigDecimal debito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(p.getId(), startDate, endDate);
            BigDecimal credito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(p.getId(), startDate, endDate);

            return new RelatorioResponse(
                    p.getNome(),
                    credito.subtract(debito)
            );
        }).toList());

        relatorio.
                add(gerarRelatorioResultado(startDate, endDate));

        return relatorio;
    }

    private RelatorioResponse gerarRelatorioResultado(LocalDate startDate, LocalDate endDate) {

        List<Receita> receitas = receitaRepository.findAll();
        BigDecimal valorReceita = receitas.stream().map(p -> {
            BigDecimal debito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(p.getId(), startDate, endDate);
            BigDecimal credito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(p.getId(), startDate, endDate);

            return credito.subtract(debito);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Despesa> despesas = despesaRepository.findAll();
        BigDecimal valorDespesa = despesas.stream().map(p -> {
            BigDecimal debito = lancamentoRepository.findSomaValorByIdInLancamentosDebitoNoPeriodo(p.getId(), startDate, endDate);
            BigDecimal credito = lancamentoRepository.findSomaValorByIdInLancamentosCreditoNoPeriodo(p.getId(), startDate, endDate);

            return debito.subtract(credito);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new RelatorioResponse(
                "Resultado",
                valorReceita.subtract(valorDespesa)
        );
    }


}
