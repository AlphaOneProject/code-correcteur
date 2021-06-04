import java.util.*;
import java.io.*;

public class Matrix {
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
        
        if ((m.rows != rows) || (m.cols != cols))
            System.out.printf("Erreur d'addition\n");
        
        for (int i = 0; i < rows; i++) 
            for (int j = 0; j < cols; j++) 
                r.data[i][j] = (byte) ((data[i][j] + m.data[i][j]) % 2);
        return r;
    }
    
    public Matrix multiply(Matrix m){
        Matrix r = new Matrix(rows,m.cols);
        
        if (m.rows != cols)
            System.out.printf("Erreur de multiplication\n");
        
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
    	// With this = H & H = (L|R) & r = H' => H' = (M|id).
    	for (int i = 0; i < rows; i++) {
			if (data[i][i + (cols - rows)] == 0) {
				int target_row = i + 1;
    			while (target_row < rows && r.getElem(target_row, i + (cols - rows)) != 1) target_row++;
    			if (target_row < rows) {
    				r.shiftRow(i, target_row);
    				System.out.println("Switch rows " + i + " & " + target_row);
    			}
    			else {
    				target_row = i - 1;
    				while (target_row >= 0 && r.getElem(target_row, i + (cols - rows)) != 1) target_row--;
    				r.addRow(target_row, i);
    				System.out.println("Add rows " + target_row + " IN " + i);
    			}
			}
    	}
    	for (int i = 0; i < rows; i++) {
    		for (int j = cols - rows; j < cols; j++) {
    			if ((i <= j - (cols - rows) && data[i][j] == 1)) {
    				// Left - Bottom triangle => 0.
    				continue;
    			}
    		}
    	}
    	for (int i = 0; i < rows; i++) {
    		for (int j = cols - rows; j < cols; j++) {
    			if ((i >= j - (cols - rows) && data[i][j] == 1)) {
    				// Right - Top triangle => 0.
    				continue;
    			}
    		}
    	}
    	return r;
    }
}

