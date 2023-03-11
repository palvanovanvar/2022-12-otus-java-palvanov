package homework;

import ru.otus.annotations.After;
import ru.otus.annotations.Before;
import ru.otus.annotations.Test;

import java.util.Random;

public class ClassTest {
    final int id = new Random().nextInt();
    String name = "Name " + id;

    @Before
    public void CheckCreate() {
        if (id < 0 || name == null){
            throw new IllegalArgumentException("id=" + id + " name=" + name);
        };
    }

    @Test
    public int getId() {
        return id;
    }

    @Test
    public String getName() {
        return name;
    }

    @Override
    @After
    public String toString(){
        return "id=" + id + " name=" + name;
    }
}
