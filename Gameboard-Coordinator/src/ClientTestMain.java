import org.json.simple.JSONObject;

import com.ifeed.gameboard.Player;

public class ClientTestMain {

	@SuppressWarnings("unchecked")
	public static void main(String args[]) {
		String host = "localhost";
		int port = 4242;
		
		Player p = new Player();
		boolean flag = p.connect(host, port);
		
		if(flag) {
			JSONObject jobj = new JSONObject();
			jobj.put("msg", "Hello!!!");
			p.send(jobj);
			System.out.println("Sent!");
		}
	}
}
