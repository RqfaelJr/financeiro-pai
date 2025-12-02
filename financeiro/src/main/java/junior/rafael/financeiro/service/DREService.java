package junior.rafael.financeiro.service;

import junior.rafael.financeiro.domain.Lancamento;
import junior.rafael.financeiro.repository.LancamentoRepository;
import junior.rafael.financeiro.response.DREResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DREService {


    private final LancamentoRepository lancamentoRepository;


    public List<DREResponse> gerarRelatorioDRE(LocalDate startDate, LocalDate endDate) {

        List<DREResponse> response = new ArrayList<>();

        List<Lancamento> lancamentoReceitas = lancamentoRepository.findByInReceitasAndDataBetween(startDate, endDate);

        List<Lancamento> lancamentosDespesas = lancamentoRepository.findByInDespesasAndDataBetween(startDate, endDate);

        Map<String, BigDecimal> mapaReceitas = lancamentoReceitas.stream()
                .collect(Collectors.toMap(
                        r -> r.getCredito().getNome(),
                        Lancamento::getValor,
                        BigDecimal::add,
                        LinkedHashMap::new
                ));

        List<DREResponse> dreReceitas = mapaReceitas.entrySet().stream()
                .map(entry -> new DREResponse(
                        entry.getKey(),
                        entry.getValue(),
                        "R"
                ))
                .toList();

        Map<String, BigDecimal> mapaDespesas = lancamentosDespesas.stream()
                .collect(Collectors.toMap(
                        d -> d.getDebito().getNome(),
                        Lancamento::getValor,
                        BigDecimal::add
                ));

        List<DREResponse> dreDespesas = mapaDespesas.entrySet().stream()
                .map(d -> new DREResponse(
                        d.getKey(),
                        d.getValue(),
                        "D"
                )).toList();

        response.addAll(dreReceitas);
        response.addAll(dreDespesas);

        return response;
    }
}
