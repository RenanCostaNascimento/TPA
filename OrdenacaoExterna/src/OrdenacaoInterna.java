import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdenacaoInterna {

	private int tamanhoRegistro;

	public OrdenacaoInterna() throws IOException {
		tamanhoRegistro = new Registro().serialize().length;
	}

	/**
	 * Criar um arquivo com o tamanho especificado.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo que será gerado.
	 * @param tamanhoArquivo
	 *            o tamanho do arquivo em bytes.
	 */
	public void gerarArquivo(String nomeArquivo, long tamanhoArquivo) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");

			while (file.length() + tamanhoRegistro < tamanhoArquivo) {
				Registro registro = new Registro();
				file.write(registro.serialize());
			}
			file.close();

			System.out.println("registros inseridos: " + Registro.quantidade);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Ordena um arquivo de qualquer tamanho, gerando n arquivos menores, todos
	 * ordenados. A quantidade de arquivos gerados depende do tamanho do arquivo
	 * original e da quantidade de memória disponível.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo original que se deseja ordenar.
	 * @param quantidadeMemoriaDisponivel
	 *            a quantidade de memória disponível, em bytes.
	 */
	public void ordenarArquivo(String nomeArquivo,
			int quantidadeMemoriaDisponivel) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");
			byte[] remanescentesFinalBuffer = new byte[0];
			int numeroArquivo, dadosLidos;
			numeroArquivo = 0;

			while (file.getFilePointer() < file.length()) {
				dadosLidos = remanescentesFinalBuffer.length;
				numeroArquivo++;
				byte[] buffer = new byte[quantidadeMemoriaDisponivel];

				System.out.println("Carregando arquivo em memória...");
				file.read(buffer);

				List<Registro> listaRegistro = new ArrayList<>();
				if (remanescentesFinalBuffer.length != 0) {
					System.out.println("Recriando registro remanescente...");
					byte[] faltantesInicioBuffer = subvetor(buffer, 0,
							tamanhoRegistro - dadosLidos - 1);
					listaRegistro.add((Registro) Registro.deserialize(concat(
							remanescentesFinalBuffer, faltantesInicioBuffer)));
					dadosLidos = tamanhoRegistro
							- remanescentesFinalBuffer.length;
				}

				System.out.println("Recriando registros...");
				while (dadosLidos + tamanhoRegistro < buffer.length) {
					byte[] byteRegistro = subvetor(buffer, dadosLidos,
							dadosLidos + tamanhoRegistro - 1);
					listaRegistro.add((Registro) Registro
							.deserialize(byteRegistro));
					dadosLidos += tamanhoRegistro;
				}

				remanescentesFinalBuffer = subvetor(buffer, dadosLidos,
						buffer.length - 1);

				System.out.println("Ordenando lista...");
				Collections.sort(listaRegistro);

				System.out.println("Criando arquivo ordenado...\n");
				gerarArquivo(nomeArquivo + "_Saida" + numeroArquivo,
						listaRegistro);
			}

			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Cria um arquivo à partir de uma lista de Registros com o nome
	 * especificado.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo que será gerado.
	 * @param listaRegistro
	 *            a lista de onde os registros serão tirados.
	 */
	private void gerarArquivo(String nomeArquivo, List<Registro> listaRegistro) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");

			for (Registro registro : listaRegistro) {
				file.write(registro.serialize());
			}

			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Faz uma cópia de um pedaço de um vetor. Semelhante ao substring de
	 * String. O vetor original não é alterado.
	 * 
	 * @param vetorOrigem
	 *            o vetor de onde se quer copiar a informação.
	 * @param posicaoInicial
	 *            a posição inicial em que a informação desejada está
	 *            localizada.
	 * @param posicaoFinal
	 *            a posição final em que a informação desejada está localizada.
	 * @return a cópia do pedaço do vetor original em um outro vetor.
	 * 
	 */
	private byte[] subvetor(byte[] vetorOrigem, int posicaoInicial,
			int posicaoFinal) {
		byte[] novoVetor = new byte[posicaoFinal - posicaoInicial + 1];
		int posicaoNovoVetor = 0;

		for (int i = posicaoInicial; i <= posicaoFinal; i++) {
			novoVetor[posicaoNovoVetor] = vetorOrigem[i];
			posicaoNovoVetor++;
		}

		return novoVetor;
	}

	/**
	 * Concatena dois vetores em um único vetor.
	 * 
	 * @param vetor1
	 *            o primeiro vetor a ser concatenado.
	 * @param vetor2
	 *            o segundo vetor a ser concatenado.
	 * @return um terceiro vetor correspondendo à concatenação dos dois vetores
	 *         passados como parâmetro.
	 */
	private byte[] concat(byte[] vetor1, byte[] vetor2) {
		byte[] novoVetor = new byte[vetor1.length + vetor2.length];
		int posicaoNovoVetor = 0;

		for (int i = 0; i < vetor1.length; i++) {
			novoVetor[posicaoNovoVetor] = vetor1[i];
			posicaoNovoVetor++;
		}
		for (int i = 0; i < vetor2.length; i++) {
			novoVetor[posicaoNovoVetor] = vetor2[i];
			posicaoNovoVetor++;
		}

		return novoVetor;
	}

	/**
	 * Cria uma String com o conteúdo de um arquivo, especificado pelo nome. O
	 * tamanho do buffer de leitura é de 500MB. O método é utilizado apenas para
	 * testes.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo que se quer transformar em String.
	 * @return a string propriamente dita.
	 */
	public void fileToString(String nomeArquivo) {
		Buffer buffer;
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");
			buffer = new Buffer((int) file.length());

			System.out.println("Carregando arquivo em memória...  "
					+ (file.length() % tamanhoRegistro));
			file.read(buffer.getBuffer());
			buffer.setQuantidadeDados(buffer.getBuffer().length);

			int quantidadeRegistros = 0;
			while (buffer.getPonteiro() < buffer.getQuantidadeDados()) {
//				System.out.println("Bytes faltantes para terminar o buffer: "
//						+ (buffer.getQuantidadeDados() - buffer.getPonteiro()));
				Registro registro = (Registro) Registro.deserialize(buffer
						.recuperarDados(tamanhoRegistro));
				System.out.println(registro);
				quantidadeRegistros++;

			}
//			System.out.println("Bytes faltantes para terminar o arquivo: "
//					+ (file.length() - file.getFilePointer()));
			System.out.println("Quantidade de Registro do arquivo: "
					+ quantidadeRegistros);

			file.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
