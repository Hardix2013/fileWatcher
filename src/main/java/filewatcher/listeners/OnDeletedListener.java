package filewatcher.listeners;

import filewatcher.events.FileEvent;

import java.util.EventListener;

public interface OnDeletedListener extends EventListener {
	void onDeleted(FileEvent event);
}
