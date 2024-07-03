package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	private PremierLeagueDAO dao;
	private List<Match> matches;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> mappa;
	
	public Model() {
		this.dao = new PremierLeagueDAO();
		this.matches = dao.listAllMatches();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.mappa = new HashMap<>();
	}
	
	public List<Match> getMatch(){
		return this.matches;
	}
	
	public void creaGrafo(Match m) {
		grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		for (Player p: dao.listAllPlayers()) {
			mappa.put(p.playerID, p);
		}
		//aggiungoi vertici
		Graphs.addAllVertices(this.grafo, this.dao.getGiocatori(m, mappa));
		
		//aggiungo gli archi
		
		for(Adiacenza a : dao.getAdiacenze(m, mappa)) {
			if(a.getPeso() >= 0) {
				//p1 meglio di p2
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), 
							a.getP2(), a.getPeso());
				}
			} else {
				//p2 meglio di p1
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), 
							a.getP1(), (-1) * a.getPeso());
				}
			}
		}
	}

	public int getV() {
		return this.grafo.vertexSet().size();
	}
	public int getA() {
		return this.grafo.edgeSet().size();
	}
	
	public Efficienza getMigliore() {
		double max =0;
		Efficienza e = null;
		for (Player p: this.grafo.vertexSet()) {
			Set<DefaultWeightedEdge> uscenti = this.grafo.outgoingEdgesOf(p);
			Set<DefaultWeightedEdge> entranti = this.grafo.incomingEdgesOf(p);
			double diff = calcolaPeso(uscenti)-calcolaPeso(entranti);
			if (diff> max) {
				max = diff;
				e  = new Efficienza(p, diff);
			}
		}
		return e;
	}

	private double calcolaPeso(Set<DefaultWeightedEdge> archi) {
		double peso = 0;
		for (DefaultWeightedEdge e : archi) {
			peso+= this.grafo.getEdgeWeight(e);
		}
		return peso;
	}
	public Map<Integer, Integer> simula(int n){
		Simulator sim = new Simulator(grafo);
		sim.initialize(n);
		sim.run();
		return sim.getResult();
	}
	
	
	
}
