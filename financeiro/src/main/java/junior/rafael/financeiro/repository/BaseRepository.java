package junior.rafael.financeiro.repository;

import junior.rafael.financeiro.domain.Base;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, Long> {
}
