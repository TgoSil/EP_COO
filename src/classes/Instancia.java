package classes;

public class Instancia {
	private String instancia;
	private int tipo;
	private int vida;
	private long tempo;
	private double x;
	private double y;
	
	public Instancia(String instancia, int tipo, int vida, long tempo, double x, double y){
		this.instancia = instancia;
		this.tipo = tipo;
		this.vida = vida;
		this.tempo = tempo;
		this.x = x;
		this.y = y;
	}
	
	public String getInstancia(){
		return this.instancia;
	}
	
	public int getTipo(){
		return this.tipo;
	}
	
	public int getVida(){
		return this.vida;
	}
	
	public long getTempo(){
		return this.tempo;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
}