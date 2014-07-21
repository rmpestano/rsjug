package com.rs.jug.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rs.jug.crud.Crud;
import com.rs.jug.model.Palestrante;
import com.rs.jug.rest.PalestranteEndpoint;
import com.rs.jug.service.PalestranteService;

@RunWith(Arquillian.class)
public class PalestranteIt
{

   @Inject
   private PalestranteService palestranteService;

   @Deployment
   public static Archive<?> createDeployment()
   {
	   WebArchive archive = ShrinkWrap.create(WebArchive.class)
	            .addClass(PalestranteService.class)
	            .addClass(Crud.class) 
	            .addClass(PalestranteEndpoint.class)
	            .addPackages(true,Palestrante.class.getPackage())
	            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
	            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
	            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
	   
	   MavenResolverSystem resolver = Maven.resolver();

	   System.out.println(archive.toString(true));
      return archive;
   }

   @Test
   public void should_be_deployed()
   {
      assertNotNull(palestranteService);
   }
   
   @Test
   @InSequence(1)
   public void shouldInsertPalestrante(){
       Palestrante p = new Palestrante();
       p.setNome("pestano");
       palestranteService.store(p);
       assertNotNull(p);
       assertNotNull(p.getId());
   }

   //testes dependentes - arquillian persistence to rescue
   @Test
   @InSequence(2)
   public void shouldRemovePalestrante(){
       Palestrante p = new Palestrante();
       p.setNome("pestano");
       Palestrante palestranteToRemove = palestranteService.find(p);
       assertNotNull(palestranteToRemove);
       assertNotEquals(p,palestranteToRemove);
       palestranteService.remove(palestranteToRemove);
       Palestrante removedPalestrante = palestranteService.find(p);
       assertTrue(removedPalestrante == null);
   }
   
   @Test
   @InSequence(3)
   @Cleanup(phase=TestExecutionPhase.BEFORE)
   public void shouldInsertPalestranteWithDbUnit(){
	   Palestrante p = new Palestrante();
       p.setNome("pestano");
       
       int numPalestrantes = palestranteService.crud().example(p).count();
       assertEquals(numPalestrantes,0);
       
       palestranteService.store(p);
       
       numPalestrantes = palestranteService.crud().example(p).count();
       assertEquals(numPalestrantes,1);
   }
   
   @Test
   @InSequence(4)
   @Cleanup(phase=TestExecutionPhase.BEFORE,strategy=CleanupStrategy.USED_TABLES_ONLY)
   @UsingDataSet("datasets/palestrante.yml")
   public void shouldRemovePalestranteWithDbUnit(){
	   Palestrante p = new Palestrante();
       p.setNome("pestano");
       
       int numPalestrantes = palestranteService.crud().example(p).count();
       assertEquals(numPalestrantes,1);
       p.setId(1L);
       palestranteService.remove(p);
       
       numPalestrantes = palestranteService.crud().example(p).count();
       assertEquals(numPalestrantes,0);
   }
   
   @Test
   @InSequence(5)
   @Cleanup(phase=TestExecutionPhase.BEFORE,strategy=CleanupStrategy.USED_TABLES_ONLY)
   @UsingDataSet("palestrante.yml")
   @ShouldMatchDataSet("palestrante_vazio.yml")
   public void shouldRemovePalestranteWithDbUnit2(){
	   Palestrante p = new Palestrante();
       p.setNome("pestano");
       p.setId(1L);
       palestranteService.remove(p);
   }
   
   @RunAsClient
   @Test
   public void shouldCallEndPoint(@ArquillianResource URL url){
	   ClientRequest request = new ClientRequest(url + "rest/palestrantes/teste/");
       request.accept(MediaType.APPLICATION_JSON);
       ClientResponse<String> response;
       try {
           response = request.get(String.class);
           assertEquals(response.getStatus(), 200);
       }catch(Exception e){
    	   fail(e.getMessage());
       }
       
   }
	   
}
