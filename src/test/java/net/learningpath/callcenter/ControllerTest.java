package net.learningpath.callcenter;

import io.vavr.control.Option;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void getRequest() {

        List<Integer> l = new ArrayList<>();
        l.add(10);
        Option<Integer> map = Option.of(l)
                .filter(List::isEmpty)
                .onEmpty(this::runOnNonEmpty)
                .peek(l1 -> l1.add(12))
                .map(l1 -> l1.get(0));

        System.out.println();
        //Controller controller = new Controller();






    }

    private void runOnEmpty() {
        System.out.println("runOnEmpty");
    }

    private void runOnNonEmpty() {
        System.out.println("runOnNonEmpty");
    }

}