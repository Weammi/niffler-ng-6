package guru.qa.niffler.jupiter.extension;

import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    public record StaticUser(String username,
                             String password,
                             boolean empty,
                             String friend,
                             String income,
                             String outcome) {
    }

    private static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_INCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();
    private static final Queue<StaticUser> WITH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedQueue<>();

    static {
        EMPTY_USERS.add(new StaticUser("weammi1", "1234", true, null, null, null));
        WITH_FRIEND_USERS.add(new StaticUser("weammi2", "1234", false, "weammi3", null, null));
        WITH_INCOME_REQUEST_USERS.add(new StaticUser("weammi3", "1234", false, null, "weammi1", null));
        WITH_OUTCOME_REQUEST_USERS.add(new StaticUser("weammi4", "1234", false, null, null, "weammi5"));
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default Type.EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    private Queue<StaticUser> getQueueByUserType(UserType type) {
        switch (type.value()) {
            case EMPTY -> {
                return EMPTY_USERS;
            }
            case WITH_FRIEND -> {
                return WITH_FRIEND_USERS;
            }
            case WITH_INCOME_REQUEST -> {
                return WITH_INCOME_REQUEST_USERS;
            }
            case WITH_OUTCOME_REQUEST -> {
                return WITH_OUTCOME_REQUEST_USERS;
            }
            default -> throw new IllegalStateException("Unknown type: " + type);
        }
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .forEach(p -> {
                    UserType ut = p.getAnnotation(UserType.class);
                    Queue<StaticUser> queue = getQueueByUserType(ut);
                    Optional<StaticUser> user = Optional.empty();
                    // Таймер для отслеживания времени
                    StopWatch sw = StopWatch.createStarted();
                    // Если пользователь прошел через of, то заполняем его в очередь
                    while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
                        user = Optional.ofNullable(queue.poll());
                    }
                    // Обновляем тестовый кейс для Allure-отчёта, чтобы установить время начала теста на текущий момент.
                    Allure.getLifecycle().updateTestCase(testCase ->
                            testCase.setStart(new Date().getTime())
                    );
                    // Заполняем пользователя и его тип в мапу
                    user.ifPresentOrElse(
                            u -> getUserMap(context).put(ut, u),
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    /**
     * Извлекаем карту пользователей или создаём новую, если её ещё нет
     */
    private Map<UserType, StaticUser> getUserMap(ExtensionContext context) {
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> userMap = (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(context.getUniqueId(), key -> new HashMap<UserType, StaticUser>());
        return userMap;
    }

    /**
     * Возвращаем пользователей в соответствующие очереди по типу
     */
    @Override
    public void afterTestExecution(ExtensionContext context) {
        @SuppressWarnings("unchecked")
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(context.getUniqueId(), Map.class);

        if (map != null) {
            for (Map.Entry<UserType, StaticUser> e : map.entrySet()) {
                Queue<StaticUser> queue = getQueueByUserType(e.getKey());
                queue.add(e.getValue());
            }
        }
    }

    /**
     * Проверяем, что параметры принадлежат типам StaticUser.class, UserType.class
     */
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    /**
     * Получаем мапу с пользователями -> ищем нужный нам тип пользователя в мапе и отдаём его
     */
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Map<UserType, StaticUser> userMap = getUserMap(extensionContext);
        UserType ut = parameterContext.findAnnotation(UserType.class).orElseThrow(() -> new ParameterResolutionException("Annotation @UserType.class not found"));
        return userMap.get(ut);
    }
}
