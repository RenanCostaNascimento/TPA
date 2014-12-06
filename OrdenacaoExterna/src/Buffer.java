
public class Buffer {

	private byte[] buffer;
	private int quantidadeDados;
	private int ultimoDadoLido;

	public Buffer(int tamanhoBuffer){
		buffer = new byte[tamanhoBuffer];
//		quantidadeDados = 0;
		ultimoDadoLido = -1;
	}

//	public int getQuantidadeDados() {
//		return quantidadeDados;
//	}
	public int getUltimoDadoLido() {

		return ultimoDadoLido;
	}
	public byte[] getBuffer() {
		return buffer;
	}
	

	public void adicionarDados(byte[] serialize) {
		// TODO Auto-generated method stub
		
	}

	public byte[] recuperarDados(int tamanhoRegistro) {
		return buffer;
		
	}

	
}
