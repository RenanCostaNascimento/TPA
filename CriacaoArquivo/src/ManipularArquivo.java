import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ManipularArquivo {

	/**
	 * Criar um arquivo com o tamanho especificado.
	 * @param nomeArquivo o nome do arquivo que será gerado.
	 * @param tamanhoArquivo o tamanho do arquivo em Giga.
	 */
	public void gerarArquivo(String nomeArquivo, float tamanhoArquivo) {
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "rw");

			for (int i = 0; i < tamanhoArquivo; i++) {
				while (file.length() < Constantes.TAMANHO_GIGA*(i+1)) {
					Registro registro = new Registro();
					file.write(registro.serialize());
				}
				System.out.println(i+1 + "° giga criado!");
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
	
	public void ordenarArquivo(String nomeArquivo, int quantidadeMemoriaDisponivel){
		try {
			RandomAccessFile file = new RandomAccessFile(nomeArquivo, "r");

			int tamanhoRegistro = new Registro().serialize().length;
			
			byte[] memoriaDisponivel = new byte[quantidadeMemoriaDisponivel];
			file.read(memoriaDisponivel);
			List<Registro> listaRegistro = new ArrayList<>();
			for (int i = 0; i < memoriaDisponivel.length; i+= tamanhoRegistro) {
				byte[] byteRegistro = subvetor(memoriaDisponivel, i, i+tamanhoRegistro);
				listaRegistro.add((Registro) Registro.deserialize(byteRegistro));
			}
			
			Collections.sort(listaRegistro);
		
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
	 *	Faz uma cópia de um pedaço de um vetor. Semelhante ao substring de String. O vetor original não é alterado.
	 * @param vetorOrigem o vetor de onde se quer copiar a informação.
	 * @param posicaoInicial a posição inicial da informação desejada.
	 * @param posicaoFinal a posição final da informação desejada.
	 * @return a cópia do pedaço do vetor original em um outro vetor.
	 * 
	 */
	private byte[] subvetor(byte[] vetorOrigem, int posicaoInicial, int posicaoFinal){
		byte[] novoVetor = new byte[posicaoFinal - posicaoInicial + 1];
		int posicaoNovoVetor = 0;
		
		for (int i = posicaoInicial; i < posicaoFinal; i++) {
			novoVetor[posicaoNovoVetor] = vetorOrigem[i];
			posicaoNovoVetor++;
		}
		
		return novoVetor;
	}
	
}
