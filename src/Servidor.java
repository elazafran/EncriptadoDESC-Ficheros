import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


public class Servidor  implements InterfazStub{
	
	public static void main(String[] args) {
		Registry reg = null; 
        try {
           reg = LocateRegistry.createRegistry(5555); 
        } catch (Exception e) {
           System.out.println ("ERROR: No se ha podido crear el registro"); 
           e.printStackTrace(); 
        }
        //Instancia del servidor
        Servidor serverObject = new Servidor(); 
        try {
     	   //Casting a la interfaz declarada como remota
           reg.rebind("Servidor_Hola", (InterfazStub) UnicastRemoteObject.exportObject (serverObject, 0)); 
        } catch (Exception e) {
           System.out.println ("ERROR: No se ha podido inscribir el objeto servidor."); 
           e.printStackTrace(); 
        }
       
		
	}
	
	@Override
	public void encriptar(SecretKey key) throws RemoteException {
		
		try{
			
		
			System.out.println("Obteniendo objeto Cipher con cifrado DES");
			Cipher desCipher = Cipher.getInstance("DES");
			System.out.println("Configurando Cipher para encriptar");
			desCipher.init(Cipher.ENCRYPT_MODE, key);
			System.out.println("Abriendo el fichero");
			
			File inf = new File("fichero_prueba.txt");
			FileInputStream is = new FileInputStream(inf);
			
			System.out.println("Abriendo el fichero cifrado");
			
			FileOutputStream os = new FileOutputStream("fichero_cifrado.txt");
			
			System.out.println("Cifrando el fichero...");
			
			byte[] buffer = new byte[8];
			int bytes_leidos = is.read(buffer);
			while(bytes_leidos != -1){
				os.write(desCipher.doFinal(buffer,0,bytes_leidos));
				bytes_leidos = is.read(buffer);
			}
			
			os.close();
			
			System.out.println("Obteniendo factoría de claves con cifrado DES");
			SecretKeyFactory keyfac = SecretKeyFactory.getInstance("DES");
			
			System.out.println("Generando keyspec");
			
			DESKeySpec keyspec = (DESKeySpec) keyfac.getKeySpec(key, DESKeySpec.class);
			
			System.out.println("Salvando la clave en un fichero");
			
			FileOutputStream cos = new FileOutputStream("clave");
			
			cos.write(keyspec.getKey());
			
			cos.close();
			
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
}
