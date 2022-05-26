package com.alibaba.pokemon.bulbasaur.task.model;

import java.io.Serializable;

/**
 * bulbasaur中的用户，不引入hecla 和 UIC
 * User: yunche.ch - - - - (ว ˙o˙)ง
 * Date: 13-11-7
 * Time: 下午2:52
 */
public class User implements Serializable {

    private static final long serialVersionUID = 4335469331871618720L;
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        User user = (User)o;

        if (id != null ? !id.equals(user.id) : user.id != null) { return false; }
        return name != null ? name.equals(user.name) : user.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
