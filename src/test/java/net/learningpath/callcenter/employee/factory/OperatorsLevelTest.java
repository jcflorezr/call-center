package net.learningpath.callcenter.employee.factory;

import io.vavr.control.Option;
import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.employee.Employee;
import net.learningpath.callcenter.employee.Operator;
import net.learningpath.callcenter.event.topic.EmployeesAvailability;
import net.learningpath.callcenter.event.topic.EmployeesAvailabilityTopic;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.reflect.Field;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EmployeesLevel.class)
public class OperatorsLevelTest {

    private static final String NEXT_LEVEL_FIELD_NAME = "nextHierarchyLevel";
    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";

    @Test
    public void shouldCreate_TheOperatorsHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = null;
        int numOfOperatorsAvailable = 5;

        OperatorsLevel operatorsLevel = OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
        assertNotNull(operatorsLevel);

        Class<? extends EmployeesLevel> operatorsLevelClass = operatorsLevel.getClass();
        Field nextLevelField = operatorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQuqueField = operatorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQuqueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(operatorsLevel);
        BlockingQueue<Operator> actualEmployeesQueue = (BlockingQueue<Operator>) employeesQuqueField.get(operatorsLevel);

        assertTrue(actualNextHierarchyLevel.isEmpty());
        assertThat(actualEmployeesQueue.size(), is(numOfOperatorsAvailable));
    }

    @Test
    public void shouldCreate_TheOperatorsHierarchyLevel_WithHigherHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = mock(SupervisorsLevel.class);
        int numOfOperatorsAvailable = 5;

        OperatorsLevel operatorsLevel = OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
        assertNotNull(operatorsLevel);

        Class<? extends EmployeesLevel> operatorsLevelClass = operatorsLevel.getClass();
        Field nextLevelField = operatorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = operatorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(operatorsLevel);
        BlockingQueue<Operator> actualEmployeesQueue = (BlockingQueue<Operator>) employeesQueueField.get(operatorsLevel);

        assertTrue(actualNextHierarchyLevel.get().getClass().isInstance(nextHierarchyLevel));
        assertThat(actualEmployeesQueue.size(), is(numOfOperatorsAvailable));
    }

    @Test(expected = HierarchyLevelException.class)
    public void shouldThrowError_WhenCurrentAndNextLevelsAreEqual() {
        int numOfOperatorsAvailable = 5;
        EmployeesLevel nextHierarchyLevel = OperatorsLevel.newHierarchyLevel(null, numOfOperatorsAvailable);
        OperatorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfOperatorsAvailable);
    }

    @Test
    public void operatorShouldAttendCallSuccessfully() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        EmployeesLevel employeesLevelMock = mock(EmployeesLevel.class);
        Operator operator = mock(Operator.class);
        BlockingQueue operatorsQueueMock = mock(ArrayBlockingQueue.class);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);

        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call clientCall = mock(Call.class);

        doThrow(InterruptedException.class).when(operatorsQueueMock).take();
        //doNothing().when(availabilityTopic).notifyAvailability();
        PowerMockito.doCallRealMethod().when(employeesLevelMock).answerCall(clientCall, availabilityTopic);

        employeesLevelMock.answerCall(clientCall, availabilityTopic);
    }

    @Test
    public void operatorShouldAttendCall() throws NoSuchFieldException, IllegalAccessException, InterruptedException {
        EmployeesLevel employeesLevelMock = mock(EmployeesLevel.class);
        Operator operator = mock(Operator.class);
        BlockingQueue<Operator> operatorsQueueMock = new ArrayBlockingQueue<>(1);
        operatorsQueueMock.put(operator);
        Whitebox.setInternalState(employeesLevelMock, EMPLOYEES_QUEUE_FIELD_NAME, operatorsQueueMock);

        EmployeesAvailabilityTopic availabilityTopic = mock(EmployeesAvailability.class);
        Call clientCall = mock(Call.class);


        doNothing().when(availabilityTopic).notifyAvailability();
        PowerMockito.doCallRealMethod().when(employeesLevelMock).answerCall(clientCall, availabilityTopic);

        employeesLevelMock.answerCall(clientCall, availabilityTopic);
    }

}