import java.io.IOException;
import java.io.RandomAccessFile;

public class Merge {

	private int tamanhoRegistro;
	private long tamanhoFinalArquivoSaida = 0;

	private RandomAccessFile[] arquivos;
	private Registro[] registros;
	private Buffer[] buffersEntrada;
	private Buffer bufferSaida;
	private RandomAccessFile arquivoSaida;

	private String nomeArquivo;
	private int quantidadeMemoriaDisponivel;
	private int quantidadeArquivos;

	public Merge(int quantidadeMemoriaDisponivel, int quantidadeArquivos,
			String nomeArquivo) throws IOException {
		this.quantidadeMemoriaDisponivel = quantidadeMemoriaDisponivel;
		this.quantidadeArquivos = quantidadeArquivos;
		this.nomeArquivo = nomeArquivo;
		this.arquivoSaida = new RandomAccessFile(nomeArquivo + "_Merge", "rw");

		arquivos = new RandomAccessFile[quantidadeArquivos];
		registros = new Registro[quantidadeArquivos];
		buffersEntrada = new Buffer[quantidadeArquivos];

		// criarBuffersEntrada(quantidadeMemoriaDisponivel, quantidadeArquivos);
		bufferSaida = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		tamanhoRegistro = new Registro().serialize().length;
	}

	/**
	 * Abre os arquivos que dever�o ser "mergeados".
	 * 
	 * @param nomeArquivo
	 *            o nome base do arquivo. Esse nome ser� tratado de modo a gerar
	 *            o nome real dos arquivos.
	 */
	private void abrirArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {
				arquivos[i] = new RandomAccessFile(nomeArquivo + (i + 1), "r");
				tamanhoFinalArquivoSaida += arquivos[i].length();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	/**
	 * Fecha os arquivos abertos.
	 */
	private void fecharArquivos() {
		for (int i = 0; i < quantidadeArquivos; i++) {
			try {
				arquivos[i].close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Realiza o merge dos arquivos especificados nos atributos da classe.
	 * 
	 * @throws IOException
	 */
	public void merge() throws IOException {
		inicializarMerge();

		while (arquivoSaida.length() < tamanhoFinalArquivoSaida) {
			int posicaoMenorRegistro = encontrarMenorRegistro();
			encherBufferSaida(posicaoMenorRegistro);
			registros[posicaoMenorRegistro] = recuperarProximoRegistro(posicaoMenorRegistro);
		}

		fecharArquivos();

	}

	/**
	 * Faz os preparativos iniciais para o merge: abrir os arquivos, carregar os
	 * buffers de entrada, recuperar o primeiro registro de cada buffer.
	 */
	private void inicializarMerge() {
		System.out.println("Inicializando merge...");
		abrirArquivos();

		for (int i = 0; i < quantidadeArquivos; i++) {
			preencherBuffer(i);
		}

		for (int i = 0; i < quantidadeArquivos; i++) {
			registros[i] = recuperarProximoRegistro(i);
		}
	}

	/**
	 * Escreve um vetor de bytes no arquivo de sa�da.
	 * 
	 * @param buffer
	 *            o buffer que dever� ser escrito.
	 * @throws IOException
	 */
	private void escreverBuffer(byte[] buffer) throws IOException {
		System.out.println("Escrevendo buffer...");
		arquivoSaida.write(buffer);
		System.out.println(tamanhoFinalArquivoSaida - arquivoSaida.length());
	}

	/**
	 * Enche o buffer de saida com um registro. Uma vez que o buffer esteja
	 * cheio, escreve o buffer no arquivo de saida e esvazia o buffer.
	 * 
	 * @param posicaoRegistro
	 *            a posi��o do registro no vetor de registros que dever� ser
	 *            adicionado ao buffer.
	 */
	private void encherBufferSaida(int posicaoRegistro) {
		try {
			// � poss�vel adicionar um registro completo no buffer
			if (bufferSaida.getQuantidadeDados() + tamanhoRegistro <= bufferSaida
					.getBuffer().length) {
				bufferSaida.adicionarDados(registros[posicaoRegistro]
						.serialize());
			} else {
				escreverBuffer(bufferSaida.getBuffer());
				bufferSaida = new Buffer(quantidadeMemoriaDisponivel
						/ (quantidadeArquivos + 1));
				bufferSaida.adicionarDados(registros[posicaoRegistro]
						.serialize());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Encontra o menor Registro dado um vetor de Registros.
	 * 
	 * @return a posi��o do menor Registro.
	 */
	private int encontrarMenorRegistro() {
		int menorPosicao = 0;
		float menorSaldo = 1000001;

		for (int i = 1; i < quantidadeArquivos; i++) {
			if (registros[i] != null && registros[i].getSaldo() < menorSaldo) {
				menorSaldo = registros[i].getSaldo();
				menorPosicao = i;
			}
		}
		return menorPosicao;
	}

	/**
	 * Recupera o pr�ximo registro no buffer de entrada especificado. O m�todo
	 * tamb�m faz o tratamento de "registros remanescentes", preechendo o buffer
	 * se for necess�rio.
	 * 
	 * @param posicaoBuffer
	 *            especifica de qual buffer o pr�ximo registro ser� recuperado.
	 * @return o Registro recuperado.
	 */
	private Registro recuperarProximoRegistro(int numeroBuffer) {

		Buffer bufferEntrada = buffersEntrada[numeroBuffer];

		try {
			// � poss�vel ler mais um registro completo
			if (bufferEntrada.getPonteiro() + tamanhoRegistro <= bufferEntrada
					.getBuffer().length) {
				return (Registro) Registro.deserialize(bufferEntrada
						.recuperarDados(tamanhoRegistro));
			}
			// registro remanescente
			else {
				if (arquivos[numeroBuffer].length()
						- arquivos[numeroBuffer].getFilePointer() != 0) {
					return recuperarRegistroRemanescente(numeroBuffer);
				}
				return null;
			}

		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Recupera um registro remanescente: um registro em que metade de seus
	 * bytes est�o em um buffer n, e a outra metade se encontra em um buffer
	 * n+1.
	 * 
	 * @param numeroBuffer
	 *            numero/posi��o do buffer no vetor de buffers.
	 * @return o registro remanescente.
	 */
	private Registro recuperarRegistroRemanescente(int numeroBuffer) {
		Buffer bufferEntrada = buffersEntrada[numeroBuffer];
		try {

			int bytesRemanescentesFinalBuffer = bufferEntrada.getBuffer().length
					- bufferEntrada.getPonteiro();

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
	 *            a posi��o do buffer no vetor de buffers que se deseja
	 *            preencher.
	 */
	private void preencherBuffer(int posicaoBuffer) {
		buffersEntrada[posicaoBuffer] = new Buffer(quantidadeMemoriaDisponivel
				/ (quantidadeArquivos + 1));
		try {
			System.out
					.println("Lendo arquivo "
							+ (posicaoBuffer + 1)
							+ ": "
							+ (arquivos[posicaoBuffer].length() - arquivos[posicaoBuffer]
									.getFilePointer()));
			if (arquivos[posicaoBuffer].length()
					- arquivos[posicaoBuffer].getFilePointer() >= buffersEntrada[posicaoBuffer]
						.getBuffer().length) {
				arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
						.getBuffer());
			} else {
				buffersEntrada[posicaoBuffer] = new Buffer(
						(int) (arquivos[posicaoBuffer].length() - arquivos[posicaoBuffer]
								.getFilePointer()));
				arquivos[posicaoBuffer].read(buffersEntrada[posicaoBuffer]
						.getBuffer());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
