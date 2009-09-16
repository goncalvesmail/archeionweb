/**
 * 
 */
package br.com.archeion.negocio.context;


/**
 * Classe responsável por inicializar e destruir o banco.
 */
public class DBContext {

	/**
	 * LOG utilizado na classe.
	 */
	//private static final Logger LOG = Logger.getLogger(DBContext.class);

	/**
	 * Recupera as informações do banco de dados a partir do properties.
	 */
	//private ResourceBundle bdConfig = ResourceBundle.getBundle("br.com.archeion.negocio.context.bdServer");

	/**
	 * Servidor do banco de dados.
	 */
	//private NetworkServerControl server;

	/**
	 * Inicializa o banco de dados.
	 */
	/*public void init() {
		LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>> Inicializando o banco de dados.");
		String start = bdConfig.getString("start");

		if (server == null && start.equals("true")) {
			try {
				final String host = bdConfig.getString("host");
				final Integer port = Integer.valueOf(bdConfig.getString("port"));

				server = new NetworkServerControl(InetAddress.getByName(host), port);
				server.start(new PrintWriter(new File(bdConfig.getString("server.log"))));
				LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>> Banco de dados ok.");
			} catch (UnknownHostException e) {
				LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>> Host do banco de dados desconhecido.");
				e.printStackTrace();
			} catch (Exception e) {
				LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>> Ocorreu um erro na hora de inicializar o banco de dados.");
				e.printStackTrace();
			}
		}
	}
	public void shutdown() {
		try {
			LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>> Shutdown no banco de dados.");
			String start = bdConfig.getString("start");
			if (start.equals("true")) {
				server.shutdown();
			}
		} catch (Exception e) {
			LOG.error(">>>>>>>>>>>>>>>>>>>>>>>>>> Ocorreu um erro na hora de dar o shutdown no banco de dados.");
			e.printStackTrace();
		}

	}*/
}
