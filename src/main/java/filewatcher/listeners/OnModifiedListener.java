package filewatcher.listeners;

import filewatcher.events.FileEvent;

import java.util.EventListener;

public interface OnModifiedListener extends EventListener {
	void onModified(FileEvent event);
}
