

import imagefilters.SharpenImageFilter;
import plugin.ImageFilter;
import plugin.Plugin;

public class SharpenPlugin implements Plugin {

	@Override
	public String getName() {
		return "Sharpen Filter";
	}

	@Override
	public ImageFilter getImageFilter() {
		return new SharpenImageFilter();
	}

}
