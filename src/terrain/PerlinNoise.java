package terrain;

import java.util.Random;

public class PerlinNoise {

	private final int[] p = new int[512];

	public PerlinNoise(long seed) {
        Random random = new Random(seed);
        int[] permutation = new int[256];
        for (int i = 0; i < 256; i++) {
            permutation[i] = i;
        }
        for (int i = 0; i < 256; i++) {
            int j = random.nextInt(256 - i) + i;
            int temp = permutation[i];
            permutation[i] = permutation[j];
            permutation[j] = temp;
        }
        for (int i = 0; i < 256; i++) {
            p[i] = p[i + 256] = permutation[i];
        }
    }

    public int getTile(double noise) {
        if(noise < -0.1) {
			return 0;
        } else if (noise < 0.2) {
            return 1; 
        } else {
            return 2;
        }
    }
    
    public double noise(double x, double y){
    	int xi = (int) Math.floor(x) & 255;
    	int yi = (int) Math.floor(y) & 255;
		
    	double xf = x - Math.floor(x);
    	double yf = y - Math.floor(y);
    	
    	double u = fade(xf);
    	double v = fade(yf);

		int g1 = p[p[xi] + yi];
    	int g2 = p[p[xi + 1] + yi];
    	int g3 = p[p[xi] + yi + 1];
    	int g4 = p[p[xi + 1] + yi + 1];
    	
    	double d1 = grad(g1, xf, yf);
    	double d2 = grad(g2, xf - 1, yf);
    	double d3 = grad(g3, xf, yf - 1);
    	double d4 = grad(g4, xf - 1, yf - 1);
    	
    	double x1Inter = lerp(u, d1, d2);
    	double x2Inter = lerp(u, d3, d4);
    	double yInter = lerp(v, x1Inter, x2Inter);
    	
    	return yInter;
    	
    }
    
    public double lerp(double amount, double left, double right) {
		return ((1 - amount) * left + amount * right);
	}
    
    public double fade(double t) { 
    	return t * t * t * (t * (t * 6 - 15) + 10); 
    }
    
    public double grad(int hash, double x, double y){
		int h = hash & 15; // Mask lower 4 bits using bitwise AND with 15 (0xF in hex)
		double u = (h < 8) ? x : y; // Choose between x and y based on lower 3 bits
		double v = (h < 4) ? y : x; // Choose between y and  x based on lower 2 bits
		return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v); // Apply sign based on lower 2 bits
	}
}