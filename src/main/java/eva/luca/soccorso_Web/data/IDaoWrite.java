package eva.luca.soccorso_Web.data;

public interface IDaoWrite<T> {
	
	boolean insert (T t); //insert object
	
	boolean delete (int i); //delete by id
	
	boolean update (T t); //update object
	
	
}
