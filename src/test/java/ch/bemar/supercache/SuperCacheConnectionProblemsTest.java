package ch.bemar.supercache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.bemar.supercache.cache.impl.DefaultRemoteChannelReceiver;
import ch.bemar.supercache.cache.impl.DefaultRemoteChannelSender;
import ch.bemar.supercache.cache.impl.SuperCache;
import ch.bemar.supercache.comm.impl.CommClient;
import ch.bemar.supercache.comm.impl.CommServer;
import ch.bemar.supercache.comm.impl.IncomingTransferListener;

public class SuperCacheConnectionProblemsTest {

	private static SuperCache<String, String> cache1;
	private static SuperCache<String, String> cache2;

	@BeforeAll
	public static void beforeClass() throws IOException {
		cache1 = configureCache("localhost:6666", 6665, "MyCache1");
		cache2 = configureCache("localhost:6665", 6666, "MyCache2");

	}

	@Test
	public void testCachePreload() throws InterruptedException {

		for (int i = 1; i < 5; i++) {
			String value1 = cache1.get("" + i);
			String value2 = cache1.get("" + i);

			Assertions.assertEquals("Value " + i, value1);
			Assertions.assertEquals("Value " + i, value2);

		}

		cache1.put("hello", "world");
		String value = cache2.get("hello");

		Assertions.assertEquals("world", value);

	}

	@Test
	public void testCacheValueDistribute() throws IOException, InterruptedException {

		cache1.put("hello", "world");
		String value = cache2.get("hello");

		Assertions.assertEquals("world", value);

	}

	@Test
	public void testCacheValueRemoveDistribute() throws IOException, InterruptedException {

		Assertions.assertTrue(cache1.remove("hello"));
		String value = cache2.get("hello");

		Assertions.assertNull(value);

	}

	private static SuperCache<String, String> configureCache(String other, int port, String name) throws IOException {

		Map<String, String> preload = new HashMap<>();
		preload.put("1", "Value 1");
		preload.put("2", "Value 2");
		preload.put("3", "Value 3");
		preload.put("4", "Value 4");

		DefaultRemoteChannelReceiver<String, String> receiver = new DefaultRemoteChannelReceiver<>();

		CommClient<String, String> client = new CommClient<>(other);

		DefaultRemoteChannelSender<String, String> sender = new DefaultRemoteChannelSender<>(client);

		IncomingTransferListener<String, String> listener = new IncomingTransferListener<>(receiver);

		CommServer<String, String> server = new CommServer<>(port, listener);

		PersistenceLoadChannel<String, String> databaseChannel = new PersistenceLoadChannel<>();
		databaseChannel.preload(preload);

		SuperCache<String, String> cache = new SuperCache<>(name, databaseChannel);
		cache.registerRemoteSenderChannel(sender);

		receiver.registerCache(cache);

		return cache;

	}
}
