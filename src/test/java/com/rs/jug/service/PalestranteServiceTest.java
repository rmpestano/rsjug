package com.rs.jug.service;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.persistence.Cleanup;
import org.jboss.arquillian.persistence.CleanupStrategy;
import org.jboss.arquillian.persistence.ShouldMatchDataSet;
import org.jboss.arquillian.persistence.TestExecutionPhase;
import org.jboss.arquillian.persistence.UsingDataSet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
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

@RunWith(Arquillian.class)
public class PalestranteServiceTest
{

   @Inject
   private PalestranteService palestranteService;

   @Deployment
   public static Archive<?> createDeployment()
   {
	   WebArchive archive = ShrinkWrap.create(WebArchive.class)
	            .addClass(PalestranteService.class)
	            .addClass(Crud.class) 
	            .addPackages(true,Palestrante.class.getPackage())
	            .addAsWebInfResource("web.xml", "web.xml")
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
}
