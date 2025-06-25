package classes;
import java.awt.Color;
import java.util.*;

public class Boss2 extends Boss {

	public Boss2 (double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis, int vida){
		super(x, y, vx, vy, angulo, vR, listaProjeteis, vida);
		this.raio = 27.0;
		this.ponto.setvX(0.25);
		vidainicial = vida;
	}

	@Override
    public void desenha(long currentTime){

        if(this.explodindo)
		{	
			double alpha = (currentTime - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
			GameLib.drawExplosion(this.ponto.getX(), this.ponto.getY(), alpha);
		}
		else
		{
			desenhabarra();
			GameLib.setColor(Color.RED);
			GameLib.drawCircle(this.ponto.getX(), this.ponto.getY(), this.raio);
			if (vida % 2 == 0) GameLib.setColor(Color.YELLOW);
			GameLib.drawCircle(this.ponto.getX(), this.ponto.getY(), this.raio);
				
		}

    }

	@Override
    public void desenhabarra()
    {
		GameLib.setColor(Color.GREEN);
		GameLib.fillRect(GameLib.WIDTH*0.5, GameLib.HEIGHT*0.05, 300 * (this.vida/ vidainicial) , 10);
	}

	@Override
	public boolean colision(LinkedList<Projetil> projeteis, long currentTime, double c){
		
        for (Projetil projetil : projeteis) {
            double dx = projetil.getX() - ponto.getX();
            double dy = projetil.getY() - ponto.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if((dist < ((this.raio + projetil.getRaio()) * c) && this.vida > 0)) //Tira vida
			{
                this.vida--;
            }
			else if ((dist < ((this.raio + projetil.getRaio()) * c) && this.vida <= 0)) //Mata o Boss
			{
				this.explodindo = true;
                this.inicioExplosao = currentTime;
                this.fimExplosao = currentTime + 500;
                return explodindo;
			}
        }
		return explodindo;
	}

	//Tipo de tiro
	@Override
	public void dispara(long currentTime, double playerY){
		
		if(currentTime > this.proxTiro && this.ponto.getY() < playerY){
			if (Math.random() <= 0.9){
				
				this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX() +10 , this.ponto.getY() +25, Math.abs(this.ponto.getvX()), this.ponto.getvY())); //Tiro Direito
				this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY() +25, 0, Math.sin(this.angulo)*0.45*(-1.0))); //Tiro Central
				this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX() -10, this.ponto.getY() +25, Math.abs(this.ponto.getvX()) * -1, this.ponto.getvY())); //Tiro da esquerda
				this.proxTiro = (long) (currentTime + 200 + Math.random() * 500);

			}
			else
			{	
				this.listaProjeteis.add(new ProjetilBoss2(this.ponto.getX() -10, this.ponto.getY() +25, Math.abs(this.ponto.getvX()) * 0.5, this.ponto.getvY() * 0.5, currentTime)); //Tiro direito
				this.listaProjeteis.add(new ProjetilBoss2(this.ponto.getX() -10, this.ponto.getY() +25, Math.abs(this.ponto.getvX()) * -1 * 0.5, this.ponto.getvY() * 0.5, currentTime)); // Tiro esquerdo
				this.proxTiro = (long) (currentTime + 200 + Math.random() * 500);
			}
			
			

		}
	}
	
	@Override
    public boolean atualizaEstado(long deltaTime, long currentTime, double playerY, LinkedList<Projetil> projetilInimigo){
		//MOVIMENTAÇÃO
		this.ponto.setX(this.ponto.getX() + this.ponto.getvX()); // Movimentação horizontal
		if(this.ponto.getX() > GameLib.WIDTH*0.95) this.ponto.setvX(this.ponto.getvX() * -1); //Troca a direção se bater na borda direita
		if(this.ponto.getX() < GameLib.WIDTH*0.05 ) this.ponto.setvX(this.ponto.getvX() * -1); //Troca a direção se bater na borda esquerda
		if (this.ponto.getY() < GameLib.HEIGHT*0.1 ) this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime * (0.8)); //Adicionando limite que o Boss 2 pode descer
		
		this.angulo += this.vR*deltaTime;

		//Vida e tiros
		if (!explodindo) colision(projetilInimigo, currentTime, 1.05);				

		dispara(currentTime, playerY);

		if(this.explodindo && currentTime>this.fimExplosao) {
			this.explodindo = false;
			return false;
		}
		
		return true;
    }

}