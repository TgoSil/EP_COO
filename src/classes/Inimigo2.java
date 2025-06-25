package classes;
import java.awt.Color;
import java.util.*;

public class Inimigo2 extends Inimigos {

	private boolean shootNow;

	public Inimigo2 (double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis){
		super(x, y, vx, vy, angulo, vR, listaProjeteis);
		this.raio = 12.0;
	}

	@Override
	public void dispara(long currentTime, double playerY){

		if(this.shootNow){

			double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
			
			for(int k = 0; k < 3; k++){				
				
				double a = angles[k] + Math.random() * Math.PI/6 - Math.PI/12;
				double vx = Math.cos(a);
				double vy = Math.sin(a);
				
				this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY(), vx*0.30, vy*0.30));
				
			}
		}
	}

	@Override
    public void desenha(long currentTime){

        if(this.explodindo){
			double alpha = (currentTime - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
			GameLib.drawExplosion(this.ponto.getX(), this.ponto.getY(), alpha);
		}else{
			GameLib.setColor(Color.MAGENTA);
			GameLib.drawDiamond(this.ponto.getX(), this.ponto.getY(), this.raio);
		}
    }

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY, LinkedList<Projetil> projetilInimigo){
		this.shootNow = false;
		double previousY = this.ponto.getY();
		
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()*Math.cos(this.angulo) * deltaTime);
		this.ponto.setY(this.ponto.getY() + this.ponto.getvY()*Math.sin(this.angulo) * deltaTime * (-1.0));
		this.angulo += this.vR*deltaTime;
						
		if (!explodindo) colision(projetilInimigo, currentTime, 1.0);				
						
		double threshold = GameLib.HEIGHT * 0.30;
						
		if(previousY < threshold && this.ponto.getY() >= threshold) {
							
			if(this.ponto.getX() < GameLib.WIDTH / 2) this.vR = 0.003;
			else this.vR = -0.003;
		}
						
		if(this.vR > 0 && Math.abs(this.angulo - 3 * Math.PI) < 0.05){
							
			this.vR = 0.0;
			this.angulo = 3 * Math.PI;
			this.shootNow = true;
		}
						
		if(this.vR < 0 && Math.abs(this.angulo) < 0.05){
							
			this.vR = 0.0;
			this.angulo = 0.0;
			this.shootNow = true;
		}

		if (!explodindo) dispara(currentTime, 0.0);

		if(this.explodindo && currentTime>this.fimExplosao) {
			this.explodindo = false;
			return false;
		}

		return true;

    }
}
