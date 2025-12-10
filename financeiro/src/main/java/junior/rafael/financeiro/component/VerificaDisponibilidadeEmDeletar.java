package junior.rafael.financeiro.component;

import junior.rafael.financeiro.repository.LancamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VerificaDisponibilidadeEmDeletar {

    private final LancamentoRepository lancamentoRepository;

    public boolean execute(Long id) {
        return lancamentoRepository.existsById(id);
    }
}
