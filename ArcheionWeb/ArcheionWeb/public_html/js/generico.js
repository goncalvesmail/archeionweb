/*
 *  Empresa  : DATAPREV
 *  Tipo     : Arquivo de funções javascript de uso generico
 *  Arquivo  : generico.js
 *  Autor    : Alexandre Dung - BRQ
 *  Criação :  21/10/2005
 */
 
/*
 * [Esta função JS verifica se cada caracter digitado é numérico]
 *
 * @param obj            [referencia ao objeto corrente] 
 * @param aceitaVirgula 
 *
 * @return [true se for tecla válida (numérico / vírgula), senão false]
 */
 
function verificaNumerico (obj, aceitaVirgula) 
{ 
   var inputKey = event.keyCode; 
   var returnCode = true; 
  
   if (( inputKey > 47 && inputKey < 58 ) || (inputKey == 44 && aceitaVirgula && obj.value.indexOf(",") == -1))
   { 
      return; 
   }
   else 
   { 
      returnCode = false;
      event.keyCode = 0; 
   } 
   event.returnValue = returnCode; 
}


/*
 * [Esta função formata a data no padrão dd/mm/aaaa incluindo as barras]
 * [na medida em que vai sendo digitada
 *
 * @param obj [contém a referencia ao objeto corrente]
 *
 */
function formataData(obj) 
{ 
   var currValue = obj.value;
   var ultimoChar = obj.value.charAt(obj.value.length-1);
   var a = currValue.split ("/").join("");
   
   // Backspace
   if (event.keyCode == 8 && (obj.value.length == 2 || obj.value.length == 5))
   {  
     // Este if eh assim mesmo, para tratar o caso do BackSpace
   } 
   else 
     if  (a.length > 3 )
     { 
       obj.value = a.substr(0,2) + "/" + a.substr(2,2) + "/" + a.substr(4);
     }   
     else
     {
       if ( a.length > 1 ) 
        {
          obj.value = a.substr(0,2) + "/" + a.substr(2) ;
        }  
     }   
}

/*
 * [Esta função formata a data(Mes/Ano) no padrão mm/aaaa incluindo as barras]
 * [na medida em que vai sendo digitada
 *
 * @param obj [contém a referencia ao objeto corrente]
 *
 */
function formataMesAno(obj) 
{ 
   var currValue = obj.value;
   var ultimoChar = obj.value.charAt(obj.value.length-1);
   var a = currValue.split ("/").join("");
   
   // Backspace
   if (event.keyCode == 8 && (obj.value.length == 2 || obj.value.length == 5))
   {  
     // Este if eh assim mesmo, para tratar o caso do BackSpace
   } 
   else 
     if  (a.length > 1 )
     { 
       obj.value = a.substr(0,2) + "/" + a.substr(2);
     }   
    
}



function excluir(msg) {
	if (confirm(msg)) {
		return true;
	}
	return false;
}

function escondeElemento( id )
{
	var elemento = document.getElementById(id);
	if( elemento!=null )
	   elemento.style.display='none';
}

function escondeElementos(idPrefix,start,end)
{
   for(i=start;i<=end;i++)
   {
      escondeElemento(idPrefix+''+i);
   }
}

function mostraElemento(id)
{
	var elemento = document.getElementById(id);
	if( elemento!=null )
	   elemento.style.display='block';
}

function mostraElementos( idPrefix, start, end )
{
   for(i=start;i<=end;i++)
   {
      mostraElemento(idPrefix+''+i);
   }
}
