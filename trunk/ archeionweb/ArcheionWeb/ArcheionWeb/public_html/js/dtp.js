<!--

	/**
	 * @todo Documente-me!
	 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
 	 * @version 0.01a
	 *
	 * @changelog	26/02/2008	Classe criada.
	 */	
	var dtp = {
	
		version : '0.01a',
		
		getEvent : function ( e ) {

			return ( e ) ? e : ( ( window.event ) ? window.event : void( 0 ) );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @param pattern
		 * @return boolean
		 */
		getKey : function ( e ) {
			
			return e.keyCode || e.which || e.charCode || void( 0 );
			
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @param pattern
		 * @return boolean
		 */
		showElement : function ( obj , e ) { 
		
			this.get( obj ).style.display = "block";
							
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		hideElement : function ( obj , e ) { 
		
			this.get( obj ).style.display = "none";
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @param flag	
		 * @return boolean
		 */
		troggleElement : function ( obj , e , force ) { 
			
			var flag = ( force instanceof Boolean && force ) || ( force.match( /[01]/ ) == "1" );
		
			if ( flag ) {
			
				this.showElement( obj , e );
			
			}
			
			else {
			
				this.hideElement( obj , e );
			
			}
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param value
		 * @param pattern
		 * @param e
		 * @return Object
		 */
		toggleElement : function ( obj , value , pattern , e ) {

			flag = value.match( pattern );
				
			if ( flag ) {
			 
				this.showElement( obj , e ); 
			
			} else {
				
				this.hideElement( obj , e );
			
			}
			
			return obj;
		
		},
		
		get : function ( id ) {
		
		 	var t = ( id instanceof Object ) ? id : document.getElementById( id );
		 
			return ( id instanceof Object ) ? id : document.getElementById( id );
		
		},
		
		reset : function ( obj , valueDefault ) {
		
			if ( obj instanceof Input ) {
			
			}
		
		},
		
		die : function ( e ) {
		
			if ( document.all ) { 
	
				e.returnValue = false; 
			
			} else { 
	
				e.preventDefault(); 
			
			}
			
		}
	
	};
	
	/**
	 * @todo Documente-me!
	 * @author Ricardo José Ramalho Moreira ( ricardojrm@gmail.com )
	 */	
	/*try {
	
		var die = function () {
		
			if ( document.all ) { 
	
				this.returnValue = false; 
			
			} else { 
	
				this.preventDefault(); 
			
			}
			
		}
				
		if ( document.all ) {
		
			alert( 'ok1' );
			alert( new Event() );
			//window.onkeypress.prototype.die = function () { die(); };
			alert( 'ok2' );
			
		} else {
		
			Event.prototype.die = die;
		
		}
			
	} catch ( e ) {

		//alert( "[Error] Não foi possível criar o método die() no objeto \"event\".\n\r" + e.toString() );	
		
	}*/

	/**
	 * @todo Documente-me!
	 * @author Ricardo José Ramalho Moreira ( ricardojrm@gmail.com )
	 */
	String.prototype.replaceAll = function ( pattern , replace ) {
    	var str = this;
    	var pos = str.indexOf(pattern);
    	while (pos > -1){
			str = str.replace(pattern, replace);
			pos = str.indexOf(pattern);
		}
    	return (str);
	}
	
	//document.onreadystatechange = function () { alert( "T" );}

//-->