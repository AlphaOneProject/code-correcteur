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
    
    public static void main(String[] arg) {
        
        byte[][] tab = {{1,0,0},{0,1,0},{0,0,1}};
        Matrix m = new Matrix(tab);
        //m.display();
        
        //Matrix hbase = loadMatrix("data/matrix-2000-6000-5-15", 2000, 6000);
        Matrix hbase = loadMatrix("data/matrix-2048-6144-5-15", 2048, 6144);
        //Matrix hbase = loadMatrix("data/matrix-15-20-3-4", 15, 20);
        //hbase.display();
        
        Matrix h = hbase.sysTransform();
        //h.display();
        Matrix g = h.genG();
        //g.display();
        
        // Generate the word.
        Matrix u = new Matrix(1, g.getRows());
        for (int i = 0; i < u.getCols(); i++) {
        	if (i % 2 == 0) u.setElem(0, i, (byte) 1);
        	else u.setElem(0, i, (byte) 0);
        }
        // Encode the word in x.
        Matrix x = u.multiply(g);
        //x.display();
        
        int failed = 0;
        int successes = 0;
        System.out.println("Started to decode");
        for (int i = 0; i < 10000; i++) {
	        // Adding noise.
	        Matrix e = x.errGen(154);
	        Matrix y = x.add(e);
	        
	        //System.out.println("\nAfter noise: ");
	        //y.display();
	        
	        // Decoding a message.
	        
	        TGraph william = new TGraph(hbase, hbase.getWC(), hbase.getWR());
	        Matrix d = william.decode(y, 200);
	        //System.out.println("\nOutput of decode: ");
	        //d.display();
	        if (d.getElem(0, 0) == (byte) -1) failed++;
	        else {
	        	if (!d.isEqualTo(x)) throw new AssertionError("Decoded word invalid");
	        	successes++;
	        }
        }
        System.out.println("Decode failed: " + failed);
        System.out.println("Decode succeeded: " + successes);
    }
}
