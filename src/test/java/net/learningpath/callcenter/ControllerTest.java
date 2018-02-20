package net.learningpath.callcenter;

import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ControllerTest {

    @Test
    public void getRequest() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        BiFunction<Call, Controller, Response> makeCall =
                (call, controller) -> controller.getRequest(call);
        List<Callable<Response>> callables = IntStream.range(1, 101)
                .mapToObj(i -> Executors.privilegedCallable(() -> makeCall.apply(new Call("Client" + i), Controller.getInstance())))
                .collect(Collectors.toList());

        List<Response> responses =
                executorService.invokeAll(callables).stream()
                .map(future -> Try.of(future::get).getOrElseThrow(() -> new RuntimeException("se daño esta vaina al invocar los callables")))
                .collect(Collectors.toList());

        System.out.println();
        System.out.println();
        System.out.println("==============================================================");


        responses.forEach(System.out::println);

        Thread.sleep(50000L);
    }

}