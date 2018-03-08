package net.learningpath.callcenter.service;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.dto.response.Response;
import net.learningpath.callcenter.employee.Operator;
import net.learningpath.callcenter.employee.Supervisor;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.event.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DispatcherImplTest {

    private Call call;
    @Mock
    private ExecutorService executorService;
    @Mock
    private EmployeesAvailabilityTopic employeesAvailability;
    @Mock
    private EmployeesLevel employeesLevel;

    private DispatcherImpl dispatcher;

    @Before
    public void setUp() {
        call = new Call("mock client");
        dispatcher = new DispatcherImpl(employeesLevel, employeesAvailability);
    }

    @Test
    public void employeeShouldAttendCall() {
        Operator operator = new Operator();

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.of(operator));
        doNothing().when(employeesLevel).returnEmployeeToQueue(operator);
        doNothing().when(executorService).execute(any());

        Response response = dispatcher.dispatchCall(call);

        assertTrue(response.isSuccess());
        assertNotNull(response.getCall());
        assertNotNull(response.getCall().getAttendedBy());
        assertThat(response.getCall().getAttendedBy(), is(equalTo(operator)));
        assertNull(response.getErrorType());
        assertNull(response.getDetails());
    }

    @Test
    public void employeeShouldWait_BeforeAttendingCall() throws InterruptedException, ExecutionException {
        Supervisor supervisor = new Supervisor();

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.none()).thenReturn(Option.of(supervisor));
        doNothing().when(employeesAvailability).notifyUnavailability(anyObject());
        doNothing().when(employeesLevel).returnEmployeeToQueue(supervisor);
        doNothing().when(executorService).execute(any());

        ExecutorService realExecutorService = Executors.newSingleThreadExecutor();
        Future<Response> responseFuture = realExecutorService.submit(() -> dispatcher.dispatchCall(call));
        Thread.sleep(2000L);

        synchronized (dispatcher) {
            Option.of(responseFuture)
                    .filter(future -> !future.isDone())
                    .peek(future -> dispatcher.notifyAll());
        }

        Response response = responseFuture.get();

        assertTrue(response.isSuccess());
        assertNotNull(response.getCall());
        assertNotNull(response.getCall().getAttendedBy());
        assertThat(response.getCall().getAttendedBy(), is(equalTo(supervisor)));
        assertNull(response.getErrorType());
        assertNull(response.getDetails());
    }

    @Test
    public void errorWhenPuttingBackTheEmployee_AfterAttendingTheCall() {
        Operator operator = new Operator();
        String expectedErrorMessage = operator.getClass().getSimpleName() + " was not enqueued, it will not be available anymore.";

        when(employeesLevel.getAvailableEmployee()).thenReturn(Option.of(operator));
        doThrow(HierarchyLevelException.employeeWasNotEnqueued(operator)).when(employeesLevel).returnEmployeeToQueue(operator);
        doNothing().when(executorService).execute(any());

        Response response = dispatcher.dispatchCall(call);

        assertFalse(response.isSuccess());
        assertNotNull(response.getCall());
        assertNull(response.getCall().getAttendedBy());
        //assertThat(response.getCall().getAttendedBy(), is(equalTo(operator)));
        assertThat(response.getErrorType(), is(HierarchyLevelException.class.getName()));
        assertNotNull(response.getDetails());
        assertThat(response.getMessage(), is(equalTo(expectedErrorMessage)));
    }

}