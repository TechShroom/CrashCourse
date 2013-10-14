package crashcourse.k.library.main;

import crashcourse.k.library.util.LUtils;
import crashcourse.k.library.util.StackTraceInfo;

public abstract class KMain {
	private static KMain insts = null;

	public abstract void onDisplayUpdate(int delta);

	public abstract void init(String[] args);

	public static void setInst(KMain inst) {
		try {
			LUtils.checkAccessor("crashcourse.k.library.*",
					StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		insts = inst;
	}

	public static KMain getInst() {
		try {
			LUtils.checkAccessor("crashcourse.k.library.*",
					StackTraceInfo.getInvokingClassName());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return insts;
	}
}
