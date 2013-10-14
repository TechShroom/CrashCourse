package crashcourse.k.library.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

@SuppressWarnings("unchecked")
public class ArrayHelper {
	public static <T, D> T[] arrayTranslate(Class<T> generic, D[] src) {
		ArrayList<T> temp = new ArrayList<T>(src.length);
		for (D o : src) {
			if (o != null && generic.isAssignableFrom(o.getClass())) {
				temp.add(generic.cast(o));
			} else if (o == null) {
				temp.add((T) null);
			} else {
				System.err.println("Lost " + o + " because it was not of type "
						+ generic.getName());
			}
		}
		return temp.toArray((T[]) Array.newInstance(generic, src.length));
	}

	public static <T> T[] randomArray(T[] in) {
		if (in.length == 0 || in.length == 1) {
			return in;
		}
		T[] test = (T[]) Array.newInstance(in.getClass().getComponentType(),
				in.length);
		boolean solved = false;
		boolean[] taken = new boolean[test.length];
		int total = test.length;
		Random r = new Random(new Random().nextInt());
		int index = 0;
		while (!solved) {
			int ra = r.nextInt(test.length);
			if (!taken[ra]) {
				test[ra] = in[index];
				taken[ra] = true;
				index++;
				total--;
			}
			if (total == 0) {
				solved = true;
			}
		}
		return test;
	}

	public static void dump(Object[] array) {
		for (Object t : array) {
			System.out.println(t);
		}
	}

	public static <T> T[] repeatRandomArray(T[] in, int count) {
		T[] array = (T[]) Array.newInstance(in.getClass().getComponentType(),
				in.length);
		System.arraycopy(in, 0, array, 0, in.length);
		while (count > -1) {
			array = randomArray(array);
			count--;
		}
		return array;
	}

	public static String dump0(Object[] array) {
		if (array == null) {
			return "<null array>";
		}
		String ret = "[";
		for (Object o : array) {
			ret += o + ", ";
		}
		ret = ret.substring(0, ret.length() - 1) + "]";
		return ret;
	}
	/**
	 * I don't know what this does
	 * @param in - i have no idea
	 * @param outtype - i still have no idea
	 * @return - an int array
	 */
	public static int[] specificTraslate(byte[] in, int[] outtype) {
		if (in != null && in.length > 0) {
			int[] out = outtype.length >= in.length ? outtype
					: new int[in.length];
			int index = 0;
			for (byte b : in) {
				out[index] = b;
				index++;
			}
			return out;
		}
		return (int[]) Array.newInstance(outtype.getClass().getComponentType(),
				0);
	}

}
