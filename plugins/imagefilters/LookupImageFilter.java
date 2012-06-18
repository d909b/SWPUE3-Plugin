package imagefilters;


import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;
import plugin.ImageFilter;


public class LookupImageFilter implements ImageFilter {

	@Override
	public BufferedImageOp getImageFilterOperation() {
		byte lut[] = new byte[256];
        for (int j=0; j<256; j++) {
            lut[j] = (byte)(256-j); 
        }
        ByteLookupTable blut = new ByteLookupTable(0, lut); 
        
		return new LookupOp(blut, null);
	}

}
