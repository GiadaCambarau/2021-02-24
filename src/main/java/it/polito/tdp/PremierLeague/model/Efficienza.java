package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class Efficienza {
	private Player p;
	private double efficienza;
	private int team;
	public Efficienza(Player p, double efficienza) {
		super();
		this.p = p;
		this.efficienza = efficienza;
	}
	public Player getP() {
		return p;
	}
	public void setP(Player p) {
		this.p = p;
	}
	public double getEfficienza() {
		return efficienza;
	}
	public void setEfficienza(double efficienza) {
		this.efficienza = efficienza;
	}
	public int getTeam() {
		return team;
	}
	public void setTeam(int team) {
		this.team = team;
	}

	@Override
	public String toString() {
		return p + "    " + efficienza ;
	}


	

	
	

}
