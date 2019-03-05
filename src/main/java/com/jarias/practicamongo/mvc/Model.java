package com.jarias.practicamongo.mvc;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.regex;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import com.jarias.practicamongo.base.Arma;
import com.jarias.practicamongo.base.Personaje;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Model {

	private MongoClient cliente;
	private MongoDatabase db;
	
	public Model() {
		conectar();
	}
	
	public void conectar() {
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
			    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));
		cliente = new MongoClient("localhost", MongoClientOptions.builder().codecRegistry(pojoCodecRegistry).build());
		db = cliente.getDatabase("juego");
	}
	
	public void desconectar() {
		cliente.close();
	}
	
	public void guardarPersonaje(Personaje personaje) {
		Document documento = new Document()
				.append("nombre", personaje.getNombre())
				.append("descripcion", personaje.getDescripcion())
				.append("vida", personaje.getVida())
				.append("ataque", personaje.getAtaque())
				.append("armas", personaje.getArmas());
		db.getCollection("personajes").insertOne(documento);
	}
	
	public void eliminarPersonaje(Personaje personaje) {
		MongoCollection<Personaje> coleccionPersonajes = db.getCollection("personajes", Personaje.class);
		coleccionPersonajes.deleteOne(eq("_id", personaje.getId()));
	}
	
	public void modificarPersonaje(Personaje personaje) {
		MongoCollection<Personaje> coleccionPersonajes = db.getCollection("personajes", Personaje.class);
		coleccionPersonajes.replaceOne(eq("_id", personaje.getId()), personaje);
	}
	
	public List<Personaje> getPersonajes(){
		MongoCollection<Personaje> coleccionPersonajes = db.getCollection("personajes", Personaje.class);
		return coleccionPersonajes.find().into(new ArrayList<Personaje>());
	}
	
	public void guardarArma(Arma arma) {
		Document documento = new Document()
				.append("nombre", arma.getNombre())
				.append("ataque", arma.getAtaque())
				.append("durabilidad", arma.getDurabilidad());
		db.getCollection("armas").insertOne(documento);
	}
	
	public void eliminarArma(Arma arma) {
		MongoCollection<Arma> coleccionArmas = db.getCollection("armas", Arma.class);
		coleccionArmas.deleteOne(eq("_id", arma.getId()));
	}
	
	public void modificarArma(Arma arma) {
		MongoCollection<Arma> coleccionArmas = db.getCollection("armas", Arma.class);
		coleccionArmas.replaceOne(eq("_id", arma.getId()), arma);
	}
	
	public List<Arma> getArmas() {
		MongoCollection<Arma> coleccionArmas = db.getCollection("armas", Arma.class);
		return coleccionArmas.find().into(new ArrayList<Arma>());
	}
	
	public List<Arma> getArmasLibres(){
		MongoCollection<Arma> coleccionArmas = db.getCollection("armas", Arma.class);
		return coleccionArmas.find(eq("personaje", null)).into(new ArrayList<Arma>());
	}
	
	public void busquedaArmas(String busqueda, DefaultListModel<Arma> mArmas) {
		if(busqueda.equals("")) {
			mArmas.removeAllElements();
			for(Arma arma : getArmas())
				mArmas.addElement(arma);
			return;
		}
		
		MongoCollection<Arma> coleccionArmas = db.getCollection("armas", Arma.class);
		List<Arma> armas = coleccionArmas.find(regex("nombre", "^" + busqueda + ".*$")).into(new ArrayList<>());
		mArmas.clear();
		for(Arma arma : armas) {
			mArmas.addElement(arma);
		}
	}
	
	public void busquedaPersonajes(String busqueda, DefaultListModel<Personaje> mPersonajes) {
		if(busqueda.equals("")) {
			mPersonajes.removeAllElements();
			for(Personaje personaje : getPersonajes())
				mPersonajes.addElement(personaje);
			return;
		}
		MongoCollection<Personaje> coleccionPersonajes = db.getCollection("personajes", Personaje.class);
		List<Personaje> personajes = coleccionPersonajes.find(regex("nombre", "^" + busqueda + ".*$")).into(new ArrayList<>());
		mPersonajes.clear();
		for(Personaje personaje : personajes) {
			mPersonajes.addElement(personaje);
		}
	}
}
