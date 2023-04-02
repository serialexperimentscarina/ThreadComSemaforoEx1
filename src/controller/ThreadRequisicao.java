package controller;

import java.util.concurrent.Semaphore;

public class ThreadRequisicao extends Thread {
	
	private Semaphore mutex;
	private int threadID;
	
	public ThreadRequisicao(Semaphore mutex, int threadID) {
		this.mutex = mutex;
		this.threadID = threadID;
	}
	
	@Override
	public void run() {
		if (threadID % 3 == 1) {
			executaRequisicao(2, 200, 1000, 1000);	
		} else if(threadID % 3 == 2) {
			executaRequisicao(3, 500, 1500, 1500);
		} else {
			executaRequisicao(3, 1000, 2000, 1500);
		}
	}
	
	private void executaRequisicao(int repeticoes, int tempoMinCalculo, int tempoMaxCalculo, int tempoBD) {
		for (int i = 0; i < repeticoes; i++) {
			int tempoCalculo = (int)((Math.random() * (tempoMaxCalculo - tempoMinCalculo + 1)) + tempoMinCalculo);
			calculos(tempoCalculo, (i * 2) + 1, (repeticoes * 2));
			try {
				mutex.acquire();
				transacaoBD(tempoBD, (i * 2) + 2, (repeticoes * 2));
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				mutex.release();
			}
		}
		System.out.println("A requisicao #" + threadID + " terminou de ser processada.");
	}
	
	private void transacaoBD(int tempoTransacao, int passoAtual, int passosTotais) {
		try {
			sleep(tempoTransacao);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("A requisicao #" + threadID + " terminou de realizar uma transacao em BD (Passo " + passoAtual + "/" + passosTotais + ")");
	}

	private void calculos(int tempoCalculo, int passoAtual, int passosTotais) {
		try {
			sleep(tempoCalculo);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("A requisicao #" + threadID + " terminou de realizar calculos (Passo " + passoAtual + "/" + passosTotais + ")");

	}

}
