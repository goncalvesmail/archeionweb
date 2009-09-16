package br.com.archeion.resources;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class MessagesResources {
	private static Logger log = Logger.getLogger("br.com.archeion.jsf");
	private static Properties properties;

	private synchronized static void loadMessages()
	{
		if( properties==null )
		{
			try
			{
				properties = new Properties();
				// Carrega propriedades do arquivo de mensagens
				properties.load(MessagesResources.class.getResourceAsStream("messages.properties"));
			}
			catch (IOException e)
			{
				log.throwing(MessagesResources.class.getName(), "static init", e);
			}
		}
	}

	/**
	 * Pega mensagem do arquivo de mensagens
	 * 
	 * @param key
	 * @return
	 */
	public static String getBundleMessage(String key)
	{
		if( properties==null )
		{
			loadMessages();
		}
		try {
			String msg = properties.get(key).toString();
			return msg;		
			
		} catch (NullPointerException e) {
			log.throwing(MessagesResources.class.getName(), "Teste: " + key, e);
			throw e;
		}
		
	}

	/**
	 * Pega mensagem do arquivo de mensagens e troca variaveis 
	 * pelas que forem passadas como parametro
	 * 
	 * @param key
	 * @return
	 */
	public static String getBundleMessage(String key, String[] params)
	{
		String msg = getBundleMessage(key);
		for (int i = 0; i < params.length; i++)
		{
			if( params[i]!=null )
			{
				msg=msg.replaceAll("\\{"+i+"\\}", params[i]);
			}
		}
		msg = msg.replaceAll("\\{[0-9]+\\}", "");
		return msg;
	}

}
