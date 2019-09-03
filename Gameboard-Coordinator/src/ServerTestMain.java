import org.json.simple.JSONObject;

import com.ifeed.gameboard.Coordinator;
import com.ifeed.gameboard.Player;

public class ServerTestMain {
	
	public static void main(String args[]) {
		int port = 4242;

		Coordinator c = new Coordinator();
		c.start(port);
		Player p = c.accept();
		
		JSONObject jobj = p.receive();
		System.out.println(jobj.get("msg"));
	}
}
