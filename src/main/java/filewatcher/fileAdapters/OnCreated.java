package filewatcher.fileAdapters;

import filewatcher.events.FileEvent;
import filewatcher.listeners.OnCreatedListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

/**
 * Реализация удаления файла\файлов в директории.
 */
@Slf4j
public class OnCreated implements OnCreatedListener {

	@Value("${remove.timeout:60000}")
	private long timeout;

	@Override
	public void onCreated(FileEvent event) {
		boolean isDirectory = event.getFile().isDirectory();
		log.info("Created " +
				(isDirectory ? "folder " : "file " +
						event.getFile().getAbsolutePath()));
		try {
			log.info("Wait timeout while AS working... timeout " + timeout);
			Thread.sleep(timeout);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("try delete " + event.getFile().getAbsolutePath());
		if (isDirectory) {
			try {
				FileUtils.deleteDirectory(event.getFile());
				if (event.getFile().list() != null) {
					log.error("Can't clean directory " + event.getFile().getAbsolutePath());
				}
			} catch (IOException e) {
				log.error("Can't clean directory. Reason: " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			if (event.getFile().delete()) {
				log.info("File " + event.getFile().getAbsolutePath() + " has been deleted");
			} else {
				log.error("Can't delete file " + event.getFile().getAbsolutePath());
			}
		}
	}
}