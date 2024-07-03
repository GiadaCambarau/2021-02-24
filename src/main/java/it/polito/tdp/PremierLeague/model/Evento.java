package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class Evento implements Comparable<Evento> {

	public enum EventType{
		goal,
		infortunio,
		espulsione
	}
	
	private int tempo;
	private int team;
	private EventType type;
	public Evento(int tempo, int team, EventType type) {
		super();
		this.tempo = tempo;
		this.team = team;
		this.type = type;
	}
	public int getTempo() {
		return tempo;
	}
	public void setTempo(int tempo) {
		this.tempo = tempo;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	@Override
	public int hashCode() {
		return Objects.hash(team, tempo, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Evento other = (Evento) obj;
		return team == other.team && tempo == other.tempo && type == other.type;
	}
	@Override
	public int compareTo(Evento o) {
		return this.tempo-o.tempo;
	}
	
	
}
