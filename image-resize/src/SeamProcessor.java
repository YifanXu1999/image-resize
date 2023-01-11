import java.util.Arrays;
import java.util.Collection;

public class  SeamProcessor{
	
	private ImageProcessor imageProcessor;
	private int height;
	private int width;
	
	public SeamProcessor(ImageProcessor imageProcessor) {
		this.imageProcessor = imageProcessor;
		height = imageProcessor.getHeight();
		width = imageProcessor.getWidth();
	}
	
	public int[] computeHorizontalCost(int row) {
		int [] cost = new int [width];
		for (int col = 0; col < width; col++) {
			if(col == 0 || col == width-1) {
				cost[col] = 255 * 3;
			} else {
				cost[col] = imageProcessor.compareColor(row, col, row, col-1) +  imageProcessor.compareColor(row, col, row, col+1);
			}
		}
		return cost;
	}
	
	public int[] compueteVerticalSeam() {
		int [] seam = new int [height];
		int [][] dp = new int [height][width];
		int [] cost;
		/*
		 *  Compute dp table whereas
		 *  dp[i][j] = energy[i][j], if i==0
		 *  dp[i][j] = energy[i][j] + min(dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1]), otherwise
		 */
		for (int i = 0; i < height; i++) {
			cost = computeHorizontalCost(i);
			for (int j = 0; j < width; j++) {
				if(i== 0) {
					dp[i][j] = cost[j]; 
				} else {
					dp[i][j] = dp[i-1][j];
					if(j-1 >=0) {
						dp[i][j] = dp[i-1][j-1] < dp[i][j] ? dp[i-1][j-1] : dp[i][j];
					}
					if(j+1 < width) {
						dp[i][j] = dp[i-1][j+1] < dp[i][j] ? dp[i-1][j+1] : dp[i][j];
					}
					dp[i][j] += cost[j];
				}
			}
		}
		
		/*
		 * The seam end at argmin_y dp[height-1][y] and we trace back to obtain the vertical seam
		 */
		int y=-1;
		int mintCost = Integer.MAX_VALUE;
		for (int j = 1; j < width - 1; j++) {
			if(dp[height-1][j] < mintCost) {
				mintCost = dp[height-1][j];
				y = j;
			}
		}
		seam[height-1] = y;
		
		for(int i = height-2; i >=0; i--) {
			y = seam[i + 1];
			//System.out.println(y);
			if(dp[i][y-1] < dp[i][y] && dp[i][y-1] < dp[i][y+1]) {
				seam[i] = y-1;
			}
			else if (dp[i][y] < dp[i][y-1] && dp[i][y] < dp[i][y+1]) {
				seam[i] = y;
			} else {
				seam[i] = y+1;
			}
		}
		return seam;
		
	}

}
