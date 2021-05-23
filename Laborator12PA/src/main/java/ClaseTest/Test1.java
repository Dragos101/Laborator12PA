package ClaseTest;

import TestInterface.Test;

import java.util.List;

/**
 * Am facut o clasa de test care are o metoda statica si una simpla.
 */

@Test
public class Test1 {
    @Test
    static int salut;
    @Test
    List<String> texte;

    @Test
    public Test1(){

    }

//    @Test
//    public static void test(){
//        System.out.println("No par");
//    }
    @Test
    public static void test2(int number){
        System.out.println("Parameters");
    }
    @Test
    public static void test3(int number){
        System.out.println("Parameters");
    }
}
