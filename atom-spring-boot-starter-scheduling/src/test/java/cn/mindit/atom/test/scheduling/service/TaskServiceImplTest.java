package cn.mindit.atom.test.scheduling.service;

import cn.mindit.atom.scheduling.config.Task;
import cn.mindit.atom.scheduling.service.TaskService;
import cn.mindit.atom.scheduling.service.impl.TaskServiceImpl;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class TaskServiceImplTest {

    private TaskService taskService;
    private ThreadPoolTaskScheduler taskScheduler;
    private StaticApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        applicationContext = new StaticApplicationContext();
        applicationContext.registerSingleton("sampleBean", SampleBean.class);
        applicationContext.refresh();

        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(2);
        taskScheduler.initialize();

        taskService = new TaskServiceImpl(validator, applicationContext, taskScheduler);
    }

    @Test
    void addTask_validTask_schedulesSuccessfully() {
        Task task = new Task("t1", "0/1 * * * * ?",
            SampleBean.class.getName(), "execute");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).containsKey("t1");
        assertThat((Object) taskService.getJobMap().get("t1")).isNotNull();
    }

    @Test
    void addTask_withParam_schedulesSuccessfully() {
        Task task = new Task("t2", "0/1 * * * * ?",
            SampleBean.class.getName(), "executeWithParam", "hello");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).containsKey("t2");
    }

    @Test
    void addTask_duplicateId_replacesExisting() {
        Task task1 = new Task("dup", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute");
        Task task2 = new Task("dup", "0/10 * * * * ?",
            SampleBean.class.getName(), "execute");

        taskService.addTask(task1);
        ScheduledFuture<?> first = taskService.getJobMap().get("dup");

        taskService.addTask(task2);
        ScheduledFuture<?> second = taskService.getJobMap().get("dup");

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(first.isCancelled()).isTrue();
        assertThat((Object) second).isNotSameAs(first);
    }

    @Test
    void addTask_invalidTask_doesNotSchedule() {
        Task task = new Task("", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void addTask_invalidCron_doesNotSchedule() {
        Task task = new Task("t1", "not-a-cron",
            SampleBean.class.getName(), "execute");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void addTask_classNotFound_doesNotSchedule() {
        Task task = new Task("t1", "0/5 * * * * ?",
            "com.nonexistent.Clazz", "execute");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void addTask_methodNotFound_doesNotSchedule() {
        Task task = new Task("t1", "0/5 * * * * ?",
            SampleBean.class.getName(), "nonExistentMethod");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void addTask_paramProvidedButMethodHasNoParam_doesNotSchedule() {
        Task task = new Task("t1", "0/5 * * * * ?",
            SampleBean.class.getName(), "noParamOnly", "unexpected");

        taskService.addTask(task);

        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void addTasks_schedulesMultiple() {
        List<Task> tasks = List.of(
            new Task("a1", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("a2", "0/10 * * * * ?", SampleBean.class.getName(), "execute")
        );

        taskService.addTasks(tasks);

        assertThat(taskService.getJobMap()).hasSize(2);
        assertThat(taskService.getJobMap()).containsKeys("a1", "a2");
    }

    @Test
    void setTask_clearsExistingAndAddsNew() {
        taskService.addTask(new Task("old", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute"));

        taskService.setTask(new Task("new", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute"));

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(taskService.getJobMap()).containsKey("new");
        assertThat(taskService.getJobMap()).doesNotContainKey("old");
    }

    @Test
    void setTasks_clearsExistingAndAddsAll() {
        taskService.addTask(new Task("old", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute"));

        taskService.setTasks(List.of(
            new Task("n1", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("n2", "0/10 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        assertThat(taskService.getJobMap()).hasSize(2);
        assertThat(taskService.getJobMap()).containsKeys("n1", "n2");
        assertThat(taskService.getJobMap()).doesNotContainKey("old");
    }

    @Test
    void removeTask_removesAndCancels() {
        taskService.addTask(new Task("rm1", "0/5 * * * * ?",
            SampleBean.class.getName(), "execute"));
        ScheduledFuture<?> future = taskService.getJobMap().get("rm1");

        taskService.removeTask("rm1");

        assertThat(taskService.getJobMap()).doesNotContainKey("rm1");
        assertThat(future.isCancelled()).isTrue();
    }

    @Test
    void removeTask_nonExistentId_doesNothing() {
        taskService.removeTask("nonexistent");
        assertThat(taskService.getJobMap()).isEmpty();
    }

    @Test
    void removeTasks_removesMultiple() {
        taskService.addTasks(List.of(
            new Task("r1", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("r2", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("r3", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        taskService.removeTasks(List.of("r1", "r3"));

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(taskService.getJobMap()).containsKey("r2");
    }

    @Test
    void removeTasksPrefix_removesMatchingTasks() {
        taskService.addTasks(List.of(
            new Task("job-a", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("job-b", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("task-c", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        taskService.removeTasksPrefix("job-");

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(taskService.getJobMap()).containsKey("task-c");
    }

    @Test
    void removeTasksSuffix_removesMatchingTasks() {
        taskService.addTasks(List.of(
            new Task("task-daily", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("task-weekly", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("task-daily2", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        taskService.removeTasksSuffix("-weekly");

        assertThat(taskService.getJobMap()).hasSize(2);
        assertThat(taskService.getJobMap()).containsKeys("task-daily", "task-daily2");
    }

    @Test
    void removeTasksContains_removesMatchingTasks() {
        taskService.addTasks(List.of(
            new Task("report-daily-sales", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("report-weekly-sales", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("cleanup-logs", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        taskService.removeTasksContains("sales");

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(taskService.getJobMap()).containsKey("cleanup-logs");
    }

    @Test
    void removeTasksRegex_removesMatchingTasks() {
        taskService.addTasks(List.of(
            new Task("task-001", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("task-002", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("job-abc", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));

        taskService.removeTasksRegex("task-\\d+");

        assertThat(taskService.getJobMap()).hasSize(1);
        assertThat(taskService.getJobMap()).containsKey("job-abc");
    }

    @Test
    void clear_removesAllAndCancels() {
        taskService.addTasks(List.of(
            new Task("c1", "0/5 * * * * ?", SampleBean.class.getName(), "execute"),
            new Task("c2", "0/5 * * * * ?", SampleBean.class.getName(), "execute")
        ));
        ScheduledFuture<?> f1 = taskService.getJobMap().get("c1");
        ScheduledFuture<?> f2 = taskService.getJobMap().get("c2");

        taskService.clear();

        assertThat(taskService.getJobMap()).isEmpty();
        assertThat(f1.isCancelled()).isTrue();
        assertThat(f2.isCancelled()).isTrue();
    }

    @Test
    void taskExecution_invokesMethod() throws InterruptedException {
        SampleBean bean = applicationContext.getBean(SampleBean.class);
        bean.reset();

        Task task = new Task("exec-test", "* * * * * ?",
            SampleBean.class.getName(), "execute");
        taskService.addTask(task);

        boolean executed = bean.awaitExecution(5, TimeUnit.SECONDS);
        assertThat(executed).isTrue();
        assertThat(bean.getExecutionCount()).isGreaterThanOrEqualTo(1);

        taskService.clear();
    }

    @Test
    void taskExecution_invokesMethodWithParam() throws InterruptedException {
        SampleBean bean = applicationContext.getBean(SampleBean.class);
        bean.reset();

        Task task = new Task("exec-param", "* * * * * ?",
            SampleBean.class.getName(), "executeWithParam", "world");
        taskService.addTask(task);

        boolean executed = bean.awaitExecution(5, TimeUnit.SECONDS);
        assertThat(executed).isTrue();
        assertThat(bean.getLastParam()).isEqualTo("world");

        taskService.clear();
    }

    public static class SampleBean {

        @Getter
        private volatile int executionCount = 0;
        @Getter
        private volatile String lastParam;
        private CountDownLatch latch = new CountDownLatch(1);

        public void execute() {
            executionCount++;
            latch.countDown();
        }

        public void executeWithParam(String param) {
            lastParam = param;
            executionCount++;
            latch.countDown();
        }

        public void noParamOnly() {
            executionCount++;
        }

        public boolean awaitExecution(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }

        public void reset() {
            executionCount = 0;
            lastParam = null;
            latch = new CountDownLatch(1);
        }

    }

}
