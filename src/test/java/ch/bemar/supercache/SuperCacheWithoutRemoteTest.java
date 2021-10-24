package ch.bemar.supercache;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ch.bemar.supercache.cache.impl.SuperCache;

public class SuperCacheWithoutRemoteTest {

	private static SuperCache<String, String> cache;

	@BeforeAll
	public static void beforeClass() throws IOException {
		cache = configureCache("localhost:6666", 6665, "MyCache1");

	}

	@Test
	public void testCachePreload() throws InterruptedException {

		for (int i = 1; i < 5; i++) {
			String value1 = cache.get("" + i);
			String value2 = cache.get("" + i);

			Assertions.assertEquals("Value " + i, value1);
			Assertions.assertEquals("Value " + i, value2);

		}

		cache.put("hello", "world");
		String value = cache.get("hello");

		Assertions.assertEquals("world", value);

	}

	@Test
	public void testCacheValueDistribute() throws IOException, InterruptedException {

		cache.put("hello", "world");
		String value = cache.get("hello");

		Assertions.assertEquals("world", value);

	}

	@Test
	public void testCacheValueRemoveDistribute() throws IOException, InterruptedException {

		Assertions.assertTrue(cache.remove("hello"));
		String value = cache.get("hello");

		Assertions.assertNull(value);

	}

	private static SuperCache<String, String> configureCache(String other, int port, String name) throws IOException {

		Map<String, String> preload = new HashMap<>();
		preload.put("1", "Value 1");
		preload.put("2", "Value 2");
		preload.put("3", "Value 3");
		preload.put("4", "Value 4");

		PersistenceLoadChannel<String, String> databaseChannel = new PersistenceLoadChannel<>();
		databaseChannel.preload(preload);

		SuperCache<String, String> cache = new SuperCache<>(name, databaseChannel);

		return cache;

	}
}
