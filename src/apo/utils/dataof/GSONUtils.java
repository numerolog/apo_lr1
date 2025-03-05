package apo.utils.dataof;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

public class GSONUtils {

	public static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

	static String configFolderName = "workdir";
	public static boolean exists(File root, String _path) throws IOException {
		if (root != null) {
			File f = new File(root.getCanonicalPath() + File.separator + configFolderName  + File.separator + _path);
			return f.exists();
		} else {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T read ( Class < T > _where, File root, String _path ) throws Exception {
		T r = null;
		File f = new File(root.getCanonicalPath() + File.separator + configFolderName + File.separator + _path + ".json");
		System.out.println("Reading "+f.getAbsolutePath());
		boolean read = f.exists();
		if (read) {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			r = readerToObject(_where, isr);
		}

		if (r == null) {
			r = _where.getConstructor().newInstance();
			if (r instanceof IFirstInitData)
				r = ((IFirstInitData<T>)r).firstInit();
		}

		if (r instanceof ISecondInitData)
			r = ((ISecondInitData<T>)r).secondInit();
		
		if (!read) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			save(r, root, _path);
		}
		return r;
	}

	public static <T> T readerToObject(Class<T> _where, Reader rr) {
		T r = null;

		try {
			JsonReader reader = new JsonReader(rr);
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			r = GSON.fromJson(reader, _where);
			try {
				reader.close();
			} catch (Exception ex) {
			}

			reader = null;
//			gson = null;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return r;
	}
	
	public static void save ( Object _where, File root, String _path ) throws Exception {
		File f = new File(root.getCanonicalPath() + File.separator + configFolderName + File.separator + _path + ".json");
		System.out.println("Saving "+f.getAbsolutePath());

		String json = objectToString(_where);
		FileOutputStream fos = new FileOutputStream(f);

		fos.write(json.getBytes());
		fos.flush();
		fos.close();
		fos = null;
	}

	public static String objectToString(Object _where) {
		String json = GSON.toJson(_where);
		return json;
	}
	
}
