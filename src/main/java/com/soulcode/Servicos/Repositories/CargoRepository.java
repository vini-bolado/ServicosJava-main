package com.soulcode.Servicos.Repositories;

import com.soulcode.Servicos.Models.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository  extends JpaRepository<Cargo, Integer> {
}
