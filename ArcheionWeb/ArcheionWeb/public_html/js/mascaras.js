<!--

	/**
	 * @todo Documente-me!
	 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
 	 * @version 2.0
	 *
	 * @changelog	26/02/2008
	 */	
	function Mascara() {
 
 		/* Letras */
		this.letras = 'ABCDEFGHIJKLMNOPQRSTUVWXYZÇabcdefghijklmnopqrstuvwxyzç',
		
		/* Letras Maiúsculas */
		this.letrasUpperCase = 'ABCDEFGHIJKLMNOPQRSTUVWXYZÇ',
		
		/* Letras Minúsculas */
		this.letrasLowerCase = 'abcdefghijklmnopqrstuvwxyzç',
		
		/* Números */		
		this.numeros = '0123456789',
		
		/* Separadores */
		this.fixos = '$().-:,/ ',
		
		/* Conjunto de Caracteres */
		this.charset = ' !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\]^_/`abcdefghijklmnopqrstuvwxyz{|}~',

		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @param pattern
		 * @return boolean
		 */
		this.process = function ( obj , e , pattern , maxlength ) { 	
		
			e = dtp.getEvent( e );
			
			var value = obj.value;
			
			if ( e && this.isNotSpecialChars( e ) ) {
			
			 var ntecla = (e.which) ? e.which : e.keyCode;
			 tecla = this.charset.substr(ntecla - 32, 1);
			 if (ntecla < 32) return true;
			
			 var tamanho = value.length;
			 if (tamanho >= pattern.length) dtp.die( e );
			
			 var pos = pattern.substr(tamanho,1);
			
			 var flag = false;
			 
			 while (this.fixos.indexOf(pos) != -1) {
			  value += pos;
			  tamanho = value.length;
			  if (tamanho >= pattern.length) return false;
			  pos = pattern.substr(tamanho,1);
			 }
			 
			 switch (pos) {
			   case '#' : if (this.numeros.indexOf(tecla) == -1) dtp.die( e ); break;
			   case 'A' : if (this.letrasUpperCase.indexOf(tecla) == -1) dtp.die( e ); break;
			   case 'a' : if (this.letrasLowerCase.indexOf(tecla) == -1) dtp.die( e ); break;
			   case 'Z' : if (this.letras.indexOf(tecla) == -1) dtp.die( e ); break;
			   case '*' : obj.value = value; return true; break;
			   default  : dtp.die( e ); break;
			 }
			 
			}
			
			obj.value = value;
			
			return true;
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isNIT = function ( obj , e ) {

			return this.process( obj , e , '###.#####.##-#' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isPIS = function ( obj , e ) {

			return this.isNIT( obj , e );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isPASEP = function ( obj , e ) {

			return this.NIT( obj , e );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isCNPJ = function ( obj , e ) {

			return this.process( obj , e , '##.###.###/####-##' );
		
		},	
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isCEI = function ( obj , e ) {

			return this.process( obj , e , '##.###.###.###-#' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e
		 * @return boolean
		 */
		this.isCPF = function ( obj , e ) {

			return this.process( obj , e , '###.###.###-##' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isCEP = function ( obj , e ) {

			return this.process( obj , e , '#####-###' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isDDD = function ( obj , e ) {

			return this.process( obj , e , '###' );
		
		},
		
		/*
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isRamal = function ( obj , e ) {

			return this.process( obj , e , '####' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isTelefoneComDDD = function ( obj , e ) {

			return this.process( obj , e , '(##) ####-####' );
		
		},
		

		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isHora = function ( obj , e ) {

			return this.process( obj , e , '##:##' );
		
		},

		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isTelefoneSemDDD = function ( obj , e ) {

			return this.process( obj , e , '####-####' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isCBO = function ( obj , e ) {

			return this.process( obj , e , '#####-#' );
		
		},
		
		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isData = function ( obj , e ) {

			return this.process( obj , e , '##/##/####' );
		
		},
		
		/**
		 * Máscara para percentual
		 * Mínimo: 00,00
		 * Máximo: 99,99
		 * 
		 * @author Jônatas Carvalho Barroso ( jonatas.barroso@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isPercentual = function ( obj , e ) {

			return this.process( obj , e , '##,##' );
		
		},
		
        /**
         * @todo Documente-me!
         * @author Washington Santiago da Silva ( washington.dasilva@previdencia.gov.br )
         * @param cur
         * @param len 
         * @return string
         */
		this.monetario = function ( cur , len )
		{
		   
		 if (Number(cur.value)<0){
		  sinal='-';
		 } else {
		  sinal='';
		  }
		   
		   n='__0123456789';
		   d=cur.value;
		   l=d.length;
		   r='';
		   if (l > 0)
		   {
		    z=d.substr(0,l-1);
		    s='';
		    a=2;
		    for (i=0; i < l; i++)
		    {
		        c=d.charAt(i);
		        if (n.indexOf(c) > a)
		        {
		            a=1;
		            s+=c;
		        };
		    };
		    l=s.length;
		    t=len-1;
		    if (l > t)
		    {
		        l=t;
		        s=s.substr(0,t);
		    };
		    if (l > 2)
		    {
		        r=s.substr(0,l-2)+','+s.substr(l-2,2);
		    }
		    else
		    {
		        if (l == 2)
		        {
		            r='0,'+s;
		        }
		        else
		        {
		            if (l == 1)
		            {
		                r='0,0'+s;
		            };
		        };
		    };
		    if (r == '')
		    {
		        r='0,00';
		    }
		    else
		    {
		        l=r.length;
		        if (l > 6)
		        {
		            j=l%3;
		            w=r.substr(0,j);
		            wa=r.substr(j,l-j-6);
		            wb=r.substr(l-6,6);
		            if (j > 0)
		            {
		                w+='.';
		            };
		            k=(l-j)/3-2;
		            for (i=0; i < k; i++)
		            {
		                w+=wa.substr(i*3,3)+'.';
		            };
		            r=w+wb;
		        };
		    };
		   };
		   if (r.length <= len)
		   {
		    cur.value=r;
		   }
		   else
		   {
		    cur.value=z;
		   };
		   
		   cur.value = sinal + cur.value;
		    
		   return 'ok';
		},

		/**
		 * @todo Documente-me!
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isMoney = function ( obj , e ) {

			return this.process( obj , e , 'R$ #####.##' );
		
		},
		
		this.reset = function ( obj , charset ) {
		
			obj.value.replaceAll( charset , "" );
			
		},
		
		this.onlyNumber = function ( obj , e ) {
		
			var keyCode = dtp.getKey( e );
			
			//alert( keyCode );
		
			if ( this.isNotSpecialChars( e ) && this.isNotNumber( keyCode ) ) {
			
				dtp.die( e );
			
			}
			
			return obj;
		
		}, 
		
		this.isNumber = function ( keyCode ) {		
			
			return ( keyCode >= 48 /* 0 */ && keyCode <= 57 /* 9 */ );
		
		},
		
		this.isNotNumber = function( keyCode ) {
		
			return !( this.isNumber( keyCode ) );
		
		},
		
		/**
		 * Verifica se a tecla pressionada é um caractere especial.
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @since XX/XX/2008
		 * @param obj
		 * @param e	
		 * @return boolean
		 */
		this.isSpecialChars = function ( e ) {
		
			//alert( e.keyCode );
		
			return ( e.keyCode && ( e.keyCode == 8  /* Back space */||
									e.keyCode == 9  /* Tab */ 		||
								    /*e.keyCode == 32 				||*/
								    e.keyCode == 33 /* Page Up */	||
								    e.keyCode == 34 /* Page Down */	||
								    e.keyCode == 35 /* End */  		||
								    e.keyCode == 36 /* Home */ 		||
								    e.keyCode == 37 /* Left */		||
								    e.keyCode == 38 /* Up */		||
								    e.keyCode == 39	/* Right */		||
								    e.keyCode == 40 /* Down */		||
								    e.keyCode == 45 /* Insert */ 	||								  
								  	e.keyCode == 46 /* Delete */
								  )
					);
		
		},

		/**
		 * Verifica se a tecla pressionada NÃO é um caractere especial.
		 * @author Ricardo José Ramalho Moreira ( ricardo.ramalho@previdencia.gov.br )
		 * @since XX/XX/2008
		 * @param obj
		 * @param e	
		 * @return boolean
		 */		
		this.isNotSpecialChars = function ( e ) {
		
			return !( this.isSpecialChars( e ) );
		
		},
				
		this.money = function ( obj , tammax , e ) {
	 
	 		obj = dtp.get( obj );
	 		
			var tecla = dtp.getKey( e );
				
			vr = obj.value;
			
			vr = vr.replace( "/", "" );
			vr = vr.replace( "/", "" );
			vr = vr.replace( ",", "" );
			vr = vr.replace( ".", "" );
			vr = vr.replace( ".", "" );
			vr = vr.replace( ".", "" );
			vr = vr.replace( ".", "" );
			tam = vr.length;
		
			if (tam < tammax && tecla != 8){ tam = vr.length + 1 ; }
		
			if (tecla == 8 ){  tam = tam - 1 ;	}
		
			if ( (tecla == 8) || (tecla >= 48 && tecla <= 57 )){
				with(campo){
					if ( tam <= 2 ){
			 			value = vr ;
			 		} else {
			 			value = vr.substr( 0, tam - 2 ) + ',' + vr.substr( tam - 2, tam ) ;
			 		}
			 	}
			 	return true
			} 
			if ((tecla == 9) || (tecla ==45) || (tecla == 46) || (tecla <= 40 && tecla >= 33)){
				return true;
			}
			
			return false;
			
		}
	
	}


	this.isEmail = function checkMail(mail){
    	var er = new RegExp(/^[a-z0-9_\-\.]+@[a-z0-9_\-\.]{2,}\.[a-z0-9]{2,}(\.[a-z0-9])?/);
    	if(typeof(mail) == "string"){
        	if(er.test(mail)){ return true; }
    	}else if(typeof(mail) == "object"){
        	if(er.test(mail.value)){
                return true;
            	    }
    	}else{
        return false;
        }
}

	try {
	
		dtp.mascara = new Mascara();
	
	} catch ( e ) {
	
		alert( "[Error] Não foi possível carregar a biblioteca dtp.mascaras.\r\n" + e.toString() );
		 
	}
	
	function format( obj , e ) {
		dtp.mascara.isTIPOCAMPO( obj , e );
	}
//-->