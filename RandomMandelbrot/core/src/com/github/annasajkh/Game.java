package com.github.annasajkh;

import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Game extends ApplicationAdapter
{
	ShapeRenderer shapeRenderer;
	Color color;
	
	float width, height;
	float maxIteration = 100f;
	float zoom;
	float ar, br;
	
	 public static Color hsvToRgba(float hue, float saturation, float value, float alpha)
    {

        int h = (int) (hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h)
        {
            case 0:
                return new Color(value, t, p, alpha);
            case 1:
                return new Color(q, value, p, alpha);
            case 2:
                return new Color(p, value, t, alpha);
            case 3:
                return new Color(p, q, value, alpha);
            case 4:
                return new Color(t, p, value, alpha);
            case 5:
                return new Color(value, p, q, alpha);
            default:
                throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " +
                                           hue +
                                           ", " +
                                           saturation +
                                           ", " +
                                           value);
        }
    }
	
	 public void reset()
	 {
		zoom = MathUtils.random(1,100);
		ar = MathUtils.random(MathUtils.random(-0.5f,-0.45f),MathUtils.random(0.45f,0.5f));
		br =  MathUtils.random(MathUtils.random(-0.5f,-0.45f),MathUtils.random(0.45f,0.5f));
	 }
	 
	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();
		color = new Color();
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
	}

	@Override
	public void render()
	{
		boolean activate = false;
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		Data[][] data = new Data[(int) width][(int) height];
		
		do
		{

			int count = 0;
			
			out:
			for(int x = 0; x < width; x++)
			{
				for(int y = 0; y < height; y++)
				{
					activate = false;
					float a = ar + (x / width - 0.5f) / zoom;
					float b =  br + (y / height - 0.5f ) / zoom;
					
					float ca = a;
					float cb = b;
					
					float n = 0;
					
					
					while(n < maxIteration)
					{
						float aa = a * a - b * b;
						float bb = 2 * a * b;
						
						a = aa + ca;
						b = bb + cb;
						
						if(Math.abs(a + b) > 16)
						{
							break;
						}
						
						n++;
					}
					
					float hue = (float) Math.sqrt(MathUtils.map(0,maxIteration,0,0.999f,n));
					
					if(hue == 0.99949986f)
					{
						count++;
					}
					
					if(count > (width * height) * 0.5f)
					{
						activate = true;
						reset();
						break out;
					}
					
					Color color = hsvToRgba(hue,1,1,1);		
					
					data[x][y] = new Data(color.r,color.g,color.b,width - x , y);
				}

			}
			
		}
		while(activate);
		
		shapeRenderer.begin(ShapeType.Point);
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[0].length; j++)
			{
				shapeRenderer.setColor(data[i][j].r,data[i][j].g,data[i][j].b,1);
				shapeRenderer.point(data[i][j].x,data[i][j].y,0);
			}
		}
		shapeRenderer.end();
		
		try
		{
			TimeUnit.SECONDS.sleep(3);
		} 
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		
		reset();

	}

	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
	}
}
