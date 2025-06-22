package classes;
import java.awt.Color;
import java.util.*;

public class Boss1 extends Boss{

    private boolean estado = true; // True = Estado 1 & False == Estado 2
    private boolean recarregando = false;
    private int escudo_vida = 750;
    private int levaDeDisparos;
    private long recriarEscudoTime;
    private long recarregandoTime;
    private double velocidadeDisparos=0.1;
    private boolean alternarVelocidadeDisparos = false;
    private boolean alternarAngulo = false; // Controla a alternância entre os ângulos

    public Boss1(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis) {
        super(x, y, vx, vy, angulo, vR, listaProjeteis);
        this.raio = 90.0;
        this.vida = 200;
    }

    @Override
	public void dispara(long currentTime, double playerY){

        if (recarregando && currentTime > recarregandoTime) {
            recarregando = false;
            velocidadeDisparos = alternarVelocidadeDisparos ? 0.1 : 0.3;
            alternarVelocidadeDisparos = !alternarVelocidadeDisparos;
        }

		if(!this.recarregando &&
         currentTime > this.proxTiro && this.ponto.getY() < playerY){
            
			int numDisparos = 13;
            double anguloLeque = Math.PI; // 180° de abertura do leque
            double deslocamento = alternarAngulo ? (Math.PI / 18) : 0;
            
            levaDeDisparos += 1;
            alternarAngulo = !alternarAngulo;

			for (int i = 0; i<numDisparos; i++) {
                if (i!=numDisparos-1) {
                    double anguloDeslocamento = -anguloLeque/2 + (anguloLeque*i/(numDisparos - 1));

                    double angulo =  Math.PI / 2 + anguloDeslocamento + deslocamento;

                    double vx = Math.cos(angulo) * velocidadeDisparos;
                    double vy = Math.sin(angulo) * velocidadeDisparos;

                    this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY() + raio*0.6, vx, vy));
                }
			}

            if (levaDeDisparos >= 25) {
                recarregando = true;
                recarregandoTime = currentTime + 3500;
                levaDeDisparos = 0;
            }
			this.proxTiro = (long) (currentTime + 200);

		}
	}
    
    public void dispara_alternativo(long currentTime, double playerY) {
        if (recarregando && currentTime > recarregandoTime) recarregando = false;

        if(!recarregando && currentTime > this.proxTiro && this.ponto.getY() < playerY) {
            if (levaDeDisparos < 5) {
                this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), 
            this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo)*0.45, 0.5));
                this.proxTiro = (long) (currentTime + 100);
                levaDeDisparos += 1;
            }
            else {
                recarregando = true;
                recarregandoTime = currentTime + 500;
                levaDeDisparos = 0;
            }
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
                        recarregando = true;
                        recarregandoTime = currentTime + 2000;
                        levaDeDisparos = 0;
                    }
                    else {
                        System.out.println("Escudo: " + escudo_vida);
                        escudo_vida -= 1; 
                    }
                }

                
                // Movimentação
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 0.2);
                if (ponto.getX() + 90 >= GameLib.WIDTH || ponto.getX() - 90 <= 0) ponto.setvX(ponto.getvX() * (-1));
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
                    escudo_vida = 300;
                    estado = true;
                    raio = 90.0;
                }
        
        
                // Movimentação
                dispara_alternativo(currentTime, PlayerY);
                if (ponto.getX() + 90 >= GameLib.WIDTH || ponto.getX() - 90 <= 0) ponto.setvX(ponto.getvX() * (-1));
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 2);
            }
        }
        else {
            if (currentTime > fimExplosao) return false;
        }

     
        return true;
    }

    


}
