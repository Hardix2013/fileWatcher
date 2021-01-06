package filewatcher.listeners;

import filewatcher.events.FileEvent;

import java.util.EventListener;

public interface OnCreatedListener extends EventListener {
	void onCreated(FileEvent event);
}
