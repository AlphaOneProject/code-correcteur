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
        
        //Matrix hbase = loadMatrix("data/matrix-2000-6000-5-15", 2000, 6000);
        Matrix hbase = loadMatrix("data/matrix-2048-6144-5-15", 2048, 6144);
        //Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        //hbase.display();
        Matrix h = hbase.sysTransform();
        //h.display();
        Matrix g = h.genG();
        //g.display();
        byte[][] alexis_tab_u = {{1, 0, 1, 0, 1}};
        Matrix u = new Matrix(alexis_tab_u);
        Matrix y = u.multiply(g);
        y.display();
        byte[][] e1 = {{0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Matrix m_e1 = new Matrix(e1);
        byte[][] e2 = {{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};
        Matrix m_e2 = new Matrix(e2);
        byte[][] e3 = {{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0}};
        Matrix m_e3 = new Matrix(e3);
        byte[][] e4 = {{0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}};
        Matrix m_e4 = new Matrix(e4);
        Matrix y1 = y.add(m_e1);
        Matrix y2 = y.add(m_e2);
        Matrix y3 = y.add(m_e3);
        Matrix y4 = y.add(m_e4);
        
        // Decode in progress...
        
        TGraph william = new TGraph(hbase, hbase.getWC(), hbase.getWR());
        Matrix d1 = william.decode(y1, 100);
        Matrix d2 = william.decode(y2, 100);
        Matrix d3 = william.decode(y3, 100);
        Matrix d4 = william.decode(y4, 100);
        d1.display();
        d2.display();
        d3.display();
        d4.display();
    }
}
