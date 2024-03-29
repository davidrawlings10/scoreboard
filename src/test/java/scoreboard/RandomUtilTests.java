package scoreboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomUtilTests {

	@Test
	public void test100() {
		assert(RandomUtil.occur(100.0));
	}

	@Test
	public void test0() {
		assert(!RandomUtil.occur(0.0));
	}

	@Test
	public void test50() {
		int count = 0;
		for (int i = 0; i < 10000; ++i) {
			if (RandomUtil.occur(50.0))
				count++;
		}
		System.out.println("count: "+count);
		try {
			assert(count > 4500 && count < 5500);
		} catch (Exception e) {
			System.out.println("count for 50.0% was " + count + " which was not is the expected range 4500-5500 for 10000 runs");
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test99() {
		int count = 0;
		for (int i = 0; i < 10000; ++i) {
			if (RandomUtil.occur(99.9))
				count++;
		}
		System.out.println("count: "+count);
		try {
			assert(count > 9900 && count < 10000);
		} catch (Exception e) {
			System.out.println("count for 99.9% was" + count + " which was not is the expected range 9900-10000 for 10000 runs");
			throw new RuntimeException(e);
		}
	}

	@Test
	public void test1() {
		int count = 0;
		for (int i = 0; i < 10000; ++i) {
			if (RandomUtil.occur(0.1))
				count++;
		}
		System.out.println("count: "+count);
		try {
			assert (count > 0 && count < 100);
		} catch (Exception e) {
			System.out.println("count for 0.1% was " + count + " which was not is the expected range 0-100 for 10000 runs");
			throw new RuntimeException(e);
		}
	}
}
