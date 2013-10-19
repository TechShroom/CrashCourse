package crashcourse.k.library.debug;

public class Debug {
	private static Object[] useablespace = new Object[1];

	public static void forceMemoryCrash() {
		useablespace = new int[Integer.MAX_VALUE][Integer.MAX_VALUE];
		useablespace.hashCode();
		forceMemoryCrash();// just in case.
	}

	public static void fixForcedMemoryCrash() {
		useablespace = null;
		System.gc();
	}

}
