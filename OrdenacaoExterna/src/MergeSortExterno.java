import java.io.IOException;
import java.io.RandomAccessFile;

public class MergeSortExterno {

	private String nomeArquivoEntrada;
	private String nomeArquivoSaida;
	private int quantidadeArquivos;
	private RandomAccessFile[] arquivosEntrada;
	private RandomAccessFile[] arquivosSaida;
	private int numeroArquivoSaidaCorrente;
	private Registro[] cacheRegistros;
	private int tamanhoRegistro;
	private long tamanhoRodada;

	/**
	 * Cria o MergeSortExterno.
	 * 
	 * @param nomeArquivoEntrada
	 *            o nome do arquivos de entrada base dos arquivos. Os nomes
	 *            reais serão gerados pela classe. Exemplo: se o nome dos
	 *            arquivos reais que se deseja realizar o merge forem 1Giga1 e
	 *            1Giga2, deve-se passar o nomeArquivoEntrada como 1Giga.
	 * @param quantidadeArquivos
	 *            a quantidade de arquivos que se deseja realizar o merge.
	 * @throws IOException
	 *             se os arquivos especificados não forem encontrados.
	 */
	public MergeSortExterno(String nomeArquivoEntrada, int quantidadeArquivos)
			throws IOException {
		this.nomeArquivoEntrada = nomeArquivoEntrada
				+ Constantes.ARQUIVO_ENTRADA;
		this.nomeArquivoSaida = nomeArquivoEntrada + Constantes.ARQUIVO_SAIDA;
		this.quantidadeArquivos = quantidadeArquivos;
		tamanhoRegistro = new Registro().serialize().length;
		arquivosEntrada = new RandomAccessFile[this.quantidadeArquivos];
		arquivosSaida = new RandomAccessFile[this.quantidadeArquivos];
		numeroArquivoSaidaCorrente = 0;
		cacheRegistros = new Registro[this.quantidadeArquivos];
		tamanhoRodada = 1;

	}

	public void mergeSortExterno() throws IOException, ClassNotFoundException {

		alternarArquivosEntradaSaida();
		// loop para percorrer os arquivos quantas vezes forem necessárias para
		// terminar o merge
		do {
			alternarArquivosEntradaSaida();
			abrirArquivos();
			// loop para percorrer o arquivo inteiro
			System.out.println("Iniciando merge...");
			System.out.println("Tamanho da rodada: " + tamanhoRodada);
			int iteracao = 0;
			while (!fimArquivos()) {
				iteracao += tamanhoRodada;
				// loop para percorrer a rodada inteira
				do {
					carregarCacheRegistros(iteracao);
					int posicaoMenorRegistro = encontrarMenorRegistro();
					if (posicaoMenorRegistro != -1) {
						escreverRegistro(posicaoMenorRegistro);
					}
				} while (!cacheVazio());
				alternarArquivoSaidaCorrente();
			}
			aumentarTamanhoRodada();
			System.out.println("Merge parcial concluído...\n");
		} while (!mergeConcluido());
		System.out.println("Merge final concluído!");
	}

	/**
	 * Aumenta o tamanho da rodada.
	 */
	private void aumentarTamanhoRodada() {
		tamanhoRodada *= 2;
		if(tamanhoRodada <= 0){
			tamanhoRodada = Long.MAX_VALUE;
		}
		
	}

	/**
	 * Verifica se o cache de registros está vazio.
	 * 
	 * @return true se estiver vazio.
	 */
	private boolean cacheVazio() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			if (cacheRegistros[i] != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Alterna o nome de arquivo de entrada com o nome de arquivo de saída.
	 */
	private void alternarArquivosEntradaSaida() {
		String nomeTemporario = nomeArquivoEntrada;
		nomeArquivoEntrada = nomeArquivoSaida;
		nomeArquivoSaida = nomeTemporario;
	}

	/**
	 * Verifica se o merge entre os arquivos especificados já foi concluído.
	 * 
	 * @return true se o merge já tiver sido concluído.
	 * @throws IOException
	 *             se o arquivo não for encontrado.
	 */
	private boolean mergeConcluido() throws IOException {
		long tamanhoArquivoSaida = arquivosSaida[0].length();
		long tamanhoArquivosEntrada = 0;

		for (int i = 0; i < quantidadeArquivos; i++) {
			tamanhoArquivosEntrada += arquivosEntrada[i].length();
		}

		if (tamanhoArquivosEntrada == tamanhoArquivoSaida) {
			return true;
		}

		return false;
	}

	/**
	 * Verifica se ainda é possível ler alguma coisa dos arquivos de entrada.
	 * 
	 * @return true se ainda existirem bytes a serem lidos de algum arquivo de
	 *         entrada.
	 * @throws IOException
	 *             se o arquivo não for encontrado.
	 */
	private boolean fimArquivos() throws IOException {

		for (int i = 0; i < quantidadeArquivos; i++) {
			if (arquivosEntrada[i].length()
					- arquivosEntrada[i].getFilePointer() > 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Escreve o registro da posição passada como paramêtro no arquivo de saída
	 * corrente.
	 * 
	 * @param posicaoRegistro
	 *            a posição do registro que se deseja escrever.
	 * @throws IOException
	 *             se o arquivo não for encontrado.
	 */
	private void escreverRegistro(int posicaoRegistro) throws IOException {

		arquivosSaida[numeroArquivoSaidaCorrente]
				.write(cacheRegistros[posicaoRegistro].serialize());
		cacheRegistros[posicaoRegistro] = null;

	}

	/**
	 * Alterna o arquivo de saida corrente.
	 */
	private void alternarArquivoSaidaCorrente() {
		numeroArquivoSaidaCorrente++;
		if (numeroArquivoSaidaCorrente == quantidadeArquivos) {
			numeroArquivoSaidaCorrente = 0;
		}
	}

	private int encontrarMenorRegistro() {
		int menorPosicao = -1;
		float menorSaldo = Float.MAX_VALUE;

		for (int i = 0; i < quantidadeArquivos; i++) {
			if (cacheRegistros[i] != null
					&& cacheRegistros[i].getSaldo() <= menorSaldo) {
				menorSaldo = cacheRegistros[i].getSaldo();
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	/**
	 * Carrega os registros do arquivo no cache de registros, se estes (o cache)
	 * estiverem vazios.
	 * 
	 * @throws IOException
	 *             se o arquivo não for encontrado.
	 * @throws ClassNotFoundException
	 *             se a classe Registro não for encontrada.
	 */
	private void carregarCacheRegistros(int iteracao) throws IOException,
			ClassNotFoundException {

		for (int i = 0; i < quantidadeArquivos; i++) {			
			if (cacheRegistros[i] == null) {		
				// é possível ler um registro do arquivo sem ultrapassar o
				// tamanho da rodada ou extrapolar o tamanho do arquivo?
				if (arquivosEntrada[i].getFilePointer() + tamanhoRegistro <= tamanhoRegistro
						* iteracao
						&& arquivosEntrada[i].getFilePointer()
								+ tamanhoRegistro <= arquivosEntrada[i]
									.length()) {
					byte[] bytesRegistro = new byte[tamanhoRegistro];
					arquivosEntrada[i].read(bytesRegistro);
					cacheRegistros[i] = (Registro) Registro
							.deserialize(bytesRegistro);
				}
				// não é possível ler do arquivo correspondente ao cache, tenta
				// ler dos outros arquivos.
				else {
					for (int j = 0; j < quantidadeArquivos; j++) {
						if (arquivosEntrada[j].getFilePointer()
								+ tamanhoRegistro < tamanhoRegistro
								* iteracao
								&& arquivosEntrada[j].getFilePointer()
										+ tamanhoRegistro <= arquivosEntrada[j]
											.length()) {
							byte[] bytesRegistro = new byte[tamanhoRegistro];
							arquivosEntrada[j].read(bytesRegistro);
							cacheRegistros[i] = (Registro) Registro
									.deserialize(bytesRegistro);
							break;
						}
					}
				}
			}
		}

	}

	/**
	 * Abre os arquivos de entrada que deverão ser "mergeados", assim como os
	 * arquivos de saída. Os arquivos de entrada terão seus ponteiros apontando
	 * para o início do arquivo, e os arquivos de saída serão apagados.
	 * 
	 * @param nomeArquivoEntrada
	 *            o nome base do arquivo. Esse nome será tratado de modo a gerar
	 *            o nome real dos arquivos.
	 */
	private void abrirArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {				
				arquivosEntrada[i] = new RandomAccessFile(
						Constantes.CAMINHO_ARQUIVO + nomeArquivoEntrada
								+ (i + 1), "r");
				arquivosEntrada[i].seek(0);

				arquivosSaida[i] = new RandomAccessFile(
						Constantes.CAMINHO_ARQUIVO + nomeArquivoSaida + (i + 1),
						"rw");
				arquivosSaida[i].setLength(0);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
