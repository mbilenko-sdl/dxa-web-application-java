package com.sdl.webapp.common.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MvcDataImpl implements MvcData {
    @JsonProperty("ControllerAreaName")
    private String controllerAreaName = "Core";

    @JsonProperty("ControllerName")
    private String controllerName;

    @JsonProperty("ActionName")
    private String actionName;

    @JsonProperty("AreaName")
    private String areaName = "Core";

    @JsonProperty("ViewName")
    private String viewName;

    @JsonProperty("RegionAreaName")
    private String regionAreaName;

    @JsonProperty("RegionName")
    private String regionName;

    @JsonIgnore
    private Map<String, String> routeValues = new HashMap<>();

    @JsonIgnore
    private Map<String, Object> metadata = new HashMap<>();

    public MvcDataImpl() {
    }

    /**
     * @param qualifiedViewName fully qualified name if defined format. Format must be 'ViewName'
     *                          or 'AreaName:ViewName' or 'AreaName:ControllerName:ViewName.'
     */
    public MvcDataImpl(String qualifiedViewName) {
        String[] parts = qualifiedViewName == null || qualifiedViewName.isEmpty() ? null : qualifiedViewName.split(":");

        if (parts == null || parts.length < 1 || parts.length > 3) {
            throw new IllegalArgumentException(
                    String.format("Invalid format for Qualified View Name: '%s'. " +
                            "Format must be 'ViewName' or 'AreaName:ViewName' " +
                            "or 'AreaName:ControllerName:ViewName.'", qualifiedViewName));
        }

        switch (parts.length) {
            case 1:
                this.setMvcData(parts[0]);
                break;
            case 2:
                this.setMvcData(parts[0], parts[1]);
                break;
            case 3:
                this.setMvcData(parts[0], parts[1], parts[2]);
        }
    }

    @JsonIgnore
    private void setMvcData(String viewName) {
        this.viewName = viewName;
    }

    @JsonIgnore
    private void setMvcData(String areaName, String viewName) {
        this.setMvcData(viewName);
        this.areaName = areaName;
    }

    @JsonIgnore
    private void setMvcData(String areaName, String controllerName, String viewName) {
        this.setMvcData(areaName, viewName);
        this.controllerName = controllerName;
    }

    @Override
    public String getControllerAreaName() {
        return controllerAreaName;
    }

    public void setControllerAreaName(String controllerAreaName) {
        this.controllerAreaName = controllerAreaName;
    }

    @Override
    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    @Override
    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    @Override
    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public String getRegionAreaName() {
        return regionAreaName;
    }

    public void setRegionAreaName(String regionAreaName) {
        this.regionAreaName = regionAreaName;
    }

    @Override
    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    @Override
    public Map<String, String> getRouteValues() {
        return routeValues;
    }

    public void setRouteValues(Map<String, String> routeValues) {
        this.routeValues = routeValues;
    }

    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MvcDataImpl that = (MvcDataImpl) o;
        return Objects.equals(controllerAreaName, that.controllerAreaName) &&
                Objects.equals(controllerName, that.controllerName) &&
                Objects.equals(actionName, that.actionName) &&
                Objects.equals(areaName, that.areaName) &&
                Objects.equals(viewName, that.viewName) &&
                Objects.equals(regionAreaName, that.regionAreaName) &&
                Objects.equals(regionName, that.regionName) &&
                Objects.equals(routeValues, that.routeValues) &&
                Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(controllerAreaName, controllerName, actionName, areaName, viewName, regionAreaName, regionName, routeValues, metadata);
    }

    public MvcData defaults(Defaults defaults) {
        return defaults.set(this);
    }

    public enum Defaults {

        ENTITY("Core", "Entity", "Entity", "Core");

        private String controllerAreaName;
        private String controllerName;
        private String actionName;
        private String areaName;

        Defaults(String controllerAreaName, String controllerName, String actionName, String areaName) {
            this.controllerAreaName = controllerAreaName;
            this.controllerName = controllerName;
            this.actionName = actionName;
            this.areaName = areaName;
        }

        private MvcData set(MvcDataImpl mvcData) {
            if (mvcData.controllerAreaName == null ) {
                mvcData.controllerAreaName = this.controllerAreaName;
            }
            if (mvcData.controllerName == null ) {
                mvcData.controllerName = this.controllerName;
            }
            if (mvcData.actionName == null ) {
                mvcData.actionName = this.actionName;
            }
            if (mvcData.areaName == null ) {
                mvcData.areaName = this.areaName;
            }
            return mvcData;
        }
    }
}
