package Servidor;

public class ModifiedCountDownLatch {
    
    private final int bonusFactor;      // Fator de multiplicação (ex: 2x pontos)
    private int bonusCount;             // Quantas pessoas ainda podem receber bónus
    private final long waitPeriod;      // Tempo limite em milissegundos
    private int count;                  // Quantas respostas faltam para abrir a barreira
    
    // Construtor
    public ModifiedCountDownLatch(int bonusFactor, int bonusCount, int waitPeriodInSeconds, int count) {
        this.bonusFactor = bonusFactor;
        this.bonusCount = bonusCount;
        this.waitPeriod = waitPeriodInSeconds * 1000L; // Converter para ms
        this.count = count;
    }

    /**
     * Chamado pela thread de cada cliente quando envia uma resposta.
     * @return O fator de multiplicação (bonusFactor ou 1).
     */
    public synchronized int countDown() {
        int currentBonus = 1;

        // Se ainda estamos à espera de respostas
        if (count > 0) {
            count--;
            
            // Verifica se este jogador ainda apanhou um bónus
            if (bonusCount > 0) {
                currentBonus = bonusFactor;
                bonusCount--;
            }

            // Se foi o último a responder, acorda a thread principal do jogo
            if (count == 0) {
                notifyAll();
            }
        }
        
        return currentBonus;
    }

    /**
     * Chamado pelo Servidor (Game Loop) para esperar pelo fim da ronda.
     * Desbloqueia se todos responderem OU se o tempo acabar.
     */
    public synchronized void await() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        long timeElapsed = 0;

        // Loop de espera: Enquanto faltarem respostas E houver tempo
        while (count > 0 && timeElapsed < waitPeriod) {
            
            // Wait recebe o tempo que FALTA (não o tempo total)
            wait(waitPeriod - timeElapsed);
            
            // Atualiza o tempo passado
            timeElapsed = System.currentTimeMillis() - startTime;
        }
        
        // Se sairmos do loop, ou count == 0 (todos responderam) 
        // ou timeElapsed >= waitPeriod (tempo acabou).
        // Em ambos os casos, o servidor pode prosseguir.
    }
}