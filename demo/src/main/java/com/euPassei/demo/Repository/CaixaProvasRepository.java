package com.euPassei.demo.Repository;

import com.euPassei.demo.Entity.CaixaProvas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CaixaProvasRepository extends JpaRepository<CaixaProvas, Long> {

    public List<CaixaProvas> findByUserId(Long id);
    public Optional<CaixaProvas> findByIdAndUserId(Long id,  Long userId);

}
