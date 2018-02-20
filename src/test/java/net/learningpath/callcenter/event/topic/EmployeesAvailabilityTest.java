package net.learningpath.callcenter.event.topic;

import net.learningpath.callcenter.dto.request.Call;
import net.learningpath.callcenter.event.listener.Listener;
import net.learningpath.callcenter.service.DispatcherImpl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.mock;

public class EmployeesAvailabilityTest {

    private static final String AVAILAVILITY_FIELD_NAME = "employeesAvailable";
    private static final String LISTENERS_FIELD_NAME = "listeners";

    private static EmployeesAvailabilityTopic employeesAvailability;

    @BeforeClass
    public static void init() {
        employeesAvailability = EmployeesAvailability.getInstance();
        DispatcherImpl dispatcher = mock(DispatcherImpl.class);
        employeesAvailability.register(dispatcher);
    }

    @Test
    public void notifyUnavailability() {
        Call aClient = new Call("a client");
        employeesAvailability.notifyUnavailability(aClient);
    }

    @Test
    public void notifyAvailability() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends EmployeesAvailabilityTopic> employeesAvailabilityClass = employeesAvailability.getClass();
        Field availabilityField = employeesAvailabilityClass.getDeclaredField(AVAILAVILITY_FIELD_NAME);
        availabilityField.setAccessible(true);
        availabilityField.set(employeesAvailability, new AtomicBoolean(false));
        employeesAvailability.notifyAvailability();
    }

    @AfterClass
    public static void tearDown() throws NoSuchFieldException, IllegalAccessException {
        Class<? extends EmployeesAvailabilityTopic> employeesAvailabilityClass = employeesAvailability.getClass();
        Field listenersField = employeesAvailabilityClass.getDeclaredField(LISTENERS_FIELD_NAME);
        listenersField.setAccessible(true);
        List<Listener> listeners = (List<Listener>) listenersField.get(employeesAvailability);
        Listener mockListener = listeners.get(0);
        employeesAvailability.unregister(mockListener);
    }

}