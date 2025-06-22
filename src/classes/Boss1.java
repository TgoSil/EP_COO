package classes;
import java.awt.Color;
import java.util.*;

public class Boss1 extends Boss{

    private boolean estado = true; // True = Estado 1 & False == Estado 2
    private double escudo_vida = 10;
    private long recriarEscudoTime;
    private long recarregandoTime;

    public Boss1(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis) {
        super(x, y, vx, vy, angulo, vR, listaProjeteis);
        this.raio = 90.0;
        this.vida = 200;
    }

    @Override
	public void dispara(long currentTime, double playerY){
		
		if(currentTime > this.proxTiro && this.ponto.getY() < playerY){
				
			this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
            this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo), Math.sin(this.angulo)*0.45*(-1.0)));
            // this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
								
			this.proxTiro = (long) (currentTime + 200);

		}
	}

    @Override
    public void dispara_alternativo(long currentTime, double playerY) {
        if(currentTime > this.proxTiro && this.ponto.getY() < playerY) {
            this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo)*0.45, Math.sin(this.angulo)*0.45*(-1.0)));
								
			this.proxTiro = (long) (currentTime + 50);
        }
    }

    @Override
    public void desenha(long currentTime) {
       if (!explodindo) {
            if (estado) {
                GameLib.setColor(Color.RED);
                GameLib.drawTriangle(this.ponto.getX(), this.ponto.getY(), this.raio*0.5);
                GameLib.setColor(Color.CYAN);
                GameLib.drawCircle(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, this.raio*0.125);
                if (escudo_vida % 2 == 1) GameLib.setColor(Color.GREEN);
                GameLib.drawSemiCircle(ponto.getX(), ponto.getY(), this.raio);
            }
            else {
                GameLib.setColor(Color.RED);
                GameLib.drawTriangle(this.ponto.getX(), this.ponto.getY(), this.raio*0.64);
                if (vida % 2 == 0) GameLib.setColor(Color.YELLOW);
                GameLib.drawCircle(this.ponto.getX(), this.ponto.getY()+this.raio*0.78, this.raio*0.16);
            }
        }
        else {
            double alpha = (currentTime - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
            GameLib.drawExplosion(this.ponto.getX(), this.ponto.getY(), alpha);
        }
    }

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY, LinkedList<Projetil> projetilPlayer) {

        
        if (!explodindo) {
            if (estado) {
                if (colision(projetilPlayer, currentTime, 1.05)) {
                    if (escudo_vida <= 0) {
                        estado = false;
                        raio = 70.0;
                        recriarEscudoTime = currentTime + 10000;
                    }
                    else {
                        System.out.println("Escudo: " + escudo_vida);
                        escudo_vida -= 1; 
                    }
                }

                
                // Movimentação
                if (ponto.getX() + raio >= GameLib.WIDTH || ponto.getX() - raio <= 0) ponto.setvX(ponto.getvX() * (-1));
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 0.1);
                dispara(currentTime, PlayerY);

            }
            else {
                if (colision(projetilPlayer, currentTime, 1.05)) {
                    if (vida <=0) {
                        explodindo = true;
                        inicioExplosao = currentTime;
                        fimExplosao = currentTime + 500;
                    }
                    else {
                        System.out.println("Vida: " + vida);
                        vida -= 1;
                    }
                }

                if (currentTime > recriarEscudoTime) {
                    escudo_vida = 100;
                    estado = true;
                    raio = 90.0;
                }
        
        
                // Movimentação
                dispara_alternativo(currentTime, PlayerY);
                if (ponto.getX() + raio >= GameLib.WIDTH || ponto.getX() - raio <= 0) ponto.setvX(ponto.getvX() * (-1));
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 3);
            }
        }
        else {
            if (currentTime > fimExplosao) return false;
        }

     
        return true;
    }

    


}
