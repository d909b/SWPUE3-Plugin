package plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Implements a plugin manager class as a singleton object.
 * 
 * @author Patrick Schwab
 */
public class PluginManager {
	static PluginManager instance = null;
	
	ArrayList<Plugin> plugins = null;
	File pluginDirectory = null;
	
	/**
	 * Get an instance of the PluginManager class.
	 * Only one instance may exist at a time.
	 * 
	 * @return The singleton instance of PluginManager.
	 */
	static public synchronized PluginManager getInstance() {
		if( instance == null ) {
			return instance = new PluginManager();
		} else {
			return instance;
		}
	}
	
	/**
	 * Set the directory from which plugins will be loaded.
	 * 
	 * @param path The path to the directory that contains the plugins.
	 * @throws IllegalArgumentException If the specified __path__ was not a directory.
	 */
	public void setPluginDirectoryPath(String path) {
		File tmp = new File(path);
		
		if( !tmp.isDirectory() ) {
			throw new IllegalArgumentException("The specified __path__ was not a directory.");
		} else {
			pluginDirectory = tmp;
			
			/** Reset plugins. */
			plugins = null;
		}
	}
	
	/** 
	 * Gets the appropriate filter for the filter name.
	 * 
	 * @param name The name of the filter plugin.
	 * @return The filter for the __name__. 
	 * @throws IllegalArgumentException If there is no plugin with the specified __name__.
	 */
	public ImageFilter getFilter(String name) {
		for(Plugin plugin : plugins) {
			if(plugin.getName().equals(name)) {
				return plugin.getImageFilter();
			}
		}
		
		throw new IllegalArgumentException();
	}
	
	/**
	 * Loads the plugins from the __pluginDirectory__.
	 * Plugins must conform to the __Plugin__ interface and must be known to the JVM CLASSPATH.
	 * 
	 * @throws IllegalStateException If no pluginDirectory has been set.
	 */
	public void loadPlugins() {
		if( pluginDirectory == null ) {
			throw new IllegalStateException();
		}
		
		FilenameFilter filter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		};
		
		plugins = new ArrayList<Plugin>();
		
		for(File file : pluginDirectory.listFiles(filter)) {
			if(file.isDirectory()) {
				continue;
			}
			
			Class<?> clazz = null;
			try {
				/** Strip extension. Regular expression from http://stackoverflow.com/questions/924394/how-to-get-file-name-without-the-extension */
				String nameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", "");
				
				clazz = Class.forName( nameWithoutExtension );
			} catch (ClassNotFoundException e) {
				System.err.println("The file [" + file.getName() + "] does not contain a known class.");
				continue;
			}
			
			Plugin plugin = null;
			try {
				plugin = loadPlugin(clazz);
			} catch (LoadingPluginFailedException e) {
				System.err.println("The file [" + file.getName() + "] could not be instantiated, because: " + e.getMessage());
				continue;
			}
			
			System.out.println("Loaded plugin: " + file.getName());
			plugins.add(plugin);
		}
	}
	
	/**
	 * Loads a single plugin of the class __clazz__.
	 * 
	 * @param clazz A class that must conform to the __Plugin__ interface.
	 * @return A loaded plugin.
	 * @throws LoadingPluginFailedException A general exception is thrown if loading the plugin is not possible.
	 */
	Plugin loadPlugin(Class<?> clazz) throws LoadingPluginFailedException {
		boolean implementsPlugin = false;
		for(Class<?> i : clazz.getInterfaces() ) {
			if( i == Plugin.class) {
				implementsPlugin = true;
				break;
			}
		}
		
		if(!implementsPlugin) {
			/** The class __clazz__ doesn't implement the plugin interface. */
			throw new LoadingPluginFailedException("The specified class does not implement the Plugin interface.");
		}
		
		Object plugin = null;
		try {
			plugin = clazz.getConstructor().newInstance();
		} catch (IllegalArgumentException e) {
			throw new LoadingPluginFailedException("An illegal argument has been specified.");
		} catch (SecurityException e) {
			throw new LoadingPluginFailedException("Access has been denied.");
		} catch (InstantiationException e) {
			throw new LoadingPluginFailedException("Instantiation failed.");
		} catch (IllegalAccessException e) {
			throw new LoadingPluginFailedException("The invoked method is not available to the class " + this.getClass().getName() + ".");
		} catch (InvocationTargetException e) {
			throw new LoadingPluginFailedException("The target method threw an exception.");
		} catch (NoSuchMethodException e) {
			throw new LoadingPluginFailedException("The invoked method does not exist.");
		}
		
		return (Plugin)plugin;
	}
	
	/**
	 * Getter for the currently loaded plugins.
	 * 
	 * @throws IllegalStateException If the plugins have not been loaded yet. 
	 * @return The list of currently loaded plugins.
	 */
	public ArrayList<Plugin> getPlugins() {
		if(plugins == null) {
			throw new IllegalStateException("Plugins have not been loaded yet.");
		}
		
		return plugins;
	}
	
	/** Represents a general error during the plugin loading process. */
	class LoadingPluginFailedException extends Exception {
		String message = null;
		
		public LoadingPluginFailedException() {
			message = "";
		}
		
		public LoadingPluginFailedException(String msg) {
			message = msg;
		}
		
		@Override
		public String getMessage() {
			return message;
		}
	}
}
