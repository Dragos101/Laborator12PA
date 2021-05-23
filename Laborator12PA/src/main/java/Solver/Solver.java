package Solver;

import TestInterface.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Clasa solver are toate solutiile pentru compulsory/optional
 */
public class Solver {

    /**
     * Verifica daca - clasa daca ca parametru are pe rand
     * 1. Package
     * 2. Variabile
     * 3. Metode
     * 4. Constructori
     * @param clasa
     */
    public void info(Class<?> clasa){
        System.out.println("\n------INFO------");
        if(clasa.getPackage() == null) System.out.println("\nClasa nu are pachet");
        else System.out.println("1.Package -> " + clasa.getPackage().getName());
        if(clasa.getDeclaredFields().length != 0){
            System.out.println("2.Variabile: ");
            for(Field field : clasa.getDeclaredFields())
                System.out.println(field.toString());
        }
        if(clasa.getDeclaredConstructors().length != 0){
            System.out.println("3.Constructori: ");
            for(Constructor<?> constructor : clasa.getDeclaredConstructors())
                System.out.print(constructor.toString());
            System.out.println();
        }
        if(clasa.getDeclaredMethods().length != 0){
            System.out.println("4.Metode: ");
            for(Method method : clasa.getDeclaredMethods())
                System.out.println(method.toString());
            System.out.println();
        }
    }

    /**
     * Verificam daca - clasa data ca parametru are functii statice care nu au parametrii
     * @param clasa
     */
    public void invoke(Class<?> clasa){
        System.out.println("\n-----INVOKE-----");
        for(Method method : clasa.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class)){
                int modifiers = method.getModifiers();
                if(Modifier.isStatic(modifiers)){
                    Parameter[] parameters = method.getParameters();
                    if(parameters.length == 0){
                        try{
                            System.out.println(method.getName());
                            method.invoke(null);
                        }
                        catch (IllegalAccessException illegalAccessException){
                            illegalAccessException.printStackTrace();
                        }
                        catch (InvocationTargetException invocationTargetException){
                            System.out.println("A esuat invoke " + invocationTargetException.getCause());
                        }
                    }
                }
            }
        }
    }

    /**
     * Verificam daca - clasa data ca parametru are metode statice / non-statice care are adnotarea @Test
     * @param clasa
     */
    public void invoke2(Class<?> clasa){
        System.out.println("-----INVOKE2-----");
        for(Method method : clasa.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class)){
                Object[] objects = new Object[1];
                objects[0] = (int) 99;
                try{
                    System.out.println(method.getName());
                    method.invoke(clasa.getDeclaredConstructor().newInstance(), objects);
                }
                catch (IllegalAccessException | InstantiationException | NoSuchMethodException illegalAccessException){
                    illegalAccessException.printStackTrace();
                }
                catch (InvocationTargetException invocationTargetException){
                    System.out.println("A esuat invoke " + invocationTargetException.getCause());
                }
            }
        }
    }

    public void explore(File file) throws MalformedURLException, ClassNotFoundException {
        System.out.println("inceput");
        if(file.getName().endsWith(".jar")){
            System.out.println("sunt aici 2");
            JarFile jarFile = null;
            try{
                jarFile = new JarFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try{
                for(Enumeration<?> list = jarFile.entries(); list.hasMoreElements();){
                    JarEntry entry = (JarEntry) list.nextElement();
                    if(entry.getName().endsWith(".class")){
                        String name = entry.getName().split("\\.")[0];
                        name = name.replace("/", ".");
                        URLClassLoader urlClassLoader = URLClassLoader.newInstance(new URL[]{file.getAbsoluteFile().toURI().toURL()});
                        try{
                            Class<?> clasa = Class.forName(name, true, urlClassLoader);
                            if(clasa.isAnnotationPresent(Test.class)){
                                int modifiers = clasa.getModifiers();
                                if(Modifier.isPublic(modifiers)) invoke2(clasa);
                            }
                        }
                        catch (NoClassDefFoundError | ClassNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
            finally {
                try{
                    jarFile.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        if(file.exists()){
            System.out.println("sunt aici 3");
            if(file.isDirectory()){
                System.out.println("Sunt aici");
                for(File file1 : Objects.requireNonNull(file.listFiles())){
                    explore(file1);
                }
            }
            else{
                if(file.getName().endsWith(".java")){
                    Class<?> clazz = Class.forName("ClaseTest.Test1");
                    if(clazz.isAnnotationPresent(Test.class)){
                        int modifiers = clazz.getModifiers();
                        if(Modifier.isPublic(modifiers)){
                            invoke2(clazz);
                        }
                    }
                }
            }
        }
    }

}
