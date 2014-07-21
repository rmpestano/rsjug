package com.rs.jug.functional.fragment;

import static org.jboss.arquillian.graphene.Graphene.guardHttp;

import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Menu{

    @Root
    private GrapheneElement menu;

    private WebElement menuPalestrante;

    private WebElement menuParticipante;

    private WebElement menuEvento;



    private WebElement getMenuPalestrante() {
        if (menuPalestrante == null) {
        	menuPalestrante = findItemByText("Palestrante");
        }
        return menuPalestrante;
    }

    private WebElement getMenuParticipante() {
        if (menuParticipante == null) {
        	menuParticipante = findItemByText("Participante");
        }
        return menuParticipante;
    }
    
    private WebElement getMenuEvento() {
        if (menuEvento == null) {
        	menuEvento = findItemByText("Evento");
        }
        return menuEvento;
    }

    public void gotoPalestrante() {
        guardHttp(getMenuPalestrante()).click();
    }

    public void gotoParticipante() {
        guardHttp(getMenuParticipante()).click();
    }
    
    public void gotoEvento() {
        guardHttp(getMenuEvento()).click();
    }


    private WebElement findItemByText(String text){
        return menu.findElement(By.xpath("//a[text() = '" + text +"']"));
    }


}
