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
		Player player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, currentTime); 
		/*
		LinkedList<Inimigo1> inimigos1 = new LinkedList();
		inimigos1.add(new Inimigo1(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, -10.0, 0.20 + Math.random() * 0.15, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0.0));
		*/
		LinkedList<Inimigos> inimigos = new LinkedList();
		
		inimigos.add(new Inimigo1(Math.random() * (GameLib.WIDTH - 20.0) + 10.0, 100.0, 0.20 + Math.random() * 0.15, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0.0));
		/*		private static List<inimigo2> inimigos2 = new LinkedList<>();
    */
		
		/*declaração e inicialização das estrelas */
		ArrayList<Estrela> estrelaPlano1 = new ArrayList<>();
		ArrayList<Estrela> estrelaPlano2 = new ArrayList<>();
		
		for (int i = 0; i < 50; i++){
		estrelaPlano1.add(new EstrelaPlano1());
		}

		for (int i = 0; i < 20; i++){
		estrelaPlano2.add(new EstrelaPlano2());
		}

		long nextEnemy2 = currentTime + 1000;
		int enemy2_count = 0;
		double enemy2_spawnX = GameLib.WIDTH * 0.20;
		
		
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
			delta = System.currentTimeMillis() - currentTime;
			currentTime = System.currentTimeMillis();

			player.atualizaEstado(delta, currentTime, 0);
			player.desenha(currentTime);
			if (!player.getExplodindo()) player.colision(currentTime, inimigos);
			
			/* inimigo 2 */
			if(currentTime > nextEnemy2){
				System.out.println("OIEEE");
				inimigos.add(new Inimigo2(enemy2_spawnX, -100.0, 0.42, 0.42, (3 * Math.PI) / 2, 0.0));
				enemy2_count++;

				if(enemy2_count < 10){
						
					nextEnemy2 = currentTime + 120;
				}else {
						
					enemy2_count = 0;
					enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
					nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
				}
			}

			/* inimigo 1 */
			for (Inimigos ini1 : inimigos) {
				ini1.desenha(currentTime);
				ini1.atualizaEstado(delta, currentTime, player.getY());
			}
			
			/* estrelas */
			for (Estrela estrela1 : estrelaPlano1) {
				estrela1.mover();
				estrela1.desenhar();
			}

			for (Estrela estrela2 : estrelaPlano2) {
				estrela2.mover();
				estrela2.desenhar();
			}

			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			GameLib.display();

			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;

			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 3 ms. */
			busyWait(currentTime + 3);
						
		}

		System.exit(0);

	}
}
