package com.rs.jug.service;

import static org.junit.Assert.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
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
	            .addClass(PalestranteService.class)
	            .addPackages(true,Palestrante.class.getPackage())
	            .addAsWebInfResource("web.xml", "web.xml")
	            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
	            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
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
}
