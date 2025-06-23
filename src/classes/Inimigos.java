package classes;
import java.util.*;

public abstract class Inimigos extends Atores {

	double angulo;
	double vR;
	
	public Inimigos(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis){
		super(x, y, vx, vy, listaProjeteis);
		this.angulo = angulo;
		this.vR = vR;
		}



	public abstract void dispara(long currentTime, double playerY);

	public abstract void desenha(long currentTime);

	public abstract boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY, LinkedList<Projetil> projetilInimigo);
	
}