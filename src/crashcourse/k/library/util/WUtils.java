package crashcourse.k.library.util;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import org.lwjgl.LWJGLException;

import de.matthiasmann.twl.FileSelector;
import de.matthiasmann.twl.FileSelector.NamedFileFilter;
import de.matthiasmann.twl.GUI;
import de.matthiasmann.twl.Widget;
import de.matthiasmann.twl.model.FileSystemModel;
import de.matthiasmann.twl.model.FileSystemModel.FileFilter;
import de.matthiasmann.twl.renderer.lwjgl.LWJGLRenderer;

public class WUtils {
	public static class WrappedFilter extends NamedFileFilter {

		public WrappedFilter(javax.swing.filechooser.FileFilter fileFilter) {
			super("Wrapped Filter!", new WrappedSubFilter(fileFilter));
		}

	}

	public static class WrappedSubFilter implements FileFilter {

		private javax.swing.filechooser.FileFilter ending;

		public WrappedSubFilter(javax.swing.filechooser.FileFilter fileFilter) {
			ending = fileFilter;
		}

		@Override
		public boolean accept(FileSystemModel model, Object file) {
			return file instanceof String
					&& ending.accept(new File((String) file));
		}

	}

	public static int returnValue = 0;
	private static WListener sw;
	private static JDialog temp = null;

	protected static boolean done;
	private static Widget fsRoot = new Widget();

	public static int windows_safe_JFC(final JFileChooser jfc,
			final int dialogType) {
		FileSelector fs = new FileSelector();
		NamedFileFilter f = new WrappedFilter(jfc.getChoosableFileFilters()[0]);
		fs.addFileFilter(f);
		fs.removeFileFilter(FileSelector.AllFilesFilter);
		try {
			GUI gui = new GUI(fsRoot, new LWJGLRenderer());
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		return JFileChooser.APPROVE_OPTION;
	}
}
