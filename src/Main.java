import classes.*;
import java.io.*;
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
		
		/* leitura do arquivo de configuração das fases */
		//Inicia variáveis que serão usadas na leitura dentro do bloco try
		String configPath = "arquivos/Config.txt";
		int playerVida = -1;
		int qtdFases = -1;
		LinkedList<Instancia>[] fases = new LinkedList[1];
		try{
			
			File configFile = new File(configPath);
			Scanner config = new Scanner(configFile);
			
			playerVida = config.nextInt();
			qtdFases = config.nextInt();
			fases = new LinkedList[qtdFases]; 
			
			for(int i=0; i<qtdFases; i++){
				String fasePath = config.next();
				File faseFile = new File(fasePath);
				Scanner fase = new Scanner(faseFile);
				fases[i] = new LinkedList();
				while(fase.hasNextLine()){
					String instancia = fase.next();
					int tipo = fase.nextInt();
					int vida = -1;
					if(instancia.equals("INIMIGO")) vida = -1;
					else vida = fase.nextInt();
					long tempo = fase.nextLong();
					double auxX = fase.nextDouble();
					double auxY = fase.nextDouble();
					fases[i].add(new Instancia(instancia, tipo, vida, currentTime + tempo, auxX, auxY));
				}
			}
			
		}catch(FileNotFoundException e){
			System.err.println("Arquivo não encontrado: " + e.getMessage());
		}
		int faseAtual = 0;

		/*declaração das variáveis player */
		// coordenada x, coordenada y, velocidade no eixo x, velocidade no eixo y, raio do player, tempo do próximo tiro.
		LinkedList<Projetil> playerProjetil = new LinkedList();
		Player player = new Player(GameLib.WIDTH / 2, GameLib.HEIGHT * 0.90, 0.25, 0.25, 12.0, currentTime, playerVida, playerProjetil); 
	
		/*declaracao da  lista de inimigos e projetil*/
		LinkedList<Inimigos> inimigos = new LinkedList();
		LinkedList<Projetil> enemyProjetil = new LinkedList();
		Iterator<Instancia> faseIterador = fases[faseAtual].iterator();
		Instancia instanciaAux = faseIterador.next();
		
		/*declaração e inicialização das estrelas */
		ArrayList<Estrela> estrelaPlano1 = new ArrayList<>();
		ArrayList<Estrela> estrelaPlano2 = new ArrayList<>();
		
		for (int i = 0; i < 50; i++){
		estrelaPlano1.add(new EstrelaPlano1());
		}

		for (int i = 0; i < 20; i++){
		estrelaPlano2.add(new EstrelaPlano2());
		}

		/*declaração dos powerups*/
		LinkedList<PowerUp> powerUps = new LinkedList();
		long proximoPowerUp = 0;	

		/* variaveis de controle de spawn dos inimigos*/
		long nextEnemy2 = 0;
		int enemy2_count = 0;
		double enemy2_spawnX = GameLib.WIDTH * 0.20;
		Instancia ini2 = new Instancia("", -1, -1, -1, -1.0, -1.0);

		boolean pFlag = false;
		int pIndex = -1;
		
		boolean iniFlag = false;
		boolean ini2Flag = false;
		int iniIndex = -1;
		
		LinkedList<Projetil> projeteisBoss = new LinkedList();
		Boss boss = null;

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

			/* Métodos de player */
			player.atualizaEstado(delta, currentTime, inimigos, boss);
			player.desenha(currentTime);

			// TESTE DA SITUAÇÃO PROBLEMA MUITO ESPECIFICA
			// if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) {
			// 	inimigos.add(new Inimigo1(GameLib.WIDTH/2.00, 10.0, 0.20 + 0.5, 0.5, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			// 	inimigos.add(new Inimigo1(GameLib.WIDTH/2.00, 10.0, 0.20 + 0.5, 0.5, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			// }
	
			/*Spawn de Inimigos e Bosses*/
			if(instanciaAux.getInstancia().equals("INIMIGO")){
				if(currentTime > instanciaAux.getTempo()){
					if(instanciaAux.getTipo()==1){
						inimigos.add(new Inimigo1(instanciaAux.getX(), instanciaAux.getY(), 0.20 + Math.random() * 0.15, 0.20 + Math.random() * 0.15, (3 * Math.PI) / 2, 0.0, enemyProjetil));
						if(faseIterador.hasNext()) instanciaAux = faseIterador.next();
					}else{
						inimigos.add(new Inimigo2(instanciaAux.getX(), instanciaAux.getY(), 0.42, 0.42, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			 			enemy2_count++;
						nextEnemy2 = currentTime + 120;
						ini2Flag = true;
						ini2 = instanciaAux;
						if(faseIterador.hasNext()) instanciaAux = faseIterador.next();
					}
				}
			}//Neste if spawnamos inimigos 1 ou 2
			else{
				if(currentTime > instanciaAux.getTempo() && boss == null){
					if(instanciaAux.getTipo() == 1){
						boss = new Boss1(instanciaAux.getX(), instanciaAux.getY(), 0.20, 0.05, (3 * Math.PI) / 2, 0.0, projeteisBoss, instanciaAux.getVida());
					}else {
						boss = new Boss2(instanciaAux.getX(), instanciaAux.getY(), .8, 0.3, (3 * Math.PI) / 2, 0.0, projeteisBoss, instanciaAux.getVida());
					}
				}
			}//Neste else spawnamos o Boss 1 ou 2

			//Spawn de inimigo 2 é tratado de forma pouco diferente, já que deve continuar spawnando suas cópias até que 10 delas tenham sido feitas
			if(currentTime > nextEnemy2 && ini2Flag){
				
			 	inimigos.add(new Inimigo2(ini2.getX(), ini2.getY(), 0.42, 0.42, (3 * Math.PI) / 2, 0.0, enemyProjetil));
			 	enemy2_count++;

			 	if(enemy2_count < 10){
						
			 		nextEnemy2 = currentTime + 120;
			 	}else {
						
			 		enemy2_count = 0;
					ini2Flag = false;
			 	}
			}
	
			/* gerencia inimigos */
			for (Inimigos ini : inimigos) {
			 	if(!ini.atualizaEstado(delta, currentTime, player.getY(), player.getProjeteis())){
			 		iniFlag = true;
			 		iniIndex = inimigos.indexOf(ini);
			 	}
			 	else ini.desenha(currentTime);
			}
			 /* Deleta inimigo morto */
			if(iniFlag){
			 	inimigos.remove(iniIndex);
			 	iniFlag = false;
			}
			
			/* gerencia projeteis inimigos (atualiza, desenha e remove) */
			for (Projetil p : enemyProjetil) {
			 	if(!p.atualizaEstado(delta, currentTime, player)){
			 		pFlag = true;
			 		pIndex = enemyProjetil.indexOf(p);
			 	}
			 	else p.desenha(currentTime);
			}
			/* Deleta projétil fora da tela */
			if(pFlag){
			 	enemyProjetil.remove(pIndex);
			 	pFlag = false;
			}

			/* Gerencia boss */
			if (boss != null) {
				if (!boss.atualizaEstado(delta, currentTime, player.getY(), playerProjetil)){
					boss = null;
					//Passando para a próxima fase ou finalizando o jogo
					faseAtual++;
					if(faseAtual < qtdFases) faseIterador = fases[faseAtual].iterator();
					else running = false;
					if(faseIterador.hasNext()) instanciaAux = faseIterador.next();
				}
				else boss.desenha(currentTime);
			}

			/* gerencia projeteis de boss (atualiza, desenha e remove) */
			for (Projetil p : projeteisBoss) {
				if(!p.atualizaEstado(delta, currentTime, player)){
					pFlag = true;
					pIndex = projeteisBoss.indexOf(p);
				}
				else p.desenha(currentTime);
			}
			
			/* Deleta projétil fora da tela */
			if(pFlag){
				projeteisBoss.remove(pIndex);
				pFlag = false;
			}

			/*instanciando e desenhando estrelas */
			for (Estrela estrela1 : estrelaPlano1) {
				estrela1.mover();
				estrela1.desenhar();
			}

			for (Estrela estrela2 : estrelaPlano2) {
				estrela2.mover();
				estrela2.desenhar();
			}

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
		

			/* chamada a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			GameLib.display();

            /*Finaliza o jogo caso Esc seja pressionado ou caso o player zere as vidas*/
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			if(player.getVida() == 0) running = false;

			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 3 ms. */
			busyWait(currentTime + 3);
						
		}

		System.exit(0);

	}
}
