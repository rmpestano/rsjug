package org.rs.jug.bdd.palestrante;

import javax.inject.Inject;

import org.hibernate.criterion.MatchMode;
import org.jbehave.core.annotations.BeforeStory;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.rs.jug.model.Palestrante;
import com.rs.jug.service.PalestranteService;

import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PalestranteStep {

	@Inject
	PalestranteService palestranteService;

	private int palestrantesEncontrados;
	
	private Palestrante palestrantePesquisa = new Palestrante();


	@Given("palestrante de $nome")
	public void palestrantedeNome(@Named("nome") String nome) {
		palestrantePesquisa.setNome(nome);
	}

	@When("pesquiso palestrante")
	public void pesquisoPalestrante() {
		palestrantesEncontrados = palestranteService.crud().example(palestrantePesquisa,MatchMode.ANYWHERE).count();
	}

	@Then("palestrantes encontrados igual a $total")
	public void groupNameMustBe(@Named("total") int total) {
		 assertEquals(palestrantesEncontrados, total);
	}

}
