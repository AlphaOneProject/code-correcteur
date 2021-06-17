
public class TGraph {
	private int n_r;
	private int n_c;
	private int w_r;
	private int w_c;
	private int[][] left;
	private int[][] right;

	public TGraph(Matrix H, int wc, int wr) {
		this.n_r = H.getRows();
		this.n_c = H.getCols();
		this.w_r = wr;
		this.w_c = wc;
		this.left = new int[n_r][w_r + 1];
		this.right = new int[n_c][w_c + 1];
		
		for (int i = 0; i < n_r; i++) {
			int l_cursor = 1;
			for (int j = 0; j < n_c; j++) {
				if (H.getElem(i, j) == 1) {
					left[i][l_cursor++] = j;
				}
			}
		}
		for (int i = 0; i < n_c; i++) {
			int r_cursor = 1;
			for (int j = 0; j < n_r; j++) {
				if (H.getElem(j, i) == 1) {
					right[i][r_cursor++] = j;
				}
			}
		}
	}
	
	public void display() {
		System.out.print("left = \n[");
		for (int i = 0; i < n_r; i++) {
			System.out.print("[");
			for (int j = 0; j < w_r + 1; j++) {
				System.out.print(left[i][j]);
				if (j < w_r) System.out.print(", ");
			}
			System.out.print("]");
			if (i < n_r - 1) System.out.println(", ");
		}
		System.out.println("]");
		System.out.print("right = \n[");
		for (int i = 0; i < n_c; i++) {
			System.out.print("[");
			for (int j = 0; j < w_c + 1; j++) {
				System.out.print(right[i][j]);
				if (j < w_c) System.out.print(", ");
			}
			System.out.print("]");
			if (i < n_c - 1) System.out.println(", ");
		}
		System.out.println("]");
	}
	
	public Matrix decode(Matrix code, int rounds) {
		if (code.getCols() != n_c) throw new IllegalArgumentException("The given code is not of the expected length: " + code.getCols() + " given and " + (n_c - 1) + " expected");
		
		Matrix x = new Matrix(code.getRows(), code.getCols());
		
		// Insert code at the first column of 'right'.
		for (int i = 0; i < code.getCols(); i++) right[i][0] = code.getElem(0, i);
		
		int[] rules_broken = new int[x.getCols()];
		int max_rules_broken = 0;
		for (int round = 0; round < rounds; round++) {
			
			// Write parities in 'left[][0]'.
			for (int i = 0; i < n_r; i++) {
    			left[i][0] = (byte) 0; 
    			for (int j = 1; j < w_r + 1; j++ ) {
    				// If left[i][0] and right[left[i][j]][0] are similar:
    				// 		left[i][0] = 0 : (0 + 0) % 2 = 0 and (1 + 1) % 2 = 0
    				// else:
    				// 		left[i][0] = 1
    				left[i][0] = (left[i][0] + right[left[i][j]][0]) % 2;
    			}
    		}
			
			/*System.out.println("\nRound " + round + ": (Post parities check)");
    		this.display();*/

			// Check if rules were broken.
			boolean valid = true;
			int i = 0;
			while (valid && i < n_r) {
				if (left[i][0] != (byte) 0) valid = false;
				i++;
			}
    		
    		// If the message is valid, return it.
    		if (valid) {
				for (i = 0; i < n_c; i++) x.setElem(0, i, (byte) right[i][0]);
				return x;
    		}
    		
    		// Finding the maximum of rules broken.
    		for (i = 0; i < rules_broken.length; i++) rules_broken[i] = 0;
    		max_rules_broken = 0;
    		for (i = 0; i < n_r; i++) {
    			if (left[i][0] == 0) continue;
    			for (int j = 1; j < w_r + 1; j++) rules_broken[left[i][j]]++;
    		}
    		for (i = 0; i < n_c; i++) {
    			if (max_rules_broken < rules_broken[i]) max_rules_broken = rules_broken[i];
    		}
    		
    		// Modify bytes having the maximum of broken rules.
    		for(i = 0; i < n_c; i++) {
    			if (rules_broken[i] == max_rules_broken) right[i][0] = 1 - right[i][0];
    		}
    		
    		/*System.out.println("\nRound " + round + ": (Post modifications)");
    		this.display();
    		System.out.println("\n");*/
		}
		
		// Default return: -1 matrix.
		for (int i = 0; i < x.getRows(); i++) {
    		for (int j = 0; j < x.getCols(); j++) {
    			x.setElem(i, j, (byte) -1);
    		}
    	}
    	return x;
	}
}
