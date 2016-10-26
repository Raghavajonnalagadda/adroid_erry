package com.populace.berrycollege.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;


/**
 * Image effects used by the in-call UI.
 */
public class PIBitmapUtils {
    private static final String TAG = "BitmapUtils";
    
    /** This class is never instantiated. */
    private PIBitmapUtils() {
    }

    //
    // Gaussian blur effect
    //
    // gaussianBlur() and related methods are borrowed from
    // BackgroundUtils.java in the Music2 code (which itself was based on
    // code from the old Cooliris android Gallery app.)
    //
    // TODO: possibly consider caching previously-generated blurred bitmaps;
    // see getAdaptedBitmap() and mAdaptedBitmapCache in the music app code.
    //

    private static final int RED_MASK = 0xff0000;
    private static final int RED_MASK_SHIFT = 16;
    private static final int GREEN_MASK = 0x00ff00;
    private static final int GREEN_MASK_SHIFT = 8;
    private static final int BLUE_MASK = 0x0000ff;

    /**
     * Creates a blurred version of the given Bitmap.
     *
     * @param bitmap the input bitmap, presumably a 96x96 pixel contact
     *               thumbnail.
     */
    public static Bitmap createBlurredBitmap(Bitmap bitmap) {
        long startTime = SystemClock.uptimeMillis();
        if (bitmap == null) {
            Log.w(TAG, "createBlurredBitmap: null bitmap");
            return null;
        }

        
        // The bitmap we pass to gaussianBlur() needs to have a width
        // that's a power of 2, so scale up to 128x128.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final int scaledSize = 128;
        bitmap = Bitmap.createScaledBitmap(bitmap,
                                           bitmap.getWidth()/4, bitmap.getHeight()/4,
                                           true /* filter */);
        bitmap = Bitmap.createScaledBitmap(bitmap,
                width, height,
                true /* filter */);

        //bitmap = gaussianBlur(bitmap);
        
        //long endTime = SystemClock.uptimeMillis();
        return bitmap;
    }

    /**
     * Apply a gaussian blur filter, and return a new (blurred) bitmap
     * that's the same size as the input bitmap.
     *
     * @param source input bitmap, whose width must be a power of 2
     */
    public static Bitmap gaussianBlur(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        // Create a source and destination buffer for the image.
        int numPixels = width * height;
        int[] in = new int[numPixels];
        int[] tmp = new int[numPixels];

        // Get the source pixels as 32-bit ARGB.
        source.getPixels(in, 0, width, 0, 0, width, height);

        // Gaussian is a separable kernel, so it is decomposed into a horizontal
        // and vertical pass.
        // The filter function applies the kernel across each row and transposes
        // the output.
        // Hence we apply it twice to provide efficient horizontal and vertical
        // convolution.
        // The filter discards the alpha channel.
        gaussianBlurFilter(in, tmp, width, height);
        gaussianBlurFilter(tmp, in, width, height);

        // Return a bitmap scaled to the desired size.
        Bitmap filtered = Bitmap.createBitmap(in, width, height, Config.ARGB_8888);
        source.recycle();
        return filtered;
    }

    private static void gaussianBlurFilter(int[] in, int[] out, int width, int height) {
        // This function is currently hardcoded to blur with RADIUS = 4.
        // (If you change RADIUS, you'll have to change the weights[] too.)
        final int RADIUS = 4;
        final int[] weights = { 13, 23, 32, 39, 42, 39, 32, 23, 13}; // Adds up to 256
        int inPos = 0;
        int widthMask = width - 1; // width must be a power of two.
        for (int y = 0; y < height; ++y) {
            // Compute the alpha value.
            int alpha = 0xff;
            // Compute output values for the row.
            int outPos = y;
            for (int x = 0; x < width; ++x) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int i = -RADIUS; i <= RADIUS; ++i) {
                    int argb = in[inPos + (widthMask & (x + i))];
                    int weight = weights[i+RADIUS];
                    red += weight *((argb & RED_MASK) >> RED_MASK_SHIFT);
                    green += weight *((argb & GREEN_MASK) >> GREEN_MASK_SHIFT);
                    blue += weight *(argb & BLUE_MASK);
                }
                // Output the current pixel.
                out[outPos] = (alpha << 24) | ((red >> 8) << RED_MASK_SHIFT)
                    | ((green >> 8) << GREEN_MASK_SHIFT)
                        | (blue >> 8);
                outPos += height;
            }
            inPos += width;
        }
    }

    //
    // Debugging
    //

    private static void log(String msg) {
        Log.d(TAG, msg);
    }
    
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
    // Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {

        final int halfHeight = height / 2;
        final int halfWidth = width / 2;

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while ((halfHeight / inSampleSize) > reqHeight
                && (halfWidth / inSampleSize) > reqWidth) {
            inSampleSize *= 2;
        }
    }

    return inSampleSize;
}
    public static Bitmap getOptimizedBitmap(String img,Context context, int reqWidth, int reqHeight, boolean widthPriority){
    	final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        File imgFile = null;
        if(img.contains("/")){
        	imgFile = new File(img);
        }else{
        	imgFile = context.getFileStreamPath(img);
        }
        
		
        
        int resId = PIResourceUtils.getDrawableResId(img, context);
        
        if(imgFile.exists() && imgFile.length() > 0){
		     BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
		}else{
        if(resId != -1)
			BitmapFactory.decodeResource(context.getResources(), resId, options);
		}
        if(widthPriority){
        	reqHeight = (reqHeight/reqWidth) * options.outHeight;
        }else{
        	reqWidth = (reqWidth/reqHeight) * options.outWidth;
        }
		// Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        if(imgFile.exists() && imgFile.length() > 0){
		     bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
		}else{
			bitmap = BitmapFactory.decodeResource(context.getResources(), resId,options);
			
		}
        return bitmap;
    }
    public static Bitmap getBitmap(String img,Context context){
    	Bitmap bitmap = null;
    	File imgFile = context.getFileStreamPath(img);
		if(imgFile.exists() && imgFile.length() > 0){
		     bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		}else{
			int resId = PIResourceUtils.getDrawableResId( img, context);
			if(resId != -1)				
				bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
		}
		return bitmap;
			
    }
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
    	if(scaleBitmapImage != null){
        int targetWidth = scaleBitmapImage.getWidth() + 10;
        int targetHeight = scaleBitmapImage.getHeight() + 10;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
                            targetHeight, Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
            ((float) targetHeight - 1) / 2,
            (Math.min(((float) targetWidth), 
            ((float) targetHeight)) / 2),
            Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap, 
            new Rect(0, 0, sourceBitmap.getWidth(),
            sourceBitmap.getHeight()), 
            new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    	}
    	return null;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float cornerRadius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
            bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
     
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = cornerRadius;
     
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
     
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
     
        return output;
      }
}