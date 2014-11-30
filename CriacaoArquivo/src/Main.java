import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

public class Main {

	private static final long TAMANHO_INTEIRO = 1073741824;
	private static final long TAMANHO_GIGA = 1073741824;
	private static final long QUANTIDADE_GIGA = 10;
	private static final String NOME_ARQUIVO = "D:\\TPA\\10Giga.bin";

	public static void main(String args[]) {
		escrita1();
//		leitura1();
	}

	private static void escrita1() {
		try {
			RandomAccessFile file = new RandomAccessFile(NOME_ARQUIVO, "rw");

			for (int i = 0; i < QUANTIDADE_GIGA; i++) {
				while (file.length() < TAMANHO_GIGA*(i+1)) {
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
	
	private static void leitura1(){
		try {
			RandomAccessFile file = new RandomAccessFile(NOME_ARQUIVO, "r");

			int tamanhoRegistro = new Registro().serialize().length;
			
			for (int i = 0; i < 100; i++) {
				byte[] buffer = new byte[tamanhoRegistro];
				
				file.read(buffer);
				
				Registro registro = (Registro) Registro.deserialize(buffer);
				
				System.out.println(registro);
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

	private static void metodo2() {

		// escrever2();
		ler2();
	}

	public static void escrever2() {
		File file = new File(NOME_ARQUIVO);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;

		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			dos = new DataOutputStream(bos);

			for (int i = 0; i < QUANTIDADE_GIGA; i++) {
				while (dos.size() < TAMANHO_GIGA) {
					byte[] registoByte = new Registro().serialize();
					dos.write(registoByte);
				}
				dos.flush();
				System.out.println(i + 1 + "° giga criado.");
			}

			fos.close();
			bos.close();
			dos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void ler2() {
		File file = new File(NOME_ARQUIVO);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);

			bis = new BufferedInputStream(fis);
			// dis = new DataInputStream(bis);

			while (bis.available() > 0) {
				System.out.println(dis.available());
				// System.out.println(Files.r);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				bis.close();
				// dis.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void escrever3() {
		File file = new File(NOME_ARQUIVO);
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);

			for (int i = 0; i < QUANTIDADE_GIGA; i++) {
				double buffer = 0;
				while (buffer < TAMANHO_GIGA) {
					Registro registro = new Registro();
					oos.writeObject(registro);
					buffer = registro.serialize().length;
				}
				oos.flush();
				System.out.println(i + 1 + "° giga criado.");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

}
