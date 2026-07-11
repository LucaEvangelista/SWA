package eva.luca.soccorso_Web.data;

import java.util.ArrayList;

public interface IDaoRead<T> {
	
	ArrayList<T> findAll();
	
	T findById(int id);

}