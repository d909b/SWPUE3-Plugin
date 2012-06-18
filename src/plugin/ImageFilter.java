package plugin;

import java.awt.image.BufferedImageOp;

public interface ImageFilter {
	/**
	 * Get the image filter operation.
	 * @return The image filter operation.
	 */
	BufferedImageOp getImageFilterOperation();
}
