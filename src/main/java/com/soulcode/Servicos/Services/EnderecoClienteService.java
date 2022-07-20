package com.soulcode.Servicos.Services;

import com.soulcode.Servicos.Models.Cliente;
import com.soulcode.Servicos.Models.EnderecoCliente;
import com.soulcode.Servicos.Repositories.ClienteRepository;
import com.soulcode.Servicos.Repositories.EnderecoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EnderecoClienteService {

    @Autowired
    EnderecoClienteRepository enderecoClienteRepository;

    @Autowired
    ClienteRepository clienteRepository;

    @Cacheable("enderecoClienteCache")
    public List<EnderecoCliente> mostrarTodosEnderecosCliente(){
        return enderecoClienteRepository.findAll();
    }
    @Cacheable(value = "enderecoClienteCache", key = "#idEnderecoCli")
    public EnderecoCliente mostrarUmEnderecoPeloId(Integer idEnderecoCli){
        Optional<EnderecoCliente> endereco = enderecoClienteRepository.findById(idEnderecoCli);
        return endereco.orElseThrow();
    }

    // CADASTRO DE UM NOVO ENDEREÇO
    //regras 1 -> para cadastrar um endereço, o cliente já deve estar cadastrado no database
    //       2 -> no momento do cadastro do endereço, precisamos passar o id do cliente que é o dono desse endereço
    //       3 -> o id do endereço vai ser o mesmo id do cliente
    //       4 -> não permitir que um endereço seja salvo sem a existência do respectivo cliente
    @CachePut(value = "enredecoClienteCache", key = "#idCliente")
    public EnderecoCliente cadastrarEnderecoDoCliente (EnderecoCliente enderecoCliente, Integer idCliente) throws Exception {
        // estamos declarando um optional de cliente e atribuindo para este os dados do cliente que receberá o novo endereço
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if(cliente.isPresent()){
            enderecoCliente.setIdEndereco(idCliente);
            enderecoClienteRepository.save(enderecoCliente);

            cliente.get().setEnderecoCliente(enderecoCliente);
            clienteRepository.save(cliente.get());
            return enderecoCliente;
        }else{
            throw new Exception();
        }
    }
    @CachePut(value = "enredecoClienteCache", key = "#enderecoCliente.idEndereco")
    public EnderecoCliente editarEndereco(EnderecoCliente enderecoCliente){
        return enderecoClienteRepository.save(enderecoCliente);
    }
}
