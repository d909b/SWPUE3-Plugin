package imagefilters;


import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import plugin.ImageFilter;

public class SharpenImageFilter implements ImageFilter {
	public static final float[] SHARPEN3x3 = { // sharpening filter kernel
        0.f, -1.f,  0.f,
       -1.f,  5.f, -1.f,
        0.f, -1.f,  0.f
    };
	
	@Override
	public BufferedImageOp getImageFilterOperation() {
		float[] data = SHARPEN3x3;
        ConvolveOp cop = new ConvolveOp(new Kernel(3, 3, data),
                                        ConvolveOp.EDGE_NO_OP,
                                        null);
        
        return cop;
	}
}
