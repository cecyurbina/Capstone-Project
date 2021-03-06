package com.udacity.surbi.listnow.data;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "key",
        "name",
        "image",
        "unit",
        "quantity",
        "imageUrl",
        "rejected",
        "checked"
})
public class Item {
    @JsonProperty("key")
    private String key;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image")
    private Boolean image;
    @JsonProperty("unit")
    private String unit;
    @JsonProperty("quantity")
    private Integer quantity;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("rejected")
    private Boolean rejected;
    @JsonProperty("checked")
    private Boolean checked;
    @JsonIgnore
    private Bitmap bitmap;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("image")
    public Boolean getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(Boolean image) {
        this.image = image;
    }

    @JsonProperty("unit")
    public String getUnit() {
        return unit;
    }

    @JsonProperty("unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    @JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @JsonProperty("imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("imageUrl")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("rejected")
    public Boolean isRejected() {
        return rejected;
    }

    @JsonProperty("rejected")
    public void setRejected(Boolean rejected) {
        this.rejected = rejected;
    }

    @JsonProperty("checked")
    public Boolean isChecked() {
        return checked;
    }

    @JsonProperty("checked")
    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @JsonProperty("bitmap")
    public Bitmap getBitmap() {
        return bitmap;
    }

    @JsonProperty("bitmap")
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}