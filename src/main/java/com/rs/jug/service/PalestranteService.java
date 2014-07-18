package com.rs.jug.service;

import com.rs.jug.crud.Crud;
import com.rs.jug.model.Palestrante;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class PalestranteService
{

   @Inject
   protected Crud<Palestrante> palestranteCrud;

   public Palestrante store(Palestrante entity)
   {
      palestranteCrud.saveOrUpdate(entity);
      return entity;
   }

   public void remove(Palestrante entity)
   {
      palestranteCrud.delete(entity);
   }

   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public Palestrante find(Palestrante entity)
   {
      return palestranteCrud.example(entity).find();
   }

   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public List<Palestrante> list(Palestrante entity)
   {
      return palestranteCrud.example(entity).list();
   }

   @TransactionAttribute(TransactionAttributeType.SUPPORTS)
   public Crud crud()
   {
      return palestranteCrud;
   }
}
