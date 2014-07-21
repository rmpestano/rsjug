package org.rs.jug.bdd.palestrante;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.MavenResolverSystem;
import org.junit.runner.RunWith;
import org.rs.jug.bdd.BaseBdd;
import org.rs.jug.bdd.Steps;

import com.rs.jug.crud.Crud;
import com.rs.jug.model.Palestrante;
import com.rs.jug.rest.PalestranteEndpoint;
import com.rs.jug.service.PalestranteService;
import com.rs.jug.util.DBUtils;

@Steps(PalestranteStep.class)
@RunWith(Arquillian.class)
public class PalestranteBdd extends BaseBdd{
	
	   @Deployment
	   public static Archive<?> createDeployment()
	   {
		   WebArchive archive = ShrinkWrap.create(WebArchive.class)
		            .addClass(PalestranteService.class)
		            .addClass(Crud.class) 
		            .addPackages(true,BaseBdd.class.getPackage())
		            .addClass(DBUtils.class)
		            .addClass(PalestranteEndpoint.class)
		            .addPackages(true,Palestrante.class.getPackage())
		            .addAsWebInfResource(new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
		            .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
		            .addAsResource("org/rs/jug/bdd/palestrante/palestrante_bdd.story")
		            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		   		
		   MavenResolverSystem resolver = Maven.resolver();
	        archive.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("com.google.guava:guava:11.0.1").withoutTransitivity().asFile());
	        archive.addAsLibraries(resolver.loadPomFromFile("pom.xml").resolve("org.jbehave:jbehave-core:3.7.5").withTransitivity().asFile());
		   System.out.println(archive.toString(true));
	      return archive;
	   }

}
