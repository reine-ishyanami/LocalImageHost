package com.reine.imagehost.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

/**
 * @author reine
 */
public class SimpleImageProperty {

    private final SimpleIntegerProperty id = new SimpleIntegerProperty();

    private final SimpleStringProperty project = new SimpleStringProperty();

    private final SimpleStringProperty name = new SimpleStringProperty();

    private final SimpleStringProperty path = new SimpleStringProperty();

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getProject() {
        return project.get();
    }

    public SimpleStringProperty projectProperty() {
        return project;
    }

    public void setProject(String project) {
        this.project.set(project);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleImageProperty that = (SimpleImageProperty) o;
        return Objects.equals(id, that.id) && Objects.equals(project, that.project) && Objects.equals(name, that.name) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project, name, path);
    }

    @Override
    public String toString() {
        return "SimpleImageProperty{" +
                "id=" + id +
                ", project=" + project +
                ", name=" + name +
                ", path=" + path +
                '}';
    }
}
