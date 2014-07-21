package com.rs.jug.util;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import com.rs.jug.crud.Crud;
import com.rs.jug.model.Palestrante;
import com.rs.jug.service.PalestranteService;


@Singleton
@Startup
public class DBUtils {
	
	@Inject
	PalestranteService palestranteService;
	
	@PostConstruct
	public void intBD(){
		criaDatasetPalestrante();
	}

	private void criaDatasetPalestrante() {
		Palestrante palestrante = new Palestrante();
		palestrante.setNome("Pestano");
		palestranteService.store(palestrante);
		Palestrante palestrante2 = new Palestrante();
		palestrante2.setNome("Dionatan");
		palestranteService.store(palestrante2);
		
	}

}
