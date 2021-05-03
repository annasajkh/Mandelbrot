package com.github.annasajkh;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
	float zoom = 1;
	
	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();
		color = new Color();
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
	}

	public void update()
	{
		if(Gdx.input.isKeyJustPressed(Keys.SPACE))
		{
			create();
		}
	}

	@Override
	public void render()
	{
		update();
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.begin(ShapeType.Point);
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				float a = 0.13856524454488f + (x / width - 0.13856524454488f) / zoom;
				float b = 0.64935990748190f + (y / height - 0.64935990748190f ) / zoom;
				
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
				
				float bright = (float) Math.sqrt(MathUtils.map(0,maxIteration,0,1,n));
				
				
				if(n == maxIteration)
				{
					bright = 0;
				}
				shapeRenderer.setColor(bright,bright,bright,1);
				shapeRenderer.point(width - x , y, 0);
			}
			
		}
		
		shapeRenderer.end();
		
		zoom *= 1.1f;
	}

	@Override
	public void dispose()
	{
		shapeRenderer.dispose();
	}
}
