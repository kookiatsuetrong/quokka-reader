import java.io.InputStream;
import java.net.URL;
import java.time.Instant;
import java.math.BigDecimal;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.persistence.Persistence;

class Start {
	public static void main(String[] data) {
		while (true) {
			Engine btc = new Engine("BTC/USD", "BTC-USD");
			btc.start();
			Engine eth = new Engine("ETH/USD", "ETH-USD");
			eth.start();
			try {
				Thread.sleep(10000);
			} catch (Exception e) { }
		}
	}
}

class Engine extends Thread {
	
	String symbol;
	String target;
	
	Engine(String symbol, String target) {
		this.symbol = symbol;
		this.target = target;
	}
	
	@Override
	public void run() {
		Price p = new Price();
		p.symbol = symbol;
		p.time = Instant.now();
		
		String base = "https://api.coinbase.com";
		try {
			String path = "/v2/prices/" + target + "/buy";
			URL url = new URL(base + path);
			InputStream input = url.openStream();
			JsonReader reader = Json.createReader(input);
			JsonObject object = reader.readObject().getJsonObject("data");
			String s = object.getString("amount");
			p.buy = new BigDecimal(s);
		} catch (Exception e) {
			System.out.println(e);
		}
		try {
			String path = "/v2/prices/" + target + "/sell";
			URL url = new URL(base + path);
			InputStream input = url.openStream();
			JsonReader reader = Json.createReader(input);
			JsonObject object = reader.readObject().getJsonObject("data");
			String s = object.getString("amount");
			p.sell = new BigDecimal(s);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		var factory = Persistence.createEntityManagerFactory("main");
		var manager = factory.createEntityManager();
		System.out.println(p.time + " " + p.symbol + " " + p.buy);
		manager.getTransaction().begin();
		manager.persist(p);
		manager.getTransaction().commit();
		manager.close();
		factory.close();
	}
}