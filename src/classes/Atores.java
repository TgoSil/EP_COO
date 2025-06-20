package classes;
import java.util.*;

public abstract class Atores{
    protected Ponto2D ponto;
	protected LinkedList<Projetil> listaProjeteis;
	protected double inicioExplosao;
    protected boolean explodindo;
	protected double fimExplosao;
    protected double raio;
	protected long proxTiro;

	// construtor

	public Atores (double x, double y, double vx, double vy){
    	this.ponto = new Ponto2D(x, y, vx, vy);
    	this.listaProjeteis = new LinkedList<>();
    	this.explodindo = false;   
		this.proxTiro = 0;
	}

	public boolean getExplodindo(){
		return this.explodindo;
	}

   //metodos colisao e disparar
    public boolean colision(LinkedList<Projetil> projeteis, long currentTime, double c){
		
        for (Projetil projetil : projeteis) {
            double dx = projetil.getX() - ponto.getX();
            double dy = projetil.getY() - ponto.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if(dist < ((this.raio + projetil.getRaio()) * c)){
                
                this.explodindo = true;
                this.inicioExplosao = currentTime;
                this.fimExplosao = currentTime + 500;
                return explodindo;
            }
        }
		return explodindo;
	}
    
	public abstract void dispara(long currentTime, double limitador);
		
}


