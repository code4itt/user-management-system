package com.store.user.management.models.facebook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PictureData {
    private String height;
    private String width;
    private String url;
	public String getHeight() {
		return height;
	}
	public String getWidth() {
		return width;
	}
	public String getUrl() {
		return url;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public void setUrl(String url) {
		this.url = url;
	}
    
    
}