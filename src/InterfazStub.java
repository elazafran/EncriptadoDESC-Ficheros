import java.rmi.Remote;
import java.rmi.RemoteException;

import javax.crypto.SecretKey;

public interface InterfazStub extends Remote{
	
	public void encriptar(SecretKey key) throws RemoteException;

}
