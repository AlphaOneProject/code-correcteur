import java.util.*;
import java.io.*;

public class Main {
    
    public static Matrix loadMatrix(String file, int r, int c) {
        byte[] tmp =  new byte[r * c];
        byte[][] data = new byte[r][c];
        try {
            FileInputStream fos = new FileInputStream(file);
            fos.read(tmp);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < r; i++)
            for (int j = 0; j< c; j++)
                data[i][j] = tmp[i * c + j];
            return new Matrix(data);
    }
    
    public static void main(String[] arg){
        
        byte[][] tab = {{1,0,0},{0,1,0},{0,0,1}};
        Matrix m = new Matrix(tab);
        m.display();
        
        //Matrix hbase = loadMatrix("data/matrix-2000-6000-5-15", 15, 20);
        Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        hbase.display();
        Matrix h = hbase.sysTransform();
        h.display();
        Matrix g = h.genG();
        g.display();
        byte[][] alexis_tab_u = {{1, 0, 1, 0, 1}};
        Matrix u = new Matrix(alexis_tab_u);
        Matrix y = u.multiply(g);
        y.display();
        
        // Decode in progress...
        
        TGraph william = new TGraph(hbase, hbase.getWC(), hbase.getWR());
        william.decode(y, 5).display();
        william.display();
    }
}
