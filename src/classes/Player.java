package classes;
import java.awt.Color;
import java.util.*;

public class Player extends Atores implements Entidade{

	public Player(double x, double y, double vx, double vy, double raio, long proxTiro){
		super(x, y, vx, vy);
		this.proxTiro = proxTiro;
		this.inicioExplosao = 0;
		this.fimExplosao = 0;
	}

	public void colision(long currentTime, LinkedList<Atores> ator) {
		for (Atores aux : ator) {
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
		ponto.setY(ponto.getY() + ponto.getvY()*delta);
	}
	public void mover_Direita(long delta) {
		ponto.setY(ponto.getX() + ponto.getvX()*delta);
	}
	public void mover_Esquerda(long delta) {
		ponto.setY(ponto.getY() - ponto.getvX()*delta);
	}
	public void mover_Baixo(long delta) {
		ponto.setY(ponto.getY() - ponto.getvY()*delta);
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
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(ponto.getX(), ponto.getY(), raio);
			}
	}

	@Override
	public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY) {
		
		if(GameLib.iskeyPressed(GameLib.KEY_UP)) mover_Cima(deltaTime);
		if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) mover_Baixo(deltaTime);
		if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) mover_Esquerda(deltaTime);
		if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) mover_Direita(deltaTime);

		if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) dispara(currentTime, 0.0);

		if(this.explodindo && currentTime>this.fimExplosao) this.explodindo = false;

		Iterator<Projetil> p = this.listaProjeteis.iterator();
		while(p.hasNext()){
			
			Projetil projetilAux = p.next();
			boolean aux = projetilAux.atualizaEstado(deltaTime, currentTime, PlayerY);
			if(!aux) this.listaProjeteis.remove(projetilAux);
		}
		return true;

	}
	
}