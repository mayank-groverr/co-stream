package practice.mayank.co_stream.security.entity;

public enum RoleEnum {

    USER,
    ADMIN;


    public String getRoleNameWithRolePrefix(){
        return "ROLE_" + this.name();
    }


}
