import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManipularArquivo {

	/**
	 * Criar um arquivo com o tamanho especificado.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo que ser� gerado.
	 * @param tamanhoArquivo
	 *            o tamanho do arquivo em bytes.
	 */
	public void gerarArquivo(String nomeArquivo, float tamanhoArquivo) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");

			while (file.length() < tamanhoArquivo) {
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
	 * original e da quantidade de mem�ria dispon�vel.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo original que se deseja ordenar.
	 * @param quantidadeMemoriaDisponivel
	 *            a quantidade de mem�ria dispon�vel, em bytes.
	 */
	public void ordenarArquivo(String nomeArquivo,
			int quantidadeMemoriaDisponivel) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");
			int tamanhoRegistro = new Registro().serialize().length;
			int memoriaBuffer, numeroArquivo;
			numeroArquivo = 0;

			if (file.length() >= quantidadeMemoriaDisponivel) {
				memoriaBuffer = quantidadeMemoriaDisponivel;
			} else {
				memoriaBuffer = (int) file.length();
			}

			while (file.getFilePointer() < file.length()) {
				numeroArquivo++;
				byte[] buffer = new byte[memoriaBuffer];
				file.read(buffer);
				List<Registro> listaRegistro = new ArrayList<>();
				for (int i = 0; i < buffer.length; i += tamanhoRegistro) {
					byte[] byteRegistro = subvetor(buffer, i, i
							+ tamanhoRegistro);
					listaRegistro.add((Registro) Registro
							.deserialize(byteRegistro));
				}

				System.out.println("Ordenando lista...");
				Collections.sort(listaRegistro);
				System.out.println("Criando arquivo...");

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
	 * Cria um arquivo � partir de uma lista de Registros com o nome
	 * especificado.
	 * 
	 * @param nomeArquivo
	 *            o nome do arquivo que ser� gerado.
	 * @param listaRegistro
	 *            a lista de onde os registros ser�o tirados.
	 */
	private void gerarArquivo(String nomeArquivo, List<Registro> listaRegistro) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");

			for (Registro registro : listaRegistro) {
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
	 * Faz uma c�pia de um peda�o de um vetor. Semelhante ao substring de
	 * String. O vetor original n�o � alterado.
	 * 
	 * @param vetorOrigem
	 *            o vetor de onde se quer copiar a informa��o.
	 * @param posicaoInicial
	 *            a posi��o inicial da informa��o desejada.
	 * @param posicaoFinal
	 *            a posi��o final da informa��o desejada.
	 * @return a c�pia do peda�o do vetor original em um outro vetor.
	 * 
	 */
	private byte[] subvetor(byte[] vetorOrigem, int posicaoInicial,
			int posicaoFinal) {
		byte[] novoVetor = new byte[posicaoFinal - posicaoInicial + 1];
		int posicaoNovoVetor = 0;

		for (int i = posicaoInicial; i < posicaoFinal; i++) {
			novoVetor[posicaoNovoVetor] = vetorOrigem[i];
			posicaoNovoVetor++;
		}

		return novoVetor;
	}

}
