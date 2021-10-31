package ch.bemar.supercache;

import java.io.IOException;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bemar.supercache.cache.impl.DefaultRemoteChannelReceiver;
import ch.bemar.supercache.cache.impl.DefaultRemoteChannelSender;
import ch.bemar.supercache.cache.impl.SuperCache;
import ch.bemar.supercache.comm.impl.CommClient;
import ch.bemar.supercache.comm.impl.CommServer;
import ch.bemar.supercache.comm.impl.IncomingTransferListener;

public class SuperCacheSpeedTest {

	private static Logger LOGGER = LoggerFactory.getLogger(SuperCacheSpeedTest.class);

	private static SuperCache<String, String> cache1;
	private static SuperCache<String, String> cache2;

	private static PersistenceLoadChannel<String, String> database;

	@BeforeAll
	public static void beforeClass() throws IOException {

		database = new PersistenceLoadChannel<>();

		cache1 = configureCache("localhost:6666", 6665, "MyCache1");
		cache2 = configureCache("localhost:6665", 6666, "MyCache2");

	}

	@Test
	public void testCache10000Rounds() throws IOException, InterruptedException {

		ThreadGroup tg = new ThreadGroup("putters");

		Thread t1 = new Thread(tg, new Runnable() {

			@Override
			public void run() {
				putToCache(cache1);

			}
		});

		Thread t2 = new Thread(tg, new Runnable() {

			@Override
			public void run() {
				putToCache(cache2);

			}
		});

		StopWatch sw = StopWatch.createStarted();

		t1.start();
		t2.start();

		while (tg.activeCount() > 0) {
			Thread.currentThread().sleep(500);
		}

		sw.stop();

		LOGGER.info("Das hat {} gedauert", sw.formatTime());

		for (int i = 0; i < 10000; i++) {

			String value1 = database.get(cache1.getName() + "-Key-" + i);
			Assertions.assertEquals(cache1.getName() + "-Value-" + i, value1);

			String value2 = database.get(cache2.getName() + "-Key-" + i);
			Assertions.assertEquals(cache2.getName() + "-Value-" + i, value2);

			value1 = cache1.get(cache1.getName() + "-Key-" + i);
			Assertions.assertEquals(cache1.getName() + "-Value-" + i, value1);

			value2 = cache2.get(cache2.getName() + "-Key-" + i);
			Assertions.assertEquals(cache2.getName() + "-Value-" + i, value2);

		}

	}

	private void putToCache(SuperCache cache) {

		for (int i = 0; i < 10000; i++) {
			cache.put(cache.getName() + "-Key-" + i, cache.getName() + "-Value-" + i);
		}

	}

	private static SuperCache<String, String> configureCache(String other, int port, String name) throws IOException {

		DefaultRemoteChannelReceiver<String, String> receiver = new DefaultRemoteChannelReceiver<>();

		CommClient<String, String> client = new CommClient<>(other);

		DefaultRemoteChannelSender<String, String> sender = new DefaultRemoteChannelSender<>(client);

		IncomingTransferListener<String, String> listener = new IncomingTransferListener<>(receiver);

		CommServer<String, String> server = new CommServer<>(port, listener);

		SuperCache<String, String> cache = new SuperCache<>(name, database);
		cache.registerRemoteSenderChannel(sender);

		receiver.registerCache(cache);

		return cache;

	}
}
