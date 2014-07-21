package com.rs.jug.functional;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.rs.jug.crud.Crud;
import com.rs.jug.view.EventoBean;

@RunWith(Arquillian.class)
public class RSJugFt {

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL context;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {

		WebArchive war = ShrinkWrap.create(WebArchive.class);
		war.addPackages(true, EventoBean.class.getPackage())
				.addPackages(true, "com.rs.jug.rest")
				.addPackages(true, "com.rs.jug.model")
				.addPackages(true, "com.rs.jug.view")
				.addPackages(true, "com.rs.jug.service")
				.addClass(Crud.class)
				.addAsWebInfResource(
						new File("src/main/webapp/WEB-INF/web.xml"), "web.xml")
				.addAsWebInfResource(new File("src/main/webapp/WEB-INF","faces-config.xml"), "faces-config.xml")		
				.addAsResource("META-INF/persistence.xml",
						"META-INF/persistence.xml")
				.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
		war.merge(ShrinkWrap.create(GenericArchive.class).as(ExplodedImporter.class).importDirectory("src/main/webapp").as(GenericArchive.class), "/", Filters.include(".*\\.(xhtml|html|css|js|png|ico)$"));
		return war;

	}

	@Test
	public void shouldOpenInitialPage() {
		browser.get(context.toString());
	}

}
