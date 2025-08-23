package Persons;

import Enums.Corner;
import Transpot.Carpet;

public interface BasicsOfSliding {
    void GoodSliding(Corner c, String name);
    void BadSliding(String name);
    void ExtremeSliding(Corner c, String name, Carpet d);
    void BadTransport(String name);
}
