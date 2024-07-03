package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;
import it.polito.tdp.PremierLeague.model.Evento.EventType;

public class Simulator {
	//parametri input
	private int nAzioni;
	private List<Player> giocatori;
	private List<Player> team1; //?
	private List<Player> team2; //?
	private int g1;
	private int g2;
	private PremierLeagueDAO dao;
	private Model model;
	
	
	private Graph<Player, DefaultWeightedEdge> grafo;
	
	private Map<Integer, List<Player>> mappaGiocatoriTeam;
	private Map<Integer, Integer> mappaRisultato; //mappa goal per team
	private Map<Integer, Integer> mappaGiocatoriEspulsi;
	private PriorityQueue<Evento> queue;
	
	public Simulator(Graph<Player, DefaultWeightedEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void initialize(int n) {
		this.model = new Model();
		 this.nAzioni = n;
        giocatori = new ArrayList<>(this.grafo.vertexSet());
        this.dao = new PremierLeagueDAO();
        this.mappaGiocatoriTeam = new HashMap<>();
        this.mappaRisultato = new HashMap<>();
        this.mappaGiocatoriEspulsi = new HashMap<>();
        this.queue = new PriorityQueue<>();
		//creo la mappa con i giocatori divisi in spadre
		for (Player p: giocatori) {
			int teamID = dao.getTeam(p.playerID);
			mappaGiocatoriTeam.putIfAbsent(teamID, new ArrayList<Player>());
			mappaGiocatoriTeam.get(teamID).add(p);
		}
		
		//popolo la mappa risultato
		for (Integer i: mappaGiocatoriTeam.keySet()) {
			mappaRisultato.put(i, 0);
			mappaGiocatoriEspulsi.put(i, 0);
		}
		
		//popolo la coda
		for (int i=0; i<nAzioni; i++) {
			double random = Math.random();
			if (random<0.5) {
				queue.add(new Evento(i, scegliTeamGoal(), EventType.goal));
			}else if (random<0.8) {
				queue.add(new Evento(i, scegliTeamEspulsione(), EventType.espulsione));
			}else {
				queue.add(new Evento(i, scegliTeamInfortunio(), EventType.infortunio));
			}
			nAzioni--;
		}
	}

	private int scegliTeamInfortunio() {
		int team = -1;
		double random  = Math.random();
		List<Integer> teams = new ArrayList<>(mappaGiocatoriTeam.keySet());
	    int team1 = teams.get(0);
	    int team2 = teams.get(1);
		
		if (random<0.5) {
			team = team1;
		}else {
			team = team2;
		}
		return team;
	}

	private int scegliTeamEspulsione() {
		Player best = model.getMigliore().getP();
		int team =-1;
		double random = Math.random();
		if (random<0.6) {
			for (Integer i: mappaGiocatoriTeam.keySet()) {
				if (mappaGiocatoriTeam.get(i).contains(best)) {
					team =i;
				}
			}
		}else {
			for (Integer i: mappaGiocatoriTeam.keySet()) {
				if (!mappaGiocatoriTeam.get(i).contains(best)) {
					team =i;
				}
			}
		}
		return team;
	}

	private int scegliTeamGoal() {
		Player best = model.getMigliore().getP();
		int team =-1;
		List<Integer> teams = new ArrayList<>(mappaGiocatoriTeam.keySet());
		int team1 = teams.get(0);
		int team2 = teams.get(1);
		if (contaGiocatori(team1) == contaGiocatori(team2)) {
			for (Integer i: mappaGiocatoriTeam.keySet()) {
				if (mappaGiocatoriTeam.get(i).contains(best)) {
					team =i;
				}
			}
		}else {
			if ( contaGiocatori(team1)>contaGiocatori(team2)) {
				team = team1;
			}else {
				team = team2;
			}
		}
		
		
		return team;
	}
	private int contaGiocatori(int team) {
		
		return 11- mappaGiocatoriEspulsi.get(team);
	}

	public void run() {
		while (!queue.isEmpty()) {
			Evento e = queue.poll();
			processa(e);
		}
	}

	private void processa(Evento e) {
		int tempo = e.getTeam();
		int team = e.getTeam();
		EventType type = e .getType();
		switch(type) {
		case goal:
			mappaRisultato.put(team, mappaRisultato.get(team)+1);
			break;
		case espulsione:
			mappaGiocatoriEspulsi.put(team, mappaGiocatoriEspulsi.get(team)+1);
			break;
		case infortunio:
			double random = Math.random();
			if (random<0.5) {
				nAzioni+= 2;
			}else {
				nAzioni+= 3;
			}
		}
		
		 
	}
	public Map<Integer, Integer> getResult(){
		return this.mappaRisultato;
	}
	
	
	
	

}
