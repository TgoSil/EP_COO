package classes;
import java.awt.Color;
import java.util.*;

public class Player extends Atores{

	private Boolean invulneravel = false;
	private double fimInvulneravel = 0;
	private int vida;
	
	/*atributos para os buffs */
	private boolean tiroRapidoAtivo = false;
	private long fimTiroRapido = 0;

	public Player(double x, double y, double vx, double vy, double raio, long proxTiro, int vida, LinkedList<Projetil> listaProjeteis){
		super(x, y, vx, vy, listaProjeteis);
		this.proxTiro = proxTiro;
		this.inicioExplosao = 0;
		this.fimExplosao = 0;	
		this.raio = raio;
		this.vida = vida;
	}

	public double getX(){
		return this.ponto.getX();
	}
	
	public double getY(){
		return this.ponto.getY();
	}
	
	public double getVida(){
		return this.vida;
	}

	public boolean getInvulneravel(){
		return this.invulneravel;
	}

	public LinkedList<Projetil> getProjeteis(){
		return this.listaProjeteis;
	}

	public void colisionAtores(long currentTime, Atores ator) {
			double dx = ator.ponto.getX() - this.ponto.getX();
			double dy = ator.ponto.getY() - this.ponto.getY();
			double dist = Math.sqrt(dx*dx + dy*dy);
			
			if (dist < (ator.raio + this.raio)) {
				this.explodindo = true;
				this.inicioExplosao = currentTime;
				this.fimExplosao = currentTime + 500;
				return;
			}
		
		}

	
	public void mover_Cima(long delta) {
		ponto.setY(ponto.getY() - ponto.getvY()*delta);
	}
	public void mover_Direita(long delta) {
		ponto.setX(ponto.getX() + ponto.getvX()*delta);
	}
	public void mover_Esquerda(long delta) {
		ponto.setX(ponto.getX() - ponto.getvX()*delta);
	}
	public void mover_Baixo(long delta) {
		ponto.setY(ponto.getY() + ponto.getvY()*delta);
	}

	@Override
	public void dispara(long currentTime, double limitador) {
		long cooldown = tiroRapidoAtivo ? 40 : 100; /*adicionei essa parte para atirar mais rapido, quando tem o buff*/
		if(currentTime > this.proxTiro){				
			this.listaProjeteis.add(new Projetilplayer(this.ponto.getX(), this.ponto.getY()-2*this.raio, 0.0, -1.0));
			this.proxTiro = currentTime + cooldown;
		}
	}

	public void desenha(long currentTime) {
			if(explodindo){
				double alpha = (currentTime - inicioExplosao) / (fimExplosao - inicioExplosao);
				GameLib.drawExplosion(ponto.getX(), ponto.getY(), alpha);
			}
			else{
				if (!invulneravel) GameLib.setColor(Color.BLUE);
				else GameLib.setColor(Color.GREEN);
				GameLib.drawPlayer(ponto.getX(), ponto.getY(), raio);
			}
	}

	public boolean atualizaEstado(long deltaTime, long currentTime, LinkedList<Inimigos> inimigos, LinkedList<Projetil> projeteis_inimigos, Boss boss, LinkedList<Projetil> projeteis_boss) {

		if (!explodindo) {
			if(GameLib.iskeyPressed(GameLib.KEY_UP) && ponto.getY() > GameLib.HEIGHT*0.06) mover_Cima(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN) && ponto.getY() < GameLib.HEIGHT*0.975) mover_Baixo(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT) && ponto.getX() > GameLib.WIDTH*0.05) mover_Esquerda(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT) && ponto.getX() < GameLib.WIDTH*0.95) mover_Direita(deltaTime);
			if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) dispara(currentTime, 0.0);


			if (!invulneravel) {
				if (boss != null) colisionAtores(currentTime, boss);
				for (Inimigos ini : inimigos) {
					colisionAtores(currentTime, ini);
				}
				colision(projeteis_boss, currentTime, 0.8);
				colision(projeteis_inimigos, currentTime, 0.8);
			}
		}
		
		if(this.explodindo && currentTime>this.fimExplosao) {
			this.vida--;
			this.explodindo = false;
			this.invulneravel = true;
			this.fimInvulneravel = currentTime + 2000;
			ponto.setX(GameLib.WIDTH/2);	
			ponto.setY(GameLib.HEIGHT*0.9);
		}

		if (this.invulneravel && currentTime>this.fimInvulneravel) {
			this.invulneravel = false;
		}

		int aux = 0;

        for (Projetil projetilAux : this.listaProjeteis) {
            if(!projetilAux.atualizaEstado(deltaTime, currentTime, boss)) aux = 1;
			else projetilAux.desenha(currentTime);
        }

		if(aux==1){
			this.listaProjeteis.remove();
		}

		/*para atirar rapido*/
		if (tiroRapidoAtivo && currentTime > fimTiroRapido) {
    		tiroRapidoAtivo = false;
		}

		return true;
	}

		/*adicionar método atirarrapido */
	public void ativarTiroRapido(long currentTime, long duracao) {
    	this.tiroRapidoAtivo = true;
    	this.fimTiroRapido = currentTime + duracao;
	}

	/*adicionar método setInvulneravel*/
	public void setInvulneravel(long currentTime, long duracao) {
    	this.invulneravel = true;
    	this.fimInvulneravel = currentTime + duracao;
	}

}