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

public class test {

	private static final float GIGA_SIZE = 1073741824;
	private static final float QUANT_GIGA = 1;
	private static final String FILE_NAME = "D:\\TPA\\test.bin";

	public static void main(String args[]) {
		escrever3();
	}

	private static void metodo1() {
		try {
			RandomAccessFile file = new RandomAccessFile(FILE_NAME, "rw");

			for (int i = 0; i < QUANT_GIGA; i++) {
				while (file.length() < GIGA_SIZE) {
					System.out.println(file.length());
					Registro registro = new Registro();
					file.write(registro.serialize());
				}
				System.out.println(i + " giga criados!");
			}

			System.out.println("registros inseridos: " + Registro.quantidade);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void metodo2() {

		// escrever2();
		ler2();
	}

	public static void escrever2() {
		File file = new File(FILE_NAME);
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		DataOutputStream dos = null;

		try {
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			dos = new DataOutputStream(bos);

			for (int i = 0; i < QUANT_GIGA; i++) {
				while (dos.size() < GIGA_SIZE) {
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
		File file = new File(FILE_NAME);
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
		File file = new File(FILE_NAME);
		ObjectOutputStream oos = null;
		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);

			for (int i = 0; i < QUANT_GIGA; i++) {
				double buffer = 0;
				while (buffer < GIGA_SIZE) {
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
