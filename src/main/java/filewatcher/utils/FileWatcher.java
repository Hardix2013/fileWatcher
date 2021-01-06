package filewatcher.utils;

import filewatcher.events.FileEvent;
import filewatcher.fileAdapters.OnDeleted;
import filewatcher.listeners.OnCreatedListener;
import filewatcher.listeners.OnDeletedListener;
import filewatcher.listeners.OnModifiedListener;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
public class FileWatcher implements Runnable {
	protected List<EventListener> listeners = new ArrayList<>();
	protected final File folder;
	protected static final List<WatchService> watchServices = new ArrayList<>();

	public FileWatcher(File folder) {
		this.folder = folder;
	}

	public void watch() {
		if (folder.exists()) {
			Thread thread = new Thread(this);
			thread.setDaemon(true);
			thread.start();
			log.info("Thread started..");
		}else {
			log.error("Folder " + folder + " not exist!!!");
		}
	}

	@Override
	public void run() {
		try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
			Path path = Paths.get(folder.getAbsolutePath());

			path.register(watchService, ENTRY_CREATE);
			watchServices.add(watchService);
			boolean poll = true;
			while (poll) {
				poll = pollEvents(watchService);
			}
		} catch (IOException | InterruptedException | ClosedWatchServiceException e) {
			Thread.currentThread().interrupt();
		}
	}

	protected boolean pollEvents(WatchService watchService) throws InterruptedException {
		WatchKey key = watchService.take();
		Path path = (Path) key.watchable();
		for (WatchEvent<?> event : key.pollEvents()) {
			notifyListeners(event.kind(), path.resolve((Path) event.context()).toFile());
		}
		return key.reset();
	}

	protected void notifyListeners(WatchEvent.Kind<?> kind, File file) {
		FileEvent event = new FileEvent(file);
		if (kind == ENTRY_CREATE) {
			for (EventListener listener : listeners) {
				((OnCreatedListener)listener).onCreated(event);
			}
			if (file.isDirectory()) {
				new FileWatcher(file).setListeners(listeners).watch();
			}
		} else if (kind == ENTRY_MODIFY) {
			for (EventListener listener : listeners) {
				((OnModifiedListener)listener).onModified(event);
			}
		} else if (kind == ENTRY_DELETE) {
			for (EventListener listener : listeners) {
				((OnDeletedListener)listener).onDeleted(event);
			}
		}
	}

	public FileWatcher addListener(EventListener listener) {
		listeners.add(listener);
		return this;
	}

	public FileWatcher removeListener(EventListener listener) {
		listeners.remove(listener);
		return this;
	}

	public List<? extends EventListener> getListeners() {
		return listeners;
	}

	public FileWatcher setListeners(List<? extends EventListener> listeners) {
		this.listeners = (List<EventListener>) listeners;
		return this;
	}

	public static List<WatchService> getWatchServices() {
		return Collections.unmodifiableList(watchServices);
	}
}