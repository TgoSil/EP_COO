package classes;
import java.awt.Color;
import java.util.*;

public class Boss1 extends Boss{
    private double vidainicial;
    private double escudovidainicial;

    private boolean estado = true; // True = Estado 1 & False == Estado 2

    private int escudoVida;
    private int escudoRecarga; // Qtd de vida que o escudo receberá após ser RECRIADO
    private long recriarEscudoTime; // Tempo para RECRIAR o escudo após ele ser QUEBRADO

    private boolean recarregando = false; // Momento de espera entre sequencias de disparos - para o jogador ter um "descanso"
    private long recarregandoTime;

    private int levaDeDisparos;
    private double velocidadeDisparos=0.1; // Cada sequência de disparos no Estado 1 terá uma velocidade (há 2 velocidades distintas)
    private boolean alternarVelocidadeDisparos = false; // Controla a alternância entre velocidade dos disparos
    private boolean alternarAngulo = false; // Controla a alternância entre os ângulos

    public Boss1(double x, double y, double vx, double vy, double angulo, double vR, LinkedList<Projetil> listaProjeteis, int vida) {
        super(x, y, vx, vy, angulo, vR, listaProjeteis, vida);
        this.raio = 90.0;
        this.vida = vida;
        this.escudoVida = vida*3;
        this.escudoRecarga = vida*2;
        this.vidainicial = vida;
        this.escudovidainicial = escudoVida;
    }

    // Método de disparo do Estado 1
    @Override
	public void dispara(long currentTime, double playerY){

        if (recarregando && currentTime > recarregandoTime) {
            recarregando = false;
            velocidadeDisparos = alternarVelocidadeDisparos ? 0.1 : 0.3; // Alterna a velocidade para cada conjunto de leva de disparos
            alternarVelocidadeDisparos = !alternarVelocidadeDisparos;
        }

		if(!this.recarregando &&
         currentTime > this.proxTiro && this.ponto.getY() < playerY){
            
			int numDisparos = 13;
            double anguloLeque = Math.PI; // 180° de abertura do leque/área de disparos
            double deslocamento = alternarAngulo ? (Math.PI / 18) : 0; // Deslocamento de 10°
            
            levaDeDisparos += 1; // Cada conjunto de disparos por loop é uma leva de disparos
            alternarAngulo = !alternarAngulo; // Faz com que as levas alternem levemente em ângulo, para evitar pontos cegos fáceis

			for (int i = 0; i<numDisparos; i++) { // For-loop que gera os disparos
                if (i!=numDisparos-1) { // Esse if garante que não vai ter uma linha de disparos específica acima da horizontal
                    double anguloDeslocamento = (anguloLeque*i/(numDisparos - 1)) - anguloLeque/2;
                    double angulo =  Math.PI / 2 + anguloDeslocamento + deslocamento;

                    // Trigonometria para determinar as velocidades em cada eixo
                    double vx = Math.cos(angulo) * velocidadeDisparos; 
                    double vy = Math.sin(angulo) * velocidadeDisparos;


                    this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), this.ponto.getY() + raio*0.6, vx, vy));
                }
			}

            // Ao atingir a quantidade de disparos de uma leva, há um tempo para recarga (~cooldown)
            if (levaDeDisparos >= 25) {
                recarregando = true;
                recarregandoTime = currentTime + 3500;
                levaDeDisparos = 0;
            }

			this.proxTiro = (long) (currentTime + 200);

		}
	}
    
    // Método de disparo do Estado 2
    public void dispara_alternativo(long currentTime, double playerY) {
        if (recarregando && currentTime > recarregandoTime) recarregando = false;

        if(!recarregando && currentTime > this.proxTiro && this.ponto.getY() < playerY) {
            if (levaDeDisparos < 5) {
                this.listaProjeteis.add(new ProjetilInimigo(this.ponto.getX(), 
            this.ponto.getY()+this.raio*0.6, Math.cos(this.angulo)*0.45, 0.5));
                this.proxTiro = (long) (currentTime + 100);
                levaDeDisparos += 1;
            }
            
            else { // Ao atingir a quantidade de disparos de uma leva, há um tempo para recarga (~cooldown)
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
                // Desenha Estado 1
                GameLib.setColor(Color.RED);
                GameLib.drawTriangle(this.ponto.getX(), this.ponto.getY(), this.raio*0.5);
                GameLib.setColor(Color.CYAN);
                GameLib.drawCircle(this.ponto.getX(), this.ponto.getY()+this.raio*0.6, this.raio*0.125);
                if (escudoVida % 2 == 1) GameLib.setColor(Color.GREEN); // Alterna cores para gerar feedback visual de dado no escudo
                GameLib.drawSemiCircle(ponto.getX(), ponto.getY(), this.raio);
                GameLib.setColor(Color.BLUE);
		        GameLib.fillRect(GameLib.WIDTH*0.5, GameLib.HEIGHT*0.05, 300 * (this.escudoVida/ escudovidainicial) , 10);
            }
            else  //Estado 2
            {
                GameLib.setColor(Color.RED);
                GameLib.drawTriangle(this.ponto.getX(), this.ponto.getY(), this.raio*0.64);
                if (vida % 2 == 0) GameLib.setColor(Color.YELLOW); // Alterna cores para gerar feedback visual de dado no boss
                GameLib.drawCircle(this.ponto.getX(), this.ponto.getY()+this.raio*0.78, this.raio*0.16);
                GameLib.setColor(Color.GREEN);
		        GameLib.fillRect(GameLib.WIDTH*0.5, GameLib.HEIGHT*0.05, 300 * (this.vida/ vidainicial) , 10);//Vida 
			
            }
            }
        else 
        {
            double alpha = (currentTime - this.inicioExplosao) / (this.fimExplosao - this.inicioExplosao);
            GameLib.drawExplosion(this.ponto.getX(), this.ponto.getY(), alpha);
        }
    }
    

    @Override
    public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY, LinkedList<Projetil> projetilPlayer) {        
        if (!explodindo) { 
            if (estado) { // Estado 1 = Com escudo

                // Colisão ESTADO 1
                if (colision(projetilPlayer, currentTime, 1.05)) {
                    if (escudoVida <= 0) { 
                        // Quando o escudo quebrar, o Estado altenará para o Estado 2 (representado por false)
                        estado = false;
                        raio = 70.0;
                        recriarEscudoTime = currentTime + 10000; // Tempo para o escudo voltar (10s)
                        
                        // Entra em recarga, para o player ter tempo de reagir à mudança de disparos
                        recarregando = true;
                        recarregandoTime = currentTime + 2000;
                        levaDeDisparos = 0;
                    }
                    else { 
                        // Dano ocorre no ESCUDO
                        // System.out.println("Escudo: " + escudoVida); // Print para ver a vida do escudo
                        escudoVida -= 1; 
                    }
                }

                
                // Movimentação e Disparo - ESTADO 1
                
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 0.2);
                // "Animação" de entrada do Boss
                if (this.ponto.getY() < GameLib.HEIGHT*0.2) this.ponto.setY(this.ponto.getY() + this.ponto.getvY() * deltaTime * (0.8));
                // Faz o boss não sair da tela no eixo X
                if (ponto.getX() + 90 >= GameLib.WIDTH || ponto.getX() - 90 <= 0) {
                    ponto.setvX(ponto.getvX() * (-1));
                    ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 3); // Evita BUG do boss travar nas bordas
                }
                dispara(currentTime, PlayerY);
            }
            
            else { // Estado 2: Sem escudo
                
                // Colisão ESTADO 2
                if (colision(projetilPlayer, currentTime, 1.05)) {
                    if (vida <=0) {
                        // Quando a vida acabar, o boss explodirá
                        explodindo = true;
                        inicioExplosao = currentTime;
                        fimExplosao = currentTime + 500;
                    }
                    else {
                        // Dano ocorre na VIDA
                        // System.out.println("Vida: " + vida); // Print para ver a vida
                        vida -= 1;
                    }
                }

                if (currentTime > recriarEscudoTime) {
                    // Se a vida não acabar, em 10s o boss voltará para o Estado 1 (representado por true)
                    escudoVida = escudoRecarga;
                    estado = true;
                    raio = 90.0;
                }
        
                // Movimentação e Disparo - ESTADO 2
                ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 2);
                // Faz o boss não sair da tela no eixo X
                if (ponto.getX() + 90 >= GameLib.WIDTH || ponto.getX() - 90 <= 0) {
                    ponto.setvX(ponto.getvX() * (-1));
                    ponto.setX(ponto.getX() + ponto.getvX() * deltaTime * 3); // Evita BUG do boss travar nas bordas
                }
                dispara_alternativo(currentTime, PlayerY); 
            }
        }
        else {
            if (currentTime > fimExplosao) return false; // Boss explodiu, hora de desinstanciar
        }
        return true;
    }
}
