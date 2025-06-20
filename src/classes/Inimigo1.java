package classes;
import java.awt.Color;

public class Inimigo1 extends Inimigos implements Entidade {

	private double raio = 9.0;

	public Inimigo1 (double x, double y, double vx, double vy, double angulo, double vR){
		super(x, y, vx, vy, angulo, vR);
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
    public boolean atualizaEstado(long deltaTime, long currentTime, double playerY){
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()*Math.cos(this.angulo) * deltaTime);
		this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime * (0.8));
		this.angulo += this.vR*deltaTime;
				
		dispara(currentTime, playerY);

		int aux = 0;
        for (Projetil projetilAux : this.listaProjeteis) {
            if(!projetilAux.atualizaEstado(deltaTime, currentTime, playerY)) aux = 1;
            projetilAux.desenha(currentTime);
        }
		if(aux==1){
			this.listaProjeteis.remove();
		}
		
		return true;
    }

}
