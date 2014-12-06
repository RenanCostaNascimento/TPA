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
	 * parâmetro for maior do que a quantidade de bytes sobrando no buffer, o
	 * método não faz nada.
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
	 * Recupera quantidadeDados do buffer, retornando as informações na forma de
	 * um vetor de bytes. O método também move o ponteiro para a ultima posição
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
