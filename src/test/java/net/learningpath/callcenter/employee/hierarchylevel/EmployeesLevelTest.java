package net.learningpath.callcenter.employee.hierarchylevel;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.employee.Operator;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeesLevel.class)
public class EmployeesLevelTest {

    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";
    private static final String NEXT_HIERARCHY_FIELD_NAME = "nextHierarchyLevel";
    private static final String EXECUTOR_SERVICE_FIELD_NAME = "executorService";

    @Test
    public void operatorShouldAttendCall() throws InterruptedException {
        EmployeesLevel employeesLevelMock = mock(EmployeesLevel.class);
        Operator operator = mock(Operator.class);
        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        operatorsQueueMock.put(operator);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);

        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call clientCall = mock(Call.class);

        doNothing().when(availabilityTopic).notifyUnavailability(any());
        PowerMockito.doCallRealMethod().when(employeesLevelMock).answerCall(clientCall, availabilityTopic);

        employeesLevelMock.answerCall(clientCall, availabilityTopic);
    }

    @Test
    public void thereAreNoEmployeesToAttendTheCall() {
        EmployeesLevel employeesLevelMock = mock(EmployeesLevel.class);

        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);
        Option<EmployeesLevel> nextHierarchyLevelMock = Option.none();
        Whitebox.setInternalState(employeesLevelMock, NEXT_HIERARCHY_FIELD_NAME, nextHierarchyLevelMock);
        ExecutorService executorServiceMock = mock(ExecutorService.class);
        Whitebox.setInternalState(employeesLevelMock, EXECUTOR_SERVICE_FIELD_NAME, executorServiceMock);

        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call clientCall = mock(Call.class);

        PowerMockito.doCallRealMethod().when(employeesLevelMock).answerCall(clientCall, availabilityTopic);

        employeesLevelMock.answerCall(clientCall, availabilityTopic);
    }

}