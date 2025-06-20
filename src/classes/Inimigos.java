package classes;
import java.util.*;

public abstract class Inimigos extends Atores {

	double angulo;
	double vR;
	
	public Inimigos(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis){
		super(x, y, vx, vy);
		this.angulo = angulo;
		this.vR = vR;
		this.listaProjeteis = listaProjeteis;
	}
	
	@Override
	public void dispara(long currentTime, double playerY){
		
		if(currentTime > this.proxTiro && this.ponto.getY() < playerY){
				
			this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY(), Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
								
			this.proxTiro = (long) (currentTime + 200 + Math.random() * 500);

		}
	}

	public abstract void desenha(long currentTime);

	public abstract boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY);
	
}