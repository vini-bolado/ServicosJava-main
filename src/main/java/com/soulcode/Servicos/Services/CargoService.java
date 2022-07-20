package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cargo;
import com.soulcode.Servicos.Repositories.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CargoService {

    @Autowired
    CargoRepository cargoRepository;

    // primeiro serviço: mostrar todos os cargos cadastrados
    @Cacheable("cargoCache")
    public List<Cargo> mostrarTodosCargos(){
        return cargoRepository.findAll();
    }

    @Cacheable(value = "cargoCache", key = "#idCargo")

    public Cargo mostrarCargoPeloId(Integer idCargo){
        Optional<Cargo> cargo = cargoRepository.findById(idCargo);
        return cargo.orElseThrow();
    }

    @CachePut(value = "cargoCache", key = "#idCargo")
    public Cargo cadastrarCargo(Cargo cargo){
        //por precaução vamos limpar o id do Cargo
        cargo.setIdCargo(null);
        return cargoRepository.save(cargo);
    }

    @CachePut(value = "cargoCache", key = "#cargo.idCargo")
    public Cargo editarCargo(Cargo cargo){
        return cargoRepository.save(cargo);
    }

    @CacheEvict(value = "cargoCache", key = "#idCargo", allEntries = true)
    public void excluirCargo(Integer idCargo){
        cargoRepository.deleteById(idCargo);
    }
}
