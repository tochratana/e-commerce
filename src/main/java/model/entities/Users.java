package model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Users {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private boolean is_deleted;
    private String uuid;
    private boolean is_logged_in;
}
