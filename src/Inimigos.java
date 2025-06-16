import java.awt.Color;
import java.util.*;

public class Inimigos extends Atores{

	double angulo;
	double vR;
	
	public Inimigos(double x, double y, double vx, double vy, double angulo, double vR){
		super(x, y, vx, vy);
		this.angulo = angulo;
		this.vR = vR;
	}
	
	@Override
	public void dispara(long currentTime, double playerY){
		
		if(currentTime > this.proxTiro && this.ponto.getY() < playerY){
				
			this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY(), Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
								
			this.proxTiro = (long) (currentTime + 200 + Math.random() * 500);

		}
		
	}
	
}

class Inimigo1 extends Inimigos implements Entidade {

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
    public boolean atualizaEstado(long deltaTime, long currentTime){
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()*Math.cos(this.angulo) * deltaTime);
		this.ponto.setY(this.ponto.getY() + this.ponto.getvY()*Math.cos(this.angulo) * deltaTime * (-1.0));
		this.angulo += this.vR*deltaTime;
				
		Iterator<Projetil> p = this.listaProjeteis.iterator();
		while(p.hasNext()){
			
			Projetil projetilAux = p.next();
			boolean aux = projetilAux.atualizaEstado(deltaTime, currentTime);
			if(!aux) this.listaProjeteis.remove(projetilAux);
		}
		return true;
    }

}

class Inimigo2 extends Inimigos implements Entidade {

	private double raio = 12.0;
	private boolean shootNow;

	public Inimigo2 (double x, double y, double vx, double vy, double angulo, double vR){
		super(x, y, vx, vy, angulo, vR);
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
    public boolean atualizaEstado(long deltaTime, long currentTime){
		this.shootNow = false;
		double previousY = this.ponto.getY();
		
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()*Math.cos(this.angulo) * deltaTime);
		this.ponto.setY(this.ponto.getY() + this.ponto.getvY()*Math.cos(this.angulo) * deltaTime * (-1.0));
		this.angulo += this.vR*deltaTime;
						
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

		dispara(currentTime, 0.0);

		Iterator<Projetil> p = this.listaProjeteis.iterator();
		while(p.hasNext()){
			
			Projetil projetilAux = p.next();
			boolean aux = projetilAux.atualizaEstado(deltaTime, currentTime);
			if(!aux) this.listaProjeteis.remove(projetilAux);
		}
		return true;

    }

}