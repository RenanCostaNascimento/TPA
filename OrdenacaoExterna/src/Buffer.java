public class Buffer {

	private byte[] buffer;
	private int ponteiro;
	private int quantidadeDados;

	public Buffer(int tamanhoBuffer) {
		buffer = new byte[tamanhoBuffer];
		ponteiro = 0;
		quantidadeDados = 0;
	}

	public byte[] getBuffer() {
		return buffer;
	}
	public int getQuantidadeDados() {
		return quantidadeDados;
	}
	public int getPonteiro() {
		return ponteiro;
	}

	/**
	 * Adicionar um vetor de bytes no final do buffer. Se o vetor passado como
	 * par�metro for maior do que a quantidade de bytes sobrando no buffer, o
	 * m�todo n�o faz nada.
	 * 
	 * @param dados
	 *            o vetor de bytes que ser quer adicionar.
	 */
	public void adicionarDados(byte[] dados) {
		if (quantidadeDados + dados.length <= buffer.length) {
			int posicaoDados = 0;
			for (int i = quantidadeDados; i < dados.length; i++) {
				buffer[i] = dados[posicaoDados];
				posicaoDados++;
			}
			quantidadeDados += dados.length;
		}
	}

	/**
	 * Recupera quantidadeDados do buffer, retornando as informa��es na forma de
	 * um vetor de bytes. O m�todo tamb�m move o ponteiro para a ultima posi��o
	 * recuperada.
	 * 
	 * @param quantidadeDados
	 *            a quantidade de dados que se deseja retornar no buffer.
	 * @return um vetor de bytes com os dados recuperados.
	 */
	public byte[] recuperarDados(int quantidadeDados) {
		byte[] dadosRecuperados = new byte[quantidadeDados];
		
		for (int i = 0; i < quantidadeDados; i++) {
			dadosRecuperados[i] = buffer[this.ponteiro+i];
		}
		ponteiro+=quantidadeDados;
		
		return dadosRecuperados;

	}

}
