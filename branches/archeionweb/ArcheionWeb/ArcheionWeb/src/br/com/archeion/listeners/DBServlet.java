package br.com.archeion.listeners;

import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class DBServlet extends HttpServlet {
	/*private ResourceBundle bdConfig = ResourceBundle
			.getBundle("br.com.archeion.negocio.context.bdServer");
	private NetworkServerControl server;

	@Override
	public void destroy() {
		try {
			String start = bdConfig.getString("start");
			if (start.equals("true")) {
				server.shutdown();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.destroy();
	}

	@Override
	public void init() throws ServletException {
		try {
			String start = bdConfig.getString("start");
			if (start.equals("true")) {
				server = new NetworkServerControl(InetAddress
						.getByName(bdConfig.getString("host")), new Integer(
						bdConfig.getString("port")));
				server.start(new PrintWriter(new File(bdConfig
						.getString("server.log"))));

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.init();
	}*/

}
