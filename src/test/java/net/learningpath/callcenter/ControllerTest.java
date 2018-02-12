package net.learningpath.callcenter;

import io.vavr.control.Option;
import io.vavr.control.Try;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void getRequest() {


        String success = Try.run(() -> runOnNonEmpty())
                .failed()
                .map(t -> "FAILURE")
                .getOrElse("SUCCESS");

        System.out.println();


    }

    private void throwException() throws Exception {
        throw new Exception("EXCEPTION...");
    }

    private void runOnNonEmpty() {
        System.out.println("runOnNonEmpty");
    }

}