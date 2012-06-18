package plugin;

public interface Plugin {
	/**
	 * Getter for the plugins' name.
	 * @return The plugins' name.
	 */
	String getName();
	
	/**
	 * Get the corresponding image filter.
	 * @return The corresponding image filter.
	 */
	ImageFilter getImageFilter();
}
