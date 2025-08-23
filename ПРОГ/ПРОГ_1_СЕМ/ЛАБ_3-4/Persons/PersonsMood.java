package Persons;

import Transpot.Carpet;
import Enums.Corner;

public interface PersonsMood {

    void Start(String name);

    String Process(String name, Corner c,Carpet d);

    void Finish(String name,String a);
}
