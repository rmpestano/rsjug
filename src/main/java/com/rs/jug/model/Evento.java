package com.rs.jug.model;

import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Version;
import java.lang.Override;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.rs.jug.model.Palestrante;
import javax.persistence.ManyToOne;
import com.rs.jug.model.Participante;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;

@Entity
public class Evento implements Serializable
{

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", updatable = false, nullable = false)
   private Long id = null;
   @Version
   @Column(name = "version")
   private int version = 0;

   @Column
   private String nome;

   @Temporal(TemporalType.DATE)
   private Date Data;

   @ManyToOne
   private Palestrante Palestrante;

   @OneToMany
   private Set<Participante> Participantes = new HashSet<Participante>();

   public Long getId()
   {
      return this.id;
   }

   public void setId(final Long id)
   {
      this.id = id;
   }

   public int getVersion()
   {
      return this.version;
   }

   public void setVersion(final int version)
   {
      this.version = version;
   }

   @Override
   public boolean equals(Object that)
   {
      if (this == that)
      {
         return true;
      }
      if (that == null)
      {
         return false;
      }
      if (getClass() != that.getClass())
      {
         return false;
      }
      if (id != null)
      {
         return id.equals(((Evento) that).id);
      }
      return super.equals(that);
   }

   @Override
   public int hashCode()
   {
      if (id != null)
      {
         return id.hashCode();
      }
      return super.hashCode();
   }

   public String getNome()
   {
      return this.nome;
   }

   public void setNome(final String nome)
   {
      this.nome = nome;
   }

   public Date getData()
   {
      return this.Data;
   }

   public void setData(final Date Data)
   {
      this.Data = Data;
   }

   @Override
   public String toString()
   {
      String result = getClass().getSimpleName() + " ";
      if (nome != null && !nome.trim().isEmpty())
         result += "nome: " + nome;
      return result;
   }

   public Palestrante getPalestrante()
   {
      return this.Palestrante;
   }

   public void setPalestrante(final Palestrante Palestrante)
   {
      this.Palestrante = Palestrante;
   }

   public Set<Participante> getParticipantes()
   {
      return this.Participantes;
   }

   public void setParticipantes(final Set<Participante> Participantes)
   {
      this.Participantes = Participantes;
   }
}