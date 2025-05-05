package exceptions;

public class ResourceException extends Exception {

	public ResourceException() {
		super("[!] ERROR buying an item!");
	}
	
	public ResourceException(String mensaje) {
		super(mensaje);
	}
}
