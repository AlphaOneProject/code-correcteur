
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
		Matrix x = code;
		// Insert code at the first column of 'right'.
		if(code.getCols() != n_c) throw new IllegalArgumentException("The given code is not of the expected length: " + code.getCols() + " given and " + (n_c - 1) + " expected");
		for (int i = 0; i < code.getCols(); i++) {
			right[i][0] = code.getElem(0, i);
		}
		for (int round = 0; round < rounds; round++) {
			// Calcul des parites.
			// Colonne [0] de left.
			
		}
    	return x;
	}
}
