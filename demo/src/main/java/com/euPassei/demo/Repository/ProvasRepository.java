package com.euPassei.demo.Repository;

import com.euPassei.demo.Entity.Provas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvasRepository extends JpaRepository<Provas, Long> {

    // sem nada aqui dentro, só foi feito pra dar save(prova) e afins.

}
