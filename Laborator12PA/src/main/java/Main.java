import ClaseTest.Test1;
import Solver.Solver;

import java.io.File;
import java.net.MalformedURLException;

public class Main {

    public static void main(String[] args) throws MalformedURLException {
        /**
         * Compulsory
         */
        Solver solver = new Solver();
        ClassLoader classLoader = Test1.class.getClassLoader();
        File file = new File("C:\\Facultate\\Java\\Laborator12PA\\src\\main\\java\\ClaseTest");
        try {
            Class aClass = classLoader.loadClass("ClaseTest.Test1");
            solver.info(aClass);
            solver.invoke(aClass);
            solver.invoke2(aClass);
            System.out.println("-----EXPLORE-----");
            solver.explore(file);
            solver.statistics();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
