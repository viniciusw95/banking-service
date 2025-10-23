package br.com.alura.service.http;


import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.resource.spi.AdministeredObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped  // significa que outro recurso vai injetá-la via CDI.
public class AgenciaService {

    @RestClient // informa que é um recurso cliente REST que vai ser injetado.
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    private List<Agencia> agencias = new ArrayList<>();
    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());
        Objects.requireNonNull(agenciaHttp);
        if (agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
            agencias.add(agencia);
        } else {
            throw new AgenciaNaoAtivaOuNaoEncontradaException();
        }
    }

    public Agencia buscarPorId(Integer id) {
        return agencias.stream().filter(agencia -> agencia.getId().equals(id)).toList().getFirst();
    }

    public void deletar(Integer id) {
        agencias.removeIf(agencia -> agencia.getId().equals(id));
    }

    public void alterar(Agencia agencia) {
        deletar(agencia.getId());
        cadastrar(agencia);
    }


}
