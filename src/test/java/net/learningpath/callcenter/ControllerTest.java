package net.learningpath.callcenter;

import io.vavr.control.Try;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.employee.Employee;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ControllerTest {

    @Test
    public void getRequest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        List<Callable<Response>> callables = IntStream.range(1, 1001)
                .mapToObj(i -> Executors.privilegedCallable(() -> Controller.getInstance().getRequest(new Call("Client" + i))))
                .collect(Collectors.toList());

        List<Response> responses =
                executorService.invokeAll(callables).stream()
                .map(future -> Try.of(future::get).getOrElseThrow(() -> new RuntimeException("response is empty !!??")))
                .collect(Collectors.toList());

        executorService.shutdown();

        responses.forEach(response -> {
            assertTrue(response.isSuccess());
            assertNotNull(response.getCall());
            assertNotNull(response.getCall().getAttendedBy());
            assertTrue(response.getCall().getAttendedBy() instanceof Employee);
            assertNull(response.getErrorType());
            assertNull(response.getDetails());
        });
    }

}