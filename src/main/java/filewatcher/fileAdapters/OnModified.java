package filewatcher.fileAdapters;

import filewatcher.events.FileEvent;
import filewatcher.listeners.OnCreatedListener;
import filewatcher.listeners.OnModifiedListener;

/**
 * Реализация восстановления файла после модификации
 */
public class OnModified implements OnModifiedListener {

	@Override
	public void onModified(FileEvent event) {

	}
}