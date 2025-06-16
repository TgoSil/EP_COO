import java.awt.Color;
import java.util.*;


public class Player extends Atores implements Entidade{

	public Player(double x, double y, double vx, double vy, double raio){
		super(x, y, vx, vy);
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
		if (ponto.getY() < limitador) {
			listaProjeteis.add(new Projetilplayer(ponto.getX(), ponto.getY(), ponto.getvX(), ponto.getvY()));
		}
	}

	@Override
	public void desenha(long currentTime) {
		GameLib.setColor(Color.BLUE);
		GameLib.drawPlayer(this.ponto.getX(), this.ponto.getY(), this.raio);
	}

	@Override
	public boolean atualizaEstado(long deltaTime) {
		for(Projetil aux : listaProjeteis) {
			aux.atualizaEstado(long deltaTime);


		}
	}
	
}