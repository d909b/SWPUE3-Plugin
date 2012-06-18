

import imagefilters.LookupImageFilter;
import plugin.ImageFilter;
import plugin.Plugin;

public class LookupPlugin implements Plugin {

	@Override
	public String getName() {
		return "Lookup Filter";
	}

	@Override
	public ImageFilter getImageFilter() {
		return new LookupImageFilter();
	}
}
