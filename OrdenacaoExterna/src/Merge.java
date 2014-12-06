import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Merge {

	private int tamanhoRegistro;

	private RandomAccessFile[] arquivos;
	private Registro[] registros;
	private Buffer[] buffersEntrada;
	private Buffer bufferSaida;

	private String nomeArquivo;
	private int quantidadeMemoriaDisponivel;
	private int quantidadeArquivos;

	public Merge(int quantidadeMemoriaDisponivel, int quantidadeArquivos,
			String nomeArquivo) throws IOException {
		this.quantidadeMemoriaDisponivel = quantidadeMemoriaDisponivel;
		this.quantidadeArquivos = quantidadeArquivos;
		this.nomeArquivo = nomeArquivo;

		arquivos = new RandomAccessFile[quantidadeArquivos];
		registros = new Registro[quantidadeArquivos];
		// criarBuffersEntrada(quantidadeMemoriaDisponivel, quantidadeArquivos);
		bufferSaida = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		tamanhoRegistro = new Registro().serialize().length;
	}

	/**
	 * Abre os arquivos que deverão ser "mergeados".
	 * 
	 * @param nomeArquivo
	 *            o nome base do arquivo. Esse nome será tratado de modo a gerar
	 *            o nome real dos arquivos.
	 */
	private void abrirArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {
				arquivos[i] = new RandomAccessFile(nomeArquivo + (i + 1), "r");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Realiza o merge dos arquivos especificados nos atributos da classe.
	 */
	public void merge() {
		inicializarMerge();

		//ok!
		while (buffersEntrada.length != 0) {
			int posicaoMenorRegistro = encontrarMenorRegistro();
			encherBufferSaida(posicaoMenorRegistro);
			registros[posicaoMenorRegistro] = recuperarProximoRegistro(posicaoMenorRegistro);
		}

	}

	/**
	 * Faz os preparativos iniciais para o merge: abrir os arquivos, carregar os buffers de entrada,
	 * recuperar o primeiro registro de cada buffer.
	 */
	private void inicializarMerge() {
		abrirArquivos();

		for (int i = 0; i < quantidadeArquivos; i++) {
			preencherBuffer(i);
		}

		for (int i = 0; i < quantidadeArquivos; i++) {
			registros[i] = recuperarProximoRegistro(i);
		}
	}

	/**
	 * Escreve um vetor de bytes no arquivo de saída.
	 * 
	 * @param buffer
	 *            o buffer que deverá ser escrito.
	 */
	private void escreverBuffer(byte[] buffer) {

	}

	/**
	 * Enche o buffer de saida com um registro. Uma vez que o buffer esteja
	 * cheio, escreve o buffer no arquivo de saida e esvazia o buffer.
	 * 
	 * @param posicaoRegistro
	 *            a posição do registro no vetor de registros que deverá ser
	 *            adicionado ao buffer.
	 */
	private void encherBufferSaida(int posicaoRegistro) {
		try {
			// é possível adicionar um registro completo no buffer
			if (bufferSaida.getUltimoDadoLido() + tamanhoRegistro < bufferSaida
					.getBuffer().length) {
				bufferSaida.adicionarDados(registros[posicaoRegistro]
						.serialize());
			} else {
				escreverBuffer(bufferSaida.getBuffer());
				bufferSaida = new Buffer(quantidadeMemoriaDisponivel
						/ (quantidadeArquivos + 1));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Encontra o menor Registro dado um vetor de Registros.
	 * 
	 * @return a posição do menor Registro.
	 */
	private int encontrarMenorRegistro() {
		int menorPosicao = 0;
		float menorSaldo = registros[0].getSaldo();

		for (int i = 1; i < quantidadeArquivos; i++) {
			if (registros[i].getSaldo() < menorSaldo) {
				menorSaldo = registros[i].getSaldo();
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	/**
	 * Recupera o próximo registro no buffer de entrada especificado. O método
	 * também faz o tratamento de "registros remanescentes", preechendo o buffer
	 * se for necessário.
	 * 
	 * @param posicaoBuffer
	 *            especifica de qual buffer o próximo registro será recuperado.
	 * @return o Registro recuperado.
	 */
	private Registro recuperarProximoRegistro(int numeroBuffer) {

		Buffer bufferEntrada = buffersEntrada[numeroBuffer];

		try {
			// é possível ler mais um registro completo
			if (bufferEntrada.getUltimoDadoLido() + tamanhoRegistro < bufferEntrada
					.getBuffer().length) {
				return (Registro) Registro.deserialize(bufferEntrada
						.recuperarDados(tamanhoRegistro));
			}
			// registro remanescente
			else {
				return recuperarRegistroRemanescente(numeroBuffer);
			}

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Recupera um registro remanescente: um registro em que metade de seus
	 * bytes estão em um buffer n, e a outra metade se encontra em um buffer
	 * n+1.
	 * 
	 * @param numeroBuffer
	 *            numero/posição do buffer no vetor de buffers.
	 * @return o registro remanescente.
	 */
	private Registro recuperarRegistroRemanescente(int numeroBuffer) {
		Buffer bufferEntrada = buffersEntrada[numeroBuffer];
		try {

			int bytesRemanescentesFinalBuffer = bufferEntrada.getBuffer().length
					- bufferEntrada.getUltimoDadoLido() - 1;

			byte[] remanescenteFinalBuffer = bufferEntrada
					.recuperarDados(bytesRemanescentesFinalBuffer);

			preencherBuffer(numeroBuffer);
			bufferEntrada = buffersEntrada[numeroBuffer];

			byte[] faltanteInicioBuffer = bufferEntrada
					.recuperarDados(tamanhoRegistro
							- bytesRemanescentesFinalBuffer);

			return (Registro) Registro.deserialize(Util.concat(
					remanescenteFinalBuffer, faltanteInicioBuffer));
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Preenche um buffer de entrada se seu arquivo correspondente.
	 * 
	 * @param posicaoBuffer
	 *            a posição do buffer no vetor de buffers que se deseja
	 *            preencher.
	 */
	private void preencherBuffer(int posicaoBuffer) {
		buffersEntrada[posicaoBuffer] = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		try {
			arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
					.getBuffer());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
