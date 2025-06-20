package classes;
public interface Entidade {
    public void desenha(long currentTime);
    public boolean atualizaEstado(long deltaTime, long currentTime, double PlayerY);
}