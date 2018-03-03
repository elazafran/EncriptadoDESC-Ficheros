import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;



public class Cliente {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String elHost=null;
		try
		{
			
			// Se localiza el servidor en el registro por su nombre “Servidor_Hola”·
			Registry registry = LocateRegistry.getRegistry("localhost", 5555);
			InterfazStub stub = (InterfazStub) registry.lookup("Servidor_Hola");
			// Se invoca el servicio remoto
			/**
			 * creamos las clave DES
			 * 
			 */
			System.out.println("Obteniendo generador de claves con cifrado DES");
			KeyGenerator keygen = KeyGenerator.getInstance("DES");
			System.out.println("Generando clave");
			SecretKey key = keygen.generateKey();
			
			
			stub.encriptar(key);
			
			/**
			 * desencriptamos 
			 */
			
			File cinf = new File("clave");
			FileInputStream cis = new FileInputStream(cinf);
			byte[] clave = new byte[(int) cinf.length()];
			cis.read(clave);

			DESKeySpec keyspec = new DESKeySpec(clave);
			SecretKeyFactory keyfac = SecretKeyFactory.getInstance("DES");

			SecretKey key2 = keyfac.generateSecret(keyspec);

			Cipher desCipher = Cipher.getInstance("DES");

			desCipher.init(Cipher.DECRYPT_MODE, key2);

			File inf = new File("fichero_cifrado.txt");
			FileInputStream is = new FileInputStream(inf);


			FileOutputStream os = new FileOutputStream("fichero_descifrado.txt");


			byte[] buffer = new byte[16];
			int bytes_leidos = is.read(buffer);
			while(bytes_leidos != -1){
				os.write(desCipher.doFinal(buffer,0,bytes_leidos));
				bytes_leidos = is.read(buffer);
			}

			os.close();
			

		} 
		catch
		(Exception e) {System.err.println("Excepción del cliente: " + e.toString());}
	}


	
}
