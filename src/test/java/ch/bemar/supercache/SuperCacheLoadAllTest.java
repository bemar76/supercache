package ch.bemar.supercache;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.bemar.supercache.cache.impl.SuperCache;

public class SuperCacheLoadAllTest {

	private static SuperCache<String, String> cache1;
	private static SuperCache<String, String> cache2;
	private static PersistenceLoadChannel<String, String> databaseChannel;

	@BeforeAll
	public static void beforeClass() throws IOException {
		databaseChannel = new PersistenceLoadChannel<>();

		cache1 = configureCache("localhost:6666", 6665, "MyCache1");

	}

	@Test
	public void testCachePreload() throws InterruptedException, IOException {

		for (int i = 1; i < 5; i++) {

			cache1.put("key" + i, "value" + i);

		}

		cache2 = configureCache("localhost:6665", 6666, "MyCache2");

		for (int i = 1; i < 5; i++) {

			Assertions.assertEquals("value" + i, cache2.get("key" + i));

		}

	}

	private static SuperCache<String, String> configureCache(String other, int port, String name) throws IOException {

		SuperCache<String, String> cache = new SuperCache<>(name, databaseChannel);

		return cache;

	}
}
