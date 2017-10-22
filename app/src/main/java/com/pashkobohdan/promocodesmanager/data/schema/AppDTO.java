package com.pashkobohdan.promocodesmanager.data.schema;

public class AppDTO {
    private String name;

    public AppDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AppDTO && name.endsWith(((AppDTO)obj).getName());
    }
}
