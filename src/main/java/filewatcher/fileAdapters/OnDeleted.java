package filewatcher.fileAdapters;

import filewatcher.events.FileEvent;
import filewatcher.listeners.OnCreatedListener;
import filewatcher.listeners.OnDeletedListener;

/**
 * Реализация восстановления файла после удаления из папки
 */
public class OnDeleted implements OnDeletedListener {

	@Override
	public void onDeleted(FileEvent event) {
		//реализация не предусмотрена
	}
}