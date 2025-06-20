package classes;
import java.awt.Color;
import java.util.*;

public class Player extends Atores implements Entidade{

	private Boolean invulneravel = false;
	private double inicioInvulneravel = 0;
	private double fimInvulneravel = 0;
	
	public Player(double x, double y, double vx, double vy, double raio, long proxTiro){
		super(x, y, vx, vy);
		this.proxTiro = proxTiro;
		this.inicioExplosao = 0;
		this.fimExplosao = 0;	
		this.raio = raio;
	}

	public double getY(){
		return this.ponto.getY();
	}

	public boolean getExplodindo(){
		return this.explodindo;
	}

	public boolean getInvulneravel(){
		return this.invulneravel;
	}

	//Colisões com atores
	public void colision(long currentTime, LinkedList<Inimigos> ator) {
		for (Inimigos aux : ator) {
			double dx = aux.ponto.getX() - this.ponto.getX();
			double dy = aux.ponto.getY() - this.ponto.getY();
			double dist = Math.sqrt(dx*dx + dy*dy);
			
			if (dist < (aux.raio + this.raio)) {
				this.explodindo = true;
				this.inicioExplosao = currentTime;
				this.fimExplosao = currentTime + 500;
				return;
			}
			
			if (colision(aux.listaProjeteis, currentTime, 0.8)) return;
		}
		
	}

	public void mover_Cima(long delta) {
		ponto.setY(ponto.getY() - ponto.getvY()*delta);
	}
	public void mover_Direita(long delta) {
		ponto.setX(ponto.getX() + ponto.getvX()*delta);
	}
	public void mover_Esquerda(long delta) {
		ponto.setX(ponto.getX() - ponto.getvX()*delta);
	}
	public void mover_Baixo(long delta) {
		ponto.setY(ponto.getY() + ponto.getvY()*delta);
	}

	@Override
	public void dispara(long currentTime, double limitador) {
		
		if(currentTime > this.proxTiro){				
			this.listaProjeteis.add(new Projetilplayer(this.ponto.getX(), this.ponto.getY()-2*this.raio, 0.0, -1.0));
			this.proxTiro = currentTime + 100;
		}
	}

	@Override
	public void desenha(long currentTime) {
			if(explodindo){
				double alpha = (currentTime - inicioExplosao) / (fimExplosao - inicioExplosao);
				GameLib.drawExplosion(ponto.getX(), ponto.getY(), alpha);
			}
			else{
				if (!invulneravel) GameLib.setColor(Color.BLUE);
				else GameLib.setColor(Color.GREEN);
				GameLib.drawPlayer(ponto.getX(), ponto.getY(), raio);
			}
	}

	@Override
	public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY) {
		
		if (!explodindo) {
			if(GameLib.iskeyPressed(GameLib.KEY_UP) && ponto.getY() > GameLib.HEIGHT*0.06) mover_Cima(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN) && ponto.getY() < GameLib.HEIGHT*0.975) mover_Baixo(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT) && ponto.getX() > GameLib.WIDTH*0.05) mover_Esquerda(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT) && ponto.getX() < GameLib.WIDTH*0.95) mover_Direita(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) dispara(currentTime, 0.0);
		}
		
		if(this.explodindo && currentTime>this.fimExplosao) {
			this.explodindo = false;
			this.invulneravel = true;
			this.inicioInvulneravel = currentTime;
			this.fimInvulneravel = currentTime + 1000;
			ponto.setX(GameLib.WIDTH/2);	
			ponto.setY(GameLib.HEIGHT*0.9);
		}

		if (this.invulneravel && currentTime>this.fimInvulneravel) {
			this.invulneravel = false;
		}

		int aux = 0;
        for (Projetil projetilAux : this.listaProjeteis) {
            if(!projetilAux.atualizaEstado(deltaTime, currentTime, PlayerY)) aux = 1;
            projetilAux.desenha(currentTime);
        }
		if(aux==1){
			this.listaProjeteis.remove();
		}

		return true;

	}
	
}