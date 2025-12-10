package junior.rafael.financeiro.component;

import junior.rafael.financeiro.domain.PatrimonioLiquido;
import junior.rafael.financeiro.repository.PatrimonioLiquidoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class PatrimonioLiquidoInitializer implements CommandLineRunner {

    private final PatrimonioLiquidoRepository patrimonioLiquidoRepository;

    public void run(String... args) {


        long total = patrimonioLiquidoRepository.count();

        if (total == 0) {
            var patrimonioInicial = new PatrimonioLiquido("Capital Social");
            patrimonioLiquidoRepository.save(patrimonioInicial);
            log.info("Patrimonio Liquido criado com sucesso!");
        } else {
            log.info("Patrimonio Liquido já existe. Total existente: {}", total);
        }
    }
}
