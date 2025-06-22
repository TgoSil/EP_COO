import classes.*;
import java.util.*;

public class Main {
/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */

public static void busyWait(long time)
    {
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
public static void main(String [] args)
{
	/* Indica que o jogo está em execução */

		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		long delta;
		long currentTime = System.currentTimeMillis();

		/*declaração das variáveis player */
		// coordenada x, coordenada y, velocidade no eixo x, velocidade no eixo y, raio do player, tempo do próximo tiro.
		LinkedList<Projetil> playerProjetil = new LinkedList();
		Player player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, currentTime, playerProjetil); 
	
		/*declaracao da  lista de inimigos e projetil*/
		LinkedList<Inimigos> inimigos = new LinkedList();
		LinkedList<Projetil> enemyProjetil = new LinkedList();
		
		/*declaração e inicialização das estrelas */
		ArrayList<Estrela> estrelaPlano1 = new ArrayList<>();
		ArrayList<Estrela> estrelaPlano2 = new ArrayList<>();

		/*declaração dos powerups*/
		LinkedList<PowerUp> powerUps = new LinkedList();
		long proximoPowerUp = 0;	
		
		for (int i = 0; i < 50; i++){
		estrelaPlano1.add(new EstrelaPlano1());
		}

		for (int i = 0; i < 20; i++){
		estrelaPlano2.add(new EstrelaPlano2());
		}

		/* variaveis de controle de spawn dos inimigos*/
		long nextEnemy1 = currentTime + 2000;
		long nextEnemy2 = currentTime + 7000;
		int enemy2_count = 0;
		double enemy2_spawnX = GameLib.WIDTH * 0.20;

		boolean pFlag = false;
		int pIndex = -1;
		
		boolean iniFlag = false;
		int iniIndex = -1;
		
		/* iniciado interface gráfica */
		GameLib.initGraphics();

		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/* -----------------                                                                             */
		/*                                                                                               */
		/* O main loop do jogo executa as seguintes operações:                                           */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu entre a última atualização     */
		/*    e o timestamp atual: posição e orientação, execução de disparos de projéteis, etc.         */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		while(running){
			// Variáveis básicas de tempo
			delta = System.currentTimeMillis() - currentTime;
			currentTime = System.currentTimeMillis();

			/*instanciando e desenhando estrelas */
			for (Estrela estrela1 : estrelaPlano1) {
				estrela1.mover();
				estrela1.desenhar();
			}

			for (Estrela estrela2 : estrelaPlano2) {
				estrela2.mover();
				estrela2.desenhar();
			}

			/* Métodos de player */
			player.atualizaEstado(delta, currentTime, inimigos);
			player.desenha(currentTime);
			
			// TESTE DA SITUAÇÃO PROBLEMA MUITO ESPECIFICA
			// if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) {
			// 	inimigos.add(new Inimigo1(GameLib.WIDTH/2.00, 10.0, 0.20 + 0.5, 0.5, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			// 	inimigos.add(new Inimigo1(GameLib.WIDTH/2.00, 10.0, 0.20 + 0.5, 0.5, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			// }

			/*spawn inimigo 1 - VAI MUDAR QUANDO TIVER O SCRIPT*/
			if(currentTime > nextEnemy1){
				inimigos.add(new Inimigo1(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, 10.0, 0.20 + Math.random() * 0.15, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0.0, enemyProjetil));
				nextEnemy1 = currentTime +500;
			}

			/*spawn inimigo 2 - VAI MUDAR QUANDO TIVER O SCRIPT*/
			if(currentTime > nextEnemy2){
				
				inimigos.add(new Inimigo2(enemy2_spawnX, -20, 0.42, 0.42, (3 * Math.PI) / 2, 0.0, enemyProjetil));
				enemy2_count++;

				if(enemy2_count < 10){
						
					nextEnemy2 = currentTime + 120;
				}else {
						
					enemy2_count = 0;
					enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
					nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
				}
			}
	
			/* gerencia inimigos */
			for (Inimigos ini : inimigos) {
				if(!ini.atualizaEstado(delta, currentTime, player.getY(), player.getProjeteis())){
					iniFlag = true;
					iniIndex = inimigos.indexOf(ini);
				}
				ini.desenha(currentTime);
			}
			/* Deleta inimigo morto */
			if(iniFlag){
				inimigos.remove(iniIndex);
				iniFlag = false;
			}
			
			/* gerencia projeteis inimigos (atualiza, desenha e remove) */
			for (Projetil p : enemyProjetil) {
				if(!p.atualizaEstado(delta, currentTime)){
					pFlag = true;
					pIndex = enemyProjetil.indexOf(p);
				}
				p.desenha(currentTime);
			}
			
			/* Deleta projétil fora da tela */
			if(pFlag){
				enemyProjetil.remove(pIndex);
				pFlag = false;
			}
			

			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			GameLib.display();

			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;

			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 3 ms. */
			busyWait(currentTime + 3);
						


			
			/* spawn powerup */
			if (currentTime > proximoPowerUp) {
    			double x = Math.random() * (GameLib.WIDTH - 20) + 10;
    			double y = 0;

    		if (Math.random() < 0.5) /* 50% chance de aparecer */ {
       		 powerUps.add(new PowerUpTiroRapido(x, y));
    		} else {
        		powerUps.add(new PowerUpInvulnerabilidade(x, y));
   			}

    			proximoPowerUp = currentTime + 10000; /* a cada 10 seg */
			}

			/* atualiza, desenha e aplica powerups  */
			Iterator<PowerUp> itPower = powerUps.iterator();
			while (itPower.hasNext()) {
				PowerUp p = itPower.next();
    				if (!p.atualizaEstado(delta, currentTime, GameLib.HEIGHT)) {
        				itPower.remove();
        				continue;
    					}
    					p.desenha(currentTime);
    				if (p.colisao(player)) {
        				p.aplicarEfeito(player, currentTime);
       					itPower.remove();
    				}
			}


		}

		System.exit(0);

	}
}
