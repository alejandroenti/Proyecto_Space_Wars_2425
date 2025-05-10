package exceptions;

public class ResourceException extends Exception {

	public ResourceException() {
		super("[!] ERROR buying ");
	}
	
	public ResourceException(String mensaje) {
		super(mensaje);
	}
}
