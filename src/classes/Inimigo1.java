package classes;
import java.awt.Color;
import java.util.*;

public class Inimigo1 extends Inimigos {

	public Inimigo1 (double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis){
		super(x, y, vx, vy, angulo, vR, listaProjeteis);
		this.raio = 9.0;
	}

	@Override
	public void dispara(long currentTime, double playerY){
		
		if(currentTime > this.proxTiro && this.ponto.getY() < playerY){
				
			this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY(), Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
								
			this.proxTiro = (long) (currentTime + 200 + Math.random() * 500);

		}
	}

	@Override
    public void desenha(long currentTime){

        if(this.explodindo){	
			double alpha = (currentTime - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
			GameLib.drawExplosion(this.ponto.getX(), this.ponto.getY(), alpha);
		}else{
			GameLib.setColor(Color.CYAN);
			GameLib.drawCircle(this.ponto.getX(), this.ponto.getY(), this.raio);
		}

    }

	@Override
    public boolean atualizaEstado(long deltaTime, long currentTime, double playerY, LinkedList<Projetil> projetilInimigo){
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()*Math.cos(this.angulo) * deltaTime);
		this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime * (0.8));
		this.angulo += this.vR*deltaTime;

		if (!explodindo) {
			colision(projetilInimigo, currentTime, 1.0);				
			dispara(currentTime, playerY);
		}
		if(this.explodindo && currentTime>this.fimExplosao) {
			this.explodindo = false;
			return false;
		}
		
		return true;
    }

}
