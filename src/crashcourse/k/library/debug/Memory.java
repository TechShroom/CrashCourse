package crashcourse.k.library.debug;

import crashcourse.k.library.util.ArrayHelper;

public class Memory {
	private static long lastF = getFree();
	private static long lastM = getMax();
	private static long lastT = getTotal();

	private Memory() {
		System.out.println("AHEM! Don't create this class!");
		byte[] chrs = "POTATO".getBytes();
		int res = 0;
		for (byte b : chrs) {
			res += b;
		}
		System.exit(res);
	}

	public static void printFree() {
		long free = getFree();
		System.out.println("FREE_MEM: " + free);
		lastF = free;
	}

	public static void printMax() {
		long max = getMax();
		System.out.println("MAX_MEM: " + max);
		lastM = max;
	}

	public static void printTotal() {
		long total = getTotal();
		System.out.println("TOTAL_MEM: " + total);
		lastT = total;
	}

	public static long getFree() {
		return Runtime.getRuntime().freeMemory();
	}

	public static long getMax() {
		return Runtime.getRuntime().maxMemory();
	}

	public static long getTotal() {
		return Runtime.getRuntime().totalMemory();
	}

	public static void printAll() {
		printFree();
		printMax();
		printTotal();
	}

	public static void comparePrint() {
		String[] lines = new String[3];
		if (getFree() < lastF) {
			lines[0] = "Free memory is less.";
		} else {
			lines[0] = "Free memory is more!";
		}

		if (getMax() < lastM) {
			lines[1] = "Max memory is less.";
		} else {
			lines[1] = "Max memory is more!";
		}

		if (getTotal() < lastT) {
			lines[2] = "Total memory is less.";
		} else {
			lines[2] = "Total memory is more!";
		}
		ArrayHelper.dump(lines);
	}

	public static void gc() {
		System.gc();
	}

}
