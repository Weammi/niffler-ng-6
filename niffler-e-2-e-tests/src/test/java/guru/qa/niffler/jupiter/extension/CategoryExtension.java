package guru.qa.niffler.jupiter.extension;

import com.github.javafaker.Faker;
import guru.qa.niffler.api.CategoryApiClient;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryApiClient categoryApiClient = new CategoryApiClient();
    private final Faker faker = new Faker();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(anno -> {
                    String name = anno.title().equals("") ? faker.funnyName().name() : anno.title();
                    CategoryJson createdCategory = new CategoryJson(
                            null,
                            name,
                            anno.username(),
                            anno.archive()
                    );
                    createdCategory = categoryApiClient.addCategory(createdCategory.username(), createdCategory);

                    if (anno.archive()) {
                        createdCategory = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );

                        createdCategory = categoryApiClient.updateCategory(createdCategory.username(), createdCategory);
                    }

                    context.getStore(NAMESPACE)
                            .put(context.getUniqueId(), createdCategory);
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        CategoryJson category = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        CategoryJson categoryJson = new CategoryJson(
                category.id(),
                category.name(),
                category.username(),
                true
        );
        categoryApiClient.updateCategory(category.username(), categoryJson);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
