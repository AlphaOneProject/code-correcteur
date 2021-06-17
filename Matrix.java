import java.util.*;
import java.io.*;

public class Matrix {
	private static Random random = new Random();
    private byte[][] data = null;
    private int rows = 0, cols = 0;
    
    public Matrix(int r, int c) {
        data = new byte[r][c];
        rows = r;
        cols = c;
    }
    
    public Matrix(byte[][] tab) {
        rows = tab.length;
        cols = tab[0].length;
        data = new byte[rows][cols];
        for (int i = 0 ; i < rows ; i ++)
            for (int j = 0 ; j < cols ; j ++) 
                data[i][j] = tab[i][j];
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public byte getElem(int i, int j) {
        return data[i][j];
    }
    
    public void setElem(int i, int j, byte b) {
        data[i][j] = b;
    }
    
    public boolean isEqualTo(Matrix m){
        if ((rows != m.rows) || (cols != m.cols))
            return false;
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                if (data[i][j] != m.data[i][j])
                    return false;
                return true;
    }
    
    public void addRow(int a, int b) {
    	for (int i = 0; i < cols; i++){
            data[b][i] = (byte) ((data[b][i] + data[a][i]) % 2);
        }
    }
    
    public void addCol(int a, int b) {
    	for (int i = 0; i < rows; i++){
    		data[i][b] = (byte) ((data[i][b] + data[i][a]) % 2);
        }
    }
    
    public void shiftRow(int a, int b){
        byte tmp = 0;
        for (int i = 0; i < cols; i++){
            tmp = data[a][i];
            data[a][i] = data[b][i];
            data[b][i] = tmp;
        }
    }
    
    public void shiftCol(int a, int b){
        byte tmp = 0;
        for (int i = 0; i < rows; i++){
            tmp = data[i][a];
            data[i][a] = data[i][b];
            data[i][b] = tmp;
        }
    }
     
    public void display() {
        System.out.print("[");
        for (int i = 0; i < rows; i++) {
            if (i != 0) {
                System.out.print(" ");
            }
            
            System.out.print("[");
            
            for (int j = 0; j < cols; j++) {
                System.out.printf("%d", data[i][j]);
                
                if (j != cols - 1) {
                    System.out.print(" ");
                }
            }
            
            System.out.print("]");
            
            if (i == rows - 1) {
                System.out.print("]");
            }
            
            System.out.println();
        }
        System.out.println();
    }
    
    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                result.data[j][i] = data[i][j];
    
        return result;
    }
    
    public Matrix add(Matrix m){
        Matrix r = new Matrix(rows,m.cols);
        
        if ((m.rows != rows) || (m.cols != cols)) throw new IllegalArgumentException("Erreur d'addition");
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                r.data[i][j] = (byte) ((data[i][j] + m.data[i][j]) % 2);
        return r;
    }
    
    public Matrix multiply(Matrix m){
        Matrix r = new Matrix(rows,m.cols);
        
        if (m.rows != cols) throw new IllegalArgumentException("Erreur de multiplication");
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < m.cols; j++) {
                r.data[i][j] = 0;
                for (int k = 0; k < cols; k++){
                    r.data[i][j] =  (byte) ((r.data[i][j] + data[i][k] * m.data[k][j]) % 2);
                }
            }
        }
        
        return r;
    }
    
    public Matrix sysTransform() {
    	Matrix r = new Matrix(data);
    	for (int i = 0; i < rows; i++) {
    		// Setup the row with a '1' in the diagonal.
			if (r.getElem(i, i + (cols - rows)) == 0) {
				int target_row = i + 1;
    			while (target_row < rows && r.getElem(target_row, i + (cols - rows)) != 1) target_row++;
    			if (target_row == rows)	throw new IllegalStateException("This matrix isn't inversible!");
    			r.shiftRow(i, target_row);
			}
			// Setup the bottom-left triangle with '0'.
			for (int j = i + 1; j < rows; j++) {
				if (r.getElem(j, i + (cols - rows)) == 1) {
					r.addRow(i, j);
				}
			}
    	}
    	// Setup the top-right triangle with '0'.
    	for (int i = rows - 1; i > 0; i--) {
    		for (int j = 0; j < rows; j++) {
				if (i != j && r.getElem(j, i + (cols - rows)) == 1) {
					r.addRow(i, j);
				}
			}
    	}
    	return r;
    }
    
    public Matrix genG() {
    	Matrix r = new Matrix((cols - rows), rows + (cols - rows));
    	for (int i = 0; i < r.rows; i++) {
    		r.setElem(i, i, (byte) 1);
    	}
    	for (int i = 0; i < r.rows; i++) {
    		for (int j = (cols - rows); j < r.cols; j++) {
    			r.setElem(i, j, getElem(j - (cols - rows), i));
    		}
    	}
    	return r;
    }
    
    public Matrix errGen(int w) {
    	Matrix r = new Matrix(1, this.getCols());
    	int weight = 0;
    	while (weight < w) {
    		int i = Matrix.random.nextInt(this.getCols());
    		if (r.getElem(0, i) == (byte) 0) {
    			r.setElem(0, i, (byte) 1);
    			weight++;
    		}
    	}
    	return r;
    }
    
    public int getWC() {
    	int max = 0;
    	for (int i = 0; i < cols; i++) {
    		int sum = 0;
    		for (int j = 0; j < rows; j++) if (getElem(j, i) == 1) sum++;
    		if (sum > max) max = sum;
    	}
    	return max;
    }
    
    public int getWR() {
    	int max = 0;
    	for (int i = 0; i < rows; i++) {
    		int sum = 0;
    		for (int j = 0; j < cols; j++) if (getElem(i, j) == 1) sum++;
    		if (sum > max) max = sum;
    	}
    	return max;
    }
}

