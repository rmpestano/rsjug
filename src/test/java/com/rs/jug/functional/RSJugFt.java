package com.rs.jug.functional;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.rs.jug.crud.Crud;
import com.rs.jug.functional.fragment.Menu;
import com.rs.jug.functional.page.PalestrantePesquisaPage;
import com.rs.jug.util.DBUtils;
import com.rs.jug.view.EventoBean;

@RunWith(Arquillian.class)
public class RSJugFt {

	@Drone
	WebDriver browser;

	@ArquillianResource
	URL context;
	
	@FindBy(id="navigation")
	Menu menu;
	
	@FindBy(id="homeLink")
	WebElement home;
	
	@Page
	PalestrantePesquisaPage palestrantePage;

	@Deployment(testable = false)
	public static WebArchive createDeployment() {

		WebArchive war = ShrinkWrap.create(WebArchive.class);
		war.addPackages(true, EventoBean.class.getPackage())
				.addPackages(true, "com.rs.jug.rest")
				.addPackages(true, "com.rs.jug.model")
				.addPackages(true, "com.rs.jug.view")
				.addPackages(true, "com.rs.jug.service")
				.addClass(DBUtils.class)
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
	@InSequence(1)
	public void shouldOpenInitialPage() {
		browser.get(context.toString());
		WebElement h1 = browser.findElement(By.xpath("//div[@id='content']//h1[contains(text(),'Welcome to Forge')]"));
		assertNotNull(h1);
		assertTrue(h1.isDisplayed());
	}
	
	@Test
	@InSequence(2)
	public void shouldNavigateToPalestrante(@InitialPage PalestrantePesquisaPage page){
		WebElement h1 = browser.findElement(By.xpath("//div[@id='content']//h1[contains(text(),'Palestrante')]"));
		assertNotNull(h1);
		assertTrue(h1.isDisplayed());
	}
	
	 
	@Test
	@InSequence(3)
	public void shouldNavigateToPalestrante(){
		Graphene.goTo(PalestrantePesquisaPage.class);
		assertTrue(palestrantePage.isDisplayed());
	}
	
	@Test
	@InSequence(4)
	public void shouldSearchPalestrantePorNome(@InitialPage PalestrantePesquisaPage page){
		assertTrue(page.isDisplayed());
		page.pesquisaPorNome("Pestano");
		List<WebElement> linhas =  browser.findElements(By.xpath("//*[@id='search:palestranteBeanPageItems']//tr//span"));
		for (WebElement linha : linhas) {
			assertTrue(linha.getText().contains("Pestano"));
		}
	}
	
	@Test
	@InSequence(5)
	public void deveNavegarViaMenu(){
		Graphene.guardHttp(home).click();
		menu.gotoPalestrante();
		assertTrue(palestrantePage.isDisplayed());
	}
	
	
}
