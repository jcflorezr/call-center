package net.learningpath.callcenter.employee.hierarchylevel;

import io.vavr.control.Option;
import net.learningpath.callcenter.employee.Supervisor;
import net.learningpath.callcenter.exceptions.HierarchyLevelException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class SupervisorsLevelTest {

    private static final String NEXT_LEVEL_FIELD_NAME = "nextHierarchyLevel";
    private static final String EMPLOYEES_QUEUE_FIELD_NAME = "employees";

    @Test
    public void shouldCreate_TheSupervisorsHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = null;
        int numOfSupervisorsAvailable = 5;

        SupervisorsLevel supervisorsLevel = SupervisorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfSupervisorsAvailable);
        assertNotNull(supervisorsLevel);

        Class<? extends EmployeesLevel> supervisorsLevelClass = supervisorsLevel.getClass();
        Field nextLevelField = supervisorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = supervisorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(supervisorsLevel);
        BlockingQueue<Supervisor> actualEmployeesQueue = (BlockingQueue<Supervisor>) employeesQueueField.get(supervisorsLevel);

        assertTrue(actualNextHierarchyLevel.isEmpty());
        assertThat(actualEmployeesQueue.size(), is(numOfSupervisorsAvailable));
    }

    @Test
    public void shouldCreate_TheSupervisorsHierarchyLevel_WithHigherHierarchyLevel() throws NoSuchFieldException, IllegalAccessException {
        EmployeesLevel nextHierarchyLevel = mock(DirectorsLevel.class);
        int numOfSupervisorsAvailable = 5;

        SupervisorsLevel supervisorsLevel = SupervisorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfSupervisorsAvailable);
        assertNotNull(supervisorsLevel);

        Class<? extends EmployeesLevel> supervisorsLevelClass = supervisorsLevel.getClass();
        Field nextLevelField = supervisorsLevelClass.getSuperclass().getDeclaredField(NEXT_LEVEL_FIELD_NAME);
        nextLevelField.setAccessible(true);
        Field employeesQueueField = supervisorsLevelClass.getSuperclass().getDeclaredField(EMPLOYEES_QUEUE_FIELD_NAME);
        employeesQueueField.setAccessible(true);

        Option<EmployeesLevel> actualNextHierarchyLevel = (Option<EmployeesLevel>) nextLevelField.get(supervisorsLevel);
        BlockingQueue<Supervisor> actualEmployeesQueue = (BlockingQueue<Supervisor>) employeesQueueField.get(supervisorsLevel);

        assertTrue(actualNextHierarchyLevel.get().getClass().isInstance(nextHierarchyLevel));
        assertThat(actualEmployeesQueue.size(), is(numOfSupervisorsAvailable));
    }

    @Test(expected = HierarchyLevelException.class)
    public void shouldThrowError_WhenCurrentAndNextLevelsAreEqual() {
        int numOfSupervisorsAvailable = 5;
        EmployeesLevel nextHierarchyLevel = SupervisorsLevel.newHierarchyLevel(null, numOfSupervisorsAvailable);
        SupervisorsLevel.newHierarchyLevel(nextHierarchyLevel, numOfSupervisorsAvailable);
    }

}