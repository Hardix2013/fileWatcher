package filewatcher;

import filewatcher.fileAdapters.OnCreated;
import filewatcher.utils.FileWatcher;

import java.io.File;

public class Application {

	public static void main(String[] args) throws InterruptedException {

		File folder = new File("/home/lucker/test");

		FileWatcher watcher = new FileWatcher(folder);

		watcher.addListener(new OnCreated()).watch();

		Thread.sleep(99999999);


	}
}
