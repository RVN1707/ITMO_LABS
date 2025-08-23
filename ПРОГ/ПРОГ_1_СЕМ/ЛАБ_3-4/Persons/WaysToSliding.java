package Persons;

import Enums.BadVehicles;
import Transpot.Carpet;
import Enums.Corner;

import java.util.Random;

public abstract class WaysToSliding implements BasicsOfSliding{

    @Override
    public void GoodSliding(Corner c, String name){
        System.out.println("Состояние желоба: "+c+", поэтому "+name+" плавно скатывается по желобу");
    }
    @Override
    public void BadSliding(String name){
        scared(name);
        BadTransport(name);
        dust(name);
    }
    @Override
    public void ExtremeSliding(Corner c, String name, Carpet d){
        losecontrol(c, name);
        System.out.println(name+" пытается схватиться за стенки желоба");
        d.ExtremeSlippedoff();
        BadTransport(name);
    }

    private void dust(String name ){
        System.out.println(name +" поднимает кучу пыли.");
    }
    private void scared(String name){
        System.out.println(name +" в напряжении");
    }
    private void losecontrol(Corner c,String name){
        System.out.println("Состояние желоба: "+c+" и "+name +" теряет контроль");
    }
    @Override
    public void BadTransport(String name) {
        Random random = new Random();
        int index = random.nextInt(BadVehicles.values().length);
        System.out.println(name + " падает и использует " + BadVehicles.values()[index] + " вместо коврика");
    }

}
