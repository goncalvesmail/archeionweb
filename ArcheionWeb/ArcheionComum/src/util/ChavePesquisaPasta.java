package util;

import java.util.Hashtable;


public class ChavePesquisaPasta {

	private Hashtable<Integer,Operadores[]> operadores = new Hashtable<Integer,Operadores[]>();
	
	public ChavePesquisaPasta() {
		operadores.put(1, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(2, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE} );
		operadores.put(3, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(4, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(5, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE} );
		operadores.put(6, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE, Operadores.CONTEM} );
		operadores.put(7, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE} );
		operadores.put(8, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(9, new Operadores[] {Operadores.CONTEM} );
		operadores.put(10, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(11, new Operadores[] {Operadores.IGUAL} );
		operadores.put(12, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE} );
		
	}
	
	public ChavesPasta[] getChavesPesquisa() {
		return ChavesPasta.values();
	}
	
	public ChavesPasta getChaves(int id) {
		return ChavesPasta.get(id);
	}
	
	public Operadores[] getOperadores( int idChave ) {
		return operadores.get(idChave);
	}
	
	public Operadores[] getTodosOperadores(  ) {
		return Operadores.values();
	}
	
}