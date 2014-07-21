package com.rs.jug.functional.page;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Location(value="palestrante/search.faces")
public class PalestrantePesquisaPage {
	
	@FindBy(xpath="//div[@id='content']//h1[contains(text(),'Palestrante')]")
	private WebElement h1;
				 		
	@FindBy(xpath="//*[@id='search:palestranteBeanExampleNome']")
	private WebElement inputNomesearch;
	
	@FindBy(xpath="//*[@id='search']/span/span/a[1]")
	private WebElement btsearch;
	
	public boolean isDisplayed(){
		return h1 != null && h1.isDisplayed();
	}
	
	public void pesquisaPorNome(String nome){
		inputNomesearch.clear();
		inputNomesearch.sendKeys(nome);
		Graphene.guardHttp(btsearch).click();
	}
}
