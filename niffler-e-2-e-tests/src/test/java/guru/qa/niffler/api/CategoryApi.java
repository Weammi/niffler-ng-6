package guru.qa.niffler.api;

import guru.qa.niffler.model.spend.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {
    @POST("internal/categories/add")
    Call<CategoryJson> addCategory(@Query("username") String username, @Body CategoryJson category);

    @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategory(@Query("username") String username, @Body CategoryJson category);

    @GET("internal/categories/all")
    Call<CategoryJson> getCategories(@Query("excludeArchived") boolean excludeArchived);
}
