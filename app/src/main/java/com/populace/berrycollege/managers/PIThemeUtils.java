package com.populace.berrycollege.managers;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.Display;
import android.widget.TextView;
public class PIThemeUtils {

	public static BitmapDrawable GetPatternBackground(int resourceid,
			Context context) {
		Bitmap localBmp = BitmapFactory.decodeResource(context.getResources(),
				resourceid);
		BitmapDrawable drawable = new BitmapDrawable(localBmp);
		drawable.setTileModeX(TileMode.REPEAT);
		return drawable;
	}

	public static String[] GetFonts() {
		return new String[] { "fonts/font1.ttf", "fonts/font2.ttf" };
	}

	public static Typeface GetFontType(Context ctx, int index) {
		if (index < PIThemeUtils.GetFonts().length)
			return Typeface.createFromAsset(ctx.getAssets(), "fonts/font1.ttf");
		else
			return null;

	}


	public static int[] GetTextColors() {
		return new int[] { Color.argb(255, 255, 255, 255) };
	}

	public static int GetTextColor(int index) {
		return PIThemeUtils.GetTextColors()[index];
	}

	public static int[] GetBackgroundColors() {
		return new int[] { Color.argb(1, 0, 0, 0) };
	}

	public static int GetBackgroundColor(int index) {
		return PIThemeUtils.GetBackgroundColors()[index];
	}

	public static Bitmap MaskedImage(int maskResourceId,
			int originalResourceId, Context context) {
		Resources resources = context.getResources();
		Bitmap original = BitmapFactory.decodeResource(resources,
				originalResourceId);
		Bitmap mask = BitmapFactory.decodeResource(resources, maskResourceId);
		return PIThemeUtils.MaskedImage(mask, original);
	}

	public static Bitmap MaskedImage(Bitmap mask, Bitmap original) {
		Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(),
				Config.ARGB_8888);
		Canvas c = new Canvas(result);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.DST_IN));
		c.drawBitmap(original, 0, 0, null);
		c.drawBitmap(mask, 0, 0, paint);
		paint.setXfermode(null);
		return result;
	}

	public static void ThemeTextView(TextView txt, int fontIndex,
			int colorIndex, float fontSize) {
		txt.setTypeface(PIThemeUtils.GetFontType(txt.getContext(), fontIndex));
		txt.setTextColor(PIThemeUtils.GetTextColor(colorIndex));
		txt.setTextSize(fontSize);
	}

	public static Bitmap ScaleImage(Bitmap original, float newWidth, float newHeight, boolean widthPriority) {
		if(original == null){
			return original;
		}else{
			
		int width = original.getWidth();
		int height = original.getHeight();
		float aspect = (float) width / height;
		
		float scaleWidth = (widthPriority)?newWidth:(newHeight*aspect);
		float scaleHeight = (!widthPriority)?newHeight:(scaleWidth / aspect); // yeah!
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth / width, scaleHeight / height);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(original, 0, 0, width, height,
				matrix, true);
		if(!resizedBitmap.equals(original))
		original.recycle();
		return resizedBitmap;
		}
	}

	public static Bitmap RotatedImageWithSize(Bitmap original, int width,
			int height, int angle) {
		if(original == null){
			return original;
		}else{
		Matrix matrix = new Matrix();
		matrix.postRotate((float) -angle, original.getWidth(), 0);
		Bitmap rotated = Bitmap.createBitmap(original, 0, 0,
				original.getWidth(), original.getHeight(), matrix, true);
		return rotated;
		}
	}

	public static Bitmap RotatedImageWithSize(int imageResourceId, int width,
			int height, int angle, Context context) {
		Bitmap original = BitmapFactory.decodeResource(context.getResources(),
				imageResourceId);
		return PIThemeUtils
				.RotatedImageWithSize(original, width, height, angle);
	}
	public static Point DisplaySize(Context context){
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return  size;
		
	}
	public static int  ActionBarHeight(Context context){
		TypedValue tv = new TypedValue();
		int actionBarHeight = 0;
		if (((Activity)context).getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
		{
		    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,((Activity)context).getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}
	public static Bitmap BlurImage (Bitmap input, Context context)
	{
		
		final int widthKernal = 5;
		  final int heightKernal = 5;

		  int w = input.getWidth();
		  int h = input.getHeight();

		  Bitmap blurBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);

		  for (int x = 0; x < w; x++) {
		   for (int y = 0; y < h; y++) {

		    int r = 0;
		    int g = 0;
		    int b = 0;
		    int a = 0;
		    
		    for (int xk = 0; xk < widthKernal; xk++) {
		     for (int yk = 0; yk < heightKernal; yk++) {
		      int px = x + xk -2;
		      int py = y + yk -2;

		      if(px < 0){
		       px = 0;
		      }else if(px >= w){
		       px = w-1;
		      }
		      
		      if(py < 0){
		       py = 0;
		      }else if(py >= h){
		       py = h-1;
		      }
		      
		      int intColor = input.getPixel(px, py);
		      r += Color.red(intColor);
		      g += Color.green(intColor);
		      b += Color.blue(intColor);
		      a += Color.alpha(intColor);
		      
		     }
		    }
		   
		    
		    blurBitmap.setPixel(x, y, Color.argb(((a + 500 < 6375)?a+500:a)/25, ((r + 500 < 6375)?r+500:r)/25, ((g + 500 < 6375)?g+500:g)/25, ((b + 500 < 6375)?b+500:b)/25));

		   }
		  }

		  return blurBitmap;
	}
	public static Bitmap CropBitmap(Bitmap originalBmp, int x, int y, int height, int width){
		if(originalBmp == null){
			return originalBmp;
		}else{
		
		Bitmap croppedBmp = Bitmap.createBitmap(originalBmp, x, y, width, height);
		return croppedBmp;
		}

	}
	public static Bitmap GetFullWidthOptimizedBitmap(Context context, int resid){
		BitmapFactory.Options o=new BitmapFactory.Options();
		o.inSampleSize = 4;
		o.inDither=false;                     //Disable Dithering mode
		o.inPurgeable=true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared

		Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), 
			    resid,o);
		Point p = DisplaySize(context);
		bitmap =ScaleImage(bitmap,p.x,p.y,true);
		return bitmap;
		
	}
}
