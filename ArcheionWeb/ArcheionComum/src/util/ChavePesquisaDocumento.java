package util;

import java.util.Hashtable;


public class ChavePesquisaDocumento {

	
	private Hashtable<Integer,Operadores[]> operadores = new Hashtable<Integer,Operadores[]>();
	
	public ChavePesquisaDocumento() {
		operadores.put(1, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(2, new Operadores[] {Operadores.IGUAL, Operadores.MAIOR, Operadores.MAIORIGUAL, 
				Operadores.MENOR, Operadores.MENORIGUAL, Operadores.DIFERENTE} );
		operadores.put(3, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(4, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(5, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );	
		operadores.put(6, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(7, new Operadores[] {Operadores.CONTEM} );
		operadores.put(8, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(9, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );
		operadores.put(10, new Operadores[] {Operadores.IGUAL, Operadores.CONTEM} );	
	}
	
	public ChaveDocumento[] getChaveDocumento() {
		return ChaveDocumento.values();
	}
	
	public ChaveDocumento getChaves(int id) {
		return ChaveDocumento.get(id);
	}
	
	public Operadores[] getOperadores( int idChave ) {
		return operadores.get(idChave);
	}
	
	public Operadores[] getTodosOperadores(  ) {
		return Operadores.values();
	}

}