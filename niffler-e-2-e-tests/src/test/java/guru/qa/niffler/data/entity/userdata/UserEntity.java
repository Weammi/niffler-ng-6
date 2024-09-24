package guru.qa.niffler.data.entity.userdata;

import guru.qa.niffler.model.spend.CurrencyValues;
import guru.qa.niffler.model.userdata.UserJson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;

@Getter
@Setter
public class UserEntity implements Serializable {

    private UUID id;
    private String username;
    private CurrencyValues currency;
    private String firstname;
    private String surname;
    private String fullname;
    private byte[] photo;
    private byte[] photoSmall;

    public static UserEntity fromJson(UserJson json) {
        UserEntity ue = new UserEntity();
        ue.setId(json.id());
        ue.setUsername(json.username());
        ue.setFirstname(json.firstname());
        ue.setSurname(json.surname());
        ue.setFullname(json.fullname());
        ue.setCurrency(json.currency());
        ue.setPhoto(json.photo() != null ? json.photo().getBytes(UTF_8) : null);
        ue.setPhotoSmall(json.photoSmall() != null ? json.photoSmall().getBytes(UTF_8) : null);

        return ue;
    }
}