package net.learningpath.callcenter.service;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.employee.hierarchylevel.DirectorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.EmployeesLevel;
import net.learningpath.callcenter.employee.hierarchylevel.OperatorsLevel;
import net.learningpath.callcenter.employee.hierarchylevel.SupervisorsLevel;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.concurrent.BlockingQueue;

import static org.mockito.Mockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EmployeesLevel.class, DirectorsLevel.class, SupervisorsLevel.class, OperatorsLevel.class})
public class DispatcherImplTest {

    private static final String DIRECTORS_LEVEL_FIELD_NAME = "directorsLevel";
    private static final String SUPERVISORS_LEVEL_FIELD_NAME = "supervisorsLevel";
    private static final String OPERATORS_LEVEL_FIELD_NAME = "operatorsLevel";
    private static final String EMPLOYEES_AVAILABILITY_FIELD_NAME = "employeesAvailability";
    private static final String CALLS_QUEUE_FIELD_NAME = "calls";

    @Test
    public void operatorShouldAttendCall() throws InterruptedException {
        DispatcherImpl dispatcherMock = mock(DispatcherImpl.class);

        EmployeesLevel directorsLevelMock = mock(DirectorsLevel.class);
        EmployeesLevel supervisorsLevelMock = mock(SupervisorsLevel.class);
        EmployeesLevel operatorsLevelMock = mock(OperatorsLevel.class);
        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call callMock = mock(Call.class);
        BlockingQueue<Call> calls = mock(BlockingQueue.class);

        Whitebox.setInternalState(dispatcherMock, DIRECTORS_LEVEL_FIELD_NAME, directorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, SUPERVISORS_LEVEL_FIELD_NAME, supervisorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, OPERATORS_LEVEL_FIELD_NAME, operatorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, EMPLOYEES_AVAILABILITY_FIELD_NAME, availabilityTopic);
        Whitebox.setInternalState(dispatcherMock, CALLS_QUEUE_FIELD_NAME, calls);

        PowerMockito.doCallRealMethod().when(dispatcherMock).dispatchCall(callMock);

        dispatcherMock.dispatchCall(callMock);
    }

    @Test
    public void callsQueue_ShouldBeUpdated_WhenNoEmployeesAvailable() throws InterruptedException {
        DispatcherImpl dispatcherMock = mock(DispatcherImpl.class);

        EmployeesLevel directorsLevelMock = mock(DirectorsLevel.class);
        EmployeesLevel supervisorsLevelMock = mock(SupervisorsLevel.class);
        EmployeesLevel operatorsLevelMock = mock(OperatorsLevel.class);
        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call callMock = mock(Call.class);
        BlockingQueue<Call> calls = mock(BlockingQueue.class);

        Whitebox.setInternalState(dispatcherMock, DIRECTORS_LEVEL_FIELD_NAME, directorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, SUPERVISORS_LEVEL_FIELD_NAME, supervisorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, OPERATORS_LEVEL_FIELD_NAME, operatorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, EMPLOYEES_AVAILABILITY_FIELD_NAME, availabilityTopic);
        Whitebox.setInternalState(dispatcherMock, CALLS_QUEUE_FIELD_NAME, calls);

        PowerMockito.doCallRealMethod().when(dispatcherMock).update(callMock);

        dispatcherMock.update(callMock);
    }

    @Test
    public void callsQueue_ShouldBeUpdated_WhenEmployeesAvailableAgain() throws InterruptedException {
        DispatcherImpl dispatcherMock = mock(DispatcherImpl.class);

        EmployeesLevel directorsLevelMock = mock(DirectorsLevel.class);
        EmployeesLevel supervisorsLevelMock = mock(SupervisorsLevel.class);
        EmployeesLevel operatorsLevelMock = mock(OperatorsLevel.class);
        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        BlockingQueue<Call> calls = mock(BlockingQueue.class);

        Whitebox.setInternalState(dispatcherMock, DIRECTORS_LEVEL_FIELD_NAME, directorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, SUPERVISORS_LEVEL_FIELD_NAME, supervisorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, OPERATORS_LEVEL_FIELD_NAME, operatorsLevelMock);
        Whitebox.setInternalState(dispatcherMock, EMPLOYEES_AVAILABILITY_FIELD_NAME, availabilityTopic);
        Whitebox.setInternalState(dispatcherMock, CALLS_QUEUE_FIELD_NAME, calls);

        PowerMockito.doCallRealMethod().when(dispatcherMock).update();

        dispatcherMock.update();
    }

}